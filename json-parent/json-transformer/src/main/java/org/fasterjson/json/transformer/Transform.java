package org.fasterjson.json.transformer;

import java.util.Map;

public interface Transform {
	public Map<String, Object> apply(Map<String, Object> input);
}
