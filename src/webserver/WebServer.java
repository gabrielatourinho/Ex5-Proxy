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
    private InputStream input;
    private BufferedReader in;
    private PrintStream out;
    private String resource;
    
    WebServer(Socket s) throws IOException {
        socket = s;
        output = socket.getOutputStream();
        input = socket.getInputStream();
        in = new BufferedReader(new InputStreamReader(input));
        out = new PrintStream(output);
        resource = "";
    }
    
    public void getRequest() { // recebe uma mensagem do cliente
        try{
            while (true){
                String message;
                message = in.readLine();

                System.out.println("Mensagem recebida do cliente ["+socket.getInetAddress().getHostName()+"]: "+message);
                
                if (message == null || message.equals("GET "))
                    break;
                
                //out.println(message);
                StringTokenizer t = new StringTokenizer(message);
                String token = t.nextToken(); // get first token
                System.out.println("token: "+token);
                if (token.equals("GET"))      // if token is "GET" // quando começa com GET
                    resource = t.nextToken(); // get second token  // armazena o restante da string em resource
                System.out.println("resource: "+resource);
                
                returnResponse();
                
            }
            
            
        } catch (IOException e){
            System.err.println("Error receiving Web request");
        }
    }
    
    public void returnResponse() {
        int c;
        try {
            FileInputStream f = new FileInputStream(resource); //tenta abrir um arquivo com o path name do resource!
            
            while ((c = f.read()) != -1){
                output.write(c);
            }
            output.write(1); // :)
            
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
            System.out.println("Iniciando Servidor.");
            ServerSocket s = new ServerSocket(3323); //responsável por esperar a conexão do cliente, passamos a porta para escutar a conexão
            
            System.out.println("Aguardando conexão.");
            WebServer w = new WebServer(s.accept()); //aguarda até a conexão ser aceita
            
            System.out.println("Conexão estabelecida.");
            
            w.getRequest();
            //w.returnResponse();
            
            System.out.println("Encerrando servidor.");
            w.close();
            
        } catch(IOException i){
            System.err.println("IOException in Server");
        }
    }
    
}
