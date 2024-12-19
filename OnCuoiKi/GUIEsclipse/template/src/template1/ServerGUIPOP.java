package template1;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ServerGUIPOP {

    private JFrame frame;
    private JTextField textField;
    private JButton btnStartServer;
    private JTextArea textArea;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	ServerGUIPOP window = new ServerGUIPOP();
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
    public ServerGUIPOP() {
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
                JOptionPane.showMessageDialog(frame, "Error: Port number cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int port = Integer.parseInt(portText);
                if (port < 1 || port > 65535) {
                    JOptionPane.showMessageDialog(frame, "Error: Port number must be between 1 and 65535.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                startServer(port);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Error: Invalid port number. Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void startServer(int port) {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                JOptionPane.showMessageDialog(frame, "Server started on port " + port, "Info", JOptionPane.INFORMATION_MESSAGE);
                textArea.append("Server started on port " + port + "\n");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    String clientInfo = clientSocket.getInetAddress() + ":" + clientSocket.getPort();
                    JOptionPane.showMessageDialog(frame, "Accepted connection from " + clientInfo, "Info", JOptionPane.INFORMATION_MESSAGE);
                    textArea.append("Accepted connection from " + clientInfo + "\n");

                    new ClientHandler(clientSocket, clientInfo).start();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error starting server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                    // Log received message in JTextArea
                    textArea.append("Received from " + clientInfo + ": " + receiveString + "\n");
                    // Optionally show the message in a pop-up for critical messages
                    JOptionPane.showMessageDialog(frame, "Received from " + clientInfo + ": " + receiveString, "Message", JOptionPane.INFORMATION_MESSAGE);
                    // Process the received message here
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
            } finally {
                try {
                    clientSocket.close();
                    // Log closing connection in JTextArea
                    textArea.append("Connection closed for " + clientInfo + "\n");
                    JOptionPane.showMessageDialog(frame, "Connection closed for " + clientInfo, "Info", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame, "Error closing connection for " + clientInfo + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    textArea.append("Error closing connection for " + clientInfo + ": " + e.getMessage() + "\n");
                }
            }
        }
    }
}
