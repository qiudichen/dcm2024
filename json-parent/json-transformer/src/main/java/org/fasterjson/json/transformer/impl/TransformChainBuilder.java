package org.fasterjson.json.transformer.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fasterjson.json.transformer.Transform;
import org.fasterjson.json.transformer.TransformInstantiator;

public class TransformChainBuilder {
	private final static String KEY_TRANSFORMER = "transformer";
	
	private final static String KEY_SPEC = "spec";

	private final List<Object> specObjs;
	
	private Map<String, Class<? extends Transform>> tranMap = new HashMap<>();
	
	private void addDefaultTransform() 
	{
		addTransformClass("add", AddTran.class);
		addTransformClass("remove", RemoveTran.class);
	}
	
	public void addTransformClass(String name, Class<? extends Transform> cls) {
		tranMap.put(name, cls);
	}
	
    public TransformChainBuilder(List<Object> specObjs) {
        this.specObjs = specObjs;
        addDefaultTransform();
    }	
    
    public TransformChain build() {
    	TransformChain chain = new TransformChain();
    	for(Object spec : specObjs) {
    		Transform transform = createTransform(spec);
    		chain.addTransform(transform);
    	}
    	return chain;
    }
    
    @SuppressWarnings("unchecked")
	private Transform createTransform(Object spec) {
    	Map<String, Object> specMap = (Map<String, Object>) spec;
    	
    	String tranName = (String)specMap.get(KEY_TRANSFORMER);
    	Class<? extends Transform> cls = tranMap.get(tranName);
    	
    	if(cls == null) {
    		try {
				Class<?> clazz = Class.forName(tranName);
				if(Transform.class.isAssignableFrom(clazz)) {
					cls = (Class<? extends Transform>)clazz;
				} else {
					throw new RuntimeException("Calss " + tranName + " doesn't implment Transform interface.");
				}
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
    	}
    	Transform transform = null;
    	try {
			transform = cls.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
    	
    	List<Object> tranSpec = (List<Object>)specMap.get(KEY_SPEC);
    	if(tranSpec != null && TransformInstantiator.class.isAssignableFrom(cls)) {
    		((TransformInstantiator)transform).applySpec(tranSpec);
    	}
    	return transform;
    }
}
