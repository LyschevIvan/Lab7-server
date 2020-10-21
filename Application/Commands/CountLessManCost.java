package com.company.Application.Commands;

import com.company.Application.Data;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * counts all products with manufactureCost less then entered
 */
class CountLessManCost extends AbstractCommand {
    public CountLessManCost(ControllersProvider controllersProvider) {
        super(controllersProvider);
    }

    @Override
    public ArrayList<Data> execute(Data data, String login){
        long cost = Long.parseLong(data.getMessage()[1]);
        long counter = controllersProvider.getTreeMapController().getProducts().values().stream().filter(p ->
            p.getManufactureCost() < cost).count();

        String ending;
        if (counter == 1)
            ending = "";
        else if((counter>=2)&&(counter<=4))
            ending = "а";
        else
            ending = "ов";
        ArrayList<Data> responseData = new ArrayList<>();
        responseData.add(new Data("Коллекция содержит "+counter+" элемент"+ending+" с рыночной стоимостью меньше заданной "));
        return responseData;

    }


    @Override
    public String getInfo() {
        return "count_less_than_manufacture_cost long : выводит количество элементов, зачения поля manufactureCost которых меньше заданного";
    }
}
