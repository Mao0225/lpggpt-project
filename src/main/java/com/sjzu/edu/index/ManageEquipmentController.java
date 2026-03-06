package com.sjzu.edu.index;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.sjzu.edu.common.model.BseEquipment;
import com.sjzu.edu.common.model.BseEquipOperation;
import com.sjzu.edu.common.model.BseMaintenance;
import com.sjzu.edu.common.model.GasStation;

import java.io.File;
import java.util.List;

public class ManageEquipmentController  extends Controller {
    ManageEquipmentService manageEquipmentService = new ManageEquipmentService();
    private GasStation gastation = new GasStation().dao();

    // 图片上传方法（保持不变）
    public void equipmentImageUpload() {
        List<UploadFile> uploadFiles = getFiles();
        if (uploadFiles != null && !uploadFiles.isEmpty()) {
            StringBuilder fileUrls = new StringBuilder();
            int count = 1;

            String savePath = "equipment";
            for (UploadFile uploadFile : uploadFiles) {
                String newFileName = uploadFile.getFileName();
                File rootDir = new File(uploadFile.getUploadPath(), savePath);
                if (!rootDir.exists()) {
                    rootDir.mkdirs();
                }
                File newFile = new File(rootDir, newFileName);
                uploadFile.getFile().renameTo(newFile);

                String fileUrl = "/upload/temp/" + savePath + "/" + newFileName;
                System.out.println("File URL " + fileUrl);

                fileUrls.append(fileUrl);
                if (count < uploadFiles.size()) {
                    fileUrls.append(",");
                }
                count++;
            }
            renderJson("url", fileUrls.toString());
        } else {
            renderError(400);
        }
    }

    public void mequiplist(){
        int pageNumber = getParaToInt("page", 1);
        int pageSize = getParaToInt("size", 10);
        // 初始列表也支持companyId筛选（统一逻辑）
        Integer companyId = getParaToInt("companyId", -1);

        // 关键修复：统一使用Page<BseEquipment>类型
        Page<BseEquipment> BseEquipmentPage = manageEquipmentService.searchByCompanyId(pageNumber, pageSize, companyId);

        List<GasStation> gastations = gastation.find("SELECT * FROM gas_station");
        setAttr("BseEquipmentlist", BseEquipmentPage);
        setAttr("gastations", gastations);
        setAttr("companyId", companyId); // 传递选中的公司ID
        render("maequipmentlist.html");
    }

    public void equipmentsearch() {
        // 关键修复：处理-1和null的情况，-1表示查询全部
        Integer companyId = getParaToInt("companyId", -1);
        System.out.println("companyId； "+companyId);

        int pageNumber = getParaToInt("page", 1);
        int pageSize = getParaToInt("size", 10);

        // 关键修复：统一使用Page<BseEquipment>类型
        Page<BseEquipment> BseEquipmentPage = manageEquipmentService.searchByCompanyId(pageNumber, pageSize, companyId);

        List<GasStation> gastations = gastation.find("SELECT * FROM gas_station");
        setAttr("gastations", gastations);
        setAttr("BseEquipmentlist", BseEquipmentPage);
        setAttr("companyId", companyId); // 修复：变量名统一为companyId
        render("maequipmentlist.html");
    }

    public void edit() {
        int equipmentId =  getParaToInt("id");
        System.out.println("解析出的设备id值为: " + equipmentId);
        setAttr("equipment", manageEquipmentService.findById(equipmentId));
        render("edit.html"); // 
    }

    public void update() {
        BseEquipment equipment = getModel(BseEquipment.class, "equipment");
        BseEquipOperation flag = new BseEquipOperation();
        flag.setFactory(equipment.getFactory());
        flag.setModel(equipment.getModel());
        flag.setName(equipment.getName());
        flag.setOperationType("修改设备");
        String writer = getSessionAttr("username");
        flag.setStaff(writer);
        flag.setStationid(equipment.getCompanyid());

        flag.save();
        if (equipment.update()) {
            System.out.println(("okkkkkn nookc mo noiq"));
            redirect("/managerequip/mequiplist");
        }
    }

    public void repairedit() {
        Integer equipmentId = getParaToInt("id");
        if (equipmentId!= null) {
            Record maintenanceRecord = manageEquipmentService.findByEquipmentIdAndCompanyId(equipmentId);
            if (maintenanceRecord!= null) {
                setAttr("maintenanceInfo", maintenanceRecord);
                render("equipment_repair.html"); // 补充：指定维修页面
            } else {
                renderText("未查询到对应的数据记录，请检查参数是否正确");
            }
        } else {
            renderError(400, "缺少必要的参数（equipmentid），无法执行查询操作");
        }
    }

    public void repair() {
        BseMaintenance maintenance = getModel(BseMaintenance.class, "maintenance");
        System.out.println("maintenance: "+maintenance);

        if (maintenance.save()) {
            redirect("/managerequip/mequiplist");
        }
    }

    public void delete() {
        Integer equipmentId = getParaToInt("id");
        if (equipmentId!= null) {
            manageEquipmentService.deleteById(equipmentId);
            redirect("/managerequip/mequiplist");
        } else {
            renderError(400, "缺少设备id参数，无法执行删除操作");
        }
    }
}