package com.javaweb.udp.SNTCungNhau;

import java.net.*;
import java.util.HashSet;
import java.util.Random;

public class Client {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 12345;

        try (DatagramSocket clientSocket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName(hostname);
            Random random = new Random();
            int n = random.nextInt(108) + 2; // Tạo số ngẫu nhiên từ 2 đến 109
            System.out.println("Generated n: " + n);

            byte[] sendData;
            byte[] receiveData = new byte[1024];

            while (true) {
                // Gửi n cho server
                sendData = Integer.toString(n).getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, port);
                clientSocket.send(sendPacket);

                // Nhận m từ server
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                int m = Integer.parseInt(response);
                System.out.println("Received m: " + m);

                int t = countCoprimes(m); // Tính số lượng các số nguyên tố cùng nhau với m
                System.out.println("Number of coprimes (t) with m: " + t);

                if (isValid(m, n, t)) {
                    System.out.println("Số " + m + " gửi về hợp lệ");

                    // Gửi "1" để kết thúc
                    sendData = "1".getBytes();
                    sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, port);
                    clientSocket.send(sendPacket);
                    break;
                } else {
                    System.out.println("Số " + m + " gửi về không hợp lệ");
                }
            }

        } catch (Exception ex) {
            System.out.println("Client error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static int countCoprimes(int m) {
        int count = 0;
        for (int i = 1; i <= m; i++) {
            if (gcd(m, i) == 1) {
                count++;
            }
        }
        return count;
    }

    private static boolean isValid(int m, int n, int t) {
        HashSet<Integer> remainders = new HashSet<>();
        for (int i = 1; i <= t; i++) {
            int remainder = (int) (Math.pow(m, i) % n);
            if (!remainders.add(remainder)) {
                return false; // Nếu có số trùng, m không hợp lệ
            }
        }
        return true; // Nếu không có số trùng, m hợp lệ
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}
