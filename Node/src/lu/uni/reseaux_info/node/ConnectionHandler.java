package lu.uni.reseaux_info.node;

import java.io.IOException;
import java.net.Socket;

public class ConnectionHandler extends Thread{
	
	private final Socket connection;

	ConnectionHandler(Socket connection){
		this.connection = connection;
	}
	
	@Override
	public void run(){
		try{
			//Write code here
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
