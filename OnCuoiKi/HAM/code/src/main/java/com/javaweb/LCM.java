package com.javaweb;

import java.util.Scanner;

public class LCM {

    // Hàm tìm UCLN của hai số a và b
    public static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;  // Lấy phần dư khi chia a cho b
            a = temp;   // Gán a bằng b cũ
        }
        return a;  // a là UCLN
    }

    // Hàm tìm BCNN của hai số a và b
    public static int lcm(int a, int b) {
        return Math.abs(a * b) / gcd(a, b);  // Sử dụng công thức BCNN
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nhập số a: ");
        int a = scanner.nextInt();
        System.out.print("Nhập số b: ");
        int b = scanner.nextInt();

        System.out.println("Bội chung nhỏ nhất của " + a + " và " + b + " là: " + lcm(a, b));

        scanner.close();
    }
}