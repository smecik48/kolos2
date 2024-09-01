package com.example.demo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class DBHandler {
    static String url = "jdbc:sqlite:baza.db";
    public static void createDb(){
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                var meta  = conn.getMetaData();
            }
            PreparedStatement ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS entry (token TEXT NOT NULL, x INTEGER NOT NULL, y INTEGER NOT NULL, color TEXT NOT NULL, time TEXT NOT NULL);");
            ResultSet rs = ps.executeQuery();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static void insertValues(String id, int x, int y, String color){
        try{
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement ps = conn.prepareStatement("INSERT INTO entry (token, x, y, color, timestamp) VALUES(?, ?, ?, ?, ?);");
            ps.setString(1, id);
            ps.setInt(2, x);
            ps.setInt(3, y);
            ps.setString(4, color);
            ps.setLong(5, System.currentTimeMillis());
        } catch (SQLException ex){
            System.err.println(ex.getMessage());
        }
    }

    public static void color(){
        ResultSet rs = null;
        try {
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM baza;");
            rs = ps.executeQuery();
            BufferedImage image = ImageIO.read(new File("image.png"));
            while (rs.next()) {
                int x = rs.getInt("x");
                int y = rs.getInt("y");
                String color = rs.getString("color");
                image.setRGB(x, y, Color.decode(color).getRGB());
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}
