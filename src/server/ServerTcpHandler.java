package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Logger;

class ServerTcpHandler extends Thread{
    private static final Logger logger = Logger.getLogger("Tcp Handler Logger");
    private final ChatServer chatServer;


    ServerTcpHandler(ChatServer chatServer){
        this.chatServer = chatServer;
    }

    @Override
    public void run(){
        try{
            ServerSocket serverSocket = new ServerSocket(chatServer.getPort());
            while(!serverSocket.isClosed()){
                ClientConnection clientConnection = new ClientConnection(serverSocket.accept(), chatServer);
                chatServer.addService(clientConnection);
            }
        } catch (IOException exception){
            logger.warning(exception.getMessage());
        }
    }
}
