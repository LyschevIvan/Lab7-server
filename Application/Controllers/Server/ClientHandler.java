package com.company.Application.Controllers.Server;

import com.company.Application.Commands.CommandInvoker;
import com.company.Application.Controllers.DbUsers;
import com.company.Application.Data;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;



public class ClientHandler implements  Runnable {
    volatile Data data;
    final Socket socket;
    final private DbUsers dataBase;
    final private CommandInvoker commandInvoker;
    final private ExecutorService fixedPool;
    final private ExecutorService cachedPool;
//    private volatile boolean ready = false;
    private ArrayList<Data> responses = new ArrayList<>();

    public ClientHandler(Socket socket, DbUsers dataBase, CommandInvoker commandInvoker, ExecutorService fixedPool, ExecutorService cachedPool) {
        this.socket = socket;
        this.dataBase = dataBase;
        this.commandInvoker = commandInvoker;
        this.fixedPool = fixedPool;
        this.cachedPool = cachedPool;

    }

    @Override
    public void run() {

        fixedPool.submit(this::handleRequest);

//        Thread handle = new Thread(this::handleData);
//        handle.start();
//        cachedPool.submit(this::sendResponse);

    }
    private boolean handleRequest() throws IOException {
        System.out.println("New client connected");

        InputStream in = socket.getInputStream();
        try{
            while (!socket.isClosed()){
                System.out.println("waiting for data...");
                ObjectInputStream objectInputStream = new ObjectInputStream(in);
                data = (Data)objectInputStream.readObject();
                Thread handle = new Thread(this::handleData);
                handle.start();
//                ready = false;
            }
        }
        catch (SocketException e) {

            System.out.println("disconnected");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }



    private void handleData(){

        int minLength = data.getMinLength();
        String[] args = data.getMessage();
        String login = "";
        String pass = "";
        if (args.length >= minLength+1){
           login = args[minLength];
        }
        if (args.length == minLength+2){
            pass = args[minLength+1];
        }
        if (!login.equals("")){
            if(dataBase.isUserExists(login)){
                if (dataBase.checkPass(login, pass))
                    responses = commandInvoker.executeCommand(data,login);
                else {
                    responses.clear();
                    responses.add(new Data("Пароль не совпадает"));
                }
            }
            else{
                if (data.getMessage()[0].equals("reg")){
                responses = commandInvoker.executeCommand(data,login);
            }
            else {
                responses.clear();
                responses.add(new Data("Пользователь с таким именем не найден! \nзарегистрируйтесь с помощью команды reg login password"));
            }

            }
        }
        else {
            responses.clear();
            responses.add(new Data("Пожайлуйста введите логин и пароль в конце команды"));




        }
        cachedPool.submit(this::sendResponse);

    }
    private void sendResponse(){
        try {
            ByteBuffer buff = ByteBuffer.allocate(2048);

            for (Data response : responses){
                OutputStream out = socket.getOutputStream();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

                objectOutputStream.writeObject(response);
                objectOutputStream.flush();
                buff.put(outputStream.toByteArray()).flip();
                out.write(buff.array());
                out.flush();
                System.out.println("sent");
                buff.clear();
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }




    }

}
