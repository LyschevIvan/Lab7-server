
package com.company.Application.Commands;

import com.company.Application.Data;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * removes if key is lower
 */
class RemoveLwrKey extends AbstractCommand {
    public RemoveLwrKey(ControllersProvider controllersProvider) {
        super(controllersProvider);
    }

    @Override
    public ArrayList<Data> execute(Data data, String login) {
        Integer key = Integer.valueOf(data.getMessage()[1]);
        ArrayList<Data> responses = new ArrayList<>();
        try {
            controllersProvider.getDbController().removeLwKey(key, login);
            Iterator<Integer> keyIterator = controllersProvider.getTreeMapController().getKeyIterator();
            while(keyIterator.hasNext()){
                Integer k = keyIterator.next();
                if(k<key)
                    keyIterator.remove();
            }
            responses.add(new Data("Продукт успешно удален!"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Ошибка при удалении!");
            responses.add(new Data("Ошибка при удалении!"));
        }
        return responses;


    }

    @Override
    public String getInfo() {
        return "remove_lower_key key : удалить из коллекции все элементы, ключ которых меньше чем заданный";
    }
}
