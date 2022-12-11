package com.example.bec.model.command.convert;

import com.example.bec.model.command.ChildrenDatasetModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConvertParamsLinkModel {
    private ChildrenDatasetModel params;
    private ChildrenDatasetModel dataset;
}
