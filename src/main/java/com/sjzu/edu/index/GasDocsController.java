package com.sjzu.edu.index;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.upload.UploadFile;
import com.sjzu.edu.common.model.GasDocs;
import com.sjzu.edu.common.model.User;

import java.io.File;
import java.util.Date;

@Path(value = "/", viewPath = "/gasdocs")
public class GasDocsController extends Controller {

    @Inject
    GasDocsService service;

    public void gasDocslist() {

        int pageNumber = getParaToInt("page", 1);
        int pageSize = getParaToInt("size", 10);
        String inspOrg = getPara("inspOrg");
        String reportNo = getPara("reportNo");

        setAttr("inspOrg", inspOrg);
        setAttr("reportNo", reportNo);
        setAttr("gasDocs", service.search(pageNumber, pageSize, inspOrg, reportNo));

        render("gasDocsList.html");
    }

    public void add() {
        render("add.html");
    }

    public void edit() {
        Integer id = getParaToInt();
        if (id == null) {
            renderError(400);
            return;
        }
        setAttr("gas_docs", service.findById(id));
    }

    public void save() {
        UploadFile uploadFile = getFile("file");
        GasDocs gasDocs = getModel(GasDocs.class, "gas_docs");
        User loginUser = getSessionAttr("user");

        handleUploadAndMeta(uploadFile, gasDocs, true);
        if (loginUser != null) {
            gasDocs.setCreateUserid(loginUser.getId());
            System.out.println("创建用户id：" + loginUser.getId());
        }
        if (gasDocs.save()) {
            redirect("/gasDocs/gasDocslist");
        } else {
            setAttr("gas_docs", gasDocs);
            render("add.html");
        }
    }

    public void update() {
        UploadFile uploadFile = getFile("file");
        GasDocs gasDocs = getModel(GasDocs.class, "gas_docs");

        handleUploadAndMeta(uploadFile, gasDocs, false);

        if (gasDocs.update()) {
            redirect("/gasDocs/gasDocslist");
        } else {
            setAttr("gas_docs", gasDocs);
            render("edit.html");
        }
    }

    public void delete() {
        Integer id = getParaToInt();
        if (id != null) {
            service.delete(id);
        }
        redirect("/gasDocs/gasDocslist");
    }

    private void handleUploadAndMeta(UploadFile uploadFile, GasDocs gasDocs, boolean isCreate) {
        if (uploadFile != null) {
            String newFileName = uploadFile.getFileName();
            String savePath = "gasdocs";
            File rootDir = new File(uploadFile.getUploadPath(), savePath);
            if (!rootDir.exists()) {
                rootDir.mkdirs();
            }
            File newFile = new File(rootDir, newFileName);
            uploadFile.getFile().renameTo(newFile);

            String fileUrl = "/upload/temp/" + savePath + "/" + newFileName;
            gasDocs.setFileUrl(fileUrl);
        }

        if (isCreate) {
            gasDocs.setCreateTime(new Date());
            Object userObj = getSessionAttr("user");
            if (userObj instanceof User) {
                gasDocs.setCreateUserid(((User) userObj).getId());
            }
        }
    }
}
