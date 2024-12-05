package udp;

import java.util.ArrayList;
import java.util.List;


public class Fibo {
    public static int findFibonacciPosition(int k) {
        if (k <= 0) return -1;
        int a = 1, b = 1, position = 1;
        if (k == 1) return 2;
        while (b <= k) {
            if (b == k) return position + 1;
            int next = a + b;
            a = b;
            b = next;
            position++;
        }
        return -1;
    }

    public static List<Integer> getPrimesLessThanK(int k) {
        List<Integer> primeList = new ArrayList<>();
        for (int i = 2; i < k; i++) {
            if (isPrime(i)) {
                primeList.add(i);
            }
        }
        return primeList;
    }

    public static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}
