package com.javaweb.bai4.db;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ClientGUI {

    private JFrame frame;
    private JTextField textField;
    private JButton btnNewButton;
    private JLabel lblRequestToServer;
    private JTextField textField_1;
    private JButton btnOk;
    private Socket socket;
    private JScrollPane scrollPane;
    private JTable table;

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
                    // Display error message in popup
                    JOptionPane.showMessageDialog(frame, "Error: Already connected to the server. Cannot reconnect.", "Connection Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check if the port text is empty
                if (portText.isEmpty()) {
                    // Display error message in popup
                    JOptionPane.showMessageDialog(frame, "Error: Port number cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Try to parse the port number
                try {
                    int port = Integer.parseInt(portText);

                    // Check if the port is within the valid range (1-65535)
                    if (port < 1 || port > 65535) {
                        // Display error message in popup
                        JOptionPane.showMessageDialog(frame, "Error: Port number must be between 1 and 65535.", "Port Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // If validation is passed, start the server
                    connectToServer(port);

                } catch (NumberFormatException ex) {
                    // Display error message in popup
                    JOptionPane.showMessageDialog(frame, "Error: Invalid port number. Please enter a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
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
                    // Display error message in popup
                    JOptionPane.showMessageDialog(frame, "Error: Please connect to the server first.", "Connection Error", JOptionPane.ERROR_MESSAGE);
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
                    // Display error message in popup
                    JOptionPane.showMessageDialog(frame, "Error: Request cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                out.println(request);
                    String response = null;
   
                        processRequest(request, in, out);
                  
            }
        });
        btnOk.setBounds(341, 41, 85, 21);
        frame.getContentPane().add(btnOk);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 82, 416, 171);
        frame.getContentPane().add(scrollPane);
        
        String[] columnNames = {"ID", "Name", "Age"};
        Object[][] data = {};
        table = new JTable(new DefaultTableModel(data, columnNames));
        scrollPane.setViewportView(table);
    }
    private void connectToServer(int port) {
        try {
            // Try to create a connection to the server
            this.socket = new Socket("localhost", port);

            // If connection is successful, display a success message
            JOptionPane.showMessageDialog(frame, "Connected to server on port " + port + ".", "Connection Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (UnknownHostException e) {
            // Error when the host (server) is not found
            JOptionPane.showMessageDialog(frame, "Error: Unknown host. Unable to connect to the server at localhost.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            // IO-related error (e.g., connection failed, network error)
            JOptionPane.showMessageDialog(frame, "Error: IOException occurred while trying to connect to the server.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private void processRequest(String request, BufferedReader in, PrintWriter out) {
        String rq = request.trim().toUpperCase();
        if (rq.startsWith("CREATE")) {
            handleCreateRequest(in);
        } else if (rq.startsWith("SELECT")) {
            handleReadRequest(in);
        } else if (rq.startsWith("UPDATE")) {
            handleUpdateRequest(in);
        } else if (rq.startsWith("DELETE")) {
            handleDeleteRequest(in);
        } else {
            out.println("Unknown request type");
        }
    }

    private void handleCreateRequest(BufferedReader in) {
        try {
            String serverResponse = in.readLine();
            JOptionPane.showMessageDialog(frame, serverResponse.contains("success") ? "Record Created Successfully" : "Failed to Create Record", "Server Response", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error while handling CREATE request.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleReadRequest(BufferedReader in) {
        try {
            String serverResponse = in.readLine();
            if (serverResponse != null && serverResponse.startsWith("[")) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0);
                String[] rows = serverResponse.substring(1, serverResponse.length() - 1).split("},");
                for (String row : rows) {
                    String[] columns = row.replace("{", "").replace("}", "").split(", ");
                    model.addRow(new Object[]{columns[0].split(":")[1], columns[1].split(":")[1].replace("\"", ""), columns[2].split(":")[1]});
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No Data Found", "Server Response", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error while handling READ request.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateRequest(BufferedReader in) {
        try {
            String serverResponse = in.readLine();
            JOptionPane.showMessageDialog(frame, serverResponse.contains("success") ? "Record Updated Successfully" : "Failed to Update Record", "Server Response", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error while handling UPDATE request.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteRequest(BufferedReader in) {
        try {
            String serverResponse = in.readLine();
            JOptionPane.showMessageDialog(frame, serverResponse.contains("success") ? "Record Deleted Successfully" : "Failed to Delete Record", "Server Response", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error while handling DELETE request.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

//INSERT INTO users (id, name, age)  VALUES (122, 'John Doe', 25); 
//UPDATE users SET name = 'John Smith', age = 26 WHERE id = 1;
//SELECT * FROM users