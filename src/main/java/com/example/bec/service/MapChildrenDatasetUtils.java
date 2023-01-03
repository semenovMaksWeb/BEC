package com.example.bec.service;

import com.example.bec.model.command.ChildrenDatasetModel;
import com.example.bec.model.command.LinkDateModel;

import java.util.HashMap;
import java.util.Map;

public class MapChildrenDatasetUtils {

    private final Map<String, Object> dataset;
    private final Map<String, Object> params;

    public MapChildrenDatasetUtils(Map<String, Object> dataset, Map<String, Object> params){
        this.dataset = dataset;
        this.params = params;
    }
    private Object checkDatasetOrParams(
            LinkDateModel<ChildrenDatasetModel> elem
    ){
        Object res = null;
        if (elem.getDataset() != null){
            res = elem.getDataset().searchData(this.dataset, null);
        }
        if (elem.getParams() != null){
            res = elem.getParams().searchData(this.params, null);
        }
        if (elem.getValue() != null){
            res = elem.getValue();
        }
        return res;
    }
    public Map<String, Object> getObject(
            Map<String, LinkDateModel<ChildrenDatasetModel>> config
    ){
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, LinkDateModel<ChildrenDatasetModel>> elem: config.entrySet()) {
            Object res = checkDatasetOrParams(elem.getValue());
            result.put(elem.getKey(), res);
        }
        return result;
    }
    public Object getObjectKey(LinkDateModel<ChildrenDatasetModel> config){
        return this.checkDatasetOrParams(config);
    }

}
