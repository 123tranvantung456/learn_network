package com.javaweb;
import java.util.*;

public class Fibo {
    // tính 0
    public static boolean isFibonacci(int number) {
        if (number < 0) {
            return false;
        }

        int a = 0;
        int b = 1;

        if (number == a || number == b) {
            return true;
        }

        while (b < number) {
            int next = a + b;
            a = b;
            b = next;

            if (b == number) {
                return true;
            }
        }

        return false;
    }
    // ko tính 0
    public static boolean isFibonacci1(int number) {
        if (number <= 0) {
            return false; // Loại trừ số 0 và các số âm
        }

        int a = 0;
        int b = 1;

        if (number == b) {
            return true; // 1 là số Fibonacci
        }

        while (b < number) {
            int next = a + b;
            a = b;
            b = next;

            if (b == number) {
                return true;
            }
        }

        return false;
    }


    // Ko có 0
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
    // Có 0 và 0 bắt đầu vị trí 1
    public static int getFibonacciPosition1(int number) {
        if (number == 0) {
            return 1; // Số 0 ở vị trí 1
        }

        int a = 0; // Số Fibonacci đầu tiên là 0
        int b = 1; // Số Fibonacci thứ hai là 1
        int position = 2; // Vị trí của số 1 là 2

        if (number == b) {
            return position;
        }

        while (b < number) {
            int next = a + b;
            a = b;
            b = next;
            position++;

            if (b == number) {
                return position;
            }
        }

        return -1; // Không phải số Fibonacci
    }
    // Có 0 và 0 bắt đầu vị trí 0
    public static int getFibonacciPosition2(int number) {
        if (number == 0) {
            return 0; // Số 0 ở vị trí 0
        }

        int a = 0; // Số Fibonacci đầu tiên là 0
        int b = 1; // Số Fibonacci thứ hai là 1
        int position = 1; // Vị trí của số 1 là 1

        if (number == b) {
            return position;
        }

        while (b < number) {
            int next = a + b;
            a = b;
            b = next;
            position++;

            if (b == number) {
                return position;
            }
        }

        return -1; // Không phải số Fibonacci
    }


    // 1 -> n, co tinh n
    public static List<Integer> getFibonacciUpToN(int n) {
        List<Integer> fibonacciList = new ArrayList<>();

        if (n <= 0) {
            return fibonacciList;  // Trả về list rỗng nếu n <= 0
        }

        int a = 0; // Fibonacci thứ nhất
        int b = 1; // Fibonacci thứ hai

        // Thêm các số Fibonacci vào danh sách
        while (b <= n) {
            if (b > 0) {  // Không tính số 0
                fibonacciList.add(b);
            }
            int next = a + b;
            a = b;
            b = next;
        }

        return fibonacciList;
    }

    // 0 -> n, co tinh n
    public static List<Integer> getFibonacciUpToN1(int n) {
        List<Integer> fibonacciList = new ArrayList<>();

        if (n < 0) {
            return fibonacciList;  // Trả về list rỗng nếu n < 0
        }

        int a = 0; // Fibonacci thứ nhất
        int b = 1; // Fibonacci thứ hai

        // Thêm số Fibonacci 0 vào danh sách nếu n >= 0
        fibonacciList.add(a);

        // Thêm các số Fibonacci vào danh sách
        while (b <= n) {
            fibonacciList.add(b);
            int next = a + b;
            a = b;
            b = next;
        }

        return fibonacciList;
    }

    public static void main(String[] args) {
        System.out.println(isFibonacci(0));
        System.out.println(isFibonacci1(0));
        System.out.println(getFibonacciPosition(1));
        System.out.println(getFibonacciPosition1(1));
        System.out.println(getFibonacciPosition2(1));
        System.out.println(getFibonacciUpToN(9));
        System.out.println(getFibonacciUpToN1(9));
    }
}
