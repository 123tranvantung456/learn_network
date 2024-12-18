package com.javaweb.bai2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class ClientGUI {

    private JFrame frame;
    private JTextField textField;
    private JButton btnNewButton;
    private JTextArea textArea;
    private JLabel lblRequestToServer;
    private JTextField textField_1;
    private JButton btnOk;
    private DatagramSocket socket;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ClientGUI window = new ClientGUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ClientGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        btnNewButton = new JButton("Connect");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
                    textArea.append("Ready to send requests to server on port " + port + ".\n");

                } catch (NumberFormatException ex) {
                    textArea.append("Error: Invalid port number. Please enter a valid integer.\n");
                }
            }
        });
        btnNewButton.setBounds(341, 10, 85, 21);
        frame.getContentPane().add(btnNewButton);

        textField = new JTextField();
        textField.setBounds(127, 11, 204, 19);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        JLabel lblNewLabel = new JLabel("Port of server");
        lblNewLabel.setBounds(27, 14, 92, 13);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblResponseOfServer = new JLabel("Response of server");
        lblResponseOfServer.setBounds(32, 75, 131, 13);
        frame.getContentPane().add(lblResponseOfServer);

        lblRequestToServer = new JLabel("Request to server");
        lblRequestToServer.setBounds(27, 45, 92, 13);
        frame.getContentPane().add(lblRequestToServer);

        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(127, 42, 204, 19);
        frame.getContentPane().add(textField_1);

        btnOk = new JButton("Ok");
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String request = textField_1.getText().trim();
                String portText = textField.getText().trim();

                if (request.isEmpty()) {
                    textArea.append("Error: Request cannot be empty.\n");
                    return;
                }

                if (portText.isEmpty()) {
                    textArea.append("Error: Port number cannot be empty.\n");
                    return;
                }

                try {
                    int port = Integer.parseInt(portText);
                    sendRequest(request, port);
                    receiveResponse();

                } catch (NumberFormatException ex) {
                    textArea.append("Error: Invalid port number.\n");
                } catch (IOException ex) {
                    textArea.append("Error in sending/receiving data: " + ex.getMessage() + "\n");
                }
            }
        });
        btnOk.setBounds(341, 41, 85, 21);
        frame.getContentPane().add(btnOk);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(27, 123, 387, 118);
        frame.getContentPane().add(scrollPane);

        textArea = new JTextArea();
        scrollPane.setViewportView(textArea);
        textArea.setEditable(false);
    }

    private void sendRequest(String request, int port) throws IOException {
        socket = new DatagramSocket();
        byte[] sendData = request.getBytes(StandardCharsets.UTF_8);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("localhost"), port);
        socket.send(sendPacket);
        textArea.append("Request sent to server: " + request + "\n");
    }

    private void receiveResponse() throws IOException {
        try {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);
            String response = new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8);
            textArea.append("Response from server: \n" + response + "\n");
        } finally {
            socket.close();
        }
    }
}