package com.example.bec.service;

import com.example.bec.model.command.ChildrenDatasetModel;

import java.util.Map;

public class ChildrenDataService {
    public Object searchData(Map<String , Object> data, ChildrenDatasetModel childrenDatasetModel){
        if (childrenDatasetModel.getChildren() == null) {
            return data.get(childrenDatasetModel.getKey());
        }else {
            return searchData(
                    (Map<String, Object>) data.get(childrenDatasetModel.getKey()),
                    childrenDatasetModel.getChildren()
            );
        }
    }
}
