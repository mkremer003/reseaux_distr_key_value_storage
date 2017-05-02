package lu.uni.reseaux_info.node;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

import lu.uni.reseaux_info.commons.StreamHelper;

public class ConnectionHandler extends Thread{
	
	private final Socket connection;
	private final NodeData data;

	ConnectionHandler(Socket connection, NodeData data){
		this.data = data;
		this.connection = connection;
	}
	
	@Override
	public void run(){
		try{
			InputStream in = connection.getInputStream();
			OutputStream out = connection.getOutputStream();
			
			String[] message = StreamHelper.readFromInput(in).split(":");
			if(message[0].equals("SET")) {
				data.getKeyMap().put(message[2], message[3]);
			}else if(message[0].equals("GET")) {
				//TODO: Get message implementation
			}else {
				System.out.println(message[0] + " is a wrong package type.");
			}
			System.out.println("Received: " + message);
			//Write code here
		}catch(IOException e){
			System.err.println("Connection with " + connection + " has been terminated due to an error");
			e.printStackTrace();
		}finally{
			try {
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Connection with " + connection + " has been closed");
		}
	}
}
