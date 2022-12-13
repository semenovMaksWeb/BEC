package com.example.bec.utils;

import com.example.bec.enums.ValidateParamsEnum;
import com.example.bec.model.command.validate.ValidateParamsModel;
import com.example.bec.model.command.validate.ValidateParamsRulesModel;

import lombok.Getter;
import lombok.Setter;
import java.util.*;

@Getter
@Setter
public class ValidateUtils {
    private Map<String , Object> params;
    private List<ValidateParamsModel> validateParamModels;
    private Map<String , List<String>> result = new HashMap<>();

    public ValidateUtils(Map<String, Object> params, List<ValidateParamsModel> validateParamModels){
        this.params = params;
        this.validateParamModels = validateParamModels;
    }

    private Boolean checkNull(String key){
        return this.getParams().get(key) == null || this.getParams().get(key) == "";
    }

    private void validateReq(ValidateParamsRulesModel validateParamsRulesModel, String key){
        if (this.checkNull(key)){
            saveResult(key, validateParamsRulesModel.getError());
        }
    }

    private void validateVar(ValidateParamsRulesModel validateParamsRulesModel, String key){
        if (checkNull(key)){
            return;
        }
        if (!Objects.equals(this.params.get(key).getClass().getSimpleName(), validateParamsRulesModel.getParams().get("type"))){
            saveResult(key, validateParamsRulesModel.getError());
        }
    }

    public void validateStart(){
        for (ValidateParamsModel validateParamsModel : this.validateParamModels) {

            for (ValidateParamsRulesModel validateParamsRulesModel : validateParamsModel.getRiles()){
                /* Проверка пустой строки */
                if (Objects.equals(validateParamsRulesModel.getType(), ValidateParamsEnum.req.getTitle())){
                    validateReq(validateParamsRulesModel, validateParamsModel.getKey());
                }
                /* Проверка типа переменных */
                if (Objects.equals(validateParamsRulesModel.getType(), ValidateParamsEnum.var.getTitle())){
                    validateVar(validateParamsRulesModel, validateParamsModel.getKey());
                }
            }
        }
    }

    private void saveResult(String key, String  error){
        result.computeIfAbsent(key, k -> new ArrayList<>());
        result.get(key).add(error);
    }
}
