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
		Scanner scanner = new Scanner(System.in);
		Socket clientSocket = null;
		try{
			while(true){
				System.out.println("Enter node address and port: (Format: ADDRESS:PORT)");
				String input[] = scanner.nextLine().split(":");
				if(input.length == 2){
					String ip = input[0];
					int port = 0; 
					try{
						port = Integer.parseInt(input[1]);
						if(port < 0){
							System.out.println("Invalid port: " + input[1]);
							continue;
						}
					}catch(NumberFormatException e){
						System.out.println("Invalid port: " + input[1]);
						continue;
					}
					clientSocket = new Socket(ip, port);
					System.out.println("Connected to client " + ip + ":" + port);
				}else{
					System.out.println("Invalid node address and port");
					continue;
				}
				break;
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			
			while(true) {
				int action = scanner.nextInt();
				System.out.println("1. Send a message\n2.EXIT");
				if(action == 1) {
					System.out.println("Enter the GET or SET message you wish to send in the format TYPE:KEY or TYPE:KEY:VALUE");
					String[] input = scanner.nextLine().split(":");
					if(input[0].equals("SET")) {
						StreamHelper.writeToOutput(out, input[0] + ":" + System.currentTimeMillis() % 100000 + ":" + input[1]);
					}else if(input[0].equals("GET")) {
						StreamHelper.writeToOutput(out, input[0] + ":" + System.currentTimeMillis() % 100000 + ":" + input[1] + ":" + input[2]);
					}else {
						System.out.println("Wrong package type. Only GET or SET is accepted.");
					}
					
					System.out.println("Waiting for response...");
					System.out.println("Received on client: " + StreamHelper.readFromInput(in));
				}else if(action == 2) {
					break;
				}else {
					System.out.println("Wrong input. Enter 1 or 2.");
				}
			}
			
			
		}finally{
			scanner.close();
			if(clientSocket != null)clientSocket.close();
		}
	}
}
