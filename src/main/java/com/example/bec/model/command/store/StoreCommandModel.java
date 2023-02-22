package com.example.bec.model.command.store;

import com.example.bec.enums.StoreCommandTypeEnums;
import com.example.bec.enums.StoreFindCommandTecEnum;
import lombok.Getter;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class StoreCommandModel {
    private final Map<String, Object> data = new HashMap<>();
    private final Environment env;

    public StoreCommandModel(Environment env) {
        this.env = env;
    }

    public Object storeGetData(StoreFindCommandModel storeFindCommandModel) throws IOException {
        if (storeFindCommandModel.getType().equals(StoreCommandTypeEnums.value.getTitle())){
            return storeFindCommandModel.getValue();
        }
        else if (storeFindCommandModel.getType().equals(StoreCommandTypeEnums.dataset.getTitle())){
            Object res = searchValue(storeFindCommandModel.getKey());
            if (storeFindCommandModel.getTec() != null){
                return storeGetTec(storeFindCommandModel.getTec(), res);
            }
            return res;
        }
        else if (storeFindCommandModel.getType().equals(StoreCommandTypeEnums.properties.getTitle())){
            return this.env.getProperty(storeFindCommandModel.getProperties());
        }
        return null;
    }
    private Object storeGetTec(String tec, Object res){
        if (tec.equals(StoreFindCommandTecEnum.getStatusCode.getTitle()) && res instanceof ResponseEntity){
            return ((ResponseEntity<?>) res).getStatusCode().value();
        }
        return null;
    }
    public Map<String, Object> storeGetData(Map<String, StoreFindCommandModel> storeFindCommandModelMap) throws IOException {
        Map<String, Object> data = new HashMap<>();
        for (Map.Entry<String, StoreFindCommandModel> storeFindCommandModel: storeFindCommandModelMap.entrySet()){
            data.put(
                    storeFindCommandModel.getKey(),
                    this.storeGetData(storeFindCommandModel.getValue())
            );
        }
        return data;
    }

    public void updateValue(List<String> keys, Object value){
        Object linkUpdate = searchValue(keys.subList(0, keys.size() - 1));
            assert linkUpdate != null;
            ((HashMap<String, Object>) linkUpdate).put(keys.get(keys.size() - 1), value);
    }

    public Object searchValue(String key){
        return  this.data.get(key);
    }

    public Object searchValue(List<String> keys){
        Object link = this.getData();
        for (String key: keys){
            link = ((HashMap<?, ?>) link).get(key);
        }
        return link;
    }

    public void updateData(Map<String, Object> map){
        this.data.putAll(map);
    }
}
