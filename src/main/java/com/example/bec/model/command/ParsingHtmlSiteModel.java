package com.example.bec.model.command;


import com.example.bec.model.command.store.StoreFindCommandModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParsingHtmlSiteModel {
    private String type;
    private Map<String, StoreFindCommandModel> params;
}
