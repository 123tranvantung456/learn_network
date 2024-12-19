package com.javaweb.udp.NgayThangSNT;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 12345;

        try (DatagramSocket socket = new DatagramSocket()) {
            Scanner scanner = new Scanner(System.in);
            InetAddress serverAddress = InetAddress.getByName(hostname);

            while (true) {
                System.out.println("Nhập chuỗi ngày tháng năm (DD/MM/YYYY) hoặc 'exit' để thoát:");
                String message = scanner.nextLine();

                // Nếu người dùng nhập 'exit', thoát khỏi vòng lặp
                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("Đóng kết nối.");
                    break;
                }

                // Gửi chuỗi nhập từ người dùng tới server
                byte[] sendData = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, port);
                socket.send(sendPacket);

                // Nhận kết quả từ server
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Kết quả từ server: " + response);
            }

        } catch (UnknownHostException ex) {
            System.out.println("Không tìm thấy server: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Lỗi I/O: " + ex.getMessage());
        }
    }
}
