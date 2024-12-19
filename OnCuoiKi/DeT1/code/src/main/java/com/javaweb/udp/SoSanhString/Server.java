package com.javaweb.udp.SoSanhString;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class Server {
    public static void main(String[] args) {
        int port = 12345; // Port lắng nghe
        String randomString = generateRandomString(30);
        System.out.println("Chuỗi ngẫu nhiên x: " + randomString);

        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("Server đang lắng nghe trên cổng " + port);

            byte[] receiveData = new byte[1024];
            byte[] sendData;

            while (true) {
                // Nhận dữ liệu từ client
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String clientInput = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Nhận từ client: " + clientInput);

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Kiểm tra chuỗi có chỉ chứa ký tự alphabet hay không
                String response;
                if (!isAlphabetOnly(clientInput)) {
                    response = "Khong dung dinh dang";
                } else {
                    int comparison = clientInput.compareTo(randomString);
                    if (comparison > 0) {
                        response = ">";
                    } else if (comparison < 0) {
                        response = "<";
                    } else {
                        response = "Chuc mung vi da doan dung";
                        sendData = response.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                        socket.send(sendPacket);
                        break; // Kết thúc server khi đoán đúng
                    }
                }

                // Gửi phản hồi đến client
                sendData = response.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                socket.send(sendPacket);

                // Nếu phản hồi không phải "Khong dung dinh dang" và không đoán đúng, gửi gợi ý
                if (!"Khong dung dinh dang".equals(response) && !"Chuc mung vi da doan dung".equals(response)) {
                    String hint = "Gợi ý: " + generateHint(clientInput, randomString);
                    sendData = hint.getBytes();
                    sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                    socket.send(sendPacket);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Hàm tạo chuỗi ngẫu nhiên dài 30 ký tự
    private static String generateRandomString(int length) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    // Hàm kiểm tra chuỗi có chỉ chứa ký tự alphabet
    private static boolean isAlphabetOnly(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        for (char c : input.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    // Hàm tạo gợi ý cho client
    private static String generateHint(String clientInput, String randomString) {
        StringBuilder hint = new StringBuilder();
        if (clientInput.length() > 30) {
            clientInput = clientInput.substring(0, 30);
        }

        // Nếu độ dài chuỗi client khác chuỗi server, cân bằng độ dài
        if (clientInput.length() < randomString.length()) {
            hint.append(randomString.substring(0, clientInput.length()));
            for (int i = clientInput.length(); i < randomString.length(); i++) {
                hint.append("_");
            }
            return hint.toString();
        }

        for (int i = 0; i < randomString.length(); i++) {
            if (i < clientInput.length() && clientInput.charAt(i) == randomString.charAt(i)) {
                hint.append(clientInput.charAt(i));
            } else {
                hint.append("_");
            }
        }
        return hint.toString();
    }
}
