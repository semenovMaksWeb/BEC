package com.example.bec.model.command;

import com.example.bec.model.command.file.CommandFileOperationModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommandExcelModel {
    private List<CommandFileOperationModel> operation;
}
