package webretriever;
import java.io.*;
import java.net.*;
import java.util.Scanner;
/**
 *
 * @author Gabriela
 */
public class WebRetriever {

    private Socket socket;
    private OutputStream output;
    private InputStream input;
    private BufferedReader in;
    private PrintStream out;
    
    WebRetriever (String server, int port) throws IOException, UnknownHostException {
        System.out.println("Iniciando conexão com servidor.");
        socket = new Socket(server, port);
        System.out.println("Conexão estabelecida.");
        output = socket.getOutputStream();
        input = socket.getInputStream();
        in = new BufferedReader(new InputStreamReader(input));
        out = new PrintStream(output);
    }
    
    public void request(){
        try{
            Scanner scanner = new Scanner(System.in);
            
            while (true){
                System.out.println("Digite o nome do arquivo que deseja acessar: ");
                String message = "GET "+scanner.nextLine();
                out.println(message);
                if (message.equals("GET "))
                    break;
                getResponse();   
            }
            
        } catch (Exception e){
            System.err.println("Error in HTTP request");
        }
    }
   
    public void getResponse(){
        int c;
        try {
            while ((c = input.read()) != -1){
                if (c == 1)
                    break;
                System.out.print((char) c);
            }
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
            System.out.println("Iniciando cliente.");
            
            WebRetriever w = new WebRetriever("localhost", 2525);
            
            w.request(); // envia as mensagens/solicitações ao servidor
            //w.getResponse();
            
            System.out.println("Encerrando conexão.");
            w.close();
            
        } catch (UnknownHostException h){
            System.err.println("Hostname Unknown");
        } catch (IOException i){
            System.err.println("IOException in connectins to Host");
        }
    }
    
}
