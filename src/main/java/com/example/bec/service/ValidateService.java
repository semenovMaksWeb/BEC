package com.example.bec.service;


import com.example.bec.enums.ValidateParamsEnum;
import com.example.bec.model.command.validateParams.ValidateParams;
import com.example.bec.model.command.validateParams.ValidateParamsRules;

import lombok.Getter;
import lombok.Setter;
import java.util.*;

@Getter
@Setter
public class ValidateService {
    private Map<String , Object> params;
    private List<ValidateParams> validateParams;
    private Map<String , List<String>> result = new HashMap<>();

    public ValidateService(Map<String, Object> params, List<ValidateParams> validateParams){
        this.params = params;
        this.validateParams = validateParams;
    }

    private Boolean checkNull(String key){
        return this.getParams().get(key) == null || this.getParams().get(key) == "";
    }

    private void validateReq(ValidateParamsRules validateParamsRules, String key){
        if (this.checkNull(key)){
            saveResult(key, validateParamsRules.getError());
        }
    }
    private void validateVar(ValidateParamsRules validateParamsRules, String key){
        if (checkNull(key)){
            return;
        }
        if (!Objects.equals(this.params.get(key).getClass().getSimpleName(), validateParamsRules.getParams().get("type"))){
            saveResult(key, validateParamsRules.getError());
        }
    }

    public void validateStart(){
        for (ValidateParams validateParams : this.validateParams) {

            for (ValidateParamsRules validateParamsRules: validateParams.getRiles()){
                /* Проверка пустой строки */
                if (Objects.equals(validateParamsRules.getType(), ValidateParamsEnum.req.getTitle())){
                    validateReq(validateParamsRules, validateParams.getKey());
                }
                /* Проверка типа переменных */
                if (Objects.equals(validateParamsRules.getType(), ValidateParamsEnum.var.getTitle())){
                    validateVar(validateParamsRules, validateParams.getKey());
                }
            }
        }
    }

    private void saveResult(String key, String  error){
        result.computeIfAbsent(key, k -> new ArrayList<>());
        result.get(key).add(error);
    }

}
