package com.example.bec.model.command.parsing_html;

import com.example.bec.model.command.ChildrenDatasetModel;
import com.example.bec.model.command.LinkDateModel;

import java.util.Map;

public class ParsingHtml extends ChildrenDatasetModel {
    private String name;
    private Map<String, LinkDateModel<ChildrenDatasetModel>> params;
}
