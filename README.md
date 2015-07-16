# ProtRes


## Usage: 

First, you need to start the Server:

	java ServeurCentrale tcpPORT

To start one client:

	java Client serverAddress tcpServerPort udpServerPort textToShare

## Synopsis

This project is a basic implementation of the [Peer-to-Peer (P2P)](https://en.wikipedia.org/wiki/Peer-to-peer) architecture. A Server centralizes the informations on the pairs while the data exchange is a charge on the exchanging clients.

Each pair has an information to share, in this software a text, let's say his own favourite song title as an example. When the pair accedes the network by connecting to the central server, he retrieves all the informations on the other pairs in order to share them his own favourite song title. In the meanwhile, the other pairs have been notified of a new entry in the network and they all start sharing their information to the new participant. 

This way at the end of the exchange process, each participant to the network knows every other participant's favourite song. 

The combination of **CTRL+C** keys or an **program interrupt signal** on a client make him disconnect from the network after notifying the server.  

## Evolution

A few modifications to the code could make this software easily develop to support other format of data exchange, including our personal pictures, audio or video content. In this case, the automatic process of sharing implemented here should be changed to allow the pair to choose the file to get and which pair to download from, in order to avoid the possibility of a consistent but undesired network automatic exchange. 


