package com.example.bec.service;

import com.example.bec.enums.CommandTypeFileOperationEnums;
import com.example.bec.model.command.file.CommandFileModel;
import com.example.bec.model.command.file.CommandFileOperationModel;
import com.example.bec.model.command.store.StoreCommandModel;
import com.example.bec.utils.FileUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileService {

    private void fileConfig(
            CommandFileModel commandFileModel,
            StoreCommandModel storeCommandModel,
            FileUtils fileUtils
    ) throws IOException {
        for (CommandFileOperationModel commandFileOperationModel: commandFileModel.getOperation()){
            Map<String , Object> data = new HashMap<>();
            if (commandFileOperationModel.getParams() != null){
                data = storeCommandModel.storeGetData(commandFileOperationModel.getParams());
            }

            /* создание файла */
           if (commandFileOperationModel.getType().equals(CommandTypeFileOperationEnums.createFile.getTitle())){
                fileUtils.createFile();
            }

            /* скачать файл по url */
           else if (commandFileOperationModel.getType().equals(CommandTypeFileOperationEnums.downloadFileUrl.getTitle())){
                storeCommandModel.updateValue(
                        commandFileOperationModel.getKey(),
                        fileUtils.downloadFileUrl((String) data.get("url"))
                );
            }

            /* запись файл по ReadableByteChannel */
           else if (commandFileOperationModel.getType().equals(CommandTypeFileOperationEnums.outputStream.getTitle())){
                fileUtils.outputStream((ReadableByteChannel) data.get("byte"));
            }

            /* запись файл по строку */
           else if (commandFileOperationModel.getType().equals(CommandTypeFileOperationEnums.outputString.getTitle())){
                fileUtils.updateTextFile(data.get("string").toString());
            }

            /* чтения файла строкой */
           else if (commandFileOperationModel.getType().equals(CommandTypeFileOperationEnums.loaderString.getTitle())){
                storeCommandModel.updateValue(
                        commandFileOperationModel.getKey(),
                        fileUtils.readFile()
                );
            }

            /* возращение имен файлов в каталоге [{id,name}] */
           else if (commandFileOperationModel.getType().equals(CommandTypeFileOperationEnums.fileNameCatalog.getTitle())){
                storeCommandModel.updateValue(
                        commandFileOperationModel.getKey(),
                        fileUtils.catalogFileNames()
                );
            }

            /* возращение имен файлов в каталоге [String] */
           else if (commandFileOperationModel.getType().equals(CommandTypeFileOperationEnums.catalogFileNamesString.getTitle())){
                storeCommandModel.updateValue(
                        commandFileOperationModel.getKey(),
                        fileUtils.catalogFileNamesString()
                );
            }

            /* возвращение файла json */
           else if (commandFileOperationModel.getType().equals(CommandTypeFileOperationEnums.outputJson.getTitle())){
                storeCommandModel.updateValue(
                        commandFileOperationModel.getKey(),
                        fileUtils.getJsonFile()
                );
            }
        }
    }

    public void fileConfig(
            CommandFileModel commandFileModel,
            StoreCommandModel storeCommandModel
    ) throws IOException {
        if (commandFileModel.getCatalog() != null && commandFileModel.getName() != null){
            String catalog = (String) storeCommandModel.storeGetData(commandFileModel.getCatalog());
            String name = (String) storeCommandModel.storeGetData(commandFileModel.getName());
            FileUtils fileUtils = new FileUtils(catalog, name);
            this.fileConfig(commandFileModel, storeCommandModel, fileUtils);
        }else if (commandFileModel.getCatalog() != null){
            String catalog = (String) storeCommandModel.storeGetData(commandFileModel.getCatalog());
            FileUtils fileUtils = new FileUtils(catalog);
            this.fileConfig(commandFileModel, storeCommandModel, fileUtils);
        }
    }
}
