package com.example.bec.service;

import com.example.bec.enums.OperatorTypeEnum;
import com.example.bec.model.command.IfsModel;



import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;

@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class IfsService {
    private List<IfsModel> ListIfsModel;
    private Map<String , Object> dataset;
    private Map<String , Object> params;
    private ChildrenDataService childrenDataService = new ChildrenDataService();

    public IfsService(List<IfsModel> listIfsModel, Map<String, Object> dataset, Map<String, Object> params) {
        this.ListIfsModel = listIfsModel;
        this.dataset = dataset;
        this.params = params;
    }

    public boolean checkIfs(){
        for(IfsModel ifsModel: ListIfsModel){
            if (ifsModel.getDataset() != null){
                ifsModel.setValue(childrenDataService.searchData(this.dataset, ifsModel.getDataset()));
            }
            else if (ifsModel.getParams() != null){
                ifsModel.setValue(childrenDataService.searchData(this.params, ifsModel.getParams()));
            }
        }
        return convertIfs();
    }

    private boolean convertIfs(){
        Object val1 = null;
        Object val2 = null;
        String operator = null;
        int index = -1;
        /** TODO придумать логику для прогона сложнее условии состоящие не из 3 элементов
          возможно while(true) с break когда все прогонится */
        for(IfsModel ifsModel: ListIfsModel){
            index++;
            if (ifsModel.getValue() != null){
                if (val1 == null){
                    val1 = ifsModel.getValue();
                }else if (val2 == null){
                    val2 = ifsModel.getValue();
                }
            }
            if (ifsModel.getOperator() != null){
                operator = ifsModel.getOperator();
            }
            if (val1 != null && val2 != null && operator != null){
                for (int i = 0; i < index; i ++){
                    ListIfsModel.remove(i);
                }
                ListIfsModel.add(
                    0,
                    new IfsModel(null, null,  switchIFs(val1, val2, operator), null)
                );
                break;
            }
        }
        return (boolean) ListIfsModel.get(0).getValue();
    }
   boolean switchIFs(Object val1, Object val2, String operator){
        if (operator.equals(OperatorTypeEnum.equals.getTitle())){
            if (val1 instanceof String && val2 instanceof String){
                return val1.equals(val2);
            }
            if (val1 instanceof Integer && val2 instanceof Integer){
                return val1 == val2;
            }
        }
        return false;
    }
}
