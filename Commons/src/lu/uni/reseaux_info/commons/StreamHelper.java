package lu.uni.reseaux_info.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamHelper {
	
	public static String readFromInput(InputStream in) throws IOException{
		StringBuffer sb = new StringBuffer();
		int read;
		while((read = in.read()) != -1){
			sb.append((char)read);
		}
		return sb.toString();
	}
	
	public static void writeToOutput(OutputStream out, String message) throws IOException{
		out.write(message.getBytes());
	}
	
}
