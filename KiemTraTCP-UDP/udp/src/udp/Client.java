package udp;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class Client extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JButton btnNewButton;
	private JTextArea textArea;
	private DatagramSocket clientSocket;
	private InetAddress serverAddress;
	private final int portServer = 6996;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client frame = new Client();
					frame.startServer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Client() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnNewButton = new JButton("Gửi");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HandleSend();
			}
		});
		btnNewButton.setBounds(341, 26, 85, 21);
		contentPane.add(btnNewButton);
		
		textField = new JTextField();
		textField.setBounds(74, 27, 254, 19);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Nhập K");
		lblNewLabel.setBounds(23, 30, 51, 13);
		contentPane.add(lblNewLabel);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(23, 108, 403, 134);
		contentPane.add(textArea);
		
		JLabel lblNhnTServer = new JLabel("Response của Server");
		lblNhnTServer.setBounds(23, 85, 170, 13);
		contentPane.add(lblNhnTServer);
	}
	
	public void startServer(){
		try {
			clientSocket = new DatagramSocket();
			serverAddress = InetAddress.getByName("localhost");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread t1 = new Thread(new ThreadRV());
		t1.start();
		 
	}
	
	public void HandleSend() {
		int number = 0;
		try {
			number = Integer.parseInt(textField.getText());
		} catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Dữ liệu nhập vào không phải là 1 số, nhập lại", "Error", JOptionPane.ERROR_MESSAGE);
            return;
		}
		if (Fibo.findFibonacciPosition(number) > -1) {
			byte[] dataSend = new byte[2048];
			try {
				dataSend = String.valueOf(number).getBytes("UTF-8");
				DatagramPacket packetSend = new DatagramPacket(dataSend, dataSend.length, serverAddress, portServer);
				clientSocket.send(packetSend);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
            JOptionPane.showMessageDialog(null, "Số nhập vào không thuộc dãy fibonanci", "Error", JOptionPane.ERROR_MESSAGE);
            
		}
	}
	
	public class ThreadRV implements Runnable{

		@Override
		public void run() {
			while(true) {
				byte[] dataReceive = new byte[2048];
				DatagramPacket packetReceive = new DatagramPacket(dataReceive, dataReceive.length);
				try {
					clientSocket.receive(packetReceive);
					String resultReceive = new String(packetReceive.getData(), 0, packetReceive.getLength(), "UTF-8");
					textArea.append(resultReceive + "\n");
					btnNewButton.setEnabled(false);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
