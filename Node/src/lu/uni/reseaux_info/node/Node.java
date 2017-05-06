package lu.uni.reseaux_info.node;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Node implements AutoCloseable, Closeable{
	
	private final NodeData data;
	private final ServerSocket welcomeSocket;
	
	public Node(int port, NodeData data) throws IOException{
		this.data = data;
		this.welcomeSocket = new ServerSocket(port);
	}
	
	public Node(int port) throws IOException{
		this(port, new NodeData());
	}
	
	public Node() throws IOException{
		this(0);
	}
	
	public void launch() throws IOException{
		try{
			System.out.println("Listening on " + welcomeSocket + "...");
			while(true){
				Socket connectionSocket = welcomeSocket.accept();
				System.out.println("Incoming connection from " + connectionSocket);
				new ConnectionHandler(connectionSocket, data).start();
			}
		}catch(SocketException e){
			System.out.println("Listening on " + welcomeSocket + " has been interrupted");
		}finally{
			welcomeSocket.close();
		}
	}
	
	@Override
	public void close() throws IOException{
		welcomeSocket.close();
	}
	
	public NodeData getData(){
		return data;
	}
	
	public int getPort(){
		return welcomeSocket.getLocalPort();
	}
	
	public String getAddress(){
		return welcomeSocket.getInetAddress().getHostAddress();
	}

}
