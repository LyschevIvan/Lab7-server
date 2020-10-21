
package com.company.Application.Commands;

import com.company.Application.Data;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * remove if key is greater
 */
class RemoveGrKey extends AbstractCommand {
    public RemoveGrKey(ControllersProvider controllersProvider) {
        super(controllersProvider);
    }

    @Override
    public ArrayList<Data> execute(Data data, String login)  {
        ArrayList<Data> responses = new ArrayList<>();
        Integer key = Integer.valueOf(data.getMessage()[1]);
        try{

            controllersProvider.getDbController().removeGrKey(key, login);
            Iterator<Integer> keyIterator = controllersProvider.getTreeMapController().getKeyIterator();
            while (keyIterator.hasNext()){
                Integer k = keyIterator.next();
                if(k>key)
                    keyIterator.remove();
            }
            responses.add(new Data("Продукт успешно удален!")) ;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Ошибка при удалении!");
            responses.add(new Data("Ошибка при удалении!"));
        }
        return responses;


    }


    @Override
    public String getInfo() {
        return "remove_greater_key key : удалить из коллекции все элементы, ключ которых превышает заданный";
    }
}
