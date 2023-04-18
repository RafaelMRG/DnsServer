package br.unip.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

public class Server extends Thread{

    private final DatagramSocket socket;
    private boolean running;
    private final byte[] buf = new byte[256];
    private HashMap<String, String> dnsDictionary;

    public Server setDnsDictionary(HashMap<String, String> dnsDictionary) {
        this.dnsDictionary = dnsDictionary;
        return this;
    }

    public Server() throws SocketException {
        socket = new DatagramSocket(4445);
        System.out.println("--- SERVIDOR INICIADO ---");
    }

    @Override
    public void run() {
        while (true) {
            try {
                DatagramPacket datagramReceived = new DatagramPacket(buf, buf.length);

                System.out.println("\nEsperando requisição do cliente ...");
                socket.receive(datagramReceived);

                InetAddress address = datagramReceived.getAddress();
                int port = datagramReceived.getPort();

                Packet receivedPacket = Packet.toPacket(datagramReceived.getData());
                System.out.println("Pacote recebido com pedido de resolução para o ip: " + receivedPacket.getDnsIp());

                leaveLoopIfAsked(receivedPacket.getDnsIp());

                receivedPacket.setDnsUrl(dnsDictionary);
                byte[] packetBack = receivedPacket.toByteArray();

                DatagramPacket datagramSend = new DatagramPacket(packetBack, packetBack.length, address, port);
                socket.send(datagramSend);
                System.out.println("Enviando confirmação de recebimento para o cliente ...");

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e){
                break;
            }
        }
        System.out.println("\nFechando o servidor...\n");
        socket.close();
    }

    private void leaveLoopIfAsked(String ip) throws Exception {
        if (ip.equals("END")){
            throw new Exception();
        }
    }
}
