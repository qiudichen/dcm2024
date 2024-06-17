package org.fasterjson.json.tools;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.fasterjson.json.tools.exception.JsonUnmarshalException;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtilImplTest {

	@Test
	public void readTest() {
		ObjectMapper objectMapper = new ObjectMapper();
		
		JsonUtil jsonutil = new JsonUtilImpl(objectMapper);
		JsonWriter jsonWriter = new JsonWriterImpl(objectMapper);
		
		try {
			List<Object> specs = jsonutil.classpathToList("/spec.json");
			String specsString = jsonWriter.toPrettyJsonString(specs);
			System.out.println(specsString);
			
			Map<String, Object> input = jsonutil.classpathToMap("/input3.json");
			String inputString = jsonWriter.toPrettyJsonString(input);
			
			System.out.println(inputString);

			OutputStream out = new FileOutputStream("test.json");
			JsonGenerator generator = jsonWriter.createGenerator(out);
			jsonWriter.writeStartArray(generator);

			jsonWriter.writeObject(generator, specs);
			
			jsonWriter.writeEndArrayAndclose(generator);
			out.close();
			
		} catch (JsonUnmarshalException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
