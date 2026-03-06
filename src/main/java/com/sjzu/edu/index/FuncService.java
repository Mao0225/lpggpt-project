package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.Func;

public class FuncService {

    private Func dao = new Func().dao();

    public void delete(int id){
        dao.deleteById(id);
    }

    public Func findById(Integer id) {
        return dao.findById(id);
    }

    public Page<Func> paginate(int pageNumber, int pageSize) {
        return dao.paginate(pageNumber, pageSize, "select *", "from func order by id asc");
    }

    public Page<Func> paginate(int pageNumber, int pageSize, String searchKey) {
        if (searchKey == null || searchKey.trim().isEmpty()) {
            return paginate(pageNumber, pageSize);
        }

        String keyword = "%" + searchKey.trim() + "%";
        return dao.paginate(
                pageNumber,
                pageSize,
                "select *",
                "from func where fun_name like ? or fun_url like ? order by id asc",
                keyword,
                keyword
        );
    }

}
