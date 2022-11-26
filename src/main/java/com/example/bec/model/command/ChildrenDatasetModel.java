package com.example.bec.model.command;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChildrenDatasetModel {
    String key;
    ChildrenDatasetModel children;
}
