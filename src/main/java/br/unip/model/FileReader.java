package br.unip.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

public class FileReader {

    public static HashMap<String, String> readDnsTable(){
        String fileName = "dns.txt";
        Path path = Paths.get("src/main/resources/", fileName);
        File fileTxt;

        try {
            fileTxt = path.toFile();
            Scanner scanner = new Scanner(fileTxt);
            return readLines(scanner);
        } catch (IOException e) {
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
