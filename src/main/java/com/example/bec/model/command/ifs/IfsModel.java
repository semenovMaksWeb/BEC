package com.example.bec.model.command.ifs;

import com.example.bec.model.command.LinkDateModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IfsModel extends LinkDateModel<IfsParamsModel> {
    private Object value;
    private String operator;

    public IfsModel(Object value){
        this.value = value;
    }

}
