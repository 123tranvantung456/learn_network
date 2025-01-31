package com.javaweb.bai1.string;

import java.awt.EventQueue;
import javax.swing.*;
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
        //chạy phần startServer này trong 1 luồng khác để ko ảnh hưởng tới luồng của swing(ảnh hưởng đến nút btnStartServer)
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                textArea.append("Server started on port " + port + "\n");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    String clientInfo = clientSocket.getInetAddress() + ":" + clientSocket.getPort();
                    textArea.append("Accepted connection from " + clientInfo + "\n");
                    // mỗi client là 1 luồng
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

// Đảo chuỗi
                    String reversedString = Str.reverseString(receiveString);

// Chuyển đổi chuỗi thành chữ in hoa
                    String upperCaseString = Str.toUpperCase(receiveString);

// Chuyển đổi chuỗi thành chữ in thường
                    String lowerCaseString = Str.toLowerCase(receiveString);

// Chuyển đổi chuỗi thành chữ cái mix
                    String mixedCaseString = Str.mixCase(receiveString);

// Đếm số từ trong chuỗi
                    int wordCount = Str.countWords(receiveString);

// Đếm số lần xuất hiện của các nguyên âm
                    Map<Character, Integer> vowels = Str.countVowels(receiveString);
                    StringBuilder vowelStr = new StringBuilder();
                    for (Map.Entry<Character, Integer> entry : vowels.entrySet()) {
                        vowelStr.append("vowel: ").append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
                    }

// Đảo vị trí các từ trong chuỗi
                    String reversedWord = Str.reverseWords(receiveString);

// Đếm số lần xuất hiện của các từ trùng lặp
                    Map<String, Integer> duplicateWords = Str.countDuplicateWords(receiveString);
                    StringBuilder duplicateWordStr = new StringBuilder();
                    for (Map.Entry<String, Integer> entry : duplicateWords.entrySet()) {
                        duplicateWordStr.append("word: ").append(entry.getKey()).append(" : ").append(entry.getValue()).append(", ");
                    }

// Gửi các kết quả về cho client
                    writer.println("reversedString: " + reversedString
                            + "\nupperCaseString: " + upperCaseString
                            + "\nlowerCaseString: " + lowerCaseString
                            + "\nmixedCaseString: " + mixedCaseString
                            + "\nwordCount: " + wordCount
                            + "\nreversedWords: " + reversedWord
                            + "\n" + duplicateWordStr
                            + "\n" + vowelStr

                    );
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
