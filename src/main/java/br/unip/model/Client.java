package br.unip.model;

import java.io.IOException;
import java.net.*;

public class Client extends Thread {

    private final DatagramSocket socket;
    private final InetAddress address;
    private final UserTerminal terminal;

    private final byte[] buf = new byte[256];

    public Client(UserTerminal terminal) throws SocketException, UnknownHostException {
        this.terminal = terminal;
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
        System.out.println(" --- CLIENTE INICIADO ---");
    }

    public Packet sendResolveRequest(byte[] packetData) {
        try {
            DatagramPacket datagramToSend = new DatagramPacket(packetData, packetData.length, address, 4445);
            socket.send(datagramToSend);

            DatagramPacket datagramReceived = new DatagramPacket(buf, buf.length);
            socket.receive(datagramReceived);

            return Packet.toPacket(datagramReceived.getData());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void close() {
        socket.close();
    }

    @Override
    public void run(){
        while(true){

            String ip = terminal.readString("\nDigite o ip que deseja resolver o DNS ... (END para desligar o servidor)");
            Packet packetToSend = new Packet().setDnsIp(ip);
            Packet responseFromServer = this.sendResolveRequest(packetToSend.toByteArray());

            System.out.println("\nResposta do servidor é:\nDNS: " + responseFromServer.getDnsUrl() + "\nIP: " + responseFromServer.getDnsIp());

            String shouldEnd = terminal.readString("Continuar? Y/N");

            if(shouldEnd.equals("N")) break;
        }
        System.out.println("\nFechando o cliente...\n");
        socket.close();
    }

}
