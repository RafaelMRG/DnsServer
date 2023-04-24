package br.unip.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

public class FileReader {

    public static HashMap<String, String> readDnsTable(){
        String fileName = "dns.txt";
        Path path = Paths.get("src/main/resources/", fileName);
        Path path2 = Paths.get("", fileName);

        File fileTxt;
        Scanner scanner;

        fileTxt = path.toFile();
        try {
            scanner = new Scanner(fileTxt);
        } catch (IOException e) {
            scanner = getFallbackScanner(path2);
        }
        return readLines(scanner);
    }

    private static Scanner getFallbackScanner(Path path){
        File file = path.toFile();
        try {
            return new Scanner(file);
        } catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    private static HashMap<String, String> readLines(Scanner scanner){
        HashMap<String, String> dnsDictionary = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String ip = line.split(":")[0];
            String url = line.split(":")[1];
            dnsDictionary.put(ip, url);
        }
        scanner.close();
        return dnsDictionary;
    }
}
