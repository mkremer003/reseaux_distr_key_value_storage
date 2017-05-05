package lu.uni.reseaux_info.node;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

import lu.uni.reseaux_info.commons.ConnectionInfo;

public class NodeLauncher implements AutoCloseable, Closeable{
	
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
						if(args[i].startsWith("-")){
							i--;
							break;
						}else{
							try{
								data.getNeighborAddresses().add(new ConnectionInfo(args[i]));
							}catch(IllegalArgumentException e){
								System.out.println("Invalid neighbor: " + args[i]);
							}
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
				if(input.length >= 1 && !CommandLineHandler.handleCommand(input, data, nodeLauncher)){
					break;
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
	
	@Override
	public void close() throws IOException{
		welcomeSocket.close();
	}

}
