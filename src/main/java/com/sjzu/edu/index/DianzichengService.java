package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.Dianzicheng;

import java.util.List;

public class DianzichengService {

    private Dianzicheng dao = new Dianzicheng().dao();

    public void delete(int id){
        dao.deleteById(id);
    }

    public Dianzicheng findById(Integer id) {
        return dao.findById(id);
    }

    public Page<Dianzicheng> paginate(int pageNumber, int pageSize) {
        return dao.paginate(pageNumber, pageSize, "select *", "from dianzicheng order by bianma asc");
    }

    public void deleteById(Integer paraToInt) {
    }

}