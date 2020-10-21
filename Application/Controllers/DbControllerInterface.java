package com.company.Application.Controllers;

import com.company.Application.Exceptions.InfiniteCoordinate;
import com.company.Application.Exceptions.WrongArgumentException;
import com.company.Application.ProductClasses.Product;
import com.company.Application.ProductClasses.UnitOfMeasure;

import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;
import java.util.ArrayList;

public interface DbControllerInterface {

    BasicFileAttributes getAttrs();
    void addProduct(Integer key, Product product, String login) throws SQLException;
    void removeProduct(int key, String login) throws SQLException;
    boolean updateProduct(int key, Product product, String login) throws SQLException;
    void removeByOUM(UnitOfMeasure unitOfMeasure, String login) throws SQLException;
    void removeGrKey(int key, String login)throws SQLException;
    void removeLwKey(int key, String login) throws SQLException;
    ArrayList<Integer> clear(String login) throws SQLException;
    void saveTree() throws SQLException;
    void loadTree(String path, TreeMapController treeMapController) throws SQLException, WrongArgumentException, InfiniteCoordinate;
    void close() throws SQLException;
    boolean isUserExists(String login);
    boolean checkPass(String login,String pass);
    void addUser(String login, String pass) throws SQLException;

}


