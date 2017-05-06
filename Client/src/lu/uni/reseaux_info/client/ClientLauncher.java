package lu.uni.reseaux_info.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import lu.uni.reseaux_info.commons.StreamHelper;

public class ClientLauncher {

	public static void main(String[] args) throws IOException {
		Socket clientSocket = null;
		try(Scanner scanner = new Scanner(System.in);) {
			while (true) {
				while (true) {
					System.out.println("Enter node address and port: (Format: ADDRESS:PORT)");
					String input[] = scanner.nextLine().split(":");
					if (input.length == 2) {
						String ip = input[0];
						int port = 0;
						try {
							port = Integer.parseInt(input[1]);
							if (port < 0) {
								System.out.println("Invalid port: " + input[1]);
								continue;
							}
						} catch (NumberFormatException e) {
							System.out.println("Invalid port: " + input[1]);
							continue;
						}
						clientSocket = new Socket(ip, port);
						System.out.println("Connected to client " + ip + ":" + port);
					} else {
						System.out.println("Invalid node address and port");
						continue;
					}
					break;
				}
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
				
				String[] packageString = new String[3];
				while(true) {
					System.out.println("Enter your message (q to quit)");
					System.out.println("Expected format: TYPE:KEY or TYPE:KEY:VALUE");
					System.out.println("Allowed message types: SET or GET");
					packageString = scanner.nextLine().split(":");
					if(packageString[0].equals("GET") || packageString[0].equals("SET")) {
						break;
					}else{
						System.out.println("Wrong input. Your message has to be of the format TYPE:KEY or TYPE:KEY:VALUE. Accepted types: GET, SET");
					}
				}
				
				if(packageString[0].equals("GET")) {
					StreamHelper.writeToOutput(out,
							packageString[0] + ":" + System.currentTimeMillis() % 100000 + ":" + packageString[1]);
					
				}else if(packageString[0].equals("SET")) {
					StreamHelper.writeToOutput(out,
							packageString[0] + ":" + System.currentTimeMillis() % 100000 + ":" + packageString[1] + ":" + packageString[2]);
					
				}else if(packageString[0].equals("q")){
					break;
				}
				
				System.out.println("Waiting for response...");
				System.out.println("Received on client: " + StreamHelper.readFromInput(in));
				System.out.println("Closing connection.");
				
			}

		} finally {
			if (clientSocket != null)
				clientSocket.close();
		}
	}
}
