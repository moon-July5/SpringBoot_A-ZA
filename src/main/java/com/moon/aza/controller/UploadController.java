package com.moon.aza.controller;

import com.moon.aza.entity.Member;
import com.moon.aza.service.AwsS3Service;
import com.moon.aza.support.CurrentMember;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RestController
public class UploadController {
    private final AwsS3Service awsS3Service;

    @PostMapping("/image/upload")
    public void postImage(@RequestParam MultipartFile upload, HttpServletResponse res, HttpServletRequest req,
                          @CurrentMember Member member){
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

            // UUID
            String uuid = UUID.randomUUID().toString();

            // 저장 경로
            String uploadPath = "upload/" + member.getId().toString()+"/";

            // 파일 이름 중간에 _를 이용하여 구분
            String saveName = uploadPath + uuid + "_" + fileName;

            log.info("saveName : "+saveName);

            // ckEditor 로 전송
            printWriter = res.getWriter();
            String callback = req.getParameter("CKEditorFuncNum");
            String fileUrl = awsS3Service.upload(upload, saveName);
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

}
