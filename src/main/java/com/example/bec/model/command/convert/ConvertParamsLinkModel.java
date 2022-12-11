package com.example.bec.model.command.convert;

import com.example.bec.model.command.ChildrenDatasetModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConvertParamsLinkModel {
    private Map<String,ChildrenDatasetModel> params;
    private Map<String,ChildrenDatasetModel> dataset;

    public void getObjectDataset(Map<String, Object> result, Map<String, Object> data){
        if (this.dataset == null){
            return;
        }
        for (Map.Entry<String, ChildrenDatasetModel> d: this.dataset.entrySet()) {
            result.put(d.getKey(), d.getValue().searchData(data));
        }
    }
    public void getObjectParams(Map<String, Object> result, Map<String, Object> data){
        if (this.params == null){
            return;
        }
        for (Map.Entry<String, ChildrenDatasetModel> p: this.params.entrySet()) {
            result.put(p.getKey(), p.getValue().searchData(data));
        }
    }
}
