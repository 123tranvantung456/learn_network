package com.javaweb.bai4.db;

import java.io.PrintWriter;
import java.sql.*;

public class DatabaseManager {

    // Hàm kết nối đến cơ sở dữ liệu
    public static Connection getConnection() throws SQLException {
        try {
            // Tải driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/ltm";
            String user = "root";
            String password = "123456";

            // Kết nối đến MySQL
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new SQLException("Failed to connect to the database", e);
        }
    }

    // Hàm trả về dữ liệu cho client dưới dạng JSON
    private static String DataToClient(ResultSet rs) throws SQLException {
        StringBuilder json = new StringBuilder();
        json.append("[");
        while (rs.next()) {
            json.append("{")
                    .append("\"id\":").append(rs.getInt("id")).append(", ")
                    .append("\"name\":").append("\"").append(rs.getString("name")).append("\"").append(", ")
                    .append("\"age\":").append(rs.getInt("age"))
                    .append("},");
        }
        if (json.length() > 1) {
            json.setLength(json.length() - 1); // Remove last comma
        }
        json.append("]");
        return json.toString();
    }

    // Hàm tạo người dùng mới (CREATE)
    public static void handleCreate(String sql, PrintWriter out) {
        try (Connection conn = getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                int rows = stmt.executeUpdate();
                out.println(rows > 0 ? "{\"status\": \"success\"}" : "{\"status\": \"failed\"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    public static void handleRead(String sql, PrintWriter out) {
        try (Connection conn = getConnection()) {
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                String jsonResult = DataToClient(rs);
                out.println(jsonResult.length() > 0 ? jsonResult : "{\"status\": \"no_data\"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    // Hàm cập nhật thông tin người dùng (UPDATE)
    public static void handleUpdate(String sql, PrintWriter out) {
        try (Connection conn = getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                int rows = stmt.executeUpdate();
                out.println(rows > 0 ? "{\"status\": \"success\"}" : "{\"status\": \"failed\"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    // Hàm xóa người dùng (DELETE)
    public static void handleDelete(String sql, PrintWriter out) {
        try (Connection conn = getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                int rows = stmt.executeUpdate();
                out.println(rows > 0 ? "{\"status\": \"success\"}" : "{\"status\": \"failed\"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}
