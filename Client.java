import java.util.*;
import java.io.*;
import java.net.*;

//Usage: java Client serverAddress tcpServerPort udpServerPort donnees
public class Client{
	private ClientTCP tcpClient;
	private ServeurUDP udpServeur;

	public Client(String serverAddress, int tcpServerPort, int udpServerPort, String donnees){
		//Thread pour la reception de donnees de la part des autres clients
		Thread udpServerThread = new Thread((this.udpServeur = new ServeurUDP(udpServerPort)));
		udpServerThread.start();
		//Thread pour la communication avec le ServeurCentrale et l'envoie des donnees aux autres clients.
		Thread tcpClientThread = new Thread((this.tcpClient = new ClientTCP(serverAddress, tcpServerPort, udpServerPort, donnees)));
		tcpClientThread.start();
	}
	public static void main(String [] args){
		try{
			Client client = new Client(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3]);
		}catch(Exception e){
			System.out.println("\nSyntax Error!!!!");
			System.out.println("Usage:\njava Client ipAddressServer tcpServerPort udpServerPort StringDonnees");
		}
	}
}


