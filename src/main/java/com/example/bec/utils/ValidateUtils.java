package com.example.bec.utils;

import com.example.bec.enums.ValidateParamsEnum;
import com.example.bec.model.command.store.StoreCommandModel;
import com.example.bec.model.command.validate.ValidateParamsModel;
import com.example.bec.model.command.validate.ValidateParamsRulesModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

public class ValidateUtils {
    private final StoreCommandModel storeCommandModel;
    private final Map<String, List<String>> result = new HashMap<>();

    public ValidateUtils(StoreCommandModel storeCommandModel){
        this.storeCommandModel = storeCommandModel;
    }

    private Boolean checkNull(Object value){
        return value == null || value.equals("");
    }

    private void validateReq(ValidateParamsRulesModel validateParamsRulesModel, String key){
        if (this.checkNull(this.storeCommandModel.searchValue(key))){
            saveResult(key, validateParamsRulesModel.getError());
        }
    }

    private void validateVar(ValidateParamsRulesModel validateParamsRulesModel, String key) {
        if (this.storeCommandModel.searchValue(key) == null || this.storeCommandModel.searchValue(key).equals("")){
            return;
        }
        if (!Objects.equals(
                this.storeCommandModel.searchValue(key).getClass().getSimpleName(),
                validateParamsRulesModel.getParams().get("type"))
        ){
            saveResult(key, validateParamsRulesModel.getError());
        }
    }

    private ResponseEntity<Map<String, List<String>>> generatorResponse(){
        if (!this.result.isEmpty()){
            return new ResponseEntity<>(this.result, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(this.result, HttpStatus.OK);
    }

    private void saveResult(String key, String  error){
        result.computeIfAbsent(key, k -> new ArrayList<>());
        result.get(key).add(error);
    }

    public ResponseEntity<Map<String, List<String>>> validateStart(
            List<ValidateParamsModel> validateParamsModelList
    ) {
        for (ValidateParamsModel validateParamsModel : validateParamsModelList) {
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
       return this.generatorResponse();
    }
}
