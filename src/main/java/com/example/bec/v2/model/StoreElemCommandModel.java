package com.example.bec.v2.model;

import com.example.bec.v2.enums.StoreCommandTypeEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreElemCommandModel {
    private String type;
    private Object value;

    public void setTypeParams(Object value){
        this.type = StoreCommandTypeEnums.params.getTitle();
        this.value = value;
    }
    public void setTypeDataset(Object value){
        this.type = StoreCommandTypeEnums.dataset.getTitle();
        this.value = value;
    }
}
