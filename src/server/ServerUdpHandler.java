package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.logging.Logger;

class ServerUdpHandler extends Thread{

    ChatServer chatServer;
    private static final Logger logger = Logger.getLogger("Tcp Handler Logger");

    ServerUdpHandler(ChatServer chatServer){
        this.chatServer = chatServer;
    }

    @Override
    public void run(){
        try{
            DatagramSocket datagramSocket = new DatagramSocket(chatServer.getPort());
            while(!datagramSocket.isClosed()){
                byte[] receiveBuffer = new byte[2048];
                Arrays.fill(receiveBuffer, (byte)0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                datagramSocket.receive(receivePacket);

                String message = new String(receivePacket.getData());
                String[] splitNickname = message.split(":", 2);
                String sender = splitNickname[0];
                System.out.println("Recieved UDP message from " + message);

                chatServer.getConnectedClients().entrySet().stream()
                        .filter(connection -> !connection.getKey().equals(sender))
                        .forEach(connection -> {
                            try{
                                Integer port = connection.getValue().getSocket().getPort();
                                InetAddress address = InetAddress.getByName("localhost");
                                byte[] sendBuffer = message.getBytes();
                                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
                                datagramSocket.send(sendPacket);
                            } catch(Exception exception){
                                logger.warning(exception.getMessage());
                            }
                        });
            }
        } catch (IOException exception){
            logger.warning(exception.getMessage());
        }
    }

}
