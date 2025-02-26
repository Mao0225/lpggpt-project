package com.sjzu.edu.index;

import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.sjzu.edu.common.model.BseEquipOperation;
import com.sjzu.edu.common.model.BseEquipment;
import com.sjzu.edu.common.model.BseMaintenance;

import java.io.File;
import java.util.List;

//import com.sjzu.edu.model.Equipment;
@Path(value = "/", viewPath = "/equipment")
public class EquipmentController extends Controller {

    private EquipmentService equipmentService = new EquipmentService();

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

    // 处理获取设备列表展示的请求方法，类似你提供示例中的分页查询展示逻辑
    public void equipmentList() {
        // 从请求中获取参数
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        int pageSize = getParaToInt("size", 10);
        String equipmentName = getPara("equipmentName");
        String equipmentKind = getPara("equipmentKind");
        setAttr("equipmentName", equipmentName);
        setAttr("equipmentKind", equipmentKind);
        // 调用服务层的方法进行搜索
        Page<Record> equipmentPage = equipmentService.search(pageNumber, pageSize, equipmentName, equipmentKind, this);

        // 将结果设置为属性

        // 将结果设置为属性
        setAttr("equipmentlist", equipmentPage);

        // 渲染页面（这里假设对应的页面是equipment.html，你需要根据实际情况修改）
        render("equipment.html");
    }
    // 处理设备删除操作
    public void delete() {
        Integer equipmentId = getParaToInt("id"); // 明确指定获取名为id的参数转换为整型，获取可能为null
        if (equipmentId!= null) {
            equipmentService.deleteById(equipmentId);
            redirect("/equipment/equipmentList");
        } else {
            // 处理参数为空的情况，比如返回错误提示信息给前端，告知缺少必要的设备id参数
            renderError(400, "缺少设备id参数，无法执行删除操作");
        }
    }

    public void add() {
        Integer companyId =  getSessionAttr("stationid");
        String writer = getSessionAttr("username");

        System.out.println("writer: "+writer);
        setAttr("companyId", companyId);
        setAttr("writer", writer);
        setAttr("equipment", equipmentService.findById(companyId));
        setAttr("gasstation",equipmentService.findBycoampanyId(companyId));


        render("add.html");
    }
    // 新增的保存方法，用于保存设备相关数据到数据库
    public void save() {
        Integer key = equipmentService.findMaxId();
        BseEquipOperation flag = new BseEquipOperation();



        BseEquipment equipment = getModel(BseEquipment.class, "equipment");
        flag.setFactory(equipment.getFactory());
        flag.setModel(equipment.getModel());
        flag.setName(equipment.getName());
        flag.setOperationType("新增设备");
        String writer = getSessionAttr("username");
        flag.setStaff(writer);
        flag.setStationid(equipment.getCompanyid());

        flag.save();

        System.out.println(equipment);

        if (equipment.save()) {
            // 保存成功，可以重定向到设备列表页面或者显示成功消息
            redirect("/equipment/equipmentList");
        }
    }
    public void edit() {
        int equipmentId =  getParaToInt("id");
        System.out.println("解析出的设备id值为: " + equipmentId); // 添加这行日志输出
        setAttr("equipment", equipmentService.findById(equipmentId));
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
            // 保存成功，可以重定向到列表页面或者显示成功消息
            redirect("/equipment/equipmentList");
        }
    }
    public void repairedit() {
        // 获取前端传递过来的equipmentid参数，转换为整型，可能为null
        Integer equipmentId = getParaToInt("id");


        if (equipmentId!= null) {
            // 调用EquipmentService的方法查找对应的数据记录
            Record maintenanceRecord = equipmentService.findByEquipmentIdAndCompanyId(equipmentId);
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
            redirect("/equipment/equipmentList");
        }
    }
}
