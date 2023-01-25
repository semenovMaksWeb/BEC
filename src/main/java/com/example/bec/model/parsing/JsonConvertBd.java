package com.example.bec.model.parsing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JsonConvertBd {
    private String name;
    private List<Map<String, Object>> data;
}
