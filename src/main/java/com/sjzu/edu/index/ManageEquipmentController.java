package com.sjzu.edu.index;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.sjzu.edu.common.model.*;

import java.io.File;
import java.util.List;

public class ManageEquipmentController  extends Controller {
    ManageEquipmentService manageEquipmentService = new ManageEquipmentService();
    private GasStation gastation = new GasStation().dao();
    // 新增处理设备图片文件上传的方法
    public void equipmentImageUpload() {
        List<UploadFile> uploadFiles = getFiles();
        if (uploadFiles != null && !uploadFiles.isEmpty()) {
            StringBuilder fileUrls = new StringBuilder();
            int count = 1;

            // 获取配置文件中的根路径
            for (UploadFile uploadFile : uploadFiles) {
                String newFileName = uploadFile.getFileName(); // 获取原始文件名，而不是生成新的文件名

                String savePath = "equipment"; // 新的保存路径
                File rootDir = new File(uploadFile.getUploadPath(), savePath);
                if (!rootDir.exists()) {
                    rootDir.mkdirs();
                }
                File newFile = new File(rootDir, newFileName);
                uploadFile.getFile().renameTo(newFile);

                // 构建文件的可访问URL路径，这里假设你的项目中访问文件的前缀是"/equipment_image_access/"，按需调整
                String fileUrl = "/upload/temp/" + savePath + "/" + newFileName;
                System.out.println("File URL " + fileUrl);

                fileUrls.append(fileUrl);
                if (count < uploadFiles.size()) {
                    fileUrls.append(",");
                }
                count++;
            }

            // 返回所有文件路径的字符串
            System.out.println("File URLs: " + fileUrls.toString());
            renderJson("url", fileUrls.toString());
        } else {
            renderError(400);
        }
    }

    public void mequiplist(){
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        int pageSize = getParaToInt("size", 10);
        Page<BseEquipment> BseEquipmentPage=manageEquipmentService.paginate(pageNumber,pageSize);
        List<GasStation> gastations = gastation.find("SELECT * FROM gas_station");
        setAttr("BseEquipmentlist", BseEquipmentPage);
        setAttr("gastations", gastations);
        render("maequipmentlist.html");
    }
   public void equipmentsearch()
   {   Integer companyId=getParaToInt("companyId");
       System.out.println("companyId； "+companyId);
       setAttr("companyId", companyId);
       int pageNumber = getParaToInt("page", 1); // 默认为第1页
       int pageSize = getParaToInt("size", 10);
       Page<Record> BseEquipmentPage=manageEquipmentService.search(pageNumber,pageSize,companyId);
       List<GasStation> gastations = gastation.find("SELECT * FROM gas_station");
       setAttr("gastations", gastations);
       setAttr("BseEquipmentlist", BseEquipmentPage);
       render("maequipmentlist.html");
   }
    public void edit() {
        int equipmentId =  getParaToInt("id");
        System.out.println("解析出的设备id值为: " + equipmentId); // 添加这行日志输出
        setAttr("equipment", manageEquipmentService.findById(equipmentId));
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
            // 保存成功，可以重定向到列表页面或者显示成功消息
            redirect("/managerequip/mequiplist");
        }
    }
    public void repairedit() {
        // 获取前端传递过来的equipmentid参数，转换为整型，可能为null
        Integer equipmentId = getParaToInt("id");


        if (equipmentId!= null) {
            // 调用EquipmentService的方法查找对应的数据记录
            Record maintenanceRecord = manageEquipmentService.findByEquipmentIdAndCompanyId(equipmentId);
            if (maintenanceRecord!= null) {
                // 将查询到的数据记录设置为属性，以便传递给前端页面，这里属性名假设为maintenanceInfo，你可以按需修改
                setAttr("maintenanceInfo", maintenanceRecord);
            } else {
                // 如果没有查询到对应的数据，可以返回相应的提示信息给前端，比如数据不存在等提示
                renderText("未查询到对应的数据记录，请检查参数是否正确");
            }
        } else {
            // 如果参数有空值，返回错误提示信息给前端，告知缺少必要参数
            renderError(400, "缺少必要的参数（equipmentid或companyid），无法执行查询操作");
        }
    }

    public void repair()
    {
        BseMaintenance maintenance = getModel(BseMaintenance.class, "maintenance");
        System.out.println("maintenance: "+maintenance);

        if (maintenance.save()) {
            // 保存成功，可以重定向到设备列表页面或者显示成功消息
            redirect("/managerequip/mequiplist");
        }
    }


    public void delete() {
        Integer equipmentId = getParaToInt("id"); // 明确指定获取名为id的参数转换为整型，获取可能为null
        if (equipmentId!= null) {
            manageEquipmentService.deleteById(equipmentId);
            redirect("/managerequip/mequiplist");
        } else {
            // 处理参数为空的情况，比如返回错误提示信息给前端，告知缺少必要的设备id参数
            renderError(400, "缺少设备id参数，无法执行删除操作");
        }
    }
}
