package com.javaweb.time;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(6996);
            while (true) {
                Socket socket = serverSocket.accept();
                serverSocket.close();
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                System.out.println(socket.isClosed());
                out.println(new Date());
                break;
            }
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Server Error");
        }
        finally {
            if(serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
