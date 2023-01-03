package com.example.bec.model.command.convert;

import com.example.bec.model.command.ChildrenDatasetModel;
import com.example.bec.model.command.LinkDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConvertParamsLinkModel extends LinkDate<Map<String,ChildrenDatasetModel>> {

    public void getObjectDataset(Map<String, Object> result, Map<String, Object> data){
        if (this.getDataset() == null){
            return;
        }
        for (Map.Entry<String, ChildrenDatasetModel> d: this.getDataset().entrySet()) {
            result.put(d.getKey(), d.getValue().searchData(data));
        }
    }

    public void getObjectParams(Map<String, Object> result, Map<String, Object> data){
        if (this.getParams() == null){
            return;
        }
        for (Map.Entry<String, ChildrenDatasetModel> p: this.getParams().entrySet()) {
            result.put(p.getKey(), p.getValue().searchData(data));
        }
    }
}
