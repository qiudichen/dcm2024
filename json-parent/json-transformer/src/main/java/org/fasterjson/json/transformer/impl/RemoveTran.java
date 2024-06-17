package org.fasterjson.json.transformer.impl;

import java.util.List;
import java.util.Map;

import org.fasterjson.json.transformer.Transform;
import org.fasterjson.json.transformer.TransformInstantiator;
import org.fasterjson.json.transformer.spec.ElementNodePath;

/**
 *     {
 *        "transformer": "remove",
 *        "spec": [
 *        	{ "path" : "page|categories/persistent|removed" },
 *        	{ "path" : "location" }
 *        ]
 *    }
 * @author DavidChen
 *
 */
public class RemoveTran implements Transform, TransformInstantiator {
	private final static String KEY_TATH = "path";
	
	private ElementNodePath root;
	@Override
	public Map<String, Object> apply(Map<String, Object> rootInput) {
		if(!root.hasChild()) {
			return rootInput;
		}
		
		for(ElementNodePath child : root.getChildern()) {
			remove(child, rootInput);
		}
		return rootInput;
	}

	private void remove(ElementNodePath current, Map<String, Object> input) {
		String path = current.getPath();
		if(!current.hasChild()) {
			remove(path, input);
			return;
		}
		
		Object item = input.get(path);
		if(item instanceof Map) {
			for(ElementNodePath child : current.getChildern()) {
				remove(child, (Map<String, Object>)item);							
			}
		}
		else if(item instanceof List) {
			for(ElementNodePath child : current.getChildern()) {
				remove(child, (List<Object>)item);
			}
		}
	}
	
	
	private void remove(ElementNodePath current, List<Object> input) {
		String path = current.getPath();
		if(!current.hasChild()) {
			remove(path, input);
		}

		for(Object item : input) {
			if(item instanceof Map) {
				remove(current, (Map<String, Object>)item);
			}
		}
	}
	
	private void remove(String key, Map<String, Object> input) {
		input.remove(key);
	}
	
	private void remove(String key, List<Object> input) {
		for(Object childInput : input) {
			if(childInput instanceof Map) {
				remove(key, (Map<String, Object>)childInput);			
			}
			else if(input instanceof List) {
				remove(key, (List<Object>)childInput);
			}
		}
	}
	
	@Override
	public void applySpec(List<Object> specs) {
		root = new ElementNodePath();
		for(Object obj : specs) {
			Map<String, Object> spec = (Map<String, Object>)obj;
			root.addSpecPath((String)spec.get(KEY_TATH));
		}
	}
}
