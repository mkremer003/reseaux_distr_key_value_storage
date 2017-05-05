package lu.uni.reseaux_info.node;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import lu.uni.reseaux_info.commons.ConnectionInfo;

public class NodeLauncher {
	
	private final NodeData data;
	private final ServerSocket welcomeSocket;
	
	public NodeLauncher(int port, NodeData data) throws IOException{
		this.data = data;
		this.welcomeSocket = new ServerSocket(port);
	}
	
	public NodeLauncher(int port) throws IOException{
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
		
		final NodeLauncher nodeLauncher = new NodeLauncher(port, data);
		final Thread serverThread = new Thread(() -> {
			try {
				nodeLauncher.launch();
			} catch (IOException e) {
				System.err.println("Node execution will be terminated due to an unexpected error:");
				e.printStackTrace();
				System.exit(1);
			}
		});
		serverThread.start();
		
		try(Scanner s = new Scanner(System.in)){
			System.out.println("You can enter commands over the command line. Enter \"help\" for an overview of available commands.");
			while(true){
				String input[] = s.nextLine().split(" ");
				if(input.length >= 1){
					if(input[0].equalsIgnoreCase("help")){
						System.out.println("Available commands:");
						System.out.println("* help\t\t\tShows available commands");
						System.out.println("* add <ip> <port>\tAdds a neighbor node to the list");
						System.out.println("* list\t\t\tShows currently registered neighbors");
						System.out.println("* mappings\t\tShows currently registered key to value mappings");
						System.out.println("* save <file>\t\tSaves the data of this node to a file");
						System.out.println("* load <file>\t\tLoads the data for this node from a file");
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
					}else if(input[0].equalsIgnoreCase("save")){
						if(input.length >= 2){
							try{
								data.saveToFile(new File(input[1]));
								System.out.println("Data has been saved");
							}catch(IOException e){
								System.err.println("Data could not be saved");
								e.printStackTrace();
							}
						}else{
							System.out.println("Expected format: save <file>");
						}
					}else if(input[0].equalsIgnoreCase("load")){
						if(input.length >= 2){
							try{
								if(data.loadFromFile(new File(input[1]))){
									System.out.println("Data has been loaded");
								}else{
									System.out.println("Data was not loaded");
								}
							}catch(IOException e){
								System.err.println("Data could not be loaded");
								e.printStackTrace();
							}
						}else{
							System.out.println("Expected format: load <file>");
						}
					}else if(input[0].equalsIgnoreCase("exit")){
						System.out.println("Terminating server thread...");
						nodeLauncher.close();
						break;
					}else{
						System.out.println("Unknown command: " + input[0]);
					}
				}
			}
		}
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
	
	public void close() throws IOException{
		welcomeSocket.close();
	}

}
