package lu.uni.reseaux_info.testing;

import java.io.IOException;

import lu.uni.reseaux_info.commons.ConnectionInfo;
import lu.uni.reseaux_info.node.NodeData;
import lu.uni.reseaux_info.node.NodeLauncher;

public class TestEnvironment {

	public static void main(String[] args) throws IOException {
		final int ports[] = new int[]{1234, 9876, 1357};

		launchNodeAsync(ports[0], ports);
		launchNodeAsync(ports[1], ports);
		launchNodeAsync(ports[2], ports);
	}
	
	private static void launchNodeAsync(int port, int... neighborPorts){
		new Thread(() -> {
			NodeLauncher nodeLauncher = null;
			try {
				NodeData data = new NodeData();
				for(int nport : neighborPorts){
					if(nport != port)data.getNeighborAddresses().add(new ConnectionInfo("0.0.0.0", nport));
				}
				nodeLauncher = new NodeLauncher(port, data);
				nodeLauncher.launch();
			} catch (IOException e) {
				System.out.format("Testing server on port %d has been terminated\n", port);
				e.printStackTrace();
			} finally {
				if(nodeLauncher != null){
					try {
						nodeLauncher.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
}
