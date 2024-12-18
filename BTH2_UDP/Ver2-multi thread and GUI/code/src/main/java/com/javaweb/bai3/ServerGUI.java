package com.javaweb.bai3;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

public class ServerGUI {

    private JFrame frame;
    private JTextField textField;
    private JButton btnStartServer;
    private JButton btnStopServer;
    private JTextArea textArea;
    private DatagramSocket serverSocket;
    private boolean isRunning = false;

    // Danh sách lưu trữ địa chỉ các client đã kết nối
    private Set<InetSocketAddress> clientAddresses = new HashSet<>();

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
        textField.setBounds(60, 10, 200, 25);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        btnStartServer = new JButton("Start Server");
        btnStartServer.setBounds(270, 10, 100, 25);
        frame.getContentPane().add(btnStartServer);

        btnStopServer = new JButton("Stop Server");
        btnStopServer.setBounds(380, 10, 100, 25);
        btnStopServer.setEnabled(false);
        frame.getContentPane().add(btnStopServer);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 50, 450, 300);
        frame.getContentPane().add(scrollPane);

        textArea = new JTextArea();
        scrollPane.setViewportView(textArea);
        textArea.setEditable(false);

        btnStartServer.addActionListener(e -> startServer());
        btnStopServer.addActionListener(e -> stopServer());
    }

    private void startServer() {
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

            serverSocket = new DatagramSocket(port);
            isRunning = true;
            btnStartServer.setEnabled(false);
            btnStopServer.setEnabled(true);
            textArea.append("Server started on port " + port + "\n");

            new Thread(() -> {
                try {
                    while (isRunning) {
                        byte[] receiveBuffer = new byte[1024];
                        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                        serverSocket.receive(receivePacket);

                        // Lấy thông tin client
                        InetAddress clientAddress = receivePacket.getAddress();
                        int clientPort = receivePacket.getPort();
                        InetSocketAddress clientSocketAddress = new InetSocketAddress(clientAddress, clientPort);

                        // Thêm client vào danh sách nếu chưa có
                        clientAddresses.add(clientSocketAddress);

                        // Lấy nội dung tin nhắn
                        String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        textArea.append("Received from " + clientSocketAddress + ": " + message + "\n");

                        // Gửi tin nhắn cho tất cả các client khác
                        broadcastMessage(message, clientSocketAddress);
                    }
                } catch (IOException e) {
                    if (isRunning) {
                        textArea.append("Error: " + e.getMessage() + "\n");
                    }
                }
            }).start();

        } catch (NumberFormatException ex) {
            textArea.append("Error: Invalid port number. Please enter a valid integer.\n");
        } catch (IOException ex) {
            textArea.append("Error starting server: " + ex.getMessage() + "\n");
        }
    }

    private void stopServer() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
        isRunning = false;
        btnStartServer.setEnabled(true);
        btnStopServer.setEnabled(false);
        textArea.append("Server stopped.\n");
    }

    private void broadcastMessage(String message, InetSocketAddress sender) {
        new Thread(() -> {
            byte[] sendBuffer = message.getBytes();

            for (InetSocketAddress client : clientAddresses) {
                // Không gửi lại cho client đã gửi tin nhắn
                if (!client.equals(sender)) {
                    try {
                        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, client.getAddress(), client.getPort());
                        serverSocket.send(sendPacket);
                        textArea.append("Sent message to " + client + "\n");
                    } catch (IOException e) {
                        textArea.append("Error sending message to " + client + ": " + e.getMessage() + "\n");
                    }
                }
            }
        }).start();
    }
}
