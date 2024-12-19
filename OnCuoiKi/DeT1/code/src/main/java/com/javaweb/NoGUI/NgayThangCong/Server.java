package com.javaweb.NoGUI.NgayThangCong;

import java.io.*;
import java.net.*;
import java.util.Calendar;

public class Server {
    public static void main(String[] args) {
        int port = 12345;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            // Lắng nghe kết nối liên tục từ client
            while (true) {
                // Chấp nhận kết nối từ client
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                // Tạo luồng xử lý cho từng kết nối
                new ClientHandler(socket).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true)
        ) {
            String receivedText;

            // Lặp qua các kết nối nhận từ client
            while ((receivedText = reader.readLine()) != null) {
                System.out.println("Received: " + receivedText);

                // Xử lý và gửi lại kết quả cho client
                String response = processInput(receivedText);
                writer.println(response);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

//    private String processInput(String input) {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        sdf.setLenient(false);
//
//        try {
//            // Kiểm tra định dạng ngày tháng năm
//            Date date = sdf.parse(input);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            calendar.add(Calendar.DAY_OF_MONTH, 7);
//
//            return sdf.format(calendar.getTime());
//        } catch (Exception e) {
//            // Trả về ngày hệ thống nếu không đúng định dạng
//            return sdf.format(new Date());
//        }
//    }

    private String processInput(String input) {
        String[] dateParts = input.split("/");

        if (dateParts.length != 3) {
            return getCurrentDate();
        }

        try {
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);

            if (month < 1 || month > 12 || day < 1 || day > getDaysInMonth(month, year)) {
                return getCurrentDate();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month - 1, day);
            calendar.add(Calendar.DAY_OF_MONTH, 7);

            return String.format("%02d/%02d/%d", calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

        } catch (NumberFormatException e) {
            return getCurrentDate();
        }
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return String.format("%02d/%02d/%d", calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
    }

    private int getDaysInMonth(int month, int year) {
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                return 31;
            case 4: case 6: case 9: case 11:
                return 30;
            case 2:
                return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) ? 29 : 28;
            default:
                return 0;
        }
    }

}
