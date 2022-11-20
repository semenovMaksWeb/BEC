package com.example.bec.model.command;

import com.example.bec.model.command.validateParams.ValidateParams;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommandValidateParams {
    private List<ValidateParams> params;
}
