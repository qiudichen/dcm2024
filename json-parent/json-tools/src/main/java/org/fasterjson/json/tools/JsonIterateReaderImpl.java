package org.fasterjson.json.tools;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class JsonIterateReaderImpl implements Iterator<Map<String, Object>>, Closeable {

    private static final TypeReference<Map<String, Object>> mapTypeReference =
            new TypeReference<Map<String, Object>>() {};
            
	private final ObjectMapper objectMapper;
	private final JsonFactory jsonFactory;
	private JsonParser jsonParser;
	
	private final InputStream inputStream;
	
	private Map<String, Object> nextObject;
	
	private boolean isInitialized;
	
	public JsonIterateReaderImpl(ObjectMapper objectMapper, String classPath) {
		this.objectMapper = objectMapper == null ? new ObjectMapper() : objectMapper;
		configureStockJSonObjectMapper(objectMapper);
		this.jsonFactory = this.objectMapper.getFactory();
		this.jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
		this.inputStream = this.getClass().getResourceAsStream(classPath);
	}
	
	public JsonIterateReaderImpl(String classPath) {
		this(new ObjectMapper(), classPath);
	}
	
    private void init() {
        this.initJsonParser();
        this.initFirstElement();
        this.isInitialized = true;
    }
    
    private void initJsonParser() {
		try {
            this.jsonParser = jsonFactory.createParser(inputStream);
        } catch (final IOException e) {
            throw new RuntimeException("There was a problem setting up the JsonParser: " + e.getMessage(), e);
        }
	}
	    
    private void initFirstElement() {
        try {
            // Check that the first element is the start of an array
            final JsonToken arrayStartToken = this.jsonParser.nextToken();
            if (arrayStartToken != JsonToken.START_ARRAY) {
                throw new IllegalStateException("The first element of the Json structure was expected to be a start array token, but it was: " + arrayStartToken);
            }

            // Initialize the first object
            this.initNextObject();
        } catch (final Exception e) {
            throw new RuntimeException("There was a problem initializing the first element of the Json Structure: " + e.getMessage(), e);
        }

    }
    
    private void initNextObject() {
        try {
            JsonToken nextToken = this.jsonParser.nextToken();
            
            // Check for the end of the array which will mean we're done
            if (nextToken == JsonToken.END_ARRAY) {
                this.nextObject = null;
                return;
            }

            // Make sure the next token is the start of an object
            if (nextToken != JsonToken.START_OBJECT) {
                throw new IllegalStateException("The next token of Json structure was expected to be a start object token, but it was: " + nextToken);
            }

            // Get the next product and make sure it's not null
            this.nextObject = this.jsonParser.readValueAs(mapTypeReference);
            if (this.nextObject == null) {
                throw new IllegalStateException("The next parsed object of the Json structure was null");
            }
        } catch (final Exception e) {
            throw new RuntimeException("There was a problem initializing the next Object: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean hasNext() {
    	if (!this.isInitialized) {
    		this.init();
    	}
    	return this.nextObject != null;
    }
    
    @Override
    public Map<String, Object> next() {
    	// Makes sure we're initialized first
        if (!this.isInitialized) {
            this.init();
        }
        
        // Store the current next object for return
        final Map<String, Object> currentNextObject = this.nextObject;
        
        // Initialize the next object
        this.initNextObject();
        
        return currentNextObject;
    }
            
	@Override
	public void close() throws IOException {
		closeQuietly(this.jsonParser);
        closeQuietly(this.inputStream);		
	}

	public static void closeQuietly(Closeable closeable) {
		try {
			closeable.close();
		} catch(Exception e) {
			
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
