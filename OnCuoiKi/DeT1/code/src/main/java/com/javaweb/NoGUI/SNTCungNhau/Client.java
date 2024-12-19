package com.javaweb.NoGUI.SNTCungNhau;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Random;

public class Client {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 12345;

        try (Socket socket = new Socket(hostname, port)) {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            Random random = new Random();
            int n = random.nextInt(108) + 2; // Tạo số ngẫu nhiên từ 2 đến 109
            System.out.println("Generated n: " + n);

            while (true) {
                writer.println(n); // Gửi n cho server

                String response = reader.readLine();
                int m = Integer.parseInt(response);
                System.out.println("Received m: " + m);

                int t = countCoprimes(m); // Tính số lượng các số nguyên tố cùng nhau với m
                System.out.println("Number of coprimes (t) with m: " + t);

                if (isValid(m, n, t)) {
                    System.out.println("Số " + m + " gửi về hợp lệ");
                    writer.println("1"); // Gửi "1" để kết thúc
                    break;
                } else {
                    System.out.println("Số " + m + " gửi về không hợp lệ");
                }
            }

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }

    private static int countCoprimes(int m) {
        int count = 0;
        for (int i = 1; i <= m; i++) {
            if (gcd(m, i) == 1) {
                count++;
            }
        }
        return count;
    }

    private static boolean isValid(int m, int n, int t) {
        HashSet<Integer> remainders = new HashSet<>();
        for (int i = 1; i <= t; i++) {
            int remainder = (int) (Math.pow(m, i) % n);
            if (!remainders.add(remainder)) {
                return false; // Nếu có số trùng, m không hợp lệ
            }
        }
        return true; // Nếu không có số trùng, m hợp lệ
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}