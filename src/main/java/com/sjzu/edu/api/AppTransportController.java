package com.sjzu.edu.api;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.sjzu.edu.common.model.Drivercar;
import com.sjzu.edu.common.model.GasBottle;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Path(value = "/", viewPath = "/apptransport")
@Clear
public class AppTransportController extends Controller {
    private GasBottle dao = new GasBottle().dao();
    private Drivercar ddao = new Drivercar().dao();

    public void list() {
        addCorsHeaders(); // 添加这一行来允许跨域请求

        String telephone = getPara("telephone");
        int pageNum = getParaToInt("pageNum"); // 第二个参数是默认值
        int pageSize = getParaToInt("pageSize"); // 第二个参数是默认值
        System.out.println("手机号："+telephone);
//        LocalDate currentDate = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String formattedDate = currentDate.format(formatter);
//        System.out.println("formattedDate Date: " + formattedDate);

        try {
            String fromSql = "from gas_bottle where 1=1  ";
            fromSql += " and tel = " + telephone +" order by id desc";
//            fromSql += "and tel = " + telephone+ " and date(end_time) = "+formattedDate+" ";
            System.out.println(fromSql);
            // 获取总记录数
            GasBottle totalRecord = dao.findFirst("SELECT COUNT(*) AS total FROM gas_bottle WHERE  tel = " + telephone +"");
            if (totalRecord != null) {
                int total = totalRecord.getInt("total");
                System.out.println(total);
                // 获取当前页的记录
                Page<GasBottle> records = dao.paginate(pageNum,pageSize,"select *",fromSql);
                System.out.println(records.toString());
                JSONObject json = new JSONObject();
                json.put("total", total);
                json.put("rows", records);

                renderJson(json);
            } else {
                // 如果没有找到记录，可以返回一个空的JSON对象
                JSONObject json = new JSONObject();
                json.put("total", 0);
                json.put("rows", new ArrayList<GasBottle>()); // 使用 ArrayList 而不是数组
                renderJson(json);
            }
        } catch (Exception e) {
            // 这里可以添加更详细的错误处理，比如记录日志
            e.printStackTrace();
            renderError(500); // 返回服务器内部错误
        }
    }
    public void searchList(){
        addCorsHeaders(); // 添加这一行来允许跨域请求
        String telephone = getPara("telephone");
        String code = getPara("qipingid");
        String time = getPara("riqi");
        int pageNum = getParaToInt("pageNum"); // 第二个参数是默认值
        int pageSize = getParaToInt("pageSize"); // 第二个参数是默认值
        System.out.println(code+"\n"+time+"\n"+pageNum+"\n"+pageSize+"\n");
        try {
            String fromSql = "from gas_bottle where 1=1 ";
            if(code!=null){
                fromSql += "and bottle_id like '%" + code+"%'" ;
            }
            if (time!=null){
                fromSql += " AND end_time >= '" + time + " 00:00:00' AND end_time < '" + time + " 23:59:59'";

            }
            fromSql += " and tel = " + telephone +" order by id desc";
            System.out.println(fromSql);
            // 获取总记录数
            GasBottle totalRecord = dao.findFirst("SELECT COUNT(*) AS total FROM gas_bottle WHERE tel = " + telephone +" ");
            if (totalRecord != null) {
                int total = totalRecord.getInt("total");
                System.out.println(total);
                // 获取当前页的记录
                Page<GasBottle> records = dao.paginate(pageNum,pageSize,"select *",fromSql);
//                System.out.println(records.toString());
                JSONObject json = new JSONObject();
                json.put("total", total);
                json.put("rows", records);

                renderJson(json);
            } else {
                System.out.println(fromSql);
                // 如果没有找到记录，可以返回一个空的JSON对象
                JSONObject json = new JSONObject();
                json.put("total", 0);
                json.put("rows", new ArrayList<GasBottle>()); // 使用 ArrayList 而不是数组
                renderJson(json);
            }
        } catch (Exception e) {
            // 这里可以添加更详细的错误处理，比如记录日志
            e.printStackTrace();
            renderError(500); // 返回服务器内部错误
        }
    }

