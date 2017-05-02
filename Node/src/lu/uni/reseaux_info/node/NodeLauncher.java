package lu.uni.reseaux_info.node;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NodeLauncher {

	public static void main(String[] args) throws IOException {
		final Map<String, String> data = Collections.synchronizedMap(new HashMap<String, String>());
		System.out.println("Starting node...");
		int port = 18065;
		for(int i = 0 ; i < args.length ; i++){
			String arg = args[i];
			if(arg.equalsIgnoreCase("-port") && i < args.length - 1){
				port = Integer.parseInt(args[++i]);
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
