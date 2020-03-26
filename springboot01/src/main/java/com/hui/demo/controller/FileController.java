package com.hui.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class FileController {

    private static final String filePath = "D:\\tools\\";

    private Logger logger = LoggerFactory.getLogger(FileController.class);

    @GetMapping("/upload")
    public String get(ModelMap modelMap) {
        modelMap.addAttribute("msg","文件上传下载");
        return "upload";
    }

    @ResponseBody
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
       if(file.isEmpty()) {
           return "文件为空";
       }
       String fileName = file.getOriginalFilename();
       logger.info("上传的文件名:{}",fileName);
       // 设置文件存储路径
        String path = filePath + fileName;
        File dst = new File(path);
        // 检测目录是否存在
        if(!dst.getParentFile().exists()) {
            // 新建文件夹
            dst.getParentFile().mkdir();
        }
        file.transferTo(dst); // 文件写入
        return "上传成功";
    }
    @ResponseBody
    @GetMapping("/download")
    public String downLoadFile(HttpServletResponse response) {

        String fileName = "test.jpg";
        File file = new File(filePath + fileName);
        if(file.exists()) {
            // 设置强制下载不打开
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment;fileName="+fileName);
            byte[] buffer =new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1){
                    os.write(buffer,0 ,i);
                    i = bis.read(buffer);
                }
                return "下载成功";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "下载失败";
    }
}
