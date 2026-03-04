# Java Proxy Server

## Description
A Java-based HTTP proxy server with a built-in caching system. 
The server intercepts client web requests, checks a local cache, 
and either serves cached content or fetches it from the live web server.
Includes a separate client for sending URL requests to the proxy.

## Features
- Handles HTTP GET requests from a client via socket connection
- Caches up to 3 web pages locally to reduce redundant requests
- Serves cached pages instantly without re-fetching from the web
- Automatically evicts the oldest cached page when the limit is reached (FIFO)
- Validates HTTP 200 OK responses before caching content
- Client and server communicate over localhost on port 8888

## How It Works
1. **ProxyClient** connects to the proxy server and sends a URL
2. **ProxyServer** checks `list.txt` to see if the page is already cached
3. If cached → serves the saved file directly to the client
4. If not cached → fetches the page via HTTP GET from the real server
5. If response is 200 OK → saves content locally and updates `list.txt`
6. If cache is full (3 URLs) → removes oldest entry before adding new one

## Technologies Used
- Java
- Socket Programming (`ServerSocket`, `Socket`)
- HTTP Protocol (GET requests)
- File I/O (`BufferedReader`, `PrintWriter`)
- Java Collections (`ArrayList`)

## How to Run
1. Clone the repository
2. Compile both files:
```
   javac ProxyServer.java
   javac ProxyClient.java
```
3. In one terminal, start the server:
```
   java ProxyServer
```
4. In another terminal, run the client:
```
   java ProxyClient
```
5. Enter a URL when prompted (e.g. `www.google.com`)

## What I Learned
- Socket programming and client-server architecture in Java
- How HTTP GET requests and responses work at a low level
- Implementing a FIFO cache with a fixed size limit
- File I/O for persistent cache storage
- How proxy servers act as intermediaries between clients and web servers
