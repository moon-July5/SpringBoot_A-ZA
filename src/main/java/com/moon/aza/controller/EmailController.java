package com.moon.aza.controller;

import com.moon.aza.dto.UserDTO;
import com.moon.aza.entity.User;
import com.moon.aza.security.dto.UserAuthDTO;
import com.moon.aza.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@Log4j2
@RequiredArgsConstructor
@RestController
public class EmailController {
    private final EmailService emailService;

    @GetMapping("/user/email/send")
    public void sendMail(UserDTO user) throws MessagingException {
        log.info("/user/email/send");
        log.info("UserDTO : "+user);
        StringBuffer emailcontent = new StringBuffer();
        emailcontent.append("<!DOCTYPE html>");
        emailcontent.append("<html>");
        emailcontent.append("<head>");
        emailcontent.append("</head>");
        emailcontent.append("<body>");
        emailcontent.append(
                " <div" 																																																	+
                        "	style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 400px; height: 600px; border-top: 4px solid #02b875; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">"		+
                        "	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">"																															+
                        "		<span style=\"font-size: 15px; margin: 0 0 10px 3px;\">A-ZA </span><br />"																													+
                        "		<span style=\"color: rgb(18, 51, 90);\">메일인증</span> 안내입니다."																																				+
                        "	</h1>\n"																																																+
                        "	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">"																													+
                        user.getId()																																																+
                        "		님 안녕하세요.<br />"																																													+
                        "		A-ZA에 가입해 주셔서 진심으로 감사드립니다.<br />"																																						+
                        "		아래 <b style=\"color: rgb(18, 51, 90);\">'메일 인증'</b> 버튼을 클릭하여 회원가입을 완료해 주세요.<br />"																													+
                        "		감사합니다."																																															+
                        "	</p>"																																																	+
                        "	<a style=\"color: #FFF; text-decoration: none; text-align: center;\""																																	+
                        "	href=\"http://localhost:8080/user/email/certified?username=" + user.getEmail() + "&certified=" + user.getCertified() + "\" target=\"_blank\">"														+
                        "		<p"																																																	+
                        "			style=\"display: inline-block; width: 210px; height: 45px; margin: 30px 5px 40px; background: rgb(18, 51, 90); line-height: 45px; vertical-align: middle; font-size: 16px;\">"							+
                        "			메일 인증</p>"																																														+
                        "	</a>"																																																	+
                        "	<div style=\"border-top: 1px solid #DDD; padding: 5px;\"></div>"																																		+
                        " </div>"
        );
        emailcontent.append("</body>");
        emailcontent.append("</html>");
        emailService.sendMail(user.getEmail(), "[A-ZA 이메일 인증]", emailcontent.toString());
    }
}
