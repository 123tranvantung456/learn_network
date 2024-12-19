package com.javaweb.udp.SNTCungNhau;

import java.io.*;
import java.net.*;
import java.util.Random;

public class Server {
    public static void main(String[] args) {
        int port = 12345;

        try (DatagramSocket serverSocket = new DatagramSocket(port)) {
            System.out.println("Server is listening on port " + port);

            byte[] receiveData = new byte[1024];
            Random random = new Random();

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);  // Nhận dữ liệu từ client

                String received = new String(receivePacket.getData(), 0, receivePacket.getLength());
                int n = Integer.parseInt(received);
                System.out.println("Received n: " + n);

                if (n == 1) {
                    break;  // Kết thúc khi nhận được 1 từ client
                }

                // Sinh một số ngẫu nhiên m trong khoảng từ 2 đến n
                int m = random.nextInt(n - 1) + 2;
                System.out.println("Sending m: " + m);

                // Gửi lại số m cho client
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                byte[] sendData = Integer.toString(m).getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);

                if (n == m) {
                    System.out.println("Client confirmed. Closing connection.");
                    break;
                }
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
