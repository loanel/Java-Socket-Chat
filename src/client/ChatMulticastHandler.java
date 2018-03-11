package client;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.logging.Logger;

public class ChatMulticastHandler extends Thread{
    private static Logger logger = Logger.getLogger("Chat Multicast handler logger");
    private String nickname;
    private Integer multicastPort;
    private String multicastIp;

    ChatMulticastHandler(String nickname, Integer multicastPort, String multicastIp){
        this.nickname = nickname;
        this.multicastPort = multicastPort;
        this.multicastIp = multicastIp;
    }

    @Override
    public void run(){
        try{
            MulticastSocket multicastSocket = new MulticastSocket(multicastPort);
            multicastSocket.joinGroup(InetAddress.getByName(multicastIp));
            while(!multicastSocket.isClosed()){
                byte[] receiveBuffer = new byte[2048];
                Arrays.fill(receiveBuffer, (byte)0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                multicastSocket.receive(receivePacket);
                String message = new String(receivePacket.getData());
                if(!myMessage(message)){
                    System.out.println("Multicast message from " + message);
                }
            }
        } catch(IOException exception){
            logger.warning(exception.getMessage());
        }
    }

    private boolean myMessage(String message){
        String[] splitNickname = message.split(":", 2);
        return splitNickname[0].equals(nickname);
    }
}
