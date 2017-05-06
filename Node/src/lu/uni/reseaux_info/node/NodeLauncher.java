package lu.uni.reseaux_info.node;

import java.io.IOException;
import java.util.Scanner;

import lu.uni.reseaux_info.commons.ConnectionInfo;

public class NodeLauncher{

	public static void main(String[] args) throws IOException {
		final NodeData data = new NodeData();
		System.out.println("Starting node...");
		int port = 0;//0 means random port
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
		
		final Node node = new Node(port, data);
		final Thread serverThread = new Thread(() -> {
			try {
				node.launch();
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
				if(input.length >= 1 && !CommandLineHandler.handleCommand(input, data, node)){
					break;
				}
			}
		}
	}

}
