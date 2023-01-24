package com.example.bec.model.command.file;

import com.example.bec.model.command.store.StoreFindCommandModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CommandFileModel {
    private StoreFindCommandModel catalog;
    private StoreFindCommandModel name;
    private List<CommandFileOperationModel> operation;
}
