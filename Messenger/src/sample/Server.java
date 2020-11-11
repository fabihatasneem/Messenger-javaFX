package sample;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private ServerSocket serverSocket;
    Server() {
        try {
            serverSocket = new ServerSocket(44444);
            while(true) {
                Socket clientSocket = serverSocket.accept();
                serve(clientSocket);
            }
        }catch(IOException e){
                System.out.println("Server starts : "+e);
            }
        }

    public void serve(Socket clientSocket){
        NetworkUtil networkUtil = new NetworkUtil(clientSocket);
        new ReadServer(networkUtil);
    }

    public static void main(String[] args) {
        Server server = new Server();
    }
}