package com.example.bec.model.command;

import lombok.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChildrenDatasetModel {
    private List<String> key;

    public Object searchData(Map<String , Object> data, Object value) throws IOException {
        if (value != null){
            return  value;
        }
        Object link = data;
        for (String key: this.key){
            link = ((Map<?, ?>) link).get(key);
        }
        return link;
    }
    public void updateData(Map<String , Object> data, Object res){
        Map<String, Object> link = data;
        for (int i = 0; i <this.key.size() - 1 ; i++) {
            link = (Map<String, Object>)(link).get(key);
        }
        link.put(
                this.key.get(this.key.size() - 1),
                res
        );
    }
}
