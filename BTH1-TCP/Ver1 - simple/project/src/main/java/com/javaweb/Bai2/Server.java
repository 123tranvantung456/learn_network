package com.javaweb.Bai2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(6996);
            socket = serverSocket.accept();
            System.out.println("Client connected: " + socket.getInetAddress() + " " + socket.getPort());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                String line = br.readLine();
                try {
                    String response = line + " = " + Cal.evaluateExpression(line);
                    pw.println(response);
                } catch (Exception e) {
                    String response = "error input";
                    pw.println(response);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

