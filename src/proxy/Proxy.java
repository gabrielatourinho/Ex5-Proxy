package proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

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
        System.out.println("Aguardando conexão.");
        socket_s = ss.accept();
        System.out.println("Conexão estabelecida.");
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

                System.out.println("Mensagem recebida do cliente a ser enviada ao servidor ["+socket_s.getInetAddress().getHostName()+"]: "+message);
                
                if (message.equals("GET "))
                    break;
                
                request(message);
                getResponse();
                returnResponse();
                
            }
            
            
        } catch (IOException e){
            System.err.println("Error receiving Web request");
        }
    }
    
    public void returnResponse() {
        
        try {
            
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
            
        } catch (Exception e){
            System.err.println("Error in HTTP request");
        }
    }
   
    public void getResponse(){
        int c;
        try {
            response = "";
            while ((c = input_r.read()) != -1){
                if (c == 1)
                    break;
                
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
            System.out.println("Iniciando proxy.");
            Proxy p = new Proxy();
            
            p.getRequest();
            
            System.out.println("Encerrando conexão.");
            p.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
