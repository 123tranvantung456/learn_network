package com.javaweb.Bai1;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client {
    public static void main(String[] args) {
        Socket socket = null;

        try {
            socket = new Socket("localhost", 6996);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                Scanner sc = new Scanner(System.in);
                System.out.print("Enter String: ");
                String str = sc.nextLine();
                out.println(str);

                for (int i = 0; i < 11; i++) {
                    String response = in.readLine();
                    System.out.println(response);
                }
                //in.readLine() : tra ve null neu dong luong xuat o server
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }
}

