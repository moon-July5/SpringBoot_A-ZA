package com.moon.aza.service;

import com.moon.aza.dto.SignUpForm;
import com.moon.aza.entity.Board;
import com.moon.aza.entity.Member;
import com.moon.aza.repository.BoardRepository;
import com.moon.aza.repository.CommentRepository;
import com.moon.aza.repository.LikesRepository;
import com.moon.aza.repository.MemberRepository;
import com.moon.aza.support.UserMember;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    @Value("${spring.mail.username}")
    private String from;

    public void deleteMember(Member member){
        List<Long> boardIds = boardRepository.findByMemberId(member.getId());
        List<Long> likesIds = likesRepository.findByMemberId(member.getId());
        List<Long> commentIds = commentRepository.findByMemberId(member.getId());
        if(!commentIds.isEmpty()){
            commentRepository.deleteAllByIdIn(commentIds);
        }
        if(!likesIds.isEmpty()){
            likesRepository.deleteAllByIdIn(likesIds);
        }
        if(!boardIds.isEmpty()){
            boardRepository.deleteAllByIdIn(boardIds);
        }
        memberRepository.deleteById(member.getId());
    }

    public void sendLoginLink(Member member) throws MessagingException {
        member.generateToken();
        // 메일 보내기
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,"UTF-8");
        StringBuilder body = new StringBuilder();
        body.append("<html> <body> <span style=\"font-size:16px; font-weight:bold;\">");
        body.append(member.getNickname()+"님</span> 안녕하세요.<br>");
        body.append("<p> 이메일을 통해 로그인을 하고 싶으시면 아래의 링크를 클릭하시길 바랍니다. </p><br>");
        body.append("<a href=\"http://localhost:8080/member/login-by-email?token="
                +member.getEmailToken()+"&email="+member.getEmail()+"\">");
        body.append("로그인하기</a></body></html>");

        messageHelper.setFrom(from); // 보내는 사람
        messageHelper.setTo(member.getEmail()); // 받는 사람
        messageHelper.setSubject("A-ZA 로그인 링크"); // 메일 제목
        messageHelper.setText(body.toString(),true); // 메일 내용

        mailSender.send(mailMessage);

    }

    public void changePassword(Member member, String password){
        member.changePassword(passwordEncoder.encode(password));
        memberRepository.save(member);

    }
    public void changeNickname(Member member, String nickname){
        member.changeNickname(nickname);
        memberRepository.save(member);
        login(member);
    }

    /* 사용자 정보 조회 */
    @Override
    @Transactional(readOnly = true) // 조회 용도로만 사용
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username);
        if(member == null)
            throw new UsernameNotFoundException(username);

        return new UserMember(member);
    }

    /* 로그인 */
    public void login(Member member){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(new UserMember(member),
                member.getPassword(), Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
    }
    /* 회원정보 & 이메일 인증 보내기(실질적으로 컨트롤러에서 호출)  */
    public Member signUp(SignUpForm signUpForm) throws MessagingException {
        Member newMember = saveMember(signUpForm);
        // 이메일 인증용 토큰 생성
        newMember.generateToken();
        sendVerificationEmail(newMember);
        return newMember;
    }

    /* 회원정보 저장 로직*/
    public Member saveMember(SignUpForm signUpForm){
        Member member = Member.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .build();

        return memberRepository.save(member);
    }

    /* 이메일 인증 보내기 */
    public void sendVerificationEmail(Member member) throws MessagingException {
        // 메일 보내기
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,"UTF-8");

        StringBuilder body = new StringBuilder();
        body.append("<html> <body> <span style=\"font-size:16px; font-weight:bold;\">");
        body.append(member.getNickname()+"님</span> 안녕하세요.<br>");
        body.append("<p> 회원가입 완료를 위해 아래 이메일 인증을 해주시길 바랍니다.</p><br>");
        body.append("<a href=\"http://localhost:8080/member/email-check-token?token="
                +member.getEmailToken()+"&email="+member.getEmail()+"\">");
        body.append("이메일 인증 하기</a></body></html>");

        messageHelper.setFrom(from); // 보내는 사람
        messageHelper.setTo(member.getEmail()); // 받는 사람
        messageHelper.setSubject("A-ZA 회원 가입 인증"); // 메일 제목
        messageHelper.setText(body.toString(),true); // 메일 내용

        mailSender.send(mailMessage);
    }
    // 이메일 인증 완료 되었다는 표시 후, 자동 로그인
    public void verify(Member member){
        member.verified();
        login(member);
    }
    /* 회원가입 시, 유효성 체크*/
    public Map<String, String> validateHandling(Errors errors){
        Map<String, String> validatorResult = new HashMap<>();
        for(FieldError error : errors.getFieldErrors()){
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    public Member findMemberByEmail(String email){
        return memberRepository.findByEmail(email);
    }
}
