package com.javaweb.haUDP.BAIFIBO;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerGUI {

    private JFrame frame;
    private JTextField textField;
    private JButton btnStartServer;
    private JButton btnStopServer;
    private JTextArea textArea;
    private DatagramSocket serverSocket;
    private boolean isRunning = false;

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

                        String receiveString = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        String clientInfo = receivePacket.getAddress() + ":" + receivePacket.getPort();
                        textArea.append("Received from " + clientInfo + ": " + receiveString + "\n");

                        handleClientData(receiveString, receivePacket.getAddress(), receivePacket.getPort());
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

    private void handleClientData(String receiveString, InetAddress clientAddress, int clientPort) {
        Integer number = null;
        String response = "";
        try {
            number = Integer.parseInt(receiveString);
        } catch (Exception e) {
            response = "Error: Invalid number. Please enter a valid integer.\n";
        }

        try {
            if (number != null) {
                boolean isPrime = HamPhu.isPrime(number);
                if (!isPrime) {
                    response = "Error: Không phải là số ngyên tố.\n";
                } else {
                    response ="Đáp ứng yêu cầu: " + number + " là số nguyên tố" + "\n";
                }
            }

            byte[] sendBuffer = response.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
            serverSocket.send(sendPacket);
            textArea.append("Response sent to " + clientAddress + ":" + clientPort + "\n");
        } catch (IOException e) {
            textArea.append("Error handling client data: " + e.getMessage() + "\n");
        }
    }
}

