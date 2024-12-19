package com.javaweb.haUDP.BAISNT;

public class HamPhu {
    public static int getFibonacciPosition(int number) {
        if (number < 1) {
            return -1;
        }

        int a = 1;
        int b = 1;
        int position = 1;

        if (number == a) {
            return position;
        }

        position++;

        while (b < number) {
            int next = a + b;
            a = b;
            b = next;
            position++;

            if (b == number) {
                return position;
            }
        }

        return -1;
    }
}
