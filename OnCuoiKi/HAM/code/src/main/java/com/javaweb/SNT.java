package com.javaweb;
import java.util.*;

public class SNT {
    //check
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

    // lay cac so nguyen to 1 -> n
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

    // lay cac so nguyen to tu a -> b
    public static List<Integer> getPrimeNumbersInRange(int a, int b) {
        List<Integer> primes = new ArrayList<>();

        // Kiểm tra các số từ a đến b
        for (int i = a; i <= b; i++) {
            if (isPrime(i)) {
                primes.add(i);
            }
        }

        return primes;
    }

    // dem cac so nguyen to 1 -> n
    public static int countPrimeNumbers(int n) {
        int count = 0;

        // Kiểm tra các số từ 2 đến n
        for (int i = 2; i <= n; i++) {
            if (isPrime(i)) {
                count++;
            }
        }

        return count;
    }

    // dem cac so nguyen to a -> b
    public static int countPrimeNumbersInRange(int a, int b) {
        int count = 0;

        // Kiểm tra các số từ a đến b
        for (int i = a; i <= b; i++) {
            if (isPrime(i)) {
                count++;
            }
        }

        return count;
    }

    public static void main(String[] args) {
        System.out.println(isPrime(11));

        System.out.println(getPrimeNumbers(111));
        System.out.println(getPrimeNumbersInRange(19, 111));

        System.out.println(countPrimeNumbers(7));

        System.out.println(countPrimeNumbersInRange(7, 11));
    }
}
