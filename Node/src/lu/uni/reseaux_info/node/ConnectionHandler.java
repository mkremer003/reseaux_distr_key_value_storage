package lu.uni.reseaux_info.node;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

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
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
			
			String inputLine = StreamHelper.readFromInput(in);
			String[] message = inputLine.split(":");
			System.out.println("Received: " + inputLine);
			if(message[0].equals("SET")) {
				data.getKeyMap().put(message[2], message[3]);
				System.out.println("Set: " + data.getKeyMap().get(message[2]));
				StreamHelper.writeToOutput(out, "RES:" + System.currentTimeMillis()%100000 + ":" + message[2] + ":" + message[3]);
				System.out.println("Response sent");
			}else if(message[0].equals("GET")) {
				//TODO: Get message implementation
			}else {
				System.out.println(message[0] + " is a wrong package type.");
			}
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
	
	private static String requestAnswer(String ip, int port, String messageToSend) throws IOException{
		Socket connectionToOtherNode = null;
		try{
			connectionToOtherNode = new Socket(ip, port);

			BufferedReader in = new BufferedReader(new InputStreamReader(connectionToOtherNode.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(connectionToOtherNode.getOutputStream()));
			
			StreamHelper.writeToOutput(out, messageToSend);
			return StreamHelper.readFromInput(in);
		}finally{
			if(connectionToOtherNode != null)connectionToOtherNode.close();
		}
	}
}
