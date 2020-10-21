package com.company.Application.Controllers.Server;


import com.company.Application.Commands.CommandInvoker;
import com.company.Application.Controllers.DbUsers;
import com.company.Application.Data;
import sun.misc.Cleaner;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class ServerController{
    private final static int BUFFER_SIZE = 2048;
    int port;
    private DbUsers dataBase;
    private CommandInvoker commandInvoker;
    private ExecutorService fixedPool = Executors.newFixedThreadPool(4);
    private ExecutorService cachedPool = Executors.newCachedThreadPool();

    public ServerController(int port, DbUsers dataBase, CommandInvoker commandInvoker){
        this.dataBase = dataBase;
        this.commandInvoker = commandInvoker;
        this.port = port;

    }

    public void nexConnection() throws IOException {

            try(ServerSocket serverSocket = new ServerSocket(port)){
                System.out.println("Waiting for connection...");
                Socket socket = serverSocket.accept();
                ClientHandler client = new ClientHandler(socket, dataBase,commandInvoker, fixedPool, cachedPool);
                Thread clientThread = new Thread(client);
                clientThread.start();
            }




    }


//    void checkConnection(){
//        if (socket == null){
//            try {
//                System.out.println("waiting for connection...");
//                socket = serverSocket.accept();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.println("connected");
//        }
//
//
//    }
//
//    public Data nextRequest() throws IOException, ClassNotFoundException {
//
//            try {
//                checkConnection();
//                InputStream in = socket.getInputStream();
//                System.out.println("waiting for data...");
//                ObjectInputStream objectInputStream = new ObjectInputStream(in);
//
//                return (Data) objectInputStream.readObject();
//
//
//            } catch (SocketException e) {
//                System.out.println("disconnected");
//                socket.close();
//                socket = null;
//                return null;
//            }
//
//    }
//
//
//    public void response(Data responseData) throws IOException {
//
//            try{
//                checkConnection();
//                OutputStream out = socket.getOutputStream();
//                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
//                objectOutputStream.writeObject(responseData);
//                objectOutputStream.flush();
//                buff.put(outputStream.toByteArray()).flip();
//                out.write(buff.array());
//                out.flush();
//                System.out.println("sent");
//                buff.clear();
//            }
//            catch (SocketException e){
//                System.out.println("disconnected");
//                socket.close();
//                socket = null;
//                socket = null;
//            }
//
//
//
//
//
//
//
//    }




}
