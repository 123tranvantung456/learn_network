package com.javaweb.string;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 69;

        DatagramSocket socket = null;
        try{
            socket = new DatagramSocket();

            String message = "Hello World";
            byte[] buf = message.getBytes();
            InetAddress inetAddress = InetAddress.getByName(serverAddress);
            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, inetAddress, serverPort);
            socket.send(sendPacket);
            System.out.println("Sent message: " + message);

            byte[] buf2 = message.getBytes();
            DatagramPacket receivePacket = new DatagramPacket(buf2, buf2.length);
            socket.receive(receivePacket);
            String receivedMessage = new String(receivePacket.getData());
            System.out.println("Received message: " + receivedMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
