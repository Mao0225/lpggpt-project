package com.sjzu.edu.common.model;
import com.sjzu.edu.common.model.base.BaseFunc;

import java.util.List;
@SuppressWarnings("serial")
public class FuncParents extends BaseFunc<FuncParents> {
    private List<Func> ChildFunctions;

    public void setChildFunctions(List<Func> subFunctions) {
        this.ChildFunctions = subFunctions;
    }
    // 获取子功能列表的方法
    public List<Func> getChildFunctions() {
        return ChildFunctions;
    }

}
