package com.javaweb.time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
    public static void main(String[] args) throws RuntimeException {
        DatagramSocket ds = null;
        InetAddress address;
        try {
            ds = new DatagramSocket();
            address = InetAddress.getByName("localhost");
            int port = 6996;
            byte[] sendData;
            byte[] receiveData = new byte[1024];
            sendData = "getDate".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
            ds.send(sendPacket);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            ds.receive(receivePacket);
//            System.out.println(receivePacket.getLength());
            String str = new String(receivePacket.getData()).trim();
            System.out.println(str);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (ds != null) {
                ds.close();
            }
        }
    }
}
