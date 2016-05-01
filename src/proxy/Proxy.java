/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxy;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author Gabriela
 */
public class Proxy {
    
    private ServerSocket ss;
    private Socket socket_s, socket_r;
    private OutputStream output_s, output_r;
    private InputStream input_s, input_r;
    private BufferedReader in_s, in_r;
    private PrintStream out_s, out_r;
    private String resource, response;
    
    public Proxy() throws IOException {
        //construindo o cliente do WebServer
        socket_r = new Socket("localhost", 3323);
        output_r = socket_r.getOutputStream();
        input_r = socket_r.getInputStream();
        in_r = new BufferedReader(new InputStreamReader(input_r));
        out_r = new PrintStream(output_r);
        response = "";
        
        //construindo o servidor do WebRetriever
        ss = new ServerSocket(2525);
        socket_s = ss.accept();
        output_s = socket_s.getOutputStream();
        input_s = socket_s.getInputStream();
        in_s = new BufferedReader(new InputStreamReader(input_s));
        out_s = new PrintStream(output_s);
        resource = "";
    }
    
    public void getRequest() { // recebe uma mensagem do cliente
        try{
            
            while (true){
                String message;
                message = in_s.readLine();

                System.out.println("Mensagem recebida do cliente ["+socket_s.getInetAddress().getHostName()+"]: "+message);
                
                if (message.equals("GET "))
                    break;
                
                //out.println(message);
                /*StringTokenizer t = new StringTokenizer(message);
                String token = t.nextToken(); // get first token
                System.out.println("token: "+token);
                if (token.equals("GET"))      // if token is "GET" // quando começa com GET
                    resource = t.nextToken(); // get second token  // armazena o restante da string em resource
                System.out.println("resource: "+resource);*/
                
                request(message);
                getResponse();
                returnResponse();
                
            }
            
            
        } catch (IOException e){
            System.err.println("Error receiving Web request");
        }
    }
    
    public void returnResponse() {
        int c;
        try {
            //output_s = input_r;
            
            /*System.out.println("antes");
            FileInputStream f = new FileInputStream(resource); //tenta abrir um arquivo com o path name do resource!
            System.out.println("depois");
            
            while ((c = f.read()) != -1){
                output_s.write(c);
            }
            output_s.write(1); // :)
            
            f.close();*/
            
            for (int i = 0; i < response.length(); i++){
                output_s.write(response.charAt(i));
            }
            output_s.write(1); // :)
            
        } catch (IOException e) {
            System.err.println("IOException in reading Web server");
        }
    }
    
    public void request(String message){
        try{
            out_r.println(message);
            /*Scanner scanner = new Scanner(System.in);
            
            while (true){
                System.out.println("Digite o nome do arquivo que deseja acessar: ");
                String message = "GET "+scanner.nextLine();
                out_r.println(message);
                if (message.equals("GET "))
                    break;
                getResponse();   
            }*/
            
            /*
            String message = "GET "+path+"\n\n";
            output.write(message.getBytes());
            output.flush();*/
            
        } catch (Exception e){
            System.err.println("Error in HTTP request");
        }
    }
   
    public void getResponse(){
        int c;
        try {
            while ((c = input_r.read()) != -1){
                if (c == 1)
                    break;
                //System.out.print((char) c);
                response = response + (char)c;
            }
        } catch (IOException e) {
            System.err.println("IOException in reading from Web server");
        }
    }
    
    public void close() {
        try {
            input_s.close();
            output_s.close();
            socket_s.close();
            input_r.close();
            output_r.close();
            socket_r.close();
        } catch (IOException e) {
            System.err.println("IOException in closing connection");
        }
    }
    
    public static void main(String args[]){
        try{
            //criar o cliente do servidor, depois o servidor do cliente
            System.out.println("merda");
            Proxy p = new Proxy();
            
            p.getRequest();
            
            p.close();
            
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("deu ruim");
        }
    }
}
