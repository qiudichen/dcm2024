package org.fasterjson.json.tools;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.fasterjson.json.tools.exception.JsonMarshalException;

import com.fasterxml.jackson.core.JsonGenerator;

public interface JsonWriter {
    public String toPrettyJsonString( Object obj ) throws JsonMarshalException;
    public void writeValue(OutputStream out, Object obj ) throws JsonMarshalException;
    
    
	public JsonGenerator createGenerator(OutputStream out) throws IOException;
	
	public void writeStartArray(JsonGenerator generator) throws IOException;
	
	public void writeObject(JsonGenerator generator, Object obj) throws IOException;

	public void writeEndArrayAndclose(JsonGenerator generator) throws IOException;
}
