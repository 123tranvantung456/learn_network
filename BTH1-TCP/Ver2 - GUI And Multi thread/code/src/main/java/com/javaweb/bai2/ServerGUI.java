package com.javaweb.bai2;

import com.javaweb.bai1.Str;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class ServerGUI {
    private JFrame frame;
    private JTextField textField;
    private JButton btnStartServer;
    private JTextArea textArea;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ServerGUI window = new ServerGUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ServerGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblPort = new JLabel("Port:");
        lblPort.setBounds(20, 10, 40, 25);
        frame.getContentPane().add(lblPort);

        textField = new JTextField();
        textField.setBounds(60, 10, 300, 25);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        btnStartServer = new JButton("Start Server");
        btnStartServer.setBounds(370, 10, 100, 25);
        frame.getContentPane().add(btnStartServer);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 50, 450, 300);
        frame.getContentPane().add(scrollPane);

        textArea = new JTextArea();
        scrollPane.setViewportView(textArea);
        textArea.setEditable(false);

        btnStartServer.addActionListener(e -> {
            String portText = textField.getText().trim();
            if (portText.isEmpty()) {
                textArea.append("Error: Port number cannot be empty.\n");
                return;
            }

            try {
                int port = Integer.parseInt(portText);
                if (port < 1 || port > 65535) {
                    textArea.append("Error: Port number must be between 1 and 65535.\n");
                    return;
                }
                startServer(port);
            } catch (NumberFormatException ex) {
                textArea.append("Error: Invalid port number. Please enter a valid integer.\n");
            }
        });
    }

    private void startServer(int port) {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                textArea.append("Server started on port " + port + "\n");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    String clientInfo = clientSocket.getInetAddress() + ":" + clientSocket.getPort();
                    textArea.append("Accepted connection from " + clientInfo + "\n");
                    new ClientHandler(clientSocket, clientInfo).start();
                }
            } catch (IOException e) {
                textArea.append("Error starting server: " + e.getMessage() + "\n");
            }
        }).start();
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private String clientInfo;

        public ClientHandler(Socket clientSocket, String clientInfo) {
            this.clientSocket = clientSocket;
            this.clientInfo = clientInfo;
        }

        @Override
        public void run() {
            try (
                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                String receiveString;
                while ((receiveString = reader.readLine()) != null) {
                    textArea.append("Received from " + clientInfo + ": " + receiveString + "\n");

                    try {
                        String response = receiveString + " = " + Cal.evaluateExpression(receiveString);
                        writer.println(response);
                    } catch (Exception e) {
                        String response = "error input";
                        writer.println(response);
                    }
                }
            } catch (IOException e) {
                // Lấy thông báo lỗi
                String errorMessage = e.getMessage();

                // Phân loại lỗi và thêm vào textArea với thông tin clientInfo
                if ("Connection reset".equals(errorMessage)) {
                    textArea.append("Client " + clientInfo + " - The connection was reset by the client or server.\n");
                } else if ("Broken pipe".equals(errorMessage)) {
                    textArea.append("Client " + clientInfo + " - Attempted to write to a closed connection.\n");
                } else if (errorMessage.contains("timed out")) {
                    textArea.append("Client " + clientInfo + " - Socket operation timed out.\n");
                } else {
                    // Nếu là lỗi không xác định
                    textArea.append("Client " + clientInfo + " - Unexpected IOException: " + errorMessage + "\n");
                }
            }
            finally {
                try {
                    clientSocket.close();
                    textArea.append("Connection closed for " + clientInfo + "\n");
                } catch (IOException e) {
                    textArea.append("Error closing connection for " + clientInfo + ": " + e.getMessage() + "\n");
                }
            }
        }
    }
}
