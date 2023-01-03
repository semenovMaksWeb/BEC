package com.example.bec.model.command;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChildrenDatasetModel {
    List<String> key;

    public Object searchData(Map<String , Object> data, Object value){
        if (value != null){
            return  value;
        }
        Object link = data;
        for (String key: this.key){
            link = ((Map<?, ?>) link).get(key);
        }
        return link;
    }
}
