
package com.company.Application.Commands;

import com.company.Application.Data;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * shows information about collection
 */
class Info extends AbstractCommand {
    public Info(ControllersProvider controllersProvider) {
        super(controllersProvider);
    }

    @Override
    public ArrayList<Data> execute(Data data, String login)  {
        ArrayList<Data> responses = new ArrayList<>();
        responses.add(new Data(controllersProvider.getTreeMapController().getInfo(controllersProvider.getDbController())));
        return responses;
    }

    @Override
    public String getInfo() {
        return "info : выводит информацию о коллекции";
    }
}
