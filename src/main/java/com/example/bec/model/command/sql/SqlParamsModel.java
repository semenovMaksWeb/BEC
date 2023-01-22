package com.example.bec.model.command.sql;

import com.example.bec.model.command.store.StoreFindCommandModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SqlParamsModel {
    private StoreFindCommandModel data;
    private String type;
    private Integer index;

}
