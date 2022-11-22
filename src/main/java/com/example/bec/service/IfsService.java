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
    private IfsModel ifsModel;
    private Map<String , Object> dataset;
    private Map<String , Object> params;
    public boolean eval(){
        convertText(this.dataset, ifsModel.getDataset());
        convertText(this.params, ifsModel.getParams());
        return false;
    }

    private void convertText(Map<String , Object> data, List<IfsParamsModel> listIfsParamsModel){
        for(IfsParamsModel ifsParamsModel: listIfsParamsModel){
            if (ifsParamsModel.getChildren() == null) {
              ifsModel.setText(
                RegularService.regularIfs(
                    ifsModel.getText(),
                    ifsParamsModel.getIndex(),
                    data.get(ifsParamsModel.getKey())
                )
              );
            }else {
                convertText(
                    (Map<String, Object>) data.get(ifsParamsModel.getKey()),
                    ifsParamsModel.getChildren()
                );
            }
        }
    }
}
