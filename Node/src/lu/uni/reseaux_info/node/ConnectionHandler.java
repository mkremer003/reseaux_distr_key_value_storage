package lu.uni.reseaux_info.node;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import lu.uni.reseaux_info.commons.ConnectionInfo;
import lu.uni.reseaux_info.commons.StreamHelper;

public class ConnectionHandler extends Thread {

	private final Socket connection;
	private final NodeData data;

	ConnectionHandler(Socket connection, NodeData data) {
		this.data = data;
		this.connection = connection;
	}

	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

			String inputLine = StreamHelper.readFromInput(in);
			String[] message = inputLine.split(":");
			System.out.println("Received: " + inputLine);

			if (!data.getTreatedIdSet().contains(Integer.parseInt(message[1]))) {
				data.getTreatedIdSet().add(Integer.parseInt(message[1]));
				// SET Method
				if (message[0].equals("SET")) {
					data.getKeyMap().put(message[2], message[3]);
					System.out.println("Set: " + message[2] + " -> " + data.getKeyMap().get(message[2]));
					StreamHelper.writeToOutput(out,
							"RES:" + System.currentTimeMillis() % 100000 + ":" + message[2] + ":" + message[3]);

					// GET Method
				} else if (message[0].equals("GET")) {
					if (data.getKeyMap().get(message[2]) != null) {
						StreamHelper.writeToOutput(out, "RES:" + System.currentTimeMillis() % 100000 + ":" + message[2]
								+ ":" + data.getKeyMap().get(message[2]));
					} else {
						boolean foundKey = false;
						ArrayList<ConnectionInfo> neighbors;
						synchronized (data.getNeighborAddresses()) {
							neighbors = new ArrayList<>(data.getNeighborAddresses());
						}
						for (ConnectionInfo neighbor : neighbors) {
							if(!connection.getLocalAddress().getHostAddress().equals(neighbor.getIp()) || connection.getLocalPort() != neighbor.getPort()){
								try {
									System.out.println("Key not found at " + connection.getLocalAddress().getHostAddress() + ":" + connection.getLocalPort() + ", sending GET:" + message[1] + ":" + message[2]
											+ " package to " + neighbor.getIp() + ":" + neighbor.getPort());
									String response = requestAnswer(neighbor.getIp(), neighbor.getPort(),
											message[0] + ":" + message[1] + ":" + message[2]);
									String[] responseMessage = response.split(":");
									if (responseMessage[0].equals("RES") && !responseMessage[3].equalsIgnoreCase("null")) {
										foundKey = true;
										StreamHelper.writeToOutput(out, response);
										break;
									}
								} catch (IOException e) {
									System.out.println("Host is unreachable: " + neighbor);
								}
							}
						}
						if (!foundKey) {
							System.out.println("Key not found.");
							StreamHelper.writeToOutput(out,
									"RES:" + System.currentTimeMillis() % 100000 + ":" + message[2] + ":null");
						}
					}
				} else {
					System.out.println(message[0] + " is a wrong package type. Only SET or GET packages are accepted.");
					StreamHelper.writeToOutput(out, "RES:" + System.currentTimeMillis() % 100000 + ":null:null");
				}
			} else {
				System.out.println("The package with the id " + message[1] + " has already been treated");
				StreamHelper.writeToOutput(out,
						"RES:" + System.currentTimeMillis() % 100000 + ":" + message[2] + ":null");
			}

		} catch (IOException e) {
			System.err.println("Connection with " + connection + " has been terminated due to an error");
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Connection with " + connection + " has been closed");
		}
	}

	private static String requestAnswer(String ip, int port, String messageToSend) throws IOException {
		Socket connectionToOtherNode = null;
		try {
			connectionToOtherNode = new Socket(ip, port);

			BufferedReader in = new BufferedReader(new InputStreamReader(connectionToOtherNode.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(connectionToOtherNode.getOutputStream()));

			StreamHelper.writeToOutput(out, messageToSend);
			return StreamHelper.readFromInput(in);
		} finally {
			if (connectionToOtherNode != null)
				connectionToOtherNode.close();
		}
	}
}
