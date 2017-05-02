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
					
				}else{
					System.out.println("Invalid node address and port");
					continue;
				}
				break;
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			
			StreamHelper.writeToOutput(out, "SET:12345:Marco:Polo");
			System.out.println("Waiting for response...");
			System.out.println("Received on client: " + StreamHelper.readFromInput(in));
			//Write code here
			
		}finally{
			scanner.close();
			if(clientSocket != null)clientSocket.close();
		}
	}
}
