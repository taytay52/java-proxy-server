//importing list networking and sockets  
import java.io.*;
import java.net.*;
import java.util.*;

public class ProxyServer {
//the port for the proxy server 8888
//the chach websites making the max limit to 3 
//the list.txt puts puts the URL's that are listed into there 
    
    private static final int PORT = 8888;
    private static final int MAX_CACHE = 3;
    private static final String CACHE_LIST = "list.txt";

    public static void main(String[] args) {
      
     //creating the server socket to connect to the client 
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Proxy Server running on port " + PORT);
      
      // the connection from client to the server 
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");
            
      // doing whatever the client says to the sever to run and do 
            handleClient(clientSocket);
      
      //closing the client and the server 
            clientSocket.close();
            serverSocket.close();
      //cathing the erros the might come 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
      //the method that will have the clients requests handeld 
    private static void handleClient(Socket clientSocket) throws IOException {

      //read whatever the is sent for the client 
      //and send it back 
        BufferedReader clientInput =
                new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter clientOutput =
                new PrintWriter(clientSocket.getOutputStream(), true);
     
      //looking to see the url website from the client 
        String url = clientInput.readLine();
        System.out.println("Requested URL: " + url);
      
      //looking at the url to see if it is cashed or not with a if statment 
      //if it is then senf it to the cached page 
      // else look in the sever to get what is required 
        if (isCached(url)) {
            sendCachedPage(url, clientOutput);
        } else {
            fetchFromServer(url, clientOutput);
        }
    }
      //looking if the URL is in the txt.list already and if its doesnt exist return false
    private static boolean isCached(String url) throws IOException {
        File file = new File(CACHE_LIST);
        if (!file.exists()) return false;

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
      
      //takes the urls and compare them to the requested url 
        while ((line = reader.readLine()) != null) {
            if (line.equals(url)) {
                reader.close();
                return true;
            }
        }
      //returns false if the url is in the cache
        reader.close();
        return false;
   
      //sends the webpage to the client and says file names   
    private static void sendCachedPage(String url, PrintWriter clientOutput) throws IOException {
        File file = new File(getFileName(url));
        BufferedReader reader = new BufferedReader(new FileReader(file));
      
     //sends the code to the client 
        String line;
        while ((line = reader.readLine()) != null) {
            clientOutput.println(line);
        }

        reader.close();
    }
      //takes the webpage from the original server if its not cached 
    private static void fetchFromServer(String url, PrintWriter clientOutput) throws IOException {

      //connects to the sever port 80 
        Socket serverSocket = new Socket(url, 80);

      //sends the http get request
        PrintWriter serverOutput =
                new PrintWriter(serverSocket.getOutputStream(), true);

        BufferedReader serverInput =
                new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
     
      //requesting the homepage 
        serverOutput.println("GET / HTTP/1.1");
        serverOutput.println("Host: " + url);
        serverOutput.println("Connection: close");
        serverOutput.println();
        serverOutput.flush();
     
      //read the web server see if the http is 200
        StringBuilder response = new StringBuilder();
        String line;
        boolean is200 = false;
      
      //save the reponce 
        while ((line = serverInput.readLine()) != null) {
            response.append(line).append("\n");

      //true statment 
            if (line.contains("200 OK")) {
                is200 = true;
            }
      
            clientOutput.println(line);
        }
      //close the connection to the web server 
        serverInput.close();
        serverSocket.close();

        if (is200) {
            cachePage(url, response.toString());
        }
    }
      //saving the webpage to the file caching
    private static void cachePage(String url, String content) throws IOException {

      //write html file updating the list.txt to get a new url 
        PrintWriter writer = new PrintWriter(getFileName(url));
        writer.print(content);
        writer.close();

        updateCacheList(url);
    }
      
      //updating the list.txt file 
    private static void updateCacheList(String url) throws IOException {

        List<String> urls = new ArrayList<>();

        File file = new File(CACHE_LIST);
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                urls.add(line);
            }
            reader.close();
        }
      
      // takes the oldest url and remove it because the limit is 3 
        if (urls.size() == MAX_CACHE) {
            String removed = urls.remove(0);
            new File(getFileName(removed)).delete();
        }

      //adding a new url
        urls.add(url);

      //update the txt.list with the updated urls 
        PrintWriter writer = new PrintWriter(CACHE_LIST);
        for (String u : urls) {
            writer.println(u);
        }
        writer.close();
    }

      //convert the url to the correct file name 
    private static String getFileName(String url) {
        return url.replaceAll("[^a-zA-Z0-9]", "") + ".txt";
    }
}
