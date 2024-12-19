package com.javaweb.udp.NgayThangSNT;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server {
    public static void main(String[] args) {
        int port = 12345;

        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("Server đang lắng nghe trên cổng " + port);

            byte[] buffer = new byte[1024];

            while (true) {
                // Nhận gói tin từ client
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(receivePacket);

                String receivedText = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received: " + receivedText);

                // Xử lý chuỗi ngày tháng năm
                String response = processInput(receivedText);

                // Gửi phản hồi lại cho client
                byte[] responseData = response.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(
                        responseData, responseData.length, receivePacket.getAddress(), receivePacket.getPort());
                socket.send(responsePacket);
            }

        } catch (SocketException ex) {
            System.out.println("Socket error: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }

    private static String processInput(String input) {
        String[] dateParts = input.split("/");

        // Kiểm tra định dạng ngày tháng năm
        if (dateParts.length != 3) {
            return "Invalid date format!";
        }

        try {
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);

            // Kiểm tra ngày, tháng, năm có phải là số nguyên tố hay không
            if (isPrime(day) && isPrime(month) && isPrime(year)) {
                return "Day la 1 ngay tuyet voi";
            } else {
                return "Ngay, thang, nam khong phai la so nguyen to!";
            }

        } catch (NumberFormatException e) {
            return "Invalid date format!";
        }
    }

    private static boolean isPrime(int number) {
        if (number <= 1) return false;
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
