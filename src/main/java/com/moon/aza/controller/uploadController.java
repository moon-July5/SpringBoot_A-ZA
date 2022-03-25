package com.moon.aza.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Log4j2
@RestController
public class uploadController {
    @Value("${image.upload.path}")
    private String uploadPath;

    @PostMapping("/image/upload")
    public void postImage(@RequestParam MultipartFile upload, HttpServletResponse res, HttpServletRequest req){
        log.info("/image/upload");

        OutputStream out = null;
        PrintWriter printWriter = null;

        res.setCharacterEncoding("utf-8");
        res.setContentType("text/html;charset=utf-8");

        try{
            // 파일 이름 파악(전체경로)
            String originalName = upload.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\")+1);
            log.info("fileName : "+fileName);

            // 날짜 폴더 생성
            //String folderPath = makeFolder();

            // UUID
            String uuid = UUID.randomUUID().toString();

            // 파일 이름 중간에 _를 이용하여 구분
            String saveName = uploadPath + File.separator + uuid + "_" + fileName;

            Path savePath = Paths.get(saveName);

            byte[] bytes = upload.getBytes();

            // 이미지 저장
            out = new FileOutputStream(String.valueOf(savePath));
            out.write(bytes);
            out.flush();

            // ckEditor 로 전송
            printWriter = res.getWriter();
            String callback = req.getParameter("CKEditorFuncNum");
            String fileUrl = "/upload/"+ uuid + "_" + fileName;
            log.info("fileUrl : "+fileUrl);

            // 업로드 메시지 출력
            printWriter.println("<script type='text/javascript'>"
                    + "window.parent.CKEDITOR.tools.callFunction("
                    + callback+",'"+ fileUrl+"','이미지를 업로드하였습니다.')"
                    +"</script>");

            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(out != null) { out.close(); }
                if(printWriter != null) { printWriter.close(); }
            } catch(IOException e) { e.printStackTrace(); }
        }

    }
    // 폴더 만들기
    private String makeFolder() {
        // 연 / 월 / 일
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("//", File.separator);

        // uploadPath 경로에 folderPath 폴더 생성
        File uploadPathFolder = new File(uploadPath, folderPath);

        // 폴더가 존재하지 않는다면 생성
        if(uploadPathFolder.exists() == false){
            uploadPathFolder.mkdirs();
        }

        return folderPath;
    }
}
