package org.fasterjson.json.tools;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.fasterjson.json.tools.exception.JsonMarshalException;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonWriterImpl implements JsonWriter {
	private final ObjectWriter prettyPrintWriter;
	
	private final JsonFactory jsonFactory;
	
	private final ObjectMapper objectMapper;
	
	public JsonWriterImpl(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper == null ? new ObjectMapper() : objectMapper;
		this.jsonFactory = this.objectMapper.getFactory();
		this.jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);		
		prettyPrintWriter = this.objectMapper.writerWithDefaultPrettyPrinter();
	}	
	
	public JsonWriterImpl() {
		this(new ObjectMapper());
	}	
	
    @Override
    public String toPrettyJsonString( Object obj ) throws JsonMarshalException {
        try {
            return prettyPrintWriter.writeValueAsString( obj );
        }
        catch ( IOException e ) {
            throw new JsonMarshalException( "Unable to serialize object : " + obj, e );
        }
    }
    
    @Override
    public void writeValue(OutputStream out, Object obj) throws JsonMarshalException {
    	try {
    		prettyPrintWriter.writeValue(out, obj);
    	}
    	catch ( IOException e ) {
    		throw new JsonMarshalException( "Unable to serialize object : " + obj, e );
    	}
    }	
    
    @Override
	public JsonGenerator createGenerator(OutputStream out) throws IOException {
		return jsonFactory.createGenerator(out, JsonEncoding.UTF8);
	}
	
    @Override
	public void writeStartArray(JsonGenerator generator) throws IOException {
		generator.writeStartArray();
	}
	
    @Override
	public void writeObject(JsonGenerator generator, Object obj) throws IOException {
		String json = prettyPrintWriter.writeValueAsString(obj);
		generator.writeRawValue(json);
		generator.flush();
	}     
    
    @Override
	public void writeEndArrayAndclose(JsonGenerator generator) throws IOException {
		generator.writeEndArray();
		generator.close();
	}
}
