package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.Drivercar;

import java.awt.print.Book;

public class DriverService {


    private Drivercar dao = new Drivercar().dao();

    public Page<Drivercar> paginate(int pageNumber,int pageSize){
        return dao.paginate(pageNumber,pageSize,"select *","from drivercar order by id");
    }

    public Drivercar findByid(int id){
        return dao.findById((id));
    }

    public void deleteByid(int id){
        dao.deleteById(id);
    }
    public Page<Drivercar> search(int pageNumber, int pageSize, String drivername, String driverphone, String drivercarno) {
        StringBuilder sb = new StringBuilder(" FROM drivercar WHERE 1=1");

        if (drivername != null && !drivername.isEmpty()) {
            sb.append(" AND drivername LIKE '%").append(drivername).append("%'");
        }
        if (driverphone != null && !driverphone.isEmpty()) {
            sb.append(" AND driverphone LIKE '%").append(driverphone).append("%'");
        }
        if (drivercarno != null && !drivercarno.isEmpty()) {
            sb.append(" AND drivercarno LIKE '%").append(drivercarno).append("%'");
        }

        String select = "SELECT *";
        System.out.println(dao.paginate(pageNumber, pageSize, select, sb.toString()));
        return dao.paginate(pageNumber, pageSize, select, sb.toString());
    }
}
