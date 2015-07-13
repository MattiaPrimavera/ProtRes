import java.net.*;
import java.io.*;
import java.util.*;
import java.net.InetAddress;

//Usage: java ServeurCentrale tcpPORT
//Classe qui modelise un Serveur centralisant toutes les informations sur les clients qui veulent
//partager des donnees.	
public class ServeurCentrale implements Observer{
	private ArrayList<Service> listeClients;
	private ArrayList<String> listeClientsStr;
	private int tcpPort; 
	private int nombreConnexions;

	public ServeurCentrale(int port){
		this.tcpPort = port;
		this.nombreConnexions = 0;
		this.listeClients = new ArrayList<Service>();
		this.listeClientsStr = null;
	}
	
	//demarrage ServeurCentrale
	public void runServeur(){
		ServerSocket serverSocket = null;
		try{ 
			serverSocket = new ServerSocket(this.tcpPort);
			System.out.println("**************************************************************"); 
			System.out.println(" Server Address: "+InetAddress.getLocalHost());
			System.out.println("**************************************************************");
		}
		catch(Exception e){ e.printStackTrace();}
		do{
			try{
				Socket s = serverSocket.accept();
				nombreConnexions++;
				Service service = new Service(s, this.listeClientsStr);
				service.addObserver(this);
				Thread t = new Thread(service);
				t.start();
				this.listeClients.add(service);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}while(true);
	}

	//A chaque nouvelle connexion d'un client, on met a jour tous les autres clients...
	public void updateClients(String update){
		for(Service s : this.listeClients){
			//mais pas lui meme! 
			if(s == this.listeClients.get(this.listeClients.size()-1))
				continue;
			s.updateClient(update);
		}
	}

	//methode de l'interface Observer
	public void update(String str){
		//si un CLIENT a envoyé une requete de deconnexion
		if(str.startsWith(Protocole.prefixDeconnectRequest)){
			try{
				if(this.listeClientsStr.size() == 0) System.exit(0);
				System.out.println("Deconnexion CLIENT "+str.substring(Protocole.prefixDeconnectRequest.length(),str.length()));
				for(int i = 0 ; i < this.listeClientsStr.size(); i++){
					if(this.listeClientsStr.get(i).equals(str.substring(Protocole.prefixDeconnectRequest.length(), str.length()))){
						if(this.listeClientsStr.size() > 1){	
							this.listeClients.remove(i);
							this.listeClientsStr.remove(i);
						}
						else{
							this.listeClients = null;
							this.listeClientsStr = null;
						}
						this.nombreConnexions--;
					}
				}
			}catch(NullPointerException npe){ 
				System.out.println("All Clients Deconnected...");
				System.exit(0);
			}		
	 
			return;
		}
		//Sinon on a un update a faire
		boolean estDansListe = false;
		this.updateClients(str);
		if(this.listeClientsStr == null)
			this.listeClientsStr = new ArrayList<String>();
		else{
			
			for(String tmp: this.listeClientsStr)
				if(tmp.equals(str)) estDansListe = true;
		}
		if(!estDansListe) this.listeClientsStr.add(str);	
	}

	public static void main(String args[]){
		ServeurCentrale server = new ServeurCentrale(Integer.parseInt(args[0]));
		server.runServeur();
	}
}

interface Observer{
	public void update(String str);
}


	
