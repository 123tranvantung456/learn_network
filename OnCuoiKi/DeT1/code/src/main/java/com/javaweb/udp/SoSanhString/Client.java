package com.javaweb.udp.SoSanhString;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 12345;

        try (DatagramSocket socket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Đã kết nối tới server qua UDP");

            while (true) {
                System.out.print("Nhập một chuỗi alphabet: ");
                String input = scanner.nextLine();

                // Gửi dữ liệu tới server
                byte[] sendData = input.getBytes();
                InetAddress serverInetAddress = InetAddress.getByName(serverAddress);
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverInetAddress, port);
                socket.send(sendPacket);

                // Nhận phản hồi từ server
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Phản hồi từ server: " + response);

                if ("Khong dung dinh dang".equals(response)) {
                    continue;
                }

                if ("Chuc mung vi da doan dung".equals(response)) {
                    break;
                }

                // Nhận gợi ý từ server
                socket.receive(receivePacket);
                String hint = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println(hint);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

