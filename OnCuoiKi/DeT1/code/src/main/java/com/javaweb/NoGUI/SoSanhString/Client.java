package com.javaweb.NoGUI.SoSanhString;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 12345;

        try (Socket socket = new Socket(serverAddress, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Đã kết nối tới server");

            while (true) {
                System.out.print("Nhập một chuỗi alphabet: ");
                String input = scanner.nextLine();

                out.write(input + "\n");
                out.flush();

                String response = in.readLine();
                System.out.println("Phản hồi từ server: " + response);

                if ("Khong dung dinh dang".equals(response)) {
                    continue;
                }

                // Đọc gợi ý từ server
                if ("Chuc mung vi da doan dung".equals(response)) {
                    break;
                }

                String hint = in.readLine(); // Gợi ý
                System.out.println(hint);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
