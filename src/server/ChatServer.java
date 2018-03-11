package server;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ChatServer {

    private final static int port = 12345;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(50);
    private static final Map<String, ClientConnection> connectedClients = new HashMap<>();


    void initializeHandlers(Thread ServerTcpHandler, Thread ServerUdpHandler){
        ServerTcpHandler.start();
        ServerUdpHandler.start();
    }

    int getPort() {
        return port;
    }

    void addUser(String nickname, ClientConnection clientConnection){
        System.out.println("Adding user : " + nickname);
        connectedClients.put(nickname, clientConnection);
    }

    Map<String, ClientConnection> getConnectedClients(){
        return connectedClients;
    }

    void addService(ClientConnection connection){
        System.out.println("Adding new chat connection");
        executorService.submit(connection);
    }
}
