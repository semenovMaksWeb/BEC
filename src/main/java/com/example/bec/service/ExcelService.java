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

    public void generatorDataExcel(List<Map<String, Object>> data, XSSFSheet sheet, int rowIndex, ArrayList<String> columnList){
        for (Map<String , Object> elem : data){
            XSSFRow row = sheet.createRow(rowIndex);
            int indexCell = 0;

            for (String columnName : columnList) {
                createCell(row, elem.get(columnName), indexCell);
                indexCell++;
            }
            rowIndex++;
        }
        for(int index = 0; index < columnList.size(); index++) {
            sheet.autoSizeColumn(index);
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
                    this.generatorDataExcel(
                            (List<Map<String, Object>>) data.get("data"),
                            (XSSFSheet) data.get("sheet"),
                            (Integer) data.get("row"),
                            (ArrayList<String>) data.get("column")
                    );
                }
                /* заполнить данными excel */
                else if (commandFileOperationModel.getType().equals(CommandExcelType.setExcel.getTitle())){
                    this.setExcel(workbook, fileUtils.getFileOutputSteam());
                }
                /* заполнить данными excel ячейку */
                else if (commandFileOperationModel.getType().equals(CommandExcelType.createCell.getTitle())){
                    data.get("row");
                    this.saveCell(
                            (Sheet) data.get("sheet"),
                            (Integer) data.get("row"),
                            (Integer) data.get("cell"),
                            data.get("value")
                    );
                }
                /* заполнить данными excel строку ячейками из массива */
                else if (commandFileOperationModel.getType().equals(CommandExcelType.createRowArrayCell.getTitle())){
                    data.get("row");
                    this.createRowArrayCell(
                            (Sheet) data.get("sheet"),
                            (Integer) data.get("row"),
                            (ArrayList<String>) data.get("value")
                    );
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void saveCell(Sheet sheet, int row, int cell, Object value){
        sheet.createRow(row);
        this.createCell(sheet.getRow(row), value, cell);
    }

    public void createRowArrayCell(Sheet sheet, int rowIndex, ArrayList<String> value){
        Row row = sheet.createRow(rowIndex);
        int index = -1;
        for (String elem : value){
            index++;
            this.createCell(row, elem, index);
        }
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
