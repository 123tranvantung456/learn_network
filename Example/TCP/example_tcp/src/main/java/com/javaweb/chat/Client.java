package com.javaweb.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class Client {
    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 6996);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Client: ");
                String clientStr = scanner.nextLine();
                out.println(clientStr);
                System.out.println("Server: " + in.readLine());
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
        }
    }
}
