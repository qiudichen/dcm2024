package org.fasterjson.json.transformer.impl;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import org.fasterjson.json.tools.JsonUtilImpl;
import org.fasterjson.json.tools.JsonWriter;
import org.fasterjson.json.tools.JsonWriterImpl;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TransformChainBuilderTest {
	
	//@Test
	public void testRemove() {
		
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonUtilImpl jsonutil = new JsonUtilImpl(objectMapper);
			JsonWriter writer = new JsonWriterImpl(objectMapper);
			
			
			List<Object> specs = jsonutil.classpathToList("/specRemove.json");
			
			
			TransformChain transformChain = new TransformChainBuilder(specs).build();

			List<Object> inputs = jsonutil.classpathToList("/inputArray.json");
			
			
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			JsonGenerator generator = writer.createGenerator(outStream);
			
			writer.writeStartArray(generator);
			for(Object item : inputs) {
				transformChain.apply((Map<String, Object>)item);
				writer.writeObject(generator, item);
			}
			writer.writeEndArrayAndclose(generator);
			outStream.close();
			System.out.println(outStream.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAdd() {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonUtilImpl jsonutil = new JsonUtilImpl(objectMapper);
			JsonWriter writer = new JsonWriterImpl(objectMapper);
			
			
			List<Object> inputs = jsonutil.classpathToList("/addInputArray.json");

			List<Object> specs = jsonutil.classpathToList("/specAdd.json");
			
			
			TransformChain transformChain = new TransformChainBuilder(specs).build();

			
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			JsonGenerator generator = writer.createGenerator(outStream);
			
			writer.writeStartArray(generator);
			for(Object item : inputs) {
				transformChain.apply((Map<String, Object>)item);
				writer.writeObject(generator, item);
			}
			writer.writeEndArrayAndclose(generator);
			outStream.close();
			System.out.println(outStream.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
