package br.unip.model;

import java.io.IOException;
import java.net.*;

public class Client extends Thread {

    private final DatagramSocket socket;
    private InetAddress address;
    private final UserTerminal terminal;
    private boolean isFirstRun = true;

    private final byte[] buf = new byte[256];
    private String ipToResolve;

    public Client(UserTerminal terminal) throws SocketException, UnknownHostException {
        this.terminal = terminal;
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
        System.out.println(" --- CLIENTE INICIADO ---");
    }
    @Override
    public void run(){
        while(true){
            // Pergunta o ip do servidor DNS
            if (isFirstRun){
                askForDnsServerAddress();
                isFirstRun = false;
            }
            // Pergunta para o usuário o IP que deseja resolver
            askForIpToResolve();

            // Constroi o objeto de pacote para enviar
            Packet packetToSend = new Packet().setDnsIp(ipToResolve);
            // Envia o datagrama para o servidor e retorna a resposta do servidor
            Packet responseFromServer = this.sendResolveRequest(packetToSend.toByteArray());

            // Fecha o app cliente se a resposta do servidor voltar END para o ip e url
            if (responseFromServer.isAppCloseRequest()) break;

            // Semáforo de repetição
            String shouldEndByClient = terminal.readString("Continuar? Y/N");
            if(shouldEndByClient.equals("N")) break;
        }
        System.out.println("\nFechando o cliente...\n");
        socket.close();
    }

    private void askForDnsServerAddress() {
        String dnsServerIp = terminal.readString("Digite o ip do servidor DNS (default para localhost)");
        if (dnsServerIp.equals("default")){
            try {
                this.address = InetAddress.getByName("localhost");
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.address = new InetSocketAddress(dnsServerIp, 4445).getAddress();
        }
    }

    private void askForIpToResolve() {
        this.ipToResolve = terminal.readString("\nDigite o ip que deseja resolver o DNS ... (END para desligar o servidor)");
    }

    public Packet sendResolveRequest(byte[] packetData) {
        try {
            // Envia datagrama UDP para o servidor
            DatagramPacket datagramToSend = new DatagramPacket(packetData, packetData.length, address, 4445);
            socket.send(datagramToSend);

            // Espera a resposta do servidor com o ip resolvido
            DatagramPacket datagramReceived = new DatagramPacket(buf, buf.length);
            socket.receive(datagramReceived);


            Packet resolvedIpPacket = Packet.toPacket(datagramReceived.getData());
            // Print da resposta do servidor
            System.out.println("\nResposta do servidor é:\nDNS: " + resolvedIpPacket.getDnsUrl() + "\nIP: " + resolvedIpPacket.getDnsIp());

            return resolvedIpPacket;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void close() {
        socket.close();
    }
}
