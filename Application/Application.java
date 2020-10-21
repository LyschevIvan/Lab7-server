

package com.company.Application;

import com.company.Application.Commands.CommandInvoker;
import com.company.Application.Commands.ControllersProvider;
import com.company.Application.Controllers.*;
import com.company.Application.Controllers.Server.ServerController;
import com.company.Application.Exceptions.InfiniteCoordinate;
import com.company.Application.Exceptions.WrongArgumentException;

import java.io.*;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Class uses to start the application
 */
public class Application {
    private final int port;
    private final String login;
    private final String pass;

    public Application(int port, String login, String pass) {
        this.port = port;
        this.login = login;
        this.pass = pass;

    }

    /**
     * starts the application
     * exits if input stream is closed
     */
    public void start() throws IOException, SQLException {
        String helios = "jdbc:postgresql://pg:5432/studs";

        DataBaseController dbController = new DataBaseController(helios, login, pass);
        TreeMapController treeMapController = new TreeMapController();
        try {

            ControllersProvider controllersProvider = new ControllersProvider(treeMapController, dbController);
            CommandInvoker commandInvoker = new CommandInvoker(controllersProvider);
            ServerController serverController = new ServerController(port, dbController, commandInvoker);

            dbController.loadTree(helios, treeMapController);
            System.out.println("Успешный запуск. порт: "+ port);

            InputReader inputReader = new InputReader(dbController);
            Thread inp = new Thread(inputReader);
            inp.start();
            while (!inp.isInterrupted()){
                serverController.nexConnection();
            }



        }  catch (WrongArgumentException e) {
            e.printStackTrace();
        }
        finally {
            try {
                dbController.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


    }
    
}
