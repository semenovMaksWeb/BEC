package com.example.bec.model.command.ifs;

import com.example.bec.model.command.LinkDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IfsModel extends LinkDate<IfsParamsModel> {
    private Object value;
    private String operator;

    public IfsModel(Object value){
        this.value = value;
    }

}
