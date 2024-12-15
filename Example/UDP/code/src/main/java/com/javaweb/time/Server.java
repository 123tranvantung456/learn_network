package com.javaweb.time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

public class Server {
    public static void main(String[] args) {
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket(6996);
            System.out.println("Server started");
            byte[] receiveData = new byte[1024];
            byte[] sendData;
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                datagramSocket.receive(receivePacket);
                InetAddress address = receivePacket.getAddress();
                int port = receivePacket.getPort();
                String message = new String(receivePacket.getData());
                System.out.println("Server received: " + message);
                if (message.trim().equals("getDate")) {
                    sendData = new Date().toString().getBytes();
                }
                else {
                    sendData = "unknown".getBytes();
                }
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
                datagramSocket.send(sendPacket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }
    }
}
