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
    public void fileConfig(
            CommandFileModel commandFileModel,
            StoreCommandModel storeCommandModel
    ) throws IOException {
        String catalog = (String) storeCommandModel.storeGetData(commandFileModel.getCatalog());
        String name = (String) storeCommandModel.storeGetData(commandFileModel.getName());
        FileUtils fileUtils = new FileUtils(catalog, name);
        for (CommandFileOperationModel commandFileOperationModel: commandFileModel.getOperation()){
            Map<String , Object> data = new HashMap<>();
            if (commandFileOperationModel.getParams() != null){
                data = storeCommandModel.storeGetData(commandFileOperationModel.getParams());
            }
            /* создаие файла */
            if (commandFileOperationModel.getType().equals(CommandTypeFileOperationEnums.createFile.getTitle())){
                fileUtils.createFile();
            }
            /* скачать файл по url */
            if (commandFileOperationModel.getType().equals(CommandTypeFileOperationEnums.downloadFileUrl.getTitle())){
               storeCommandModel.updateValue(
                       commandFileOperationModel.getKey(),
                       fileUtils.downloadFileUrl((String) data.get("url"))
               );
            }
            /* запись файл по byte */
            if (commandFileOperationModel.getType().equals(CommandTypeFileOperationEnums.outputStream.getTitle())){
                fileUtils.outputStream((ReadableByteChannel) data.get("byte"));
            }
            /* запись файл по строку */
            if (commandFileOperationModel.getType().equals(CommandTypeFileOperationEnums.outputString.getTitle())){
                fileUtils.updateTextFile(data.get("string").toString());
            }
        }
    }
}
