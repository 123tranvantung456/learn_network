package com.javaweb.NoGUI.NgayThangSNT;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        int port = 12345;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            // Vòng lặp lắng nghe kết nối từ client
            while (true) {
                Socket socket = serverSocket.accept(); // Chấp nhận kết nối mới từ client
                System.out.println("New client connected");

                // Tạo luồng xử lý cho từng kết nối client
                new ClientHandler(socket).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true)
        ) {
            // Lắng nghe và xử lý nhiều chuỗi từ client
            String receivedText;
            while ((receivedText = reader.readLine()) != null) {
                System.out.println("Received: " + receivedText);

                // Xử lý chuỗi ngày tháng năm
                String response = processInput(receivedText);
                writer.println(response);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                socket.close(); // Đảm bảo đóng kết nối khi hoàn thành
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private String processInput(String input) {
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

    private boolean isPrime(int number) {
        if (number <= 1) return false;
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
