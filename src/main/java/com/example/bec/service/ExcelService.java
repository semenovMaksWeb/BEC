package com.example.bec.service;

import com.example.bec.enums.CommandExcelType;
import com.example.bec.model.command.file.CommandFileModel;
import com.example.bec.model.command.store.StoreCommandModel;
import com.example.bec.utils.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ExcelService {

    public void createSheet(Workbook wb, String name){
        wb.createSheet(name);
    }

    public Sheet getSheet(Workbook wb, String name){
        return wb.getSheet(name);
    }

    public void generatorDataExcel(List<Map<String, Object>> data, XSSFSheet sheet){
        XSSFRow rowColumn = sheet.createRow(0);
        rowCreateCell(data.get(0).entrySet(), rowColumn, "key");

        int index = 0;
        for (Map<String , Object> elem : data){
            index++;
            XSSFRow row = sheet.createRow(index);
            rowCreateCell(elem.entrySet(), row, "value");
        }
    }
    public void setExcel(Workbook workbook, FileOutputStream fos) throws IOException {
        workbook.write(fos);
        fos.close();
    }

    public void configExcel(StoreCommandModel storeCommandModel, CommandFileModel commandFileModel) throws IOException, InvalidFormatException {
        FileUtils fileUtils = new FileUtils(
                storeCommandModel.storeGetData(commandFileModel.getCatalog()).toString(),
                storeCommandModel.storeGetData(commandFileModel.getName()).toString()
        );
        fileUtils.createFile();
        Workbook workbook = WorkbookFactory.create(fileUtils.readInputStream());
        commandFileModel.getOperation().forEach(commandFileOperationModel -> {
            try {
                Map<String , Object> data = new HashMap<>();
                if (commandFileOperationModel.getParams() != null){
                     data = storeCommandModel.storeGetData(commandFileOperationModel.getParams());
                }
                /* создать страницу excel */
                if (commandFileOperationModel.getType().equals(CommandExcelType.createSheet.getTitle())){
                    this.createSheet(workbook, data.get("name").toString());
                    storeCommandModel.updateValue(
                            commandFileOperationModel.getKey(),
                            this.getSheet(workbook, data.get("name").toString())
                    );
                }
                /* получить страницу excel */
                else if (commandFileOperationModel.getType().equals(CommandExcelType.getSheet.getTitle())){
                   storeCommandModel.updateValue(
                           commandFileOperationModel.getKey(),
                           this.getSheet(workbook, data.get("name").toString())
                   );
                }
                /* заполнить данными excel */
                else if (commandFileOperationModel.getType().equals(CommandExcelType.generatorDataExcel.getTitle())){
                    this.generatorDataExcel((List<Map<String, Object>>) data.get("data"), (XSSFSheet) data.get("sheet"));
                }
                /* заполнить данными excel */
                else if (commandFileOperationModel.getType().equals(CommandExcelType.setExcel.getTitle())){
                    this.setExcel(workbook, fileUtils.getFileOutputSteam());
                }
                /* заполнить данными excel */
                else if (commandFileOperationModel.getType().equals(CommandExcelType.createCell.getTitle())){
                    data.get("row");
                    this.createCell(
                            (Sheet) data.get("sheet"),
                            (Integer) data.get("row"),
                            (Integer) data.get("cell"),
                            data.get("value")
                    );
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void rowCreateCell(Set<Map.Entry<String , Object>> data, XSSFRow row, String type ){
        int indexCol = -1;
        for (Map.Entry<String, Object> elem : data){
            indexCol++;
            if (type.equals("key")){
                createCell(row, elem.getKey(), indexCol);
            }else {
                createCell(row, elem.getValue(), indexCol);
            }
        }
    }
    public void createCell(Sheet sheet, int row, int cell, Object value){
        sheet.createRow(row);
        this.createCell(sheet.getRow(row), value, cell);
    }

    private void createCell(Row row, Object elem, int index){
        if (elem instanceof String){
            row.createCell(index).setCellValue((String) elem);
        }
        else if (elem instanceof Integer){
            row.createCell(index).setCellValue((Integer) elem);
        }
        else if (elem instanceof Date){
            row.createCell(index).setCellValue((Date) elem);
        }
        else if (elem instanceof Boolean){
            row.createCell(index).setCellValue((Boolean) elem);
        }
    }
}
