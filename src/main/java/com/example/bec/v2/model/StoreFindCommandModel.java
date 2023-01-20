package com.example.bec.v2.model;

import com.example.bec.v2.enums.StoreCommandTypeEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StoreFindCommandModel {
    private StoreCommandTypeEnums type;
    private List<String> key;
    private Object value;
}
