package lu.uni.reseaux_info.node;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import lu.uni.reseaux_info.commons.connectionInfo;

public class NodeLauncher {

	public static void main(String[] args) throws IOException {
		final NodeData data = new NodeData();
		System.out.println("Starting node...");
		int port = 18065;
		for(int i = 0 ; i < args.length ; i++){
			String arg = args[i];
			if(arg.equalsIgnoreCase("-port") && i < args.length - 1){
				port = Integer.parseInt(args[++i]);
			}else if(arg.equalsIgnoreCase("-neighbors") && i < args.length - 1){
				for(i = i + 1 ; i < args.length ; i++){
					if(args[i] != null){
						String neighbor[] = args[i].split(":");
						if(neighbor[0].startsWith("-")){
							i--;
							break;
						}else if(neighbor.length == 2){
							data.getNeighborAddresses().add(new connectionInfo(neighbor[0], Integer.parseInt(neighbor[1])));
						}else{
							System.out.println("Invalid neighbor: " + args[i]);
						}
					}
				}
			}
		}
		ServerSocket welcomeSocket = null;
		try{
			welcomeSocket = new ServerSocket(port);
			System.out.println("Listening on " + welcomeSocket + "...");
			while(true){
				Socket connectionSocket = welcomeSocket.accept();
				System.out.println("Incoming connection from " + connectionSocket);
				new ConnectionHandler(connectionSocket, data).start();
			}
		}finally{
			if(welcomeSocket != null)welcomeSocket.close();
		}
	}

}
