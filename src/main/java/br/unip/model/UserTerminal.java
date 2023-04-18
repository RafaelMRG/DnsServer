package br.unip.model;

import java.util.Scanner;

public class UserTerminal {
    private final Scanner scanner;
    private String ip;
    private String mode;

    public UserTerminal() {
        this.scanner = new Scanner(System.in);
    }

    public void readIp(){
        System.out.println("Insira o IP do servidor");
        this.ip = scanner.nextLine();
        System.out.println("Ip escolhido é: " + this.ip);
    }

    public void readMode(){
        System.out.println("Insira o modo de execução (server ou client)");
        this.mode = scanner.nextLine();
        if ( !(this.mode.equals("server") || this.mode.equals("client")) ){
            throw new RuntimeException("Modo escolhido não existe");
        }
        System.out.println("Modo escolhido é: " + this.mode);
    }

    public String readString(String label){
        System.out.println(label);
        return scanner.nextLine();
    }

    public boolean isServer(){
        return this.mode.equals("server");
    }

    public boolean isClient(){
        return !isServer();
    }

    // Getters & setters
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMode() {
        return mode;
    }
}
