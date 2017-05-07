package lu.uni.reseaux_info.testing;

import static lu.uni.reseaux_info.testing.TestEnvironment.launchAsync;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import lu.uni.reseaux_info.client.ClientLauncher;
import lu.uni.reseaux_info.node.Node;

public class BroadcastEnvironment {

	public static void main(String[] args) throws IOException, InterruptedException {
		final Node network[] = new Node[8];
		for(int i = 0 ; i < network.length ; i++)network[i] = new Node();
		
		link(network[0], network[1], network[2], network[5]);
		link(network[1], network[2], network[4], network[5]);
		link(network[2], network[0], network[5], network[4]);
		link(network[4], network[6], network[7], network[3]);
		link(network[5], network[4], network[2], network[6]);
		link(network[6], network[7], network[3], network[4]);
		link(network[7], network[4], network[3], network[6]);
		
		for(int i = 0 ; i < network.length ; i++)launchAsync(network[i]);
		
		Thread.sleep(3000);
		System.out.println("\nSending SET query in 3...");
		Thread.sleep(1000);
		System.out.println("Sending SET query in 2...");
		Thread.sleep(1000);
		System.out.println("Sending SET query in 1...");
		Thread.sleep(1000);
		
		System.out.println("\nLaunching client...");
		String clientInput = "0.0.0.0:" + network[3].getPort() + "\r\nSET:Hello:Aloha\r\n";
		InputStream in = new ByteArrayInputStream(clientInput.getBytes(StandardCharsets.UTF_8));
		System.setIn(in);
		ClientLauncher.main(new String[]{"-no-loop"});

		Thread.sleep(1000);
		System.out.println("\nSending GET query in 10...");
		Thread.sleep(7000);
		System.out.println("Sending GET query in 3...");
		Thread.sleep(1000);
		System.out.println("Sending GET query in 2...");
		Thread.sleep(1000);
		System.out.println("Sending GET query in 1...");
		Thread.sleep(1000);
		
		System.out.println("\nLaunching client...");
		clientInput = "0.0.0.0:" + network[0].getPort() + "\r\nGET:Hello\r\n";
		in = new ByteArrayInputStream(clientInput.getBytes(StandardCharsets.UTF_8));
		System.setIn(in);
		ClientLauncher.main(new String[]{"-no-loop"});
		System.exit(0);
	}
	
	private static void link(Node origin, Node... nodes){
		for(Node n : nodes){
			origin.getData().getNeighborAddresses().add(n.getConnectionInfo());
		}
	}

}
