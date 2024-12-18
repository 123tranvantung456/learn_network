package com.javaweb.bai4.db;

import java.io.PrintWriter;

public class Handle {

    public static void Handle(String command, PrintWriter out) {
        try {
            command = command.trim().toUpperCase();

            if (command.startsWith("CREATE")) {
                DatabaseManager.handleCreate(command, out);
            } else if (command.startsWith("SELECT")) {
                DatabaseManager.handleRead(command, out);
            } else if (command.startsWith("UPDATE")) {
                DatabaseManager.handleUpdate(command, out);
            } else if (command.startsWith("DELETE")) {
                DatabaseManager.handleDelete(command, out);
            } else {
                out.println("{\"status\": \"error\", \"message\": \"Invalid command\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}
