package org.fasterjson.json.tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fasterjson.json.tools.exception.JsonMarshalException;
import org.fasterjson.json.tools.exception.JsonUnmarshalException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class JsonUtilImpl implements JsonUtil {
	public static final String DEFAULT_ENCODING_UTF_8 = "utf-8";

    private static final TypeReference<Map<String, Object>> mapTypeReference =
            new TypeReference<Map<String, Object>>() {};
    private static final TypeReference<List<Object>> listTypeReference =
            new TypeReference<List<Object>>() {};
            
	private final ObjectMapper objectMapper;
	
	private final ObjectWriter prettyPrintWriter;
	
	public JsonUtilImpl(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper == null ? new ObjectMapper() : objectMapper;
		configureStockJSonObjectMapper(this.objectMapper);
		//this.objectMapper.getFactory().configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
		prettyPrintWriter = this.objectMapper.writerWithDefaultPrettyPrinter();
	}
	
	public JsonUtilImpl() {
		this(new ObjectMapper());
	}
	
    @Override
    public List<Object> classpathToList(String classPath) throws JsonUnmarshalException {
        try {
            InputStream inputStream = this.getClass().getResourceAsStream(classPath);
            return jsonToList(inputStream);
        }
        catch (JsonUnmarshalException e) {
        	throw e;
        }
        catch (Exception e) {
        	throw new RuntimeException( "Unable to load JSON object from classPath : " + classPath, e );
        }
    }
    
    @Override
    public Map<String, Object> classpathToMap( String classPath ) throws JsonUnmarshalException {
        try {
            InputStream inputStream = this.getClass().getResourceAsStream( classPath );
            return jsonToMap( inputStream );
        }
        catch (JsonUnmarshalException e) {
        	throw e;
        }        
        catch ( Exception e ) {
            throw new RuntimeException( "Unable to load JSON map from classPath : " + classPath, e );
        }
    }
    
	@Override
	public List<Object> jsonToList(String json) throws JsonUnmarshalException {
        return jsonToList( json, DEFAULT_ENCODING_UTF_8 );

	}

	@Override
	public List<Object> jsonToList(String json, String charset) throws JsonUnmarshalException {
        try {
            return jsonToList( new ByteArrayInputStream( json.getBytes(charset) ) );
        }
        catch ( UnsupportedEncodingException e ) {
            throw new RuntimeException( e );
        }
	}

	@Override
	public List<Object> jsonToList(InputStream in) throws JsonUnmarshalException {
        try {
            return objectMapper.readValue(in, listTypeReference);
        } catch (IOException e) {
            throw new JsonUnmarshalException( "Unable to unmarshal JSON to a List.", e );
        }
	}

    @Override
    public Map<String, Object> jsonToMap( InputStream in ) throws JsonUnmarshalException {
        try {
            return objectMapper.readValue( in, mapTypeReference );
        }
        catch ( IOException e ) {
            throw new JsonUnmarshalException( "Unable to unmarshal JSON to a Map.", e );
        }
    }

    @Override
    public <T> T readValue(String classPath, Class<T> valueType) throws JsonUnmarshalException {
        try {
            InputStream inputStream = this.getClass().getResourceAsStream(classPath);
            return readValue(inputStream, valueType);
        }
        catch (JsonUnmarshalException e) {
        	throw e;
        }
        catch (Exception e) {
        	throw new RuntimeException( "Unable to load JSON object from classPath : " + classPath, e );
        }
    }
    
    @Override
    public <T> T readValue(InputStream in, Class<T> valueType) throws JsonUnmarshalException  {
    	try {
    		return objectMapper.readValue(in, valueType );
    	}
    	catch ( IOException e ) {
    		throw new JsonUnmarshalException( "Unable to unmarshal JSON to a Map.", e );
    	}
    }
    
    private void configureStockJSonObjectMapper( ObjectMapper objectMapper ) {

        // All Json maps should be deserialized into LinkedHashMaps.
        SimpleModule stockModule = new SimpleModule("stockJSonMapping", new Version(1, 0, 0, null, null, null));
        
        stockModule.addAbstractTypeMapping( Map.class, LinkedHashMap.class );
        stockModule.addAbstractTypeMapping( Set.class, HashSet.class );

        objectMapper.registerModule(stockModule);

        // allow the mapper to parse JSON with comments in it
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    }
    
}
