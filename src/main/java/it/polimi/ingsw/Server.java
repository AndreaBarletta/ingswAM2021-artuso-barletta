package it.polimi.ingsw;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static final int defaultPortNumber=4545;

    public static void main(String[] args){
        int portNumber;
        if(args.length==1){
            portNumber = Integer.parseInt(args[0]);
        }else{
            portNumber = defaultPortNumber;
        }

        ServerSocket serverSocket;
        try{
            serverSocket = new ServerSocket(portNumber);
        }catch(IllegalArgumentException e){
            System.out.println("Invalid port number");
            return;
        }catch(Exception e){
            System.out.print("Error while opening server socket");
            return;
        }
        System.out.print("Hello");
        try {
            Socket clientSocket = serverSocket.accept();
        }catch(Exception e){
            System.out.println("Error while accepting client socket");
            return;
        }

        System.out.println("Accepted client");
    }
}
