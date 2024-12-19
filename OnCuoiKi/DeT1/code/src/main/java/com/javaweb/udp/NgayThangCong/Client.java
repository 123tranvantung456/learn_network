package com.javaweb.udp.NgayThangCong;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 12345;

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName(hostname);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Nhập chuỗi ngày tháng năm (DD/MM/YYYY) hoặc nhập 'exit' để thoát: ");
                String message = scanner.nextLine();

                // Kiểm tra nếu người dùng muốn thoát
                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Đã thoát.");
                    break;
                }

                // Gửi chuỗi nhập từ người dùng tới server
                byte[] sendData = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
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

