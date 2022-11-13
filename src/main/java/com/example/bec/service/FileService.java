package com.example.bec.service;



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;



public class FileService {

    private final File file;
    private final File catalog;

    public FileService(String url, String name){
        this.file = new File(url + name);
        this.catalog = new File(url);
    }
    public FileService(String url){
        this.file = new File(url);
        this.catalog = new File(url);
    }

    public File returnFile(){
        if(this.file.exists()){
            System.out.println("src.File exists");
        }
        return this.file;
    }
    public String readFile() throws IOException {
        System.out.println(this.file.toPath());
        return new String(Files.readAllBytes(this.file.toPath()));
    }

    public void createFile() throws IOException {
        if (this.catalog.mkdirs()){
            if (this.file.createNewFile()){
                System.out.println("src.File create");
            }
        }
    }

    public void deleteFile(){
        if (this.file.delete()){
            System.out.println("src.File delete");
        }
    }

    public void updateTextFile(String text) throws IOException {
        if(!this.file.exists()){
            this.createFile();
        }
        FileWriter writer = new FileWriter(file);
        writer.write(text);
        writer.close();
    }
}
