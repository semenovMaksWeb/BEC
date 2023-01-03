package com.example.bec.utils;

import com.example.bec.enums.OperatorTypeEnum;
import com.example.bec.model.command.ifs.IfsModel;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class IfsUtils{
    private List<IfsModel> listIfsModel;
    private Map<String , Object> dataset;
    private Map<String , Object> params;

    public IfsUtils(List<IfsModel> listIfsModel, Map<String, Object> dataset, Map<String, Object> params) {
        this.listIfsModel = listIfsModel;
        this.dataset = dataset;
        this.params = params;
    }

    public boolean checkIfs(){
        convertValue();
        convertComparisons();
        convertCombination();
        System.out.println(listIfsModel);
        return (boolean) listIfsModel.get(0).getValue();
    }

    private void convertValue(){
        for(IfsModel ifsModel: listIfsModel){
            if (ifsModel.getDataset() != null){
                ifsModel.setValue(ifsModel.getDataset().searchData(this.dataset, ifsModel.getValue()));
            }
            else if (ifsModel.getParams() != null){
                ifsModel.setValue(ifsModel.getParams().searchData(this.params, ifsModel.getValue()));
            }
        }
    }

    private void convertComparisons(){
        int index = -1;
        do {
            index++;
            if (listIfsModel.size() <= index){
                break;
            }
            if (listIfsModel.get(index).getOperator() == null){
                continue;
            }
            /* == */
            if (listIfsModel.get(index).getOperator().equals(OperatorTypeEnum.equals.getTitle())) {
                boolean val = this.operatorEquals(
                        listIfsModel.get(index - 1).getValue(),
                        listIfsModel.get(index + 1).getValue()
                );
                operatorComparisons(val, index);
                index--;
            }
            /* > */
            else if (listIfsModel.get(index).getOperator().equals(OperatorTypeEnum.more.getTitle())) {
                boolean val = this.operatorMove(
                        listIfsModel.get(index - 1).getValue(),
                        listIfsModel.get(index + 1).getValue()
                );
                operatorComparisons(val, index);
                index--;
            }
            /* < */
            else if (listIfsModel.get(index).getOperator().equals(OperatorTypeEnum.less.getTitle())) {
                boolean val = this.operatorLess(
                        listIfsModel.get(index - 1).getValue(),
                        listIfsModel.get(index + 1).getValue()
                );
                operatorComparisons(val, index);
                index--;
            }
        } while (listIfsModel.size() >= index);
    }

    private void convertCombination(){
        int index = -1;
        do {
            index++;
            if (listIfsModel.size() <= index){
                break;
            }
            if (listIfsModel.get(index).getOperator() == null){
                continue;
            }
            if (listIfsModel.get(index).getOperator().equals(OperatorTypeEnum.and.getTitle())) {
                boolean val = this.operatorAnd(
                        listIfsModel.get(index - 1).getValue(),
                        listIfsModel.get(index + 1).getValue()
                );
                operatorComparisons(val, index);
                index--;
            }
            else if (listIfsModel.get(index).getOperator().equals(OperatorTypeEnum.or.getTitle())) {
                boolean val = this.operatorOr(
                        listIfsModel.get(index - 1).getValue(),
                        listIfsModel.get(index + 1).getValue()
                );
                operatorComparisons(val, index);
                index--;
            }

        }while (listIfsModel.size() >= index);
    }

    private void deleteIfsModel(int index_min, int index_max){
        listIfsModel.subList(index_min, index_max + 1).clear();
    }
    private void addIfsModel(boolean val, int index){
        listIfsModel.add(
                index,
                new IfsModel(val)
        );
    }

    private void operatorComparisons(boolean val, int index){
        deleteIfsModel(index - 1, index + 1);
        addIfsModel(val, index - 1);
    }

    private boolean operatorEquals(Object val1, Object val2){
        if (val1 instanceof String && val2 instanceof String){
            return val1.equals(val2);
        }
        if (val1 instanceof Integer && val2 instanceof Integer){
            return val1 == val2;
        }
        if (val1 instanceof Boolean && val2 instanceof Boolean){
            return val1 == val2;
        }
        return false;
    }
    private boolean operatorMove(Object val1, Object val2){
        if (val1 instanceof Integer && val2 instanceof Integer){
            return (int)val1 > (int)val2;
        }
        return false;
    }

    private boolean operatorLess(Object val1, Object val2){
        if (val1 instanceof Integer && val2 instanceof Integer){
            return (int)val1 < (int)val2;
        }
        return false;
    }

    private boolean operatorAnd(Object val1, Object val2){
        if (val1 instanceof Boolean && val2 instanceof Boolean){
            return (boolean)val1 && (boolean)val2;
        }
        return false;
    }

    private boolean operatorOr(Object val1, Object val2){
        if (val1 instanceof Boolean && val2 instanceof Boolean){
            return (boolean)val1 || (boolean)val2;
        }
        return false;
    }
}
