
package com.company.Application.Commands;

import com.company.Application.Data;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * clears collection
 */
class Clear extends AbstractCommand {
    public Clear(ControllersProvider controllersProvider) {
        super(controllersProvider);
    }

    @Override
    public ArrayList<Data> execute(Data data, String login) {

        ArrayList<Data> response = new ArrayList<>();
        try {
            ArrayList<Integer> ids = controllersProvider.getDbController().clear(login);
            for (Integer id : ids){
                System.out.println(id);
                controllersProvider.getTreeMapController().remove(id);
            }
            response.add(new Data("Удалены элементы, созданные пользователем "+login));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Ошибка при очистке!");
            response.add(new Data("Ошибка при очистке!"));
        }

//        super.response(socket,data);

        return response;
    }

    @Override
    public String getInfo() {
        return "clear : очищает коллекцию";
    }
}
