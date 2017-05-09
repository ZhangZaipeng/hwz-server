package com.hwz.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;

import com.hwz.platform.YvanUtil;
import com.hwz.platform.springmvc.ResponseUtils;
import com.hwz.platform.springmvc.WebApplicationContextUtils;

public class UploadServlet extends HttpServlet {

    private static final Logger           LOG = LoggerFactory.getLogger(UploadServlet.class);
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    private ServletContext                ctx;
    private UploadConfig                  uploadConfig;

    /**
     * Constructor of the object.
     */
    public UploadServlet(){
        super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    @Override
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }

    /**
     * The doGet method of the servlet. <br>
     * This method is called when a form has its tag value method equals to get.
     * 
     * @param request the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.getWriter().println("请以POST方式上传文件");
    }

    /**
     * The doPost method of the servlet. <br>
     * This method is called when a form has its tag value method equals to post.
     * 
     * @param request the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        File file1 = null;
        request.setCharacterEncoding("UTF-8");
        // 为该请求创建一个DiskFileItemFactory对象，通过它来解析请求。执行解析后，所有的表单项目都保存在一个List中。
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(-1);
        upload.setHeaderEncoding("UTF-8");
        ModelMap model = new ModelMap();

        try {
            List<FileItem> list = upload.parseRequest(request);
            for (FileItem fileItem : list) {
                if ("file".equals(fileItem.getFieldName())) {
                    // fileItem.getName()
                    String fn = fileItem.getName();
                    int extPos = fn.lastIndexOf('.');
                    String ext = "";
                    if (extPos >= 0) {
                        ext = fn.substring(extPos);
                    }
                    String uuid = UUID.randomUUID().toString();
                    String fmt = SDF.format(new Date());
                    String fullPath = uploadConfig.getUploadPath() + "/" + fmt + "/" + uuid + ext;
                    String extentPath = uploadConfig.getUrlPrefix() + "/" + fmt + "/" + uuid + ext;
                    file1 = new File(fullPath);
                    file1.getParentFile().mkdirs();
                    file1.createNewFile();

                    InputStream ins = fileItem.getInputStream();
                    OutputStream ous = new FileOutputStream(file1);

                    try {
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while ((len = ins.read(buffer)) > -1)
                            ous.write(buffer, 0, len);
                        model.put("path", extentPath);
                    } finally {
                        ous.close();
                        ins.close();
                    }
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }

        String jsonString = YvanUtil.toJson(model);
        try {
            ResponseUtils.writeJSONToResponse(response, ResponseUtils.DEFAULT_ENCODING, jsonString, false, false);
        } catch (IOException e) {
            try {
                LOG.error("错误", e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (IOException e1) {
                throw new RuntimeException("system error.", e1);
            }
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        ctx = config.getServletContext();
        uploadConfig = WebApplicationContextUtils.getService(UploadConfig.class, ctx);
    }
}
