package lu.uni.reseaux_info.node;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

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
			
			String message = StreamHelper.readFromInput(in);
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
