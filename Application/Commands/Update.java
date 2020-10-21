
package com.company.Application.Commands;

import com.company.Application.Data;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * uses to change value by key in collection
 */
class Update extends AbstractCommand {
    public Update(ControllersProvider controllersProvider) {
        super(controllersProvider);
    }

    @Override
    public ArrayList<Data> execute(Data data, String login)  {
        int key = Integer.parseInt(data.getMessage()[1]);
        ArrayList<Data> responses = new ArrayList<>();
        Data response;
        if(data.getProduct()!= null){
            try {

                if (controllersProvider.getDbController().updateProduct(key, data.getProduct(), login)){
                    controllersProvider.getTreeMapController().getProducts().replace(key, data.getProduct());
                    responses.add(new Data("Продукт обновлен успешно"));
                }
                else{
                    responses.add(new Data("Отказано в доступе"));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                System.out.println("Ошибка при изменении");
                responses.add(new Data("Ошибка при изменении"));
            }

        }
        else {
            response = new Data(key, controllersProvider.getTreeMapController().getProducts().get(key)) ;
            if (response.getProduct() != null) {
                responses.add(response);
            }
            else {
                responses.add(new Data("Не удалось найти элемент с данным Id. Попробуйте еще раз"));
            }
        }
        return responses;

    }

    @Override
    public String getInfo() {
        return "update key : предлагает изменить данные о продукте с ключем key";
    }
}
