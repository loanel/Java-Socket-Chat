package client;

import java.io.IOException;
import java.util.logging.Logger;

public class ClientRunner {
    private static final Logger logger = Logger.getLogger("Client Logger");
    private static final Integer PORT = 12345;
    private static final Integer MULTICAST_PORT = 12346;
    private static final String MULTICAST_IP = "224.0.0.1"; // 224.0.0.0 - 239.255.255.255

    public static void main(String[] args) {
        try{
            System.out.println("Starting client!");
            ChatClient client = new ChatClient(PORT, MULTICAST_PORT, "localhost", MULTICAST_IP);
            ChatTcpHandler chatTcpHandler = new ChatTcpHandler(client.getTcpSocket());
            ChatUdpHandler chatUdpHandler = new ChatUdpHandler(client.getUdpSocket());
            ChatMulticastHandler chatMulticastHandler = new ChatMulticastHandler(client.getNickname(), MULTICAST_PORT, MULTICAST_IP);
            client.initializeChatConnection(chatTcpHandler, chatUdpHandler, chatMulticastHandler);
        } catch(IOException exception) {
            logger.warning(exception.getMessage());
        }
    }
}
