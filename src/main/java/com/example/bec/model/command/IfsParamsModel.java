package com.example.bec.model.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IfsParamsModel {
    private Integer index;
    private String key;
    private List<IfsParamsModel> children;
}
