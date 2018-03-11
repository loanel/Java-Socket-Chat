package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Logger;

class ChatClient {
    private static final Logger logger = Logger.getLogger("Client Logger");

    private String nickname;

    private final PrintWriter writer;
    private final Scanner reader;
    private final Integer port;
    private final Integer multicastPort;
    private final String hostname;
    private final String multicastIp;
    private final Socket tcpSocket;
    private final DatagramSocket datagramSocket;
    private final MulticastSocket multicastSocket;

    ChatClient(Integer port, Integer multicastPort, String hostname, String multicastIp) throws IOException{
        this.reader = new Scanner(System.in);
        this.port = port;
        this.hostname = hostname;
        this.tcpSocket = new Socket(hostname, port);
        System.out.println(tcpSocket.getLocalPort());
        this.datagramSocket = new DatagramSocket(tcpSocket.getLocalPort());
        this.writer = new PrintWriter(tcpSocket.getOutputStream(), true);

        this.nickname = scanNickname();
        System.out.println("Your nickname is : " + this.nickname);
        writer.write(nickname + "\n");
        writer.flush();

        this.multicastPort = multicastPort;
        this.multicastIp = multicastIp;
        this.multicastSocket = new MulticastSocket(multicastPort);
        multicastSocket.joinGroup(InetAddress.getByName(multicastIp));
    }

    String getNickname() {
        return nickname;
    }

    Socket getTcpSocket() {
        return tcpSocket;
    }

    DatagramSocket getUdpSocket() {
        return datagramSocket;
    }
    void initializeChatConnection(ChatTcpHandler chatTcpHandler, ChatUdpHandler chatUdpHandler, ChatMulticastHandler chatMulticastHandler) throws IOException{
        chatTcpHandler.start();
        chatUdpHandler.start();
        chatMulticastHandler.start();
        startChatRoutine();
        tcpSocket.close();
    }

    private String scanNickname(){
        System.out.println("Enter your nickname: ");
        return reader.nextLine();
    }

    private void startChatRoutine() throws IOException{
        while(reader.hasNextLine()){
            String message = reader.nextLine();
            switch(message){
                case "U":{
                    InetAddress address = InetAddress.getByName("localhost");
                    byte[] sendBuffer = (nickname + ": ░░░░░░░░░\n" +
                            "░░░░░░░░░░░░░▒▒▒▒▒░░░░░░░░░░░░░\n" +
                            "░░░░░░░▓░░▒▒▒░░░▒░▒▓▓░░▓░░░░░░░\n" +
                            "░░░░░▓▓▓▓▒▒▒▒░░░░░▒▒▓▓▓▓▓░░░░░░\n" +
                            "░░░▓▓▓▓▓▓▒▒▒▒░░░░▒▒▒▒▓▓▓▓▓▓░░░░\n" +
                            "░▓▓▓▓▓▓▓▒▒▒▒▒░░░░▒▒▒▒▒▓▓▓▓▓▓▓░░\n" +
                            "▓▓▓▓▓▓▓▓▒▒▒▒▒░░░░▒▒▒▒▒▓▓▓▓▓▓▓▓░\n" +
                            "▓▓▓▓▓▓▓▓▒░▓▓▒▒░░░▒▒▒▒▒▒▓▓▓▓▓▓▓▓\n" +
                            "▓▓▓▓▓▓▓▓▒░▓▓░▒░░░▒▒░▓▓▒▓▓▓▓▓▓▓▓\n" +
                            "▓▓▓▓▓▓▓░▒▒▒▒▒░░░░▒▒░▓▓▒▓░▓▓▓▓▓▓\n" +
                            "░▓▓▓▓▓▓░▒▒▒▒▒░░░░▒▒▒▒▒▒░░▓▓▓▓▓▓\n" +
                            "░▓▓▓▓▓▓░▒▒▒▒░░░░░░░▒▒▒░░▓▓▓▓▓▓░\n" +
                            "░░▓▓▓▓▓░▒▒▒░░░░░░░░░▒▒░░▓▓▓▓▓░░\n" +
                            "░░░▓▓▓░░░▒░▒░░░▒▒▒░░▒░░░░▓▓▓░░░\n" +
                            "░░░░░░░░░░▒░░░▓▓▓▓▒░▒░░░░░░░░░░\n" +
                            "░░░░░░░░░░▒░░░▓▓▓▓▓░▒░░░░░░░░░░\n" +
                            "░░░░░░░░░░░▒░░▓▓▓▓▒░▒░░░░░░░░░░\n" +
                            "░░░░░░░░░░░░▒▒▒▒▒▒▒▒░░░░░░░░░░░\n" +
                            "░░░░░░░░░░░░░░▒▒▒▒░░░░░░░░░░░░░\n").getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
                    datagramSocket.send(sendPacket);
                    System.out.println(new String(sendBuffer));
                    break;
                }
                case "M":{
                    InetAddress address = InetAddress.getByName(multicastIp);
                    byte[] sendBuffer = (nickname + ": multicastblaster").getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, multicastPort);
                    multicastSocket.send(sendPacket);
                    break;
                }
                default: {
                    writer.write(message + "\n");
                    writer.flush();
                    break;
                }
            }
        }
    }
}
