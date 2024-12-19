package com.javaweb.haTCP.BAITEST;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class ClientGUI {

    private JFrame frame;
    private JTextField textField;
    private JButton btnNewButton;
    private JTextArea textArea;
    private JLabel lblRequestToServer;
    private JTextField textField_1;
    private JButton btnOk;
    private Socket socket;

    /**
     * Launch the application.
     */
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

    /**
     * Create the application.
     */
    public ClientGUI() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        btnNewButton = new JButton("Connect");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String portText = textField.getText().trim();
                if(socket != null && !socket.isClosed()) {
                    textArea.append("Error: Already connected to the server. Cannot reconnect. \n");
                    return;
                }

                // Check if the port text is empty
                if (portText.isEmpty()) {
                    textArea.append("Error: Port number cannot be empty.\n");
                    return;
                }

                // Try to parse the port number
                try {
                    int port = Integer.parseInt(portText);

                    // Check if the port is within the valid range (1-65535)
                    if (port < 1 || port > 65535) {
                        textArea.append("Error: Port number must be between 1 and 65535.\n");
                        return;
                    }

                    // If validation is passed, start the server
                    connectToServer(port);

                } catch (NumberFormatException ex) {
                    // If parsing fails, show an error message
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
                if (socket == null || !socket.isConnected()) {
                    textArea.append("Error: Please connect to the server first.\n");
                    return; // Nếu chưa kết nối, không thực hiện gì
                }
                PrintWriter out = null;
                BufferedReader in = null;
                try {
                    out = new PrintWriter(socket.getOutputStream(), true);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                // Send a request to the server
                String request = textField_1.getText().trim();
                if (request.isEmpty()) {
                    textArea.append("Error: Request cannot be empty.\n");
                    return;
                }
                out.println(request);
                textArea.append("Request sent to server: " + request + "\n");
                String response = null;
                try {
                    response = in.readLine();
                    textArea.append("Response from server: " + response + "\n");
                } catch (IOException e1) {
                    // Lấy thông báo lỗi
                    String errorMessage = e1.getMessage();

                    // Phân loại lỗi và thêm vào textArea với thông tin server
                    if ("Connection reset".equals(errorMessage)) {
                        textArea.append("The connection was reset by the client or server.\n");
                    } else if ("Broken pipe".equals(errorMessage)) {
                        textArea.append("Attempted to write to a closed connection.\n");
                    } else if (errorMessage.contains("timed out")) {
                        textArea.append("Socket operation timed out.\n");
                    } else {
                        // Nếu là lỗi không xác định
                        textArea.append("Unexpected IOException: " + errorMessage + "\n");
                    }
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

    private void connectToServer(int port) {
        try {
            // Cố gắng tạo kết nối đến server
            this.socket = new Socket("localhost", port);

            // Nếu kết nối thành công, in ra thông báo
            textArea.append("Connected to server on port " + port + ".\n");

        } catch (UnknownHostException e) {
            // Lỗi khi không tìm thấy host (server)
            textArea.append("Error: Unknown host. Unable to connect to the server at localhost.\n");
        } catch (IOException e) {
            // Lỗi liên quan đến I/O (ví dụ kết nối không thành công, lỗi mạng)
            textArea.append("Error: IOException occurred while trying to connect to the server.\n");
            textArea.append("Exception message: " + e.getMessage() + "\n");
        }
    }
}
