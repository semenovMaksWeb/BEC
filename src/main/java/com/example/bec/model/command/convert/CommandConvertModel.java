package com.example.bec.model.command.convert;

import com.example.bec.model.command.convert.ConvertModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommandConvertModel {
    private List<ConvertModel> dataset;
    private List<ConvertModel> params;
}
