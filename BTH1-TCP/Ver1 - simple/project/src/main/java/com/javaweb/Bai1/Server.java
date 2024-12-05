package com.javaweb.Bai1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        boolean running = true;

        try{
            serverSocket = new ServerSocket(6996);
            System.out.println("Server stated, waiting for connection...");
            socket = serverSocket.accept();
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connection Established!");
            while(running){
                String receiveString = input.readLine();
                System.out.println("String of client: " + receiveString);

                String reversedString = Str.reverseString(receiveString);
                String upperCaseString = Str.toUpperCase(receiveString);
                String lowerCaseString = Str.toLowerCase(receiveString);
                String mixedCaseString = Str.mixCase(receiveString);
                int wordCount = Str.countWords(receiveString);
                Map<Character, Integer> vowels = Str.countVowels(receiveString);

                StringBuilder vowelStr = new StringBuilder();
                for (Map.Entry<Character, Integer> it : vowels.entrySet()) {
                    vowelStr.append("vowel: ").append(it.getKey()).append(" : ").append(it.getValue()).append("\n");
                }

                output.println("reversedString: " + reversedString
                    + "\nupperCaseString: " + upperCaseString
                        + "\nlowerCaseString: " + lowerCaseString
                        + "\nmixedCaseString: " + mixedCaseString
                        + "\nwordCount: " + wordCount
                        + "\n" + vowelStr
                );
            }
        }
        catch(IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        finally {
            try {
                if (socket != null) {
                    socket.close();
                }
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }
}