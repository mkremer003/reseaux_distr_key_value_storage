package lu.uni.reseaux_info.node;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionHandler extends Thread{
	
	private final Socket connection;

	ConnectionHandler(Socket connection){
		this.connection = connection;
	}
	
	@Override
	public void run(){
		try{
			InputStream in = connection.getInputStream();
			OutputStream out = connection.getOutputStream();
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
