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
    
    public void request(String path){
        try{
            String message = "GET"+path+"\n\n";
            output.write(message.getBytes());
            output.flush();
        } catch (IOException e){
            System.err.println("Error in HTTOP request");
        }
    }
   
    public void getResponse(){
        int c;
        try {
            while ((c = input.read()) != -1)
                System.out.print((char) c);
        } catch (IOException e) {
            System.err.println("IOException in reading from Web server");
        }
    }
    
    public void close(){
        try{
            input.close();
            output.close();
            socket.close();
        } catch (IOException e){
            System.err.println("IOException in closing connection");
        }
    }
    
    public static void main(String[] args) {
        try {
            WebRetriever w = new WebRetriever("www.nus.edu.sg", 80);
            w.request("/NUSinfo/UG/ug.html");
            w.getResponse();
            w.close();
        } catch (UnknownHostException h){
            System.err.println("Hostname Unknown");
        } catch (IOException i){
            System.err.println("IOException in connectins to Host");
        }
    }
    
}
