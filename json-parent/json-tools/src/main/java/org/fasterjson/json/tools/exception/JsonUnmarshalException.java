package org.fasterjson.json.tools.exception;

public class JsonUnmarshalException extends Exception {
    public JsonUnmarshalException( String msg ) {
        super(msg);
    }

    public JsonUnmarshalException( String msg, Throwable t ) {
        super(msg, t);
    }
}
