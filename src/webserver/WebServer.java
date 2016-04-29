/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webserver;
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

/**
 *
 * @author Gabriela
 */
public class WebServer {
    
    private Socket socket;
    private OutputStream output;
    private DataInputStream input;
    private String resource;
    
    WebServer(Socket s) throws IOException {
        socket = s;
        output = socket.getOutputStream();
        input = new DataInputStream(socket.getInputStream());
        resource = "";
    }
    
    public void getRequest() {
        try{
            String message;
            
            while ((message = input.readLine()) != null) {
                if (message.equals(""))
                    break;
                System.err.println(message);
                StringTokenizer t = new StringTokenizer(message);
                String token = t.nextToken(); // get first token
                if (token.equals("GET"))      // if token is "GET"
                    resource = t.nextToken(); // get second token
            }
        } catch (IOException e){
            System.err.println("Error receiving Web request");
        }
    }
    
    public void returnResponse() {
        int c;
        try {
            FileInputStream f = new FileInputStream("."+resource);
            while ((c = f.read()) != -1)
                output.write(c);
            f.close();
        } catch (IOException e) {
            System.err.println("IOException in reading Web server");
        }
    }
    
    public void close() {
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("IOException in closing connection");
        }
    }
    
    public static void main(String args[]){
        try {
            ServerSocket s = new ServerSocket(8080);
            WebServer w = new WebServer(s.accept());
            w.getRequest();
            w.returnResponse();
        } catch(IOException i){
            System.err.println("IOException in Server");
        }
    }
    
}
