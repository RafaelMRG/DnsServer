package br.unip;

import br.unip.model.Client;
import br.unip.model.FileReader;
import br.unip.model.Server;
import br.unip.model.UserTerminal;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        UserTerminal terminal = new UserTerminal();
        terminal.readMode();

        if (terminal.isServer()){
            Server server = new Server()
                    .setDnsDictionary(FileReader.readDnsTable());
            server.start();
        } else {
            Client client = new Client(terminal);
            client.start();
        }
    }
}