package com.example.bec.model.command.convert;

import com.example.bec.model.command.ChildrenDatasetModel;
import com.example.bec.model.command.LinkDateModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConvertModel extends ChildrenDatasetModel {
    private String type;
    private Map<String, LinkDateModel<ChildrenDatasetModel>> params;
}
