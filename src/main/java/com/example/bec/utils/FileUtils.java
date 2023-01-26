package com.example.bec.utils;

import com.example.bec.model.SelectItemModel;
`import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FileUtils {
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
    public ReadableByteChannel downloadFileUrl(String url) throws IOException {
        return Channels.newChannel(new URL(url).openStream());
    }
    public void deleteFile(){
        if (this.file.delete()){
            System.out.println("src.File delete");
        }
    }

    public List<SelectItemModel> catalogFileNames(){
        int index = 0;
        List<SelectItemModel> namesFiles = new ArrayList<>();
        if(this.catalog.isDirectory()){
            for(File item : Objects.requireNonNull(this.catalog.listFiles())){
                if(item.isFile()){
                    index++;
                    namesFiles.add(new SelectItemModel(index, item.getName() ));
                }
            }
        }
        return namesFiles;
    }
    public List<String> catalogFileNamesString(){
        List<String> namesFiles = new ArrayList<>();
        if(this.catalog.isDirectory()){
            for(File item : Objects.requireNonNull(this.catalog.listFiles())){
                if(item.isFile()){
                    namesFiles.add(item.getName());
                }
            }
        }
        return namesFiles;
    }
    public void outputStream(ReadableByteChannel readableByteChannel) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(this.file);
        fileOutputStream.getChannel()
                .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        fileOutputStream.close();
    }
    public void updateTextFile(String text) throws IOException {
        if(!this.file.exists()){
            this.createFile();
        }
        FileWriter writer = new FileWriter(file);
        writer.write(text);
        writer.close();
    }
    public Map<String, Object> getJsonFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(this.readFile(), new TypeReference<Map<String, Object> >(){});
    }
}
