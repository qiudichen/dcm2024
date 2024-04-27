package org.fasterjson.json.gurobi_demo;

import java.io.OutputStream;
import java.io.PrintWriter;

public class StandardWriter {
	private PrintWriter writer;
	
	public StandardWriter() {
		writer = new PrintWriter(System.out);
	}
	
	public StandardWriter(OutputStream out) {
		writer = new PrintWriter(out);
	}
	
	public StandardWriter print(boolean b) {
		writer.print(b);
		writer.flush();
		return this;
	}
	
	public StandardWriter print(char c) {
		writer.print(c);
		writer.flush();		
		return this;
	}
	
	public StandardWriter print(int i) {
		writer.print(i);
		writer.flush();			
		return this;
	}
	
	public StandardWriter print(long l) {
		writer.print(l);
		writer.flush();			
		return this;
	}
	
	public StandardWriter print(float f) {
		writer.print(f);
		writer.flush();			
		return this;
	}
	
	public StandardWriter print(double d) {
		writer.print(d);
		writer.flush();					
		return this;
	}
	
	public StandardWriter print(char s[]) {
		writer.print(s);
		writer.flush();	
		return this;
	}
	
	public StandardWriter print(String s) {
		writer.print(s);
		writer.flush();	
		return this;
	}
	
	public StandardWriter print(Object obj) {
		if(obj instanceof Throwable) {
			((Throwable)obj).printStackTrace(writer);
		} else {
			writer.print(obj);
		}
		writer.flush();				
		return this;
	}
	
	public StandardWriter println() {
		writer.println();
		writer.flush();
		return this;
	}
	
	public StandardWriter println(boolean b) {
		writer.println(b);
		writer.flush();
		return this;
	}
	
	public StandardWriter println(char c) {
		writer.println(c);
		writer.flush();		
		return this;
	}
	
	public StandardWriter println(int i) {
		writer.println(i);
		writer.flush();			
		return this;
	}
	
	public StandardWriter println(long l) {
		writer.println(l);
		writer.flush();			
		return this;
	}
	
	public StandardWriter println(float f) {
		writer.println(f);
		writer.flush();			
		return this;
	}
	
	public StandardWriter println(double d) {
		writer.println(d);
		writer.flush();					
		return this;
	}
	
	public StandardWriter println(char s[]) {
		writer.println(s);
		writer.flush();	
		return this;
	}
	
	public StandardWriter println(String s) {
		writer.println(s);
		writer.flush();	
		return this;
	}
	
	public StandardWriter println(Object obj) {
		if(obj instanceof Throwable) {
			((Throwable)obj).printStackTrace(writer);
			writer.println();
		} else {
			writer.println(obj);
		}
		writer.flush();			
		return this;
	}
}
