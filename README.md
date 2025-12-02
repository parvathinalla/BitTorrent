Team members :
1. Parvathi Nalla 
2. Bindhu sree reddy Alla

Output Screenshots are uploaded

Peer-to-Peer File Sharing System
Overview
This project implements a peer-to-peer file sharing system in Java. It consists of several Java classes that collectively enable peers to connect, communicate, and share files with one another. Below is a summary of the key components and functionalities implemented in this project:

Config.java
The Config class represents the configuration data for the peer-to-peer file sharing system. It reads configuration parameters from a common configuration file and a peer information file, providing methods to access these parameters.

FileManager.java
The FileManager class manages file read and write operations for a peer. It initializes file access for a specific peer and provides methods to read and write pieces of the shared file.

MyLogger.java
The MyLogger class offers logging functionality for peers in the system. It enables the logging of various events and messages, such as connection initiation, preferred neighbors, unchoking, and more, facilitating system monitoring and debugging.

Record.java
The Record class represents a record in the file sharing system, containing details about a specific peer, such as sockets for communication, download status, and file bit status. It helps manage the state of peers in the system.

PeerProcess.java
The PeerProcess class serves as the main entry point for the peer-to-peer file sharing system. It handles peer connections, incoming connections, message transmission, and disconnections. The class demonstrates how peers can connect to each other, listen for incoming connections, send common messages, and disconnect from peers.

Handshake Protocol
The project also implements a handshake protocol for initiating connections between peers. When a peer wants to connect to another peer, it sends a handshake message containing its own peer ID. Upon receiving the handshake, the receiving peer extracts the sender's peer ID and uses it to establish a connection.

Summary
The project is in the early stages of development, with key components for peer connectivity, communication, and the handshake protocol already implemented. Peers can connect to each other, send messages, and manage shared files. Future development will focus on enhancing file sharing capabilities, implementing a complete file transfer protocol, and further optimizing the system's functionality.