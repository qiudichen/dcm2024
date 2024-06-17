package org.fasterjson.json.transformer.impl;

import java.util.Map;

/**
 * 
 *    {
 *        "transformer": "move",
 *        "spec": [
 *        	{ "path" : "arryPath", "from" : "page/homePage", "to" : "homePage" },
 *        	{ "from" : "pageId", "to" : "page/pageId" }
 *        ]
 *    }
 * 
 * 
 * @author DavidChen
 *
 */

public class MoveSpec extends BaseSpec {
	
	private final static String KEY_FROM = "from";
	private final static String KEY_TO = "to";
	private static final String PATH_SEP = "\\/";
	
	private String[] fromPaths;
	
	private String[] toPaths;
	
	public MoveSpec(Map<String, String> spec) {
		super(spec);
		this.fromPaths = this.getPathByKey(spec, KEY_FROM);
		this.toPaths = this.getPathByKey(spec, KEY_TO);
	}

	private String[] getPathByKey(Map<String, String> spec, String key) {
		String value = spec.get(key);
		
		if(value == null) {
			throw new RuntimeException("Value cannot be null on key: " + key);
		}
		return value.split(PATH_SEP);
	}

	public String[] getFromPaths() {
		return fromPaths;
	}

	public String[] getToPaths() {
		return toPaths;
	}
}
