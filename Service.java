import java.util.*;
import java.io.*;
import java.net.*;

//Description de l'objet auquel on confie la tache de communiquer avec un client particulier et mettre a jour le ServeurCentrale
//avec les infos obtenues par le client et en cas de deconnexion du client.
class Service implements Runnable, Observable{
	private Socket s;
	private static ArrayList<String> listeClients;
	private static int nombreClients = 0;
	private Observer obs;
	private String actuelClientInfo;

	public Service(Socket s, ArrayList<String> contactList){
		this.s = s;
		actuelClientInfo = "";
		if((this.listeClients = contactList) == null)
			this.listeClients = new ArrayList<String>();
	}	
	public void run(){
		while(true){
			try{
				//On attends la requete du client...
				BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				String request = in.readLine();
				if(!request.startsWith(Protocole.prefixConnectRequest) && !request.startsWith(Protocole.prefixDeconnectRequest)) 
					continue;
				
				//cas requete de connexion, il faut lui envoyer la liste des clients disponibles pour le partage de donnees
				if(request.startsWith(Protocole.prefixConnectRequest)){
					int clientPort = Integer.parseInt(request.substring(Protocole.prefixConnectRequest.length(), request.length()));
					String clientAddressStr = s.getInetAddress().getHostAddress();
					this.nombreClients++;
					System.out.println("Client N^"+this.nombreClients+" at "+clientAddressStr+" on : "+clientPort);
					String tmpString = clientAddressStr+"#"+clientPort;
					this.actuelClientInfo = tmpString;
					this.sendContactList();
					this.listeClients.add(tmpString);
					this.notifyObserver(tmpString);					
				}//cas requete deconnexion, il faut enlever le client de la liste clients disponibles
				else if(request.startsWith(Protocole.prefixDeconnectRequest)){
					this.deconnectClient();
				}
			}
			catch(NullPointerException npe){
				this.deconnectClient();	
				//npe.printStackTrace(); 
				break; 
			}
			catch(SocketException se){ 
				System.out.println("Socket Closed...");	
				this.deconnectClient();
				se.printStackTrace();
				break; 
			}
			catch(Exception e){ e.printStackTrace(); }
		}
	}

	//redefinition methodes de l'interface Observable
	public void removeObserver(){
		this.obs = null;
	}

	public void addObserver(Observer obs){
		this.obs = obs;
	}

	public void notifyObserver(String str){
		this.obs.update(str);
	}

	//envoie au client connecte a la socket de cet objet Service les information sur le nouvel client connecte'
	public void updateClient(String update){
		try{
			PrintWriter out = new PrintWriter(new OutputStreamWriter(this.s.getOutputStream()));
			out.println(Protocole.updateRequest(update));
			out.flush();
			out.println(Protocole.clientListEnd);
			out.flush(); 	 
		}
		catch(IOException e){ e.printStackTrace(); }
	}

	//envoie de la liste de clients disponibles au client connecté à ce socket au moment de sa connexion.
	public void sendContactList(){
		try{
			PrintWriter out = new PrintWriter(new OutputStreamWriter(this.s.getOutputStream()));
			if(this.listeClients != null){
				for(String client : this.listeClients){
					out.println(client);
					out.flush();
				}
			}
			out.println(Protocole.clientListEnd);
			out.flush();
		}
		catch(IOException e){ e.printStackTrace(); }
	}

	//informe le ServeurCentrale de la deconnexion d'un client lui confiant les taches qu'une deconnexion implique
	public void deconnectClient(){
		this.nombreClients--;	
		this.notifyObserver(Protocole.deconnectFromServer(this.actuelClientInfo));
	}
}

interface Observable{
	public void addObserver(Observer obs);
	public void removeObserver();
	public void notifyObserver(String str);
}
