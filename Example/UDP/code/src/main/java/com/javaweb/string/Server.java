package com.javaweb.string;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.UnknownHostException;

public class Server {
    private static String VuaHoaVuaThuong(String s3) {
        int k = 0, i;
        char c;
        String st3 = "";
        k = s3.length();
        for (i = 0; i < k; i++) {
            c = s3.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                c = (char) (c + 32);
            } else if (c >= 'a' && c <= 'z') {
                c = (char) (c - 32);
            }
            st3 = st3 + c;
        }
        return st3;
    }

    public static void main(String[] args) {
        try{
            DatagramSocket ds = new DatagramSocket(69);
            DatagramPacket dp = new DatagramPacket(new byte[1024], 1024);
            while(true){
                ds.receive(dp);
                ByteArrayInputStream bis = new ByteArrayInputStream(dp.getData());
                BufferedReader dis = new BufferedReader(new InputStreamReader(bis));
                String s0 = dis.readLine();
                System.out.println("Received: " + s0.trim());

                String s = VuaHoaVuaThuong(s0);

                dp.setData(s.getBytes());
                dp.setLength(s.length());
                dp.setAddress(dp.getAddress());
                dp.setPort(dp.getPort());

                ds.send(dp);
            }
        }catch (UnknownHostException e){
            System.err.println("Unknown Host: " + e.getMessage());
        }catch (Exception e){
            System.err.println("IOException: " + e.getMessage());
        }
    }
}
