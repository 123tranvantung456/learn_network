package com.javaweb.Bai4;

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
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String command;
            while ((command = in.readLine()) != null) {
                System.out.println("Received command: " + command);

                String[] parts = command.split(" ", 2);
                String action = parts[0].toUpperCase();
                String data = parts.length > 1 ? parts[1] : "";

                switch (action) {
                    case "CREATE":
                        DatabaseManager.handleCreate(data, out);
                        break;
                    case "READ":
                        DatabaseManager.handleRead(out);
                        break;
                    case "UPDATE":
                        DatabaseManager.handleUpdate(data, out);
                        break;
                    case "DELETE":
                        DatabaseManager.handleDelete(data, out);
                        break;
                    default:
                        out.println("Invalid command!");
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (socket != null){
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
