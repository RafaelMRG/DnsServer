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

                Packet receivedPacket = receiveResolveRequest(datagramReceived);

                leaveLoopIfAsked(receivedPacket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e){
                break;
            }
        }
        System.out.println("\nFechando o servidor...\n");
        socket.close();
    }

    private Packet receiveResolveRequest(DatagramPacket datagramReceived) throws IOException {
        System.out.println("\nEsperando requisição do cliente ...");
        // Espera resposta do cliente
        socket.receive(datagramReceived);

        // Extrai o endereço ip e porta do cliente
        InetAddress address = datagramReceived.getAddress();
        int port = datagramReceived.getPort();

        // Serializa o datagrama recebido para objeto Packet
        byte[] datagramData = datagramReceived.getData();
        Packet receivedPacket = Packet.toPacket(datagramData);
        System.out.println("Pacote recebido com pedido de resolução para o ip: " + receivedPacket.getDnsIp());


        receivedPacket.setDnsUrl(dnsDictionary);
        byte[] packetBack = receivedPacket.toByteArray();

        DatagramPacket datagramBackToClient = new DatagramPacket(packetBack, packetBack.length, address, port);
        socket.send(datagramBackToClient);
        System.out.println("Enviando confirmação de recebimento para o cliente ...");
        return receivedPacket;
    }

    private void leaveLoopIfAsked(Packet packet) throws Exception {
        if (packet.isAppCloseRequest()){
            throw new Exception();
        }
    }
}
