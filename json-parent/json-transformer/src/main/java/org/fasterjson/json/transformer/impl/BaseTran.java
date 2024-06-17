package org.fasterjson.json.transformer.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.fasterjson.json.transformer.Transform;
import org.fasterjson.json.transformer.TransformInstantiator;

public abstract class BaseTran<T extends BaseSpec> implements Transform, TransformInstantiator {
	protected List<T> specs;
	
	protected abstract T createSpec(Map<String, String> spec);
	
	protected abstract void apply(T spec, Map<String, Object> input);
	
	public BaseTran() {
		
	}
	
	@Override
	public void applySpec(List<Object> specs) {
		specs = new LinkedList<>();
		for(Object item : specs) {
			Map<String, String> spec = (Map<String, String>)item;
			specs.add(createSpec(spec));
		}
	}

	@Override
	public Map<String, Object> apply(Map<String, Object> input) {
		if(specs == null || specs.isEmpty()) {
			return input;
		}
		
		for(T spec : specs) {
			apply(spec, input);
		}		
		return input;
	}	
	
	protected void addElement(String path, Map<String, Object> input, Object value) {
		Map<String, Object> element = input;
		if(element.containsKey(path)) {
			throw new RuntimeException("The element already exists : " + path);
		}
		element.put(path, value);		
	}
	
	protected Object findElementObj(String path, Map<String, Object> input) {
		Object elm = input.get(path);
		if(elm == null) {
			throw new RuntimeException("Cannot find element by key: " + path);
		}
		return elm;
	}
	
	protected Map<String, Object> findElementMap(String path, Map<String, Object> input) {
		Object elm = findElementObj(path, input);
		
		if(elm instanceof Map) {
			return (Map<String, Object>)elm;
		} else {
			throw new RuntimeException("The sub element is not Map Object, key: " + path);			
		}
	}	
}
