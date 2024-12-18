package com.javaweb.bai1;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

public class Client {
    public static void main(String[] args) {

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                byte[] buf = input.getBytes();
                InetAddress inetAddress  = InetAddress.getByName("localhost");
                DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, inetAddress, 69);
                socket.send(datagramPacket);
                System.out.println("Send to server: \n" + new String(buf));

                byte[] buf1 = new byte[1024];
                DatagramPacket datagramPacket1 = new DatagramPacket(buf1, buf1.length);
                socket.receive(datagramPacket1);
                String response = new String(datagramPacket1.getData()).trim();
                System.out.println("Server response: \n" + response);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}