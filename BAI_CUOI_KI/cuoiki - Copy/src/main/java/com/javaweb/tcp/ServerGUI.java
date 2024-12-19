package com.javaweb.tcp;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
public class ServerGUI {

    private JFrame frame;
    private JTextField textField;
    private JButton btnStartServer;
    private JTextArea textArea;

    private static final Set<String> blackList = new HashSet<>();

    static {
        blackList.add("192.168.10.100");
        blackList.add("192.168.10.105");
//        blackList.add("127.0.0.1");
    }
    /**
     * Launch the application.
     */
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

    /**
     * Create the application.
     */
    public ServerGUI() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
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
                    if (blackList.contains(clientSocket.getInetAddress().getHostAddress())) {
                        textArea.append("Client này bị chặn vì nằm trong blacklist " + clientInfo + "\n");
                        textArea.append("Dong ket noi cua: " + clientInfo + "\n");
                        clientSocket.close();  // Đóng kết nối nếu client bị chặn
                        continue;  // Không tiếp tục xử lý client này
                    }

                    new ClientHandler(clientSocket, clientInfo).start();
                }
            } catch (IOException e) {
                textArea.append("Error starting server: " + e.getMessage() + "\n");
            }
        }).start();
    }

    private class ClientHandler extends Thread {
        private final Socket clientSocket;
        private final String clientInfo;

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

                    // Handle the received request
                    String response = "";
                    if ("Please livestream".equals(receiveString)) {
                        response = "OK";
                        writer.println(response);
                    } else {
                        sendImage(clientSocket);
                    }
                }
            } catch (IOException e) {
                handleClientError(e);
            } finally {
                try {
                    clientSocket.close();
                    textArea.append("Connection closed for " + clientInfo + "\n");
                } catch (IOException e) {
                    textArea.append("Error closing connection for " + clientInfo + ": " + e.getMessage() + "\n");
                }
            }
        }

        private void sendImage(Socket clientSocket) {
            File imageFile = new File("D:\\Dowloads\\images.jpg");
            if (!imageFile.exists()) {
                textArea.append("Image file not found: " + imageFile.getAbsolutePath() + "\n");
                return;
            }

            try (BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(imageFile));
                 OutputStream out = clientSocket.getOutputStream()) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
                textArea.append("Image sent successfully to client.\n");

            } catch (IOException e) {
                textArea.append("Error sending image: " + e.getMessage() + "\n");
            }
        }

        private void handleClientError(IOException e) {
            String errorMessage = e.getMessage();
            if ("Connection reset".equals(errorMessage)) {
                textArea.append("Client " + clientInfo + " - The connection was reset by the client or server.\n");
            } else if ("Broken pipe".equals(errorMessage)) {
                textArea.append("Client " + clientInfo + " - Attempted to write to a closed connection.\n");
            } else if (errorMessage.contains("timed out")) {
                textArea.append("Client " + clientInfo + " - Socket operation timed out.\n");
            } else {
                textArea.append("Client " + clientInfo + " - Unexpected IOException: " + errorMessage + "\n");
            }
        }
    }
}
