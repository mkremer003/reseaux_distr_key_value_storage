package lu.uni.reseaux_info.node;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import lu.uni.reseaux_info.commons.ConnectionInfo;

public class NodeLauncher {
	
	private final int port;
	private final NodeData data;
	
	public NodeLauncher(int port, NodeData data){
		this.port = port;
		this.data = data;
	}
	
	public NodeLauncher(int port){
		this(port, new NodeData());
	}

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
							data.getNeighborAddresses().add(new ConnectionInfo(neighbor[0], Integer.parseInt(neighbor[1])));
						}else{
							System.out.println("Invalid neighbor: " + args[i]);
						}
					}
				}
			}
		}
		
		final int finalPort = port;
		final Thread serverThread = new Thread(() -> {
			try {
				new NodeLauncher(finalPort, data).launch();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		});
		serverThread.start();
		
		try(Scanner s = new Scanner(System.in)){
			System.out.println("You can enter commands over the command line. Enter \"help\" for an overview of available commands");
			while(true){
				String input[] = s.nextLine().split(" ");
				if(input.length >= 1){
					if(input[0].equalsIgnoreCase("help")){
						System.out.println("Available commands:");
						System.out.println("* help\t\t\tShows available commands");
						System.out.println("* add <ip> <port>\t\t\tAdds a neighbor node to the list");
						System.out.println("* list\t\t\tShows currently registered neighbors");
						System.out.println("* mappings\t\t\tShows currently registered key to value mappings");
						System.out.println("* exit\t\t\tStops the execution of this node");
					}else if(input[0].equalsIgnoreCase("add")){
						if(input.length >= 3){
							try{
								String ip = input[1];
								int nport = Integer.parseInt(input[2]);
								data.getNeighborAddresses().add(new ConnectionInfo(ip, nport));
								System.out.println("Neighbor " + ip + ":" + nport + " has been added");
							}catch(NumberFormatException e){
								System.out.println("Invalid port number: " + input[2]);
							}
						}else{
							System.out.println("Expected format: add <ip> <port>");
						}
					}else if(input[0].equalsIgnoreCase("list")){
						List<ConnectionInfo> neighbors = data.getNeighborAddresses();
						synchronized(neighbors){
							System.out.println("Current neighbors:");
							for(ConnectionInfo ci : neighbors){
								System.out.println("* " + ci.getIp() + ":" + ci.getPort());
							}
						}
					}else if(input[0].equalsIgnoreCase("mappings")){
						Map<String, String> keyMap = data.getKeyMap();
						synchronized(keyMap){
							System.out.println("Current mappings:");
							for(Map.Entry<String, String> e : keyMap.entrySet()){
								System.out.println("* " + e.getKey() + " -> " + e.getValue());
							}
						}
					}else if(input[0].equalsIgnoreCase("exit")){
						System.out.println("Terminating server thread...");
						System.exit(0);//TODO: Not very safe
						break;
					}else{
						System.out.println("Unknown command: " + input[0]);
					}
				}
			}
		}
	}
	
	public void launch() throws IOException{
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
