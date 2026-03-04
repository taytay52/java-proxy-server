//importing the input and the ouput classes 
//importing networking classes for sockets 
//importing user input with scanner 
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ProxyClient {

    public static void main(String[] args) {
        
      //connecting to the proxy sever with the the local host and the port 
        try {
            Socket socket = new Socket("127.0.0.1", 8888);

      //creating the oupput stream for the server 
            PrintWriter output =
                    new PrintWriter(socket.getOutputStream(), true);
      //creating the input stream for the server 
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));
      //creating a scaner for the user input 
      //string url taking in the website from the user when asked by the scanner to provide one
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter website (example: www.google.com): ");
            String url = scanner.nextLine();
      //sends the url to the sever 
            output.println(url);
      //says the severs responce 
            String line;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
      //closes the socket 
            socket.close();
      //syas the connection issues that may occuer 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
