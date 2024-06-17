package org.fasterjson.json.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;

import org.fasterjson.json.tools.exception.JsonUnmarshalException;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class SeatLimitIntervalTest {

	@Test
	public void readTest() {
		ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        
		JsonUtil jsonutil = new JsonUtilImpl(objectMapper);

		try {
			SeatLimitInterval seatLimit = jsonutil.readValue("/seatlimit.json", SeatLimitInterval.class);

			FileOutputStream fileOutputStream = new FileOutputStream("seatLimit.txt");

			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(seatLimit);
			objectOutputStream.flush();
			objectOutputStream.close();

			FileInputStream fileInputStream
		      = new FileInputStream("seatLimit.txt");
		    ObjectInputStream objectInputStream
		      = new ObjectInputStream(fileInputStream);
		    
		    SeatLimitInterval p2 = (SeatLimitInterval) objectInputStream.readObject();
		    objectInputStream.close(); 
			
		} catch (JsonUnmarshalException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
