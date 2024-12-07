package com.javaweb.bai2;
import java.util.Stack;

public class Cal {
    // Hàm tính toán biểu thức
    public static double evaluateExpression(String expression) throws Exception {
        Stack<Double> values = new Stack<>();
        Stack<Character> operators = new Stack<>();
        int length = expression.length();

        for (int i = 0; i < length; i++) {
            char c = expression.charAt(i);

            // Bỏ qua khoảng trắng
            if (c == ' ') {
                continue;
            }

            // Nếu là số, thì lấy toàn bộ số và đẩy vào stack giá trị
            if (Character.isDigit(c)) {
                StringBuilder sb = new StringBuilder();
                while (i < length && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i++));
                }
                values.push(Double.parseDouble(sb.toString()));
                i--; // Điều chỉnh lại chỉ số sau khi thoát vòng lặp
            }
            // Nếu gặp dấu mở ngoặc, đẩy vào stack toán tử
            else if (c == '(') {
                operators.push(c);
            }
            // Nếu gặp dấu đóng ngoặc, xử lý toàn bộ trong dấu ngoặc
            else if (c == ')') {
                while (operators.peek() != '(') {
                    values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                }
                operators.pop(); // Bỏ dấu ngoặc '(' khỏi stack
            }
            // Nếu gặp toán tử
            else if (c == '+' || c == '-' || c == '*' || c == '/') {
                // Xử lý các phép toán ưu tiên trước nếu có
                while (!operators.isEmpty() && hasPrecedence(c, operators.peek())) {
                    values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                }
                // Đẩy toán tử hiện tại vào stack
                operators.push(c);
            }
        }

        // Tính toán phần còn lại
        while (!operators.isEmpty()) {
            values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
        }

        // Kết quả cuối cùng nằm trong stack values
        return values.pop();
    }

    // Kiểm tra độ ưu tiên của toán tử
    public static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        }
        return true;
    }

    // Áp dụng toán tử lên hai giá trị
    public static double applyOperation(char op, double b, double a) throws Exception {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new Exception("Không thể chia cho 0");
                }
                return a / b;
        }
        return 0;
    }
}

