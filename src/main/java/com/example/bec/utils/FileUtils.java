package com.example.bec.utils;



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;


/* TODO это не сервис */

public class FileUtils{
    private final File file;
    private final File catalog;

    public FileUtils(String url, String name){
        this.file = new File(url + name);
        this.catalog = new File(url);
    }
    public FileUtils(String url){
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
