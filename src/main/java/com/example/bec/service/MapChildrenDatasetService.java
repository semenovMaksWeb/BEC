package com.example.bec.service;

import com.example.bec.model.command.ChildrenDatasetModel;
import com.example.bec.model.command.LinkDateModel;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class MapChildrenDatasetService {

    public Map<String, Object> getObject(
            Map<String, Object> dataset,
            Map<String, Object> params,
            Map<String, LinkDateModel<ChildrenDatasetModel>> config
    ){
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, LinkDateModel<ChildrenDatasetModel>> elem: config.entrySet()) {
            Object res = null;
            if (elem.getValue().getDataset() != null){
                res = elem.getValue().getDataset().searchData(dataset, elem.getValue());
            }
            if (elem.getValue().getParams() != null){
                res = elem.getValue().getParams().searchData(params, elem.getValue());
            }
            result.put(elem.getKey(), res);
        }
        return result;
    }

}
