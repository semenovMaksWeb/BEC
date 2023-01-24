package com.example.bec.model.command.file;

import com.example.bec.model.command.store.StoreFindCommandModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CommandFileOperationModel {
    private List<String> key;
    private String type;
    private Map<String, StoreFindCommandModel> params;
}
