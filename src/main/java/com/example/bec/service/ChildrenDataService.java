package com.example.bec.service;

import com.example.bec.model.command.ChildrenDatasetModel;

import java.util.Map;

public class ChildrenDataService {
    public Object searchData(Map<String , Object> data, ChildrenDatasetModel childrenDatasetModel){
        Object link = data;
        for (String key: childrenDatasetModel.getKey()){
            System.out.println(link);
            link = ((Map<?, ?>) link).get(key);
        }
        return link;
    }
}
