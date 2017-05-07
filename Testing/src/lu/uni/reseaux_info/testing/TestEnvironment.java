package lu.uni.reseaux_info.testing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import lu.uni.reseaux_info.commons.ConnectionInfo;
import lu.uni.reseaux_info.node.Node;

public class TestEnvironment {

	public static void main(String[] args) throws IOException {
		try(Scanner s = new Scanner(System.in)){
			System.out.println("How many nodes do you want to launch?");
			final int node_count = s.nextInt();
			if(node_count > 0){
				System.out.format("Setting up network of %d linked nodes...\n", node_count);
				final Node nodes[] = new Node[node_count];
				final ArrayList<ConnectionInfo> addresses = new ArrayList<>();
				for(int i = 0 ; i < node_count ; i++){
					try{
						nodes[i] = new Node();
						addresses.add(nodes[i].getConnectionInfo());
					}catch(IOException e){
						System.err.println("Failed to create node. Retrying...");
						e.printStackTrace();
						i--;
					}
				}
				for(int i = 0 ; i < node_count ; i++){
					nodes[i].getData().getNeighborAddresses().addAll(addresses);
				}
				for(int i = 0 ; i < node_count ; i++){
					launchAsync(nodes[i]);
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {}
				System.out.println("You can now connect to any of the created nodes with the client application");
			}else{
				System.err.println("Amount of nodes must be positive!");
			}
		}
	}
	
	static void launchAsync(Node node){
		new Thread(() -> {
			try {
				node.launch();
			} catch (IOException e) {
				System.err.println("Termination of node " + node.getConnectionInfo() + " has been terminated");
				e.printStackTrace();
			}
		}).start();
	}
	
}
