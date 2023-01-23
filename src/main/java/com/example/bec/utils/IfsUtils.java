package com.example.bec.utils;

import com.example.bec.enums.OperatorTypeEnum;
import com.example.bec.enums.StoreCommandTypeEnums;
import com.example.bec.model.command.ifs.IfsModel;
import com.example.bec.model.command.store.StoreCommandModel;
import com.example.bec.model.command.store.StoreFindCommandModel;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IfsUtils {
    private List<IfsModel> listIfsModel;
    private StoreCommandModel data;

    public IfsUtils(List<IfsModel> listIfsModel, StoreCommandModel data) {
        this.listIfsModel = listIfsModel;
        this.data = data;
    }

    public boolean checkIfs() {
        convertValue();
        convertComparisons();
        convertCombination();
        return (boolean) listIfsModel.get(0).getData().getValue();
    }

    private void convertValue() {
        for(IfsModel ifsModel: listIfsModel){
            if (ifsModel.getData() != null && ifsModel.getData().getType().equals(StoreCommandTypeEnums.dataset.getTitle())) {
                ifsModel.getData().setValue(this.data.storeGetData(ifsModel.getData()));
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
                        listIfsModel.get(index - 1).getData().getValue(),
                        listIfsModel.get(index + 1).getData().getValue()
                );
                operatorComparisons(val, index);
                index--;
            }
            /* > */
            else if (listIfsModel.get(index).getOperator().equals(OperatorTypeEnum.more.getTitle())) {
                boolean val = this.operatorMove(
                        listIfsModel.get(index - 1).getData().getValue(),
                        listIfsModel.get(index + 1).getData().getValue()
                );
                operatorComparisons(val, index);
                index--;
            }
            /* < */
            else if (listIfsModel.get(index).getOperator().equals(OperatorTypeEnum.less.getTitle())) {
                boolean val = this.operatorLess(
                        listIfsModel.get(index - 1).getData().getValue(),
                        listIfsModel.get(index + 1).getData().getValue()
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
                        listIfsModel.get(index - 1).getData().getValue(),
                        listIfsModel.get(index + 1).getData().getValue()
                );
                operatorComparisons(val, index);
                index--;
            }
            else if (listIfsModel.get(index).getOperator().equals(OperatorTypeEnum.or.getTitle())) {
                boolean val = this.operatorOr(
                        listIfsModel.get(index - 1).getData().getValue(),
                        listIfsModel.get(index + 1).getData().getValue()
                );
                operatorComparisons(val, index);
                index--;
            }

        } while (listIfsModel.size() >= index);
    }

    private void deleteIfsModel(int index_min, int index_max){
        listIfsModel.subList(index_min, index_max + 1).clear();
    }
    private void addIfsModel(boolean val, int index){
        listIfsModel.add(
                index,
                new IfsModel(new StoreFindCommandModel(val))
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
                return ((Integer) val1).intValue() == ((Integer) val2).intValue();
        }
        if (val1 instanceof Boolean && val2 instanceof Boolean){
            return val1 == val2;
        }
        return false;
    }
    private boolean operatorMove(Object val1, Object val2){
        if (val1 instanceof Integer && val2 instanceof Integer){
            return (Integer) val1 > (Integer) val2;
        }
        return false;
    }

    private boolean operatorLess(Object val1, Object val2){
        if (val1 instanceof Integer && val2 instanceof Integer){
            return (Integer)val1 < (Integer)val2;
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
