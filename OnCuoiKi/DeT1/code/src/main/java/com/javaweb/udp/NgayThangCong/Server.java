package com.javaweb.udp.NgayThangCong;

import java.io.*;
import java.net.*;
import java.util.Calendar;

public class Server {
    public static void main(String[] args) {
        int port = 12345;

        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                // Nhận dữ liệu từ client
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                String receivedText = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received: " + receivedText);

                // Xử lý dữ liệu
                String response = processInput(receivedText);

                // Gửi phản hồi về cho client
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                byte[] sendData = response.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                socket.send(sendPacket);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static String processInput(String input) {
        String[] dateParts = input.split("/");

        if (dateParts.length != 3) {
            return getCurrentDate();
        }

        try {
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);

            if (month < 1 || month > 12 || day < 1 || day > getDaysInMonth(month, year)) {
                return getCurrentDate();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month - 1, day);
            calendar.add(Calendar.DAY_OF_MONTH, 7);

            return String.format("%02d/%02d/%d", calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

        } catch (NumberFormatException e) {
            return getCurrentDate();
        }
    }

    private static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return String.format("%02d/%02d/%d", calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
    }

    private static int getDaysInMonth(int month, int year) {
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                return 31;
            case 4: case 6: case 9: case 11:
                return 30;
            case 2:
                return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) ? 29 : 28;
            default:
                return 0;
        }
    }
}
