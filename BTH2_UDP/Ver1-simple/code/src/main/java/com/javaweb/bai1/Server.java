package com.javaweb.bai1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

public class Server {
    public static void main(String[] args) {
        try{
            DatagramSocket socket = new DatagramSocket(69);
            byte[] recvData = new byte[1024];
            while(true){
                try{
                    DatagramPacket packetRv = new DatagramPacket(recvData, recvData.length);
                    socket.receive(packetRv);
                    String request  = new String(packetRv.getData()).trim();
                    System.out.println("Server received: " + request );

                    String response = "Lowercase: " + Str.toLowerCase(request) +
                            "\nUppercase: " + Str.toUpperCase(request) +
                            "\nReversed: " + Str.reverseString(request) +
                            "\nToggle case: " + Str.mixCase(request) +
                            "\nWord count: " + Str.countWords(request) +
                            "\nReversed Words: " + Str.reverseWords(request);

                    // Count vowels
                    Map<Character, Integer> vowelCounts = Str.countVowels(request);
                    for (Map.Entry<Character, Integer> entry : vowelCounts.entrySet()) {
                        response += "\nVowel '" + entry.getKey() + "': " + entry.getValue();
                    }

                    // Count duplicate words
                    Map<String, Integer> wordCounts = Str.countDuplicateWords(request);
                    for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
                        response += "\nWord '" + entry.getKey() + "' appears: " + entry.getValue() + " times";
                    }

                    InetAddress address = packetRv.getAddress();
                    int port = packetRv.getPort();

                    DatagramPacket packetSend = new DatagramPacket(response.getBytes(), response.getBytes().length, address, port);
                    socket.send(packetSend);
                }catch (IOException e) {
                System.err.println("Error in receiving/sending packet.");
                e.printStackTrace();
            }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
