package com.example.bec.model.command.foreach;

import com.example.bec.model.command.ChildrenDatasetModel;
import com.example.bec.model.command.CommandModel;
import com.example.bec.model.command.LinkDateModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ForeachModel {
    private String elem;
    private LinkDateModel<ChildrenDatasetModel> list;
    private List<CommandModel> children;
}
