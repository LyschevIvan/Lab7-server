package com.company;

import com.company.Application.Application;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        if (args.length == 3){
            boolean success = false;
            while (!success){
                try{
                    int port = Integer.parseInt(args[0]);
                    String login = args[1];
                    String pass = args[2];
                    Application app = new Application(port, login, pass);

                    try {
                        app.start();
                        success = true;
                    } catch (IOException e) {
                        System.out.println("Ошибка назначении порта!");

                        port++;
                    } catch (SQLException throwables) {
                        success = true;
                        throwables.printStackTrace();
                        System.out.println("Неправильный логин или пароль");
                    }
                }
                catch (NumberFormatException e){
                    success = true;
                    System.out.println("Неправильно введенный порт!");
                }
            }

        }
        else
        {
            System.out.println("lab6-server.jar port");
        }

    }
}
