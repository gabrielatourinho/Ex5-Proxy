/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webretriever;
import java.io.*;
import java.net.*;
/**
 *
 * @author Gabriela
 */
public class WebRetriever {

    private Socket socket;
    private OutputStream output;
    private InputStream input;
    
    WebRetriever (String server, int port) throws IOException, UnknownHostException {
        socket = new Socket(server, port);
        output = socket.getOutputStream();
        input = socket.getInputStream();
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
