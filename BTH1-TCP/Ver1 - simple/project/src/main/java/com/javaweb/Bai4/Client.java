package com.javaweb.Bai4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        Scanner scanner = new Scanner(System.in);

        try {
            socket = new Socket("localhost", 6996); // Kết nối đến server
            out = new PrintWriter(socket.getOutputStream(), true); // Tạo output stream để gửi dữ liệu
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Input stream để nhận dữ liệu từ server

            System.out.println("Connected to server. Enter your command:");

            while (true) {
                // Hiển thị menu và yêu cầu người dùng nhập lệnh
                System.out.println("Enter command (CREATE name,age / READ / UPDATE id,name,age / DELETE id): ");
                String command = scanner.nextLine();

                // Gửi lệnh từ client đến server
                out.println(command);

                // Nếu người dùng nhập "EXIT", thoát khỏi vòng lặp
                if (command.equalsIgnoreCase("EXIT")) {
                    System.out.println("Exiting...");
                    break;
                }

                // Nhận phản hồi từ server
                String response = in.readLine();
                System.out.println("Server response: " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Đóng các luồng và socket khi kết thúc
            try {
                if (scanner != null) {
                    scanner.close();
                }
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
