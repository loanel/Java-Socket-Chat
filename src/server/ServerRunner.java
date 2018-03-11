package server;

public class ServerRunner {

    public static void main(String[] args) {
        System.out.println("Starting chat server!");
        ChatServer chatServer = new ChatServer();
        ServerTcpHandler serverTcpHandler = new ServerTcpHandler(chatServer);
        ServerUdpHandler serverUdpHandler = new ServerUdpHandler(chatServer);
        chatServer.initializeHandlers(serverTcpHandler, serverUdpHandler);
    }
}
