package com.company.Application.Controllers;

import com.company.Application.Exceptions.InfiniteCoordinate;
import com.company.Application.Exceptions.WrongArgumentException;
import com.company.Application.ProductClasses.*;


import java.nio.file.attribute.BasicFileAttributes;

import java.sql.*;

import java.util.ArrayList;
import java.util.HashMap;

public class DataBaseController implements DbControllerInterface, DbUsers {
    private Connection conn;
    private final HashMap<String, String> users = new HashMap<>();
    public DataBaseController(String path, String user, String pass) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        connect(path,user,pass);

    }
    private void connect(String path, String user, String pass) throws SQLException {

        this.conn = DriverManager.getConnection(path, user, pass);
        conn.setAutoCommit(false);
        Statement statement = conn.createStatement();

        statement.execute("CREATE TABLE IF NOT EXISTS products(" +
                "key int," +
                "id bigint," +
                "name text not null," +
                "x_coord int not null," +
                "y_coord real not null," +
                "date date not null," +
                "price real," +
                "part_number text," +
                "man_const bigint not null ," +
                "uom text," +
                "person_name text not null," +
                "person_height bigint," +
                "person_weight real," +
                "hair_color text," +
                "login text," +
                "PRIMARY KEY (key)" +
                ")");
        statement.execute("CREATE TABLE IF NOT EXISTS users(" +
                "login text," +
                "pass text," +
                "PRIMARY KEY (login)" +
                ")");
        conn.commit();
    }

    public  void addProduct(Integer key, Product product, String login) throws SQLException {
        try (PreparedStatement insert = conn.prepareStatement("INSERT INTO products VALUES(?,nextval('ids'),?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
            String hair_color = product.getOwner().getHairColor() == null ? null : product.getOwner().getHairColor().toString();
            String uom = product.getUnitOfMeasure() == null ? null : product.getUnitOfMeasure().toString();
            insert.setInt(1, key);
            insert.setString(2, product.getName());
            insert.setInt(3, product.getCoordinates().getX());
            insert.setFloat(4, product.getCoordinates().getY());
            insert.setDate(5, new Date(product.getCreationDate().getTime()));
            insert.setFloat(6, product.getPrice());
            insert.setString(7, product.getPartNumber());
            insert.setLong(8, product.getManufactureCost());
            insert.setString(9, uom);
            insert.setString(10, product.getOwner().getName());
            insert.setLong(11, product.getOwner().getHeight());
            insert.setFloat(12, product.getOwner().getWeight());
            insert.setString(13, hair_color);
            insert.setString(14,login);
            insert.executeUpdate();

        }

    }

    @Override
    public synchronized boolean updateProduct(int key, Product product, String login) throws SQLException {
        try(PreparedStatement update = conn.prepareStatement("UPDATE products " +
                "Set name = ?, x_coord =?, y_coord = ?, price = ?, part_number =?, man_const = ?," +
                "uom = ?, person_name =?, person_height =?, person_weight =?, hair_color =?" +
                "WHERE key = ? AND login =?")) {
            String hair_color = product.getOwner().getHairColor() == null ? null : product.getOwner().getHairColor().toString();
            String uom = product.getUnitOfMeasure() == null ? null : product.getUnitOfMeasure().toString();
            update.setString(1, product.getName());
            update.setInt(2, product.getCoordinates().getX());
            update.setFloat(3, product.getCoordinates().getY());
            update.setFloat(4, product.getPrice());
            update.setString(5, product.getPartNumber());
            update.setLong(6, product.getManufactureCost());
            update.setString(7, uom);
            update.setString(8, product.getOwner().getName());
            update.setLong(9, product.getOwner().getHeight());
            update.setFloat(10, product.getOwner().getWeight());
            update.setString(11, hair_color);
            update.setInt(12, key);
            update.setString(13, login);
            return update.executeUpdate()>0;

        }
    }

    @Override
    public synchronized ArrayList<Integer> clear(String login) throws SQLException {
        ArrayList<Integer> ids = new ArrayList<>();
        try(PreparedStatement select = conn.prepareStatement("Select key from products where login = ?")){
            select.setString(1, login);
            try(ResultSet resultSet = select.executeQuery()) {
                while (resultSet.next()){
                    ids.add(resultSet.getInt(1));
                }
            }
        }
        try(PreparedStatement statement = conn.prepareStatement("DELETE from products where login =?")){
            statement.setString(1,login);
            statement.executeUpdate();

        }

        return ids;
    }

    @Override
    public synchronized void removeProduct(int key, String login) throws SQLException {
        try(PreparedStatement remove = conn.prepareStatement("DELETE FROM products WHERE key = ? AND login = ?")){
            remove.setInt(1, key);
            remove.setString(2,login);
            remove.executeUpdate();

        }

    }

    @Override
    public synchronized void removeByOUM(UnitOfMeasure unitOfMeasure, String login) throws SQLException {
        try(PreparedStatement remove = conn.prepareStatement("DELETE FROM products Where uom = ? AND login = ?")){
            remove.setString(1,unitOfMeasure.toString());
            remove.setString(2,login);
            remove.executeUpdate();

        }


    }

    @Override
    public synchronized void removeGrKey(int key, String login) throws SQLException {
        try(PreparedStatement remove = conn.prepareStatement("DELETE FROM products Where key > ? AND login = ?")){
            remove.setInt(1,key);
            remove.setString(2,login);
            remove.executeUpdate();

        }


    }

    @Override
    public synchronized void removeLwKey(int key, String login) throws SQLException {
        try(PreparedStatement remove = conn.prepareStatement("DELETE FROM products Where key < ? AND login = ?")){
            remove.setInt(1, key);
            remove.setString(2,login);
            remove.executeUpdate();

        }


    }

    @Override
    public BasicFileAttributes getAttrs() {
        return null;
    }

    @Override
    public synchronized void saveTree() throws SQLException {
        conn.commit();
    }



    @Override
    public void loadTree(String path, TreeMapController treeMapController) throws SQLException, WrongArgumentException {
        try(Statement statement = conn.createStatement()){
            try(ResultSet resultSet = statement.executeQuery("select * from products")) {
                while (resultSet.next()){
                    int key = resultSet.getInt(1);
                    Product product = new Product();
                    product.setId(resultSet.getLong(2));
                    product.setName(resultSet.getString(3));
                    Coordinates coordinates = new Coordinates();
                    coordinates.setX(resultSet.getInt(4));
                    try {
                        coordinates.setY(resultSet.getFloat(5));
                    } catch (InfiniteCoordinate infiniteCoordinate) {
                        try {
                            coordinates.setY(0f);
                        } catch (InfiniteCoordinate coordinate) {
                            coordinate.printStackTrace();
                        }
                    }
                    product.setCoordinates(coordinates);
                    product.setCreationDate(resultSet.getDate(6));
                    product.setPrice(resultSet.getFloat(7));
                    product.setPartNumber(resultSet.getString(8));
                    product.setManufactureCost(resultSet.getLong(9));
                    String uomStr = resultSet.getString(10);
                    UnitOfMeasure uom;
                    if (uomStr != null){
                        switch (uomStr.toLowerCase()){
                            case "kg": uom = UnitOfMeasure.KILOGRAMS;break;
                            case "gr": uom = UnitOfMeasure.GRAMS; break;
                            case "pcs": uom = UnitOfMeasure.PCS;break;
                            case "mg": uom = UnitOfMeasure.MILLIGRAMS; break;
                            default: uom = null;
                        }
                    }
                    else {
                        uom = null;
                    }
                    product.setUnitOfMeasure(uom);
                    Person owner = new Person();
                    owner.setName(resultSet.getString(11));
                    owner.setHeight(resultSet.getLong(12));
                    owner.setWeight(resultSet.getFloat(13));
                    Color color;
                    String colorString = resultSet.getString(14);
                    if(colorString != null){
                        switch (colorString.toLowerCase()){
                            case "green": color = Color.GREEN;break;
                            case "red": color = Color.RED;break;
                            case "black": color = Color.BLACK;break;
                            case "blue": color = Color.BLUE;break;
                            case "orange": color = Color.ORANGE; break;
                            default: color = null;
                        }
                    }
                    else {
                        color = null;
                    }

                    owner.setHairColor(color);
                    product.setOwner(owner);
                    treeMapController.put(key,product);
                }

            }
            }

        try(Statement statement = conn.createStatement()){
            try (ResultSet resultSet = statement.executeQuery("SELECT * from users")){
                while (resultSet.next()){
                    System.out.println(resultSet.getString(1) + " " + resultSet.getString(2));
                    users.put(resultSet.getString(1),resultSet.getString(2));
                }
            }
        }
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }

    @Override
    public boolean isUserExists(String login) {

        return users.containsKey(login);
    }

    @Override
    public boolean checkPass(String login, String pass) {
        return users.get(login).equals(Sha384.getHash(pass));

    }

    @Override
    public synchronized void addUser(String login, String pass) throws SQLException {
        try(PreparedStatement select = conn.prepareStatement("Insert into users values(?,?)")) {
            select.setString(1, login);
            select.setString(2, Sha384.getHash(pass));
            select.execute();
            users.put(login,Sha384.getHash(pass));

        }
    }
}
