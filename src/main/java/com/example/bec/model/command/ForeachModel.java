package com.example.bec.model.command;

import com.example.bec.model.command.store.StoreFindCommandModel;
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
    private List<String> elem;
    private List<String> list;
    private List<CommandModel> children;
}
