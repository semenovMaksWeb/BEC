package com.example.bec.model.command.store;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreFindCommandModel {
    private String type;
    private List<String> key;
    private Object value;
    private String tec;
    public StoreFindCommandModel(Object value){
        this.value = value;
    }
}
