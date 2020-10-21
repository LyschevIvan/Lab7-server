
package com.company.Application.Commands;

import com.company.Application.Data;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * remove by key
 */
class Remove extends AbstractCommand {
    public Remove(ControllersProvider controllersProvider) {
        super(controllersProvider);
    }

    @Override
    public ArrayList<Data> execute(Data data, String login)  {
        int k = Integer.parseInt(data.getMessage()[1]);
        ArrayList<Data> responses = new ArrayList<>();
        try {
            controllersProvider.getDbController().removeProduct(k, login);
            controllersProvider.getTreeMapController().remove(k);
            responses.add(new Data("Продукт успешно удален!"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Ошикба при удалении!");
            responses.add(new Data("Ошибка при удалении!"));
        }

        return responses;
    }



    @Override
    public String getInfo() {
        return "remove_key k : удаляет элемент с ключем k";
    }
}
