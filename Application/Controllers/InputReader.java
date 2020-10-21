
package com.company.Application.Controllers;


import java.sql.SQLException;
import java.util.Scanner;


/**
 * uses to manage input stream and handle it
 */
public class InputReader implements Runnable {

    DbControllerInterface dbControllerInterface;

    public InputReader(DbControllerInterface dbControllerInterface) {
        this.dbControllerInterface = dbControllerInterface;
    }

    @Override
    public void run() {
        Scanner console = new Scanner(System.in);
        String inp = "";
        while (!inp.equals("exit")){
            inp = console.nextLine();
            System.out.println(inp);
            if (inp.equals("save")){
                try {
                    dbControllerInterface.saveTree();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
//
        System.exit(0);
    }
}
