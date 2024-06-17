

public class JavaToC {
	
	private native void helloC();
    
	public static void main(String[] args) {
        new JavaToC().helloC();
    }
    
	static {
        System.loadLibrary("JavaToC");
    }
}
