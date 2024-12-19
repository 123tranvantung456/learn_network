package com.javaweb.NoGUI.NgayThangSNT;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 12345;

        try (Socket socket = new Socket(hostname, port)) {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // Sử dụng Scanner để nhập chuỗi từ người dùng
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Nhập chuỗi ngày tháng năm (DD/MM/YYYY) hoặc 'exit' để thoát:");
                String message = scanner.nextLine();

                // Nếu người dùng nhập 'exit', thoát khỏi vòng lặp
                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("Đóng kết nối.");
                    break;
                }

                // Gửi chuỗi nhập từ người dùng tới server
                writer.println(message);

                // Nhận kết quả từ server
                String response = reader.readLine();
                System.out.println("Kết quả từ server: " + response);
            }

        } catch (UnknownHostException ex) {
            System.out.println("Không tìm thấy server: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Lỗi I/O: " + ex.getMessage());
        }
    }
}
