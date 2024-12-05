package com.javaweb.time;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        Socket socket = null;
        try {
            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 6996;

            // Địa chỉ và cổng của máy local
            InetAddress localAddress = InetAddress.getByName("localhost");
            int localPort = 5000; // Cổng local được chỉ định

            // Tạo socket với local port cụ thể
            socket = new Socket(serverAddress, serverPort, localAddress, localPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(in.readLine());
        }catch (IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
