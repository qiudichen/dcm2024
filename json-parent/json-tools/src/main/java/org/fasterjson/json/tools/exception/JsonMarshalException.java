package org.fasterjson.json.tools.exception;

public class JsonMarshalException extends Exception {
    public JsonMarshalException( String msg ) {
        super(msg);
    }

    public JsonMarshalException( String msg, Throwable t ) {
        super(msg, t);
    }
}
