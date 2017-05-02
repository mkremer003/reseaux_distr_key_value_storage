package lu.uni.reseaux_info.commons;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class StreamHelper {
	
	/*public static String readFromInput(InputStream in) throws IOException{
		StringBuffer sb = new StringBuffer();
		int read;
		while((read = in.read()) >= 0){
			System.out.println("-> " + (char)read);
			sb.append((char)read);
		}
		return sb.toString();
	}
	
	public static void writeToOutput(OutputStream out, String message) throws IOException{
		out.write(message.getBytes());
		out.flush();
	}*/
	
	public static String readFromInput(BufferedReader in) throws IOException{
		return in.readLine();
	}
	
	public static void writeToOutput(BufferedWriter out, String message) throws IOException{
		out.write(message + "\n");
		out.flush();
	}
	
}
