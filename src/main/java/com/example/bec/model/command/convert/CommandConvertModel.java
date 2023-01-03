package com.example.bec.model.command.convert;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommandConvertModel {
    private List<ConvertModel> dataset;
    private List<ConvertModel> params;

}
