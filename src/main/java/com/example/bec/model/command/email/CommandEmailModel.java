package com.example.bec.model.command.email;

import com.example.bec.model.command.ChildrenDatasetModel;
import com.example.bec.model.command.LinkDateModel;
import com.example.bec.utils.MapChildrenDatasetUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommandEmailModel {
    private LinkDateModel<ChildrenDatasetModel> from;
    private LinkDateModel<ChildrenDatasetModel> template;
    private LinkDateModel<ChildrenDatasetModel> subject;
    private Map<String, LinkDateModel<ChildrenDatasetModel>> params;

    public Map<String, Object> generatorResult(Map<String, Object> dataset,Map<String, Object> params){
        MapChildrenDatasetUtils mapChildrenDatasetUtils = new MapChildrenDatasetUtils(dataset, params);

        Map<String, Object> result = new HashMap<>();
        result.put("params", mapChildrenDatasetUtils.getObject(this.params));
        result.put("from", mapChildrenDatasetUtils.getObjectKey(this.from));
        result.put("template", mapChildrenDatasetUtils.getObjectKey(this.template));
        result.put("subject", mapChildrenDatasetUtils.getObjectKey(this.subject));
        return result;
    }
}
