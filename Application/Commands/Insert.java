
package com.company.Application.Commands;


import com.company.Application.Data;
import com.company.Application.ProductClasses.Product;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

/**
 * insert with key
 */
class Insert extends AbstractCommand {
    public Insert(ControllersProvider controllersProvider) {
        super(controllersProvider);
    }

    @Override
    public ArrayList<Data> execute(Data data, String login)  {
        Product product = data.getProduct();
        Integer key = Integer.valueOf(data.getMessage()[1]);
        product.setCreationDate(new Date());
        ArrayList<Data> responses = new ArrayList<>();
        try {
            if (!controllersProvider.getTreeMapController().getProducts().containsKey(key)){
                controllersProvider.getDbController().addProduct(key ,product, login);
                controllersProvider.getTreeMapController().put(key,product);
                responses.add(new Data("Продукт успешно добавлен!"));
            }
            else {
                responses.add(new Data("Продукт уже содержится в коллекции, используйте команду update!"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Ошибка при добавлени!");
            responses.add(new Data("Ошибка при добавлении!"));
        }

        return responses;
    }



    @Override
    public String getInfo() {
        return "insert int: команда служит для добавления элемента в коллекцию";
    }
}
