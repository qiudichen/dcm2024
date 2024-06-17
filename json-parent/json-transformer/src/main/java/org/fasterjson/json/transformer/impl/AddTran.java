package org.fasterjson.json.transformer.impl;

import java.util.List;
import java.util.Map;

import org.fasterjson.json.transformer.spec.ElementNodePath;


public class AddTran extends BaseTran<AddSpec> {

	public AddTran() {
		super();
	}
	
	@Override
	protected AddSpec createSpec(Map<String, String> spec) {
		return new AddSpec(spec);
	}
	
	@Override
	protected void apply(AddSpec addSpec, Map<String, Object> input) {
		String label = addSpec.getLabel();
		Object value = addSpec.getValue();
		
		if(!addSpec.hasParent()) {
			addElement(label, input, value);
		} else {
			ElementNodePath parent = addSpec.getParent();
			for(ElementNodePath child : parent.getChildern()) {
				addToMapElement(child, label, input, value);
			}			
		}
	}
	
	private void addToMapElement(ElementNodePath current, String label, Map<String, Object> input, Object value) {
		Map<String, Object> element = input;
		
		String path = current.getPath();
		Object elm = findElementObj(path, element);

		if(!current.hasChild()) {
			if(elm instanceof Map) {
				element = (Map<String, Object>)elm;
				addElement(label, element, value);
			} else if(elm instanceof List) {
				for(Object item : (List<Object>)elm) {
					if(elm instanceof Map) {
						element = (Map<String, Object>)elm;
						addElement(label, element, value);
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
					addToMapElement(child, label, element, value);							
			}
			else if(elm instanceof List) {
				addToListElement(current, label, (List<Object>)elm, value);
			}				
		}
	}
	
	private void addToListElement(ElementNodePath current, String label, List<Object> input, Object value) {
		for(Object item : input) {
			if(item instanceof Map) {
				addToMapElement(current, label, (Map<String, Object>)item, value);
			} else {
				throw new RuntimeException("");
			}
		}
	}
}
