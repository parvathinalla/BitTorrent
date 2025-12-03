Team members :
1. Parvathi Nalla 
2. Bindhu sree reddy Alla

Github Link: https://github.com/parvathinalla/BitTorrent

This project implements a simplified peer-to-peer (P2P) file sharing system modeled after the BitTorrent protocol. The system enables multiple peers to exchange pieces of a file over TCP connections, maintain information about available pieces, and coordinate downloads through choking, unchoking, and interest mechanisms.

The implementation follows the project specification provided in the UF CISE course.

---

## 1. Overview

Each peer runs as an independent process.
Peers communicate using TCP sockets, exchange file pieces, and reconstruct the complete file.
The protocol includes handshake messages, bitfields, choke/unchoke control, and piece request/response messages.

The program terminates when all peers have downloaded the complete file.

---

## 2. Implemented Components

### 2.1 Configuration

Two configuration files are used:

Common.cfg

* NumberOfPreferredNeighbors
* UnchokingInterval
* OptimisticUnchokingInterval
* FileName
* FileSize (in bytes)
* PieceSize (in bytes)

PeerInfo.cfg

* PeerID
* Hostname
* Port
* HasFile (1 or 0)

### 2.2 Handshake

Each connection begins with a 32-byte handshake containing:

* Header string “P2PFILESHARINGPROJ”
* 10 zero bytes
* 4-byte peer ID

The handshake verifies that the correct peers are communicating.

### 2.3 Message Types

After the handshake, all communication uses length-prefixed messages.
All message types specified in the project are implemented:

* choke (0)
* unchoke (1)
* interested (2)
* not interested (3)
* have (4)
* bitfield (5)
* request (6)
* piece (7)

### 2.4 Bitfield Handling

Each peer maintains a bitfield indicating which pieces it owns.
After handshake, peers exchange bitfields.
The bitfield is used to determine whether a peer sends “interested” or “not interested”.

### 2.5 Choking and Unchoking

The preferred neighbors are recalculated every UnchokingInterval seconds using the download rate.
If a peer has the full file, preferred neighbors are chosen randomly among interested peers.

Every OptimisticUnchokingInterval seconds, one choked but interested neighbor is chosen randomly for optimistic unchoking.

### 2.6 Requesting and Sending Pieces

When unchoked by a neighbor, a peer sends a request for a piece it does not yet have.
A peer responds to a request by sending the corresponding piece.

Piece selection is random among the available pieces held by the neighbor.

Only one outstanding request per connection is maintained at a time.

### 2.7 File Chunking and Reconstruction

Files are divided into fixed-size chunks according to PieceSize.
The final piece is smaller if necessary.

Binary-safe reading and writing is used:

* FileInputStream for reading
* FileOutputStream for writing

This ensures that binary files such as JPGs are reconstructed correctly without corruption.

---

## 3. Logging

Each peer generates a log file named:

```
log_peer_[peerID].log
```

The following events are logged as required by the specification:

* Connections to other peers
* Incoming connections
* Preferred neighbors changes
* Optimistic unchoking changes
* Choking and unchoking events
* Received “have” messages
* Received “interested” and “not interested” messages
* Completed piece downloads
* Completion of full file download

The timestamp format follows standard date and time output.

---

## 4. Directory Structure

The working directory contains:

* peerProcess executable (Java entry point)
* Common.cfg
* PeerInfo.cfg
* log files for each peer

Each peer uses its own subdirectory:

```
peer_[peerID]/
```

Peers with HasFile = 1 must have the original file placed inside their directory before execution.

Peers reconstruct the complete file in their own directory after receiving all pieces.

---

## 5. Running the Program

1. Prepare all peer directories (e.g., peer_1001, peer_1002, etc.).
2. Place the full file only in peers marked with HasFile = 1.
3. Compile the Java source files.
4. Start each peer in the order listed in PeerInfo.cfg using:

```
java peerProcess <peerID>
```

Each peer connects automatically to all peers listed before it.

---

## 6. Termination

A peer monitors the completion state of all other peers using received bitfields and “have” messages.
When a peer determines that every peer has obtained the complete file, it performs cleanup and terminates.

---

## 7. Notes on File Handling

The system supports all file types because binary I/O is used.
This ensures that image files, PDFs, TXT files, ZIP archives, and other binary formats reconstruct without corruption.

The value of FileSize in Common.cfg must be the exact size (in bytes) of the file to be distributed.

---

## 8. Summary

This implementation completes all functional requirements of the P2P file sharing project, including:

* TCP connectivity
* Handshake protocol
* All message types
* Choking/unchoking mechanism
* Piece request and transfer
* Bitfield and state tracking
* Logging format required in the specification
* Reliable binary file reconstruction
* Proper termination

The system successfully allows multiple peers to exchange pieces and download the complete file collaboratively.