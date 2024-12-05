package udp;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class Server extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextArea textArea;
    private DatagramSocket serverSocket;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Server frame = new Server();
                    frame.start();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Server() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Request Của Client");
        lblNewLabel.setBounds(27, 32, 113, 13);
        contentPane.add(lblNewLabel);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setBounds(27, 79, 379, 163);
        contentPane.add(textArea);
    }

    public void start() {
        try {
            serverSocket = new DatagramSocket(6996);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        Thread t1 = new Thread(new ThreadRV());
        t1.start();
    }

    public class ThreadRV implements Runnable {
        @Override
        public void run() {
            while (true) {
                byte[] dataReceive = new byte[2048];
                DatagramPacket packetReceive = new DatagramPacket(dataReceive, dataReceive.length);
                try {
                    serverSocket.receive(packetReceive);
                    String resultReceive = new String(packetReceive.getData(), 0, packetReceive.getLength(), "UTF-8");
                    textArea.append("Số Fibonacci nhận được: " + resultReceive + "\n");

                    int number = Integer.parseInt(resultReceive);

                    int positionK = Fibo.findFibonacciPosition(number);
                    List<Integer> chainPrime = Fibo.getPrimesLessThanK(number);

                    StringBuilder chainPrimeStr = new StringBuilder();
                    for (int it : chainPrime) {
                        chainPrimeStr.append(it).append(", ");
                    }
                    if (chainPrime.isEmpty()) {
                        chainPrimeStr.append("Không có số nguyên tố nào nhỏ hơn " + number);
                    } else {
                        // Remove the trailing comma and space
                        chainPrimeStr.setLength(chainPrimeStr.length() - 2);
                    }

                    String sendResult = "Vị trí của " + number + " trong dãy Fibonacci: " + positionK + "\n" +
                                        "Dãy các số nguyên tố nhỏ hơn " + number + " là: " + chainPrimeStr;

                    byte[] dataSend = sendResult.getBytes("UTF-8");
                    DatagramPacket sendPacket = new DatagramPacket(dataSend, dataSend.length, packetReceive.getAddress(), packetReceive.getPort());
                    serverSocket.send(sendPacket);

                    textArea.append("Đã trả dữ liệu về cho client có ip là " +packetReceive.getAddress() + " và "
                    		+ " port là " + packetReceive.getPort());

                } catch (IOException | NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}