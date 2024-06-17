package org.fasterjson.json.transformer.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fasterjson.json.transformer.Transform;

public class TransformChain implements Transform {
	
	private List<Transform> transforms;
	
	@Override
	public Map<String, Object> apply(Map<String, Object> input) {
		if(this.transforms == null || this.transforms.isEmpty()) {
			return input;
		}
		
		Map<String, Object> result = input;
		for(Transform f : transforms) {
			result = f.apply(input);
		}
		return result;
	}

    public static TransformChain fromSpec(List<Object> input) {
        return new TransformChainBuilder(input).build();
    }
    
    public void addTransform(Transform transform) {
    	if(this.transforms == null) {
    		this.transforms = new ArrayList<>();
    	}
    	this.transforms.add(transform);
    }
}
