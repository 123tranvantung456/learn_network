package com.javaweb;

import java.util.Scanner;

public class Coprime {

    // Hàm tìm UCLN của hai số a và b
    public static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;  // Lấy phần dư khi chia a cho b
            a = temp;   // Gán a bằng b cũ
        }
        return a;  // a là UCLN
    }

    // Hàm kiểm tra hai số có phải là số nguyên tố cùng nhau không
    public static boolean areCoprime(int a, int b) {
        return gcd(a, b) == 1;  // Nếu UCLN = 1 thì là số nguyên tố cùng nhau
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nhập số a: ");
        int a = scanner.nextInt();
        System.out.print("Nhập số b: ");
        int b = scanner.nextInt();

        if (areCoprime(a, b)) {
            System.out.println(a + " và " + b + " là số nguyên tố cùng nhau.");
        } else {
            System.out.println(a + " và " + b + " không phải là số nguyên tố cùng nhau.");
        }
        scanner.close();
    }
}