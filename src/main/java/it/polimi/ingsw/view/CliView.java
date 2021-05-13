package it.polimi.ingsw.view;
import it.polimi.ingsw.Message;
import it.polimi.ingsw.MessageType;
import it.polimi.ingsw.controller.ControllerEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CliView{
    static Socket clientSocket;
    static final int defaultPortNumber=4545;
    static private PrintWriter out;
    static private BufferedReader in;

    public static void main(String[] args){
        //Args[0]=server ip
        //Args[1]=server port
        int serverPort;
        if(args.length==1){
            serverPort=defaultPortNumber;
        }else if(args.length==2){
            serverPort=Integer.parseInt(args[1]);
        }else{
            return;
        }

        try{
            clientSocket=new Socket(args[0],serverPort);
        }catch(Exception e){
            System.out.print("Error while opening client socket");
        }

        try{
            out=new PrintWriter(clientSocket.getOutputStream(),true);
            in=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }catch(Exception e){
            System.out.print("Error while creating buffers");
        }

        while(true){
            String input;
            System.out.println("Enter command: ");
            Scanner inConsole = new Scanner(System.in);
            try {
                input = inConsole.nextLine();
            }catch(Exception e){
                return;
            }
            String[] inputSplit=input.split(" ");
            switch(inputSplit[0]){
                case "connect":
                    if(inputSplit.length==2){
                        out.println(new Message(MessageType.CONNECT,new String[]{inputSplit[1]}));
                        System.out.println("connect request sent");
                    }else{
                        System.out.println("Too few arguments");
                    }
                    break;
                case "creategame":
                    if(inputSplit.length==3){
                        out.println(new Message(MessageType.CREATEGAME,new String[]{inputSplit[1],inputSplit[2]}));
                        System.out.println("creategame request sent");
                    }else{
                        System.out.println("Too few arguments");
                    }
                    break;
                case "joingame":
                    if(inputSplit.length==2){
                        out.println(new Message(MessageType.JOINGAME,new String[]{inputSplit[1]}));
                        System.out.println("joingame request sent");
                    }else{
                        System.out.println("Too few arguments");
                    }
                    break;
                default:
                    System.out.println("Command not recognized");
                    break;
            }
        }
    }
}