    public void update(){
        addCorsHeaders(); // 添加这一行来允许跨域请求
        Drivercar D = new Drivercar();

        // 处理文件上传
        UploadFile identityCardFile= getFile("identityCardPhoto");
        UploadFile driverLicenseImageFile = getFile("carLicensePhoto");
        UploadFile riskCargoLicenseFile= getFile("riskCargoLicensePhoto");
        UploadFile otherPhotoFile = getFile("otherPhoto");
        int id = getParaToInt("id");
        System.out.println("id:"+id);
        String Name = getPara("driverName");
        String Phone = getPara("driverPhone");
        String Sex = getPara("driverSex");
        String CarNo = getPara("driverCarNo");
        String riskCargoLicenseNo = getPara("riskCargoLicenseNo");
        String riskCargoLicenseIndate = getPara("riskCargoLicenseIndate");
        String identityCardNo = getPara("identityCardNo");
        System.out.println("获取到的信息："+id+","+Name+","+Phone+","+Sex+","+CarNo+","+riskCargoLicenseIndate+","+identityCardNo+","+riskCargoLicenseNo+";");
        D.setId(id);
        D.setDrivername(Name);
        D.setDriverphone(Phone);
        D.setDriversex(Sex);
        D.setDrivercarno(CarNo);
        D.setRiskCargoLicenseNo(riskCargoLicenseNo);
        D.setRiskCargoLicenseIndate(riskCargoLicenseIndate);
        D.setIdentityCardNo(identityCardNo);


        if(identityCardFile !=null) {
            String identityCardUrl = upload(identityCardFile);
            D.setIdentityCardNoImgUrl(identityCardUrl);
            System.out.println(identityCardUrl);
            System.out.println("身份证图片上传成功");
        }else {
            System.out.println("身份证图片上传失败");
        }

        if(driverLicenseImageFile !=null) {
            String driverLicenseImageUrl = upload(driverLicenseImageFile);
            D.setDriverLicenseImageUrl(driverLicenseImageUrl);
            System.out.println(driverLicenseImageUrl);
            System.out.println("驾驶证图片上传成功");
        }else{
            System.out.println("驾驶证图片上传失败");
        }

        if(riskCargoLicenseFile !=null) {
            String riskCargoLicenseUrl = upload(riskCargoLicenseFile);
            D.setRiskCargoLicenseImageUrl(riskCargoLicenseUrl);
            System.out.println(riskCargoLicenseUrl);
            System.out.println("图片上传成功");
        }else {
            System.out.println("危货证图片上传失败");
        }

        if(otherPhotoFile !=null) {
            String otherImageUrl = upload(otherPhotoFile);
            D.setOtherImageUrl(otherImageUrl);
            System.out.println(otherImageUrl);
            System.out.println("其他图片上传成功");
        }else {
            System.out.println("其他图片上传失败");
        }
        JSONObject json = new JSONObject();
        if(D.update()){
            System.out.println("更新成功"+D);
            json.put("code", 200);
            json.put("driver", D);
            renderJson(json);
        }else {
            json.put("driver", new Drivercar());
            renderJson(json);
            System.out.println("更新失败");
        }
    }

    public void login(){
        String tel = getPara("telephone");
        String password = getPara("password");
        String selectSql = "select *";
        String fromSql = "from drivercar where driverphone = " +tel+" and driverpsw= "+password;
        String fianlsql = selectSql+fromSql;
        Drivercar drivercar = ddao.findFirst(fianlsql);
        JSONObject json = new JSONObject();
        if(drivercar!=null){
            json.put("code", 200);
            json.put("driver",drivercar);
            renderJson(json);
        }else {
            json.put("driver", new Drivercar());
            renderJson(json);
            System.out.println("更新失败");
        }
    }
    public void addCorsHeaders() {
        HttpServletResponse response = getResponse();
        String origin = getHeader("Origin");
        if (StrKit.notBlank(origin)) {
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Origin", origin);
        }
        // renderNull();
    }
    //上传图片
    public String upload(UploadFile file) {
        if (file == null || file.getFile().length() == 0) {
            System.out.println("图片为空");
            return "/images/faces/face1.jpg";
        }
        System.out.println("图片不为空，开始存储");
        // 为文件生成新的保存路径
        String savePath = "/upload/driverImg/" + file.getFileName();

        // 获取项目的根目录的绝对路径
        String absolutePath = getRequest().getSession().getServletContext().getRealPath("/") + savePath;

        // 创建目标目录
        File targetDir = new File(absolutePath).getParentFile();
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        // 移动文件至目标目录
        file.getFile().renameTo(new File(absolutePath));
        System.out.println("图片存储成功"+savePath);
        // 返回相对保存地址
        return savePath;
    }
}
