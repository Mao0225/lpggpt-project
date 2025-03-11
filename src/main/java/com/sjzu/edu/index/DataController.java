package com.sjzu.edu.index;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.BseData;
import com.sjzu.edu.common.model.base.BaseBseData;

public class DataController extends Controller {
    DataService dataService = new DataService();
    public void dlist(){
        int pageNumber = getParaToInt("page",1);
        int pageSize = getParaToInt("pageSize",10);
        String uptime = getPara("uptime");
        Page<BseData> dataPage=dataService.paginate(pageNumber,pageSize,uptime);
        setAttr("uptime", uptime);
        setAttr("dataPage", dataPage);
        render("datalist.html");
        dataService.printTableStructure();
    }
public void delete(int id){
        dataService.delete(id);
        System.out.println(("5assjhq0"));
        redirect("/data/dlist");
}

}
