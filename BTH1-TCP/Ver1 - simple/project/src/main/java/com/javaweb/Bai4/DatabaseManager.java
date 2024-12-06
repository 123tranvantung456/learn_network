package com.javaweb.Bai4;

import java.io.PrintWriter;
import java.sql.*;

public class DatabaseManager {

    // Hàm kết nối đến cơ sở dữ liệu
    public static Connection getConnection() throws SQLException {
        try {
            // Tải driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Cấu hình chuỗi kết nối MySQL (thay đổi thông tin kết nối theo cơ sở dữ liệu của bạn)
            String url = "jdbc:mysql://localhost:3306/ltm?useSSL=false&serverTimezone=UTC";
            String user = "root";  // Thay bằng tên người dùng MySQL của bạn
            String password = "123456";  // Thay bằng mật khẩu MySQL của bạn

            // Kết nối đến MySQL
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new SQLException("Failed to connect to the database", e);
        }
    }


    // Hàm tạo người dùng mới (CREATE)
    public static void handleCreate(String data, PrintWriter out) {
        try (Connection conn = getConnection()) {
            String[] parts = data.split(",");
            if (parts.length != 2) {
                out.println("Invalid CREATE command. Format: CREATE name,age");
                return;
            }

            String name = parts[0].trim();
            int age = Integer.parseInt(parts[1].trim());

            String sql = "INSERT INTO users (name, age) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setInt(2, age);
                int rows = stmt.executeUpdate();
                out.println("User created successfully. Rows affected: " + rows);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Database error: " + e.getMessage());
        }
    }

    // Hàm đọc tất cả người dùng (READ)
    public static void handleRead(PrintWriter out) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM users";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                StringBuilder result = new StringBuilder();
                while (rs.next()) {
                    result.append(rs.getInt("id")).append(", ")
                            .append(rs.getString("name")).append(", ")
                            .append(rs.getInt("age")).append("\n");
                }

                out.println(result.length() > 0 ? result.toString() : "No users found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Database error: " + e.getMessage());
        }
    }

    // Hàm cập nhật thông tin người dùng (UPDATE)
    public static void handleUpdate(String data, PrintWriter out) {
        try (Connection conn = getConnection()) {
            String[] parts = data.split(",", 3);
            if (parts.length != 3) {
                out.println("Invalid UPDATE command. Format: UPDATE id,name,age");
                return;
            }

            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            int age = Integer.parseInt(parts[2].trim());

            String sql = "UPDATE users SET name = ?, age = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setInt(2, age);
                stmt.setInt(3, id);
                int rows = stmt.executeUpdate();
                out.println(rows > 0 ? "User updated successfully." : "User not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Database error: " + e.getMessage());
        }
    }

    // Hàm xóa người dùng (DELETE)
    public static void handleDelete(String data, PrintWriter out) {
        try (Connection conn = getConnection()) {
            int id;
            try {
                id = Integer.parseInt(data.trim());
            } catch (NumberFormatException e) {
                out.println("Invalid DELETE command. Format: DELETE id");
                return;
            }

            String sql = "DELETE FROM users WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int rows = stmt.executeUpdate();
                out.println(rows > 0 ? "User deleted successfully." : "User not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Database error: " + e.getMessage());
        }
    }
}
