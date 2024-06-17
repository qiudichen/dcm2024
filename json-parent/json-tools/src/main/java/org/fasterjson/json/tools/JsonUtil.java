package org.fasterjson.json.tools;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.fasterjson.json.tools.exception.JsonMarshalException;
import org.fasterjson.json.tools.exception.JsonUnmarshalException;

public interface JsonUtil {
	List<Object> classpathToList(String classPath) throws JsonUnmarshalException;
	public Map<String, Object> classpathToMap( String classPath ) throws JsonUnmarshalException;
    List<Object> jsonToList( String json) throws JsonUnmarshalException ;
    List<Object> jsonToList( String json , String charset ) throws JsonUnmarshalException ;
    List<Object> jsonToList( InputStream in ) throws JsonUnmarshalException;
    public Map<String, Object> jsonToMap( InputStream in ) throws JsonUnmarshalException;
    public <T> T readValue(String classPath, Class<T> valueType) throws JsonUnmarshalException;
    public <T> T readValue(InputStream in, Class<T> valueType) throws JsonUnmarshalException;
}
