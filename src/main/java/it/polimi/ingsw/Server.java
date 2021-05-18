package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        Controller controller=new Controller();

        ExecutorService executor= Executors.newCachedThreadPool();
        while(true){
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected"+clientSocket.getInetAddress());
                executor.submit(new ClientHandler(clientSocket,controller));
            }catch(Exception e){
                System.out.println("Error while accepting client socket: "+e.getMessage());
            }
        }
    }
}
