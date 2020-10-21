
package com.company.Application.Commands;

import com.company.Application.Data;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * shows last 13 entered commands
 */
class History extends AbstractCommand {
    CommandInvoker commandInvoker;
    public History(ControllersProvider controllersProvider, CommandInvoker commandInvoker) {
        super(controllersProvider);
        this.commandInvoker = commandInvoker;
    }

    @Override
    public ArrayList<Data> execute(Data data, String login) {
        StringBuilder stringBuilder = new StringBuilder();
        commandInvoker.getEnteredCommands().forEach(s -> stringBuilder.append(s+"\n"));
        ArrayList<Data> responses = new ArrayList<>();
        responses.add(new Data(stringBuilder.toString()));
        return responses;
    }


    @Override
    public String getInfo() {
        return "history : выводит последние 13 введенных команд";
    }
}
