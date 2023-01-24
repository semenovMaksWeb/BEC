package com.example.bec.model.command;

import com.example.bec.model.command.store.StoreCommandModel;
import com.example.bec.model.command.store.StoreFindCommandModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommandEmailModel {
    private StoreFindCommandModel from;
    private StoreFindCommandModel template;
    private StoreFindCommandModel subject;
    private Map<String, StoreFindCommandModel> params;

    public Map<String, Object> generatorResult(StoreCommandModel data) throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("params", data.storeGetData(this.params));
        result.put("from", data.storeGetData(this.from));
        result.put("template", data.storeGetData(this.template));
        result.put("subject", data.storeGetData(this.subject));
        return result;
    }
}
