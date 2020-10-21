
package com.company.Application.Commands;

import com.company.Application.Data;
import com.company.Application.ProductClasses.Product;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;


/**
 * print collection content to System.out
 */
class Show extends AbstractCommand {
    public Show(ControllersProvider controllersProvider) {
        super(controllersProvider);
    }

    @Override
    public ArrayList<Data> execute(Data data, String login)  {
        ArrayList<Data> responses = new ArrayList<>();
        TreeMap<Integer,Product> products = controllersProvider.getTreeMapController().getProducts();
        List<Map.Entry<Integer, Product>> sorted = products.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList());
        if (!sorted.isEmpty()){
            for (Map.Entry<Integer, Product> entry : sorted){
                responses.add(new Data(entry.getKey(),entry.getValue()));
//                System.out.println(new Data(entry.getKey(),entry.getValue()).toString());
            }
        }
        else {
            responses.add(new Data("Коллекция пуста"));
        }
        return responses;
    }



    @Override
    public String  getInfo() {
       return "show : выводит элементы коллекции";
    }
}
