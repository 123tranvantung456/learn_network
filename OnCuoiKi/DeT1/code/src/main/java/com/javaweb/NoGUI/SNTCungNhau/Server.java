package com.javaweb.NoGUI.SNTCungNhau;
import java.io.*;
import java.net.*;
import java.util.Random;

public class Server {
    public static void main(String[] args) {
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                new ClientHandler(socket).start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            Random random = new Random();

            int attempts = 0;
            while (attempts < 10) {
                String received = reader.readLine();
                if (received == null) {
                    break;
                }

                int n = Integer.parseInt(received);
                System.out.println("Received n: " + n);

                if (n == 1) {
                    break;
                }

                // Sinh một số ngẫu nhiên m trong khoảng từ 2 đến n
                int m = random.nextInt(n - 1) + 2;
                System.out.println("Sending m: " + m);
                writer.println(m); // Gửi m cho client

                if (n == m) {
                    System.out.println("Client confirmed. Closing connection.");
                    break;
                }

                attempts++;
            }

            System.out.println("Closing connection with client.");
            socket.close();

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
