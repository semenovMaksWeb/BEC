package com.example.bec.model.command;

import com.example.bec.model.command.store.StoreFindCommandModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommandConvertModel {
    private String type;
    private Map<String, StoreFindCommandModel> params;
}
