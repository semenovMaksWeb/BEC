package com.example.bec.service;

import com.example.bec.model.command.IfsModel;
import com.example.bec.model.command.IfsParamsModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IfsService {
    private List<IfsModel> ListIfsModel;
    private Map<String , Object> dataset;
    private Map<String , Object> params;
    public boolean eval(){
        for(IfsModel ifsModel: ListIfsModel){
            if (ifsModel.getDataset() != null){
                ifsModel.setValue(convertText(this.dataset, ifsModel.getDataset()));
            }
            else if (ifsModel.getParams() != null){
                ifsModel.setValue(convertText(this.params, ifsModel.getDataset()));
            }
        }
        return false;
    }

    private Object convertText(Map<String , Object> data, IfsParamsModel ifsParamsModel){
        if (ifsParamsModel.getChildren() == null) {
            return data.get(ifsParamsModel.getKey());
        }else {
            return  convertText(
                (Map<String, Object>) data.get(ifsParamsModel.getKey()),
                ifsParamsModel.getChildren()
            );
        }

//        if (listIfsParamsModel == null){
//            return;
//        }
//        for(IfsParamsModel ifsParamsModel: listIfsParamsModel){
//            if (ifsParamsModel.getChildren() == null) {
//              ifsModel.setText(
//                RegularService.regularIfs(
//                    ifsModel.getText(),
//                    ifsParamsModel.getIndex(),
//                    data.get(ifsParamsModel.getKey())
//                )
//              );
//            }else {
//                convertText(
//                    (Map<String, Object>) data.get(ifsParamsModel.getKey()),
//                    ifsParamsModel.getChildren()
//                );
//            }
//        }
    }
}
