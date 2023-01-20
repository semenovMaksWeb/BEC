package com.example.bec.v2.model;

import com.example.bec.v2.utils.CheckTypeUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class StoreCommandModel {
    private Map<String, StoreElemCommandModel> data = new HashMap<>();
    private CheckTypeUtils checkTypeUtils = new CheckTypeUtils();

    public Object storeGetData(StoreFindCommandModel storeFindCommandModel){
        return switch (storeFindCommandModel.getType()) {
            case value -> storeFindCommandModel.getValue();
            case dataset, params -> getValue(storeFindCommandModel);
        };
    }

    public void updateValue(StoreFindCommandModel storeFindCommandModel, Object value){
        List<String> keys = storeFindCommandModel.getKey();
        Object linkUpdate = searchValue(keys.subList(0, keys.size() - 1));
        if (checkTypeUtils.checkHashMap(linkUpdate)){
            assert linkUpdate != null;
            ((HashMap<String, Object>) linkUpdate).put(keys.get(keys.size() - 1), value);
        }
    }

    private Object getValue(StoreFindCommandModel storeFindCommandModel){
        return searchValue(storeFindCommandModel.getKey());
    }

    private Object searchValue(List<String> keys){
        Object link = null;
        int index = -1;
        for (String key: keys){
            index++;
            if (index == 0 && this.getData().get(key).getValue() == null){
                return null;
            }
            if (index == 0){
                link = this.getData().get(key).getValue();
                continue;
            }
            if (checkTypeUtils.checkHashMap(link)){
                link = ((HashMap<?, ?>) link).get(key);
            }
        }
        return link;
    }
}
