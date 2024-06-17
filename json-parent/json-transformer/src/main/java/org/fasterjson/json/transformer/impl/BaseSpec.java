package org.fasterjson.json.transformer.impl;

import java.util.Map;

import org.fasterjson.json.transformer.spec.ElementNodePath;

public class BaseSpec {
	protected static final String PATH_SEP = "\\/";
	
	protected final static String KEY_PATH = "path";
	
	protected ElementNodePath parent;
	
	public BaseSpec(Map<String, String> spec) {
		String temp = spec.get(KEY_PATH);
		if(temp != null && !temp.isBlank()) {
			this.parent = new ElementNodePath();
			this.parent.addSpecPath(temp);
		}
		
	}

	public ElementNodePath getParent() {
		return parent;
	}

	public boolean hasParent() {
		return this.parent != null && this.parent.hasChild();
	}
}
