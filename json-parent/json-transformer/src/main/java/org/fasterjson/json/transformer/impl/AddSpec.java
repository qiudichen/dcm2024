package org.fasterjson.json.transformer.impl;

import java.util.Map;

/**
 * 
 *    {
 *        "transformer": "add",
 *        "spec": [
 *        	{ "path" : "arryPath", "label" : "homePage", "value" : "132"},
 *        	{ "label" : "page/pageId", "value" : "abc" }
 *        ]
 *    }
 * 
 * 
 * @author DavidChen
 *
 */

public class AddSpec extends BaseSpec {
	private final static String KEY_VALUE = "value";
	
	private final static String KEY_LABEL = "label";

	private String label;

	private Object value;
	
	public AddSpec(Map<String, String> spec) {
		super(spec);
		this.label = spec.get(KEY_LABEL);
		this.value = spec.get(KEY_VALUE);
	}

	public String getLabel() {
		return label;
	}

	public Object getValue() {
		return value;
	}
}
