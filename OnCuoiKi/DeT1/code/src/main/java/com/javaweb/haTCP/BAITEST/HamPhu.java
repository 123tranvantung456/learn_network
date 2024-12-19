package com.javaweb.haTCP.BAITEST;

import java.util.ArrayList;
import java.util.List;

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

    public static boolean isPrime(int number) {
        if (number < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }

        return true;
    }

    public static List<Integer> getPrimeNumbers(int n) {
        List<Integer> primes = new ArrayList<>();

        // Kiểm tra các số từ 2 đến n
        for (int i = 2; i <= n; i++) {
            if (isPrime(i)) {
                primes.add(i);
            }
        }

        return primes;
    }

}
