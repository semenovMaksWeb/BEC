package com.example.bec.model.command.ifs;

import com.example.bec.model.command.store.StoreFindCommandModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IfsModel {
    private StoreFindCommandModel data;
    private String operator;

    public IfsModel(StoreFindCommandModel storeFindCommandModel) {
        this.data = storeFindCommandModel;
    }
}
