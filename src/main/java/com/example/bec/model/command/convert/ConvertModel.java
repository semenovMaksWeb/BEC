package com.example.bec.model.command.convert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConvertModel {
    private String key;
    private  String type;
    private Map<String, Object> params;
}
