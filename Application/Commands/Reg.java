package com.company.Application.Commands;

import com.company.Application.Data;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Reg extends AbstractCommand{
    public Reg(ControllersProvider controllersProvider) {
        super(controllersProvider);
    }

    @Override
    public ArrayList<Data> execute(Data data, String login1)  {
        ArrayList<Data> responses = new ArrayList<>();
        int minLength = data.getMinLength();
        String[] args = data.getMessage();
        String login = "";
        String pass = "";
        if (args.length >= minLength+1){
            login = args[minLength];
        }
        if (args.length == minLength+2){
            pass = args[minLength+1];
        }
        try {
            controllersProvider.getDbController().addUser(login, pass);
            responses.add(new Data("Успешная регистрация"));
        }
        catch (SQLException e){
            responses.add(new Data("Пользователь с таким логином уже существует"));
        }

        return responses;
    }

    @Override
    String getInfo() {
        return "reg login password: для регистрации новых пользователей";
    }
}
