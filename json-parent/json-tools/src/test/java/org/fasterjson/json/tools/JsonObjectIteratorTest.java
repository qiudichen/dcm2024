package org.fasterjson.json.tools;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonObjectIteratorTest {


	@Test
	public void readTest() {
		try {
			ObjectMapper objectMapper = new ObjectMapper();

			JsonWriter writer = new JsonWriterImpl(objectMapper);
			
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			JsonGenerator generator = writer.createGenerator(outStream);
			
			JsonIterateReaderImpl jsonutil = new JsonIterateReaderImpl(objectMapper, "/inputArray.json");
			
			writer.writeStartArray(generator);
			
			while(jsonutil.hasNext()) {
				Map<String, Object> item = jsonutil.next();
				writer.writeObject(generator, item);
				System.out.println(item);
			}
			writer.writeEndArrayAndclose(generator);
			outStream.close();
			System.out.println(outStream.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
