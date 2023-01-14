package com.example.bec.model.command.sql;

import com.example.bec.model.command.ChildrenDatasetModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SqlParamsModel extends ChildrenDatasetModel {
    private String type;
    private Integer index;

}
