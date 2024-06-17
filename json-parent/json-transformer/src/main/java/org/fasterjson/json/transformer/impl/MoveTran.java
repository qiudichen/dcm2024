package org.fasterjson.json.transformer.impl;

import java.util.List;
import java.util.Map;

import org.fasterjson.json.transformer.spec.ElementNodePath;

public class MoveTran extends BaseTran<MoveSpec>  {
	
	public MoveTran() {
		super();
	}
	
	@Override
	protected MoveSpec createSpec(Map<String, String> spec) {
		return new MoveSpec(spec);
	}
	
	@Override
	protected void apply(MoveSpec spec, Map<String, Object> input) {
		String fromPaths[] = spec.getFromPaths();
		String toPaths[] = spec.getToPaths();
		
		if(!spec.hasParent()) {
			moveElement(fromPaths, toPaths, input);
			return;
		}
		
		ElementNodePath parent = spec.getParent();
		for(ElementNodePath child : parent.getChildern()) {
			moveElement(child, input, fromPaths, toPaths);
		}			
	}
	
	private void moveElement(ElementNodePath current, Map<String, Object> input, String fromPaths[], String toPaths[]) {
		Map<String, Object> element = input;
		String path = current.getPath();
		Object elm = findElementObj(path, element);
		
		if(!current.hasChild()) {
			if(elm instanceof Map) {
				element = (Map<String, Object>)elm;
				moveElement(fromPaths, toPaths, element);
			} else if(elm instanceof List) {
				for(Object item : (List<Object>)elm) {
					if(elm instanceof Map) {
						element = (Map<String, Object>)elm;
						moveElement(fromPaths, toPaths, element);
					} else {
						throw new RuntimeException("");
					}
				}
			} else {
				throw new RuntimeException("");
			}
			return;
		}
		
		for(ElementNodePath child : current.getChildern()) {
			if(elm instanceof Map) {
					element = (Map<String, Object>)elm;
					moveElement(child, element, fromPaths, toPaths);							
			}
			else if(elm instanceof List) {
				moveElement(current, (List<Object>)elm, fromPaths, toPaths);
			}		
		}
	}
	
	private void moveElement(ElementNodePath current, List<Object> input, String fromPaths[], String toPaths[]) {
		for(Object item : input) {
			if(item instanceof Map) {
				moveElement(current, (Map<String, Object>)item, fromPaths, toPaths);
			} else {
				throw new RuntimeException("");
			}
		}		
	}
	
	private void moveElement(String fromPaths[], String toPaths[], Map<String, Object> input) {
		Object value = findElementAndDelete(fromPaths, input);
		findElementAndAdd(toPaths, input, value);
	}
	

	private Object findElementAndDelete(String[] fromPaths, Map<String, Object> input) {
		Map<String, Object> element = input;
		for(int i = 0; i < fromPaths.length - 1; i++) {
			element = findElement(fromPaths[i],element);
		}
		
		Object value = element.remove(fromPaths[fromPaths.length - 1]);

		if(value == null) {
			throw new RuntimeException("Cannot find element by key: " + fromPaths[fromPaths.length - 1]);
		}
		return value;
	}
	
	private void findElementAndAdd(String[] toPaths, Map<String, Object> input, Object value) {
		Map<String, Object> element = input;
		for(int i = 0; i < toPaths.length - 1; i++) {
			element = findElement(toPaths[i],element);
		}
		
		if(element.containsKey(toPaths[toPaths.length - 1])) {
			throw new RuntimeException("The element already exists : " + toPaths[toPaths.length - 1]);
		}
		
		element.put(toPaths[toPaths.length - 1], value);
	}
	
	private Map<String, Object> findElement(String fromPath, Map<String, Object> input) {
		Object elm = input.get(fromPath);
		if(elm == null) {
			throw new RuntimeException("Cannot find element by key: " + fromPath);
		}
		
		if(elm instanceof Map) {
			return (Map<String, Object>)elm;
		} else {
			throw new RuntimeException("The sub element is not " + fromPath);			
		}
	}

}
