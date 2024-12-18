package com.javaweb.bai3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class ClientGUI1 {

    private JFrame frame;
    private JTextField textField;
    private JButton btnNewButton;
    private JTextArea textArea;
    private JLabel lblRequestToServer;
    private JTextField textField_1;
    private JButton btnOk;
    private DatagramSocket socket;
    private String serverAddress = "localhost";
    private boolean isSocketConnected = false;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ClientGUI1 window = new ClientGUI1();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ClientGUI1() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // Connect button to specify the server port
        btnNewButton = new JButton("Connect");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String portText = textField.getText().trim();

                if (portText.isEmpty()) {
                    appendToTextArea("Error: Port number cannot be empty.\n");
                    return;
                }

                try {
                    int port = Integer.parseInt(portText);
                    if (port < 1 || port > 65535) {
                        appendToTextArea("Error: Port number must be between 1 and 65535.\n");
                        return;
                    }

                    // Initialize socket
                    if (socket == null || socket.isClosed()) {
                        socket = new DatagramSocket();
                    }

                    isSocketConnected = true;
                    appendToTextArea("Ready to send requests to server on port " + port + ".\n");
                    receiveResponse();
                } catch (NumberFormatException ex) {
                    appendToTextArea("Error: Invalid port number. Please enter a valid integer.\n");
                } catch (IOException ex) {
                    appendToTextArea("Error: Unable to create socket. " + ex.getMessage() + "\n");
                }
            }
        });
        btnNewButton.setBounds(341, 10, 85, 21);
        frame.getContentPane().add(btnNewButton);

        // Text field to input the server port
        textField = new JTextField();
        textField.setBounds(127, 11, 204, 19);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        // Label for the "Port of server"
        JLabel lblNewLabel = new JLabel("Port of server");
        lblNewLabel.setBounds(27, 14, 92, 13);
        frame.getContentPane().add(lblNewLabel);

        // Label for the server's response
        JLabel lblResponseOfServer = new JLabel("Response of server");
        lblResponseOfServer.setBounds(32, 75, 131, 13);
        frame.getContentPane().add(lblResponseOfServer);

        // Label for the request to the server
        lblRequestToServer = new JLabel("Request to server");
        lblRequestToServer.setBounds(27, 45, 92, 13);
        frame.getContentPane().add(lblRequestToServer);

        // Text field to input the request message
        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(127, 42, 204, 19);
        frame.getContentPane().add(textField_1);

        // OK button to send the request to the server
        btnOk = new JButton("Ok");
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String request = textField_1.getText().trim();
                String portText = textField.getText().trim();

                if (!isSocketConnected) {
                    appendToTextArea("Error: Please connect to the server first.\n");
                    return;
                }

                if (request.isEmpty()) {
                    appendToTextArea("Error: Request cannot be empty.\n");
                    return;
                }

                try {
                    int port = Integer.parseInt(portText);
                    sendRequest(request, port);
                } catch (NumberFormatException ex) {
                    appendToTextArea("Error: Invalid port number.\n");
                } catch (IOException ex) {
                    appendToTextArea("Error in sending data: " + ex.getMessage() + "\n");
                }
            }
        });
        btnOk.setBounds(341, 41, 85, 21);
        frame.getContentPane().add(btnOk);

        // Scroll pane for displaying server responses
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(27, 123, 387, 118);
        frame.getContentPane().add(scrollPane);

        // Text area to display responses from the server
        textArea = new JTextArea();
        scrollPane.setViewportView(textArea);
        textArea.setEditable(false);
    }

    private void sendRequest(String request, int port) throws IOException {
        byte[] sendData = request.getBytes(StandardCharsets.UTF_8);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(serverAddress), port);
        socket.send(sendPacket);
        appendToTextArea("Request sent to server: " + request + "\n");
    }

    private void receiveResponse() {
        new Thread(() -> {
            try {
                while (isSocketConnected) {
                    byte[] receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    socket.receive(receivePacket);
                    String response = new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8);
                    appendToTextArea("Response from server: \n" + response + "\n");
                }
            } catch (IOException e) {
                appendToTextArea("Error in receiving data: " + e.getMessage() + "\n");
            }
        }).start();
    }

    private void appendToTextArea(String text) {
        SwingUtilities.invokeLater(() -> textArea.append(text));
    }
}
