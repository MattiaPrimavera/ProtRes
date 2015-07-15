# ProtRes

## Usage: 

* Démarrer en premier le Serveur:
	java ServeurCentrale tcpPORT

* Puis démarrer chaque client:
	java Client serverAddress tcpServerPort udpServerPort data

## Synopsis

This project is basic implementation of the [Peer-to-Peer (P2P)](https://en.wikipedia.org/wiki/Peer-to-peer) architecture. A Server centralizes the informations on the pairs while the data exchange is a charge on the exchanging clients.

Each pair has an information to share, in this software a text, let's say his own favourite song title as an example. When the pair accedes the network by connecting to the central server, he retrieves all the informations on the other pairs in order to share them his own favourite song title. In the meanwhile, the other pairs have been notified of a new entry in the network and they all starts sharing their information to the new participant. 

## Evolution

A few modifications to the code could make this software easily evoluate to support other format of data exchange, including our personal pictures, audio or video content. In this case, the automatic process of sharing implemented here should be changed to allow the pair to choose the file to get and which pair to download from. 