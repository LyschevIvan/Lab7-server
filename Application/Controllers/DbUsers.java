package com.company.Application.Controllers;

import java.sql.SQLException;

public interface DbUsers {
    boolean isUserExists(String login);
    boolean checkPass(String login,String pass);
    void addUser(String login, String pass) throws SQLException;
}
