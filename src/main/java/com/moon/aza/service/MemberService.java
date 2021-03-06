package com.moon.aza.service;

import com.moon.aza.dto.SignUpForm;
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
        // ?????? ?????????
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,"UTF-8");
        StringBuilder body = new StringBuilder();
        body.append("<html> <body> <span style=\"font-size:16px; font-weight:bold;\">");
        body.append(member.getNickname()+"???</span> ???????????????.<br>");
        body.append("<p> ???????????? ?????? ???????????? ?????? ???????????? ????????? ????????? ??????????????? ????????????. </p><br>");
        body.append("<a href=\"http://localhost:8080/member/login-by-email?token="
                +member.getEmailToken()+"&email="+member.getEmail()+"\">");
        body.append("???????????????</a></body></html>");

        messageHelper.setFrom(from); // ????????? ??????
        messageHelper.setTo(member.getEmail()); // ?????? ??????
        messageHelper.setSubject("A-ZA ????????? ??????"); // ?????? ??????
        messageHelper.setText(body.toString(),true); // ?????? ??????

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

    /* ????????? ?????? ?????? */
    @Override
    @Transactional(readOnly = true) // ?????? ???????????? ??????
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username);
        if(member == null)
            throw new UsernameNotFoundException(username);

        return new UserMember(member);
    }

    /* ????????? */
    public void login(Member member){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(new UserMember(member),
                member.getPassword(), Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
    }
    /* ???????????? & ????????? ?????? ?????????(??????????????? ?????????????????? ??????)  */
    public Member signUp(SignUpForm signUpForm) throws MessagingException {
        Member newMember = saveMember(signUpForm);
        // ????????? ????????? ?????? ??????
        newMember.generateToken();
        sendVerificationEmail(newMember);
        return newMember;
    }

    /* ???????????? ?????? ??????*/
    public Member saveMember(SignUpForm signUpForm){
        Member member = Member.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .build();

        return memberRepository.save(member);
    }

    /* ????????? ?????? ????????? */
    public void sendVerificationEmail(Member member) throws MessagingException {
        // ?????? ?????????
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,"UTF-8");

        StringBuilder body = new StringBuilder();
        body.append("<html> <body> <span style=\"font-size:16px; font-weight:bold;\">");
        body.append(member.getNickname()+"???</span> ???????????????.<br>");
        body.append("<p> ???????????? ????????? ?????? ?????? ????????? ????????? ???????????? ????????????.</p><br>");
        body.append("<a href=\"http://localhost:8080/member/email-check-token?token="
                +member.getEmailToken()+"&email="+member.getEmail()+"\">");
        body.append("????????? ?????? ??????</a></body></html>");

        messageHelper.setFrom(from); // ????????? ??????
        messageHelper.setTo(member.getEmail()); // ?????? ??????
        messageHelper.setSubject("A-ZA ?????? ?????? ??????"); // ?????? ??????
        messageHelper.setText(body.toString(),true); // ?????? ??????

        mailSender.send(mailMessage);
    }
    // ????????? ?????? ?????? ???????????? ?????? ???, ?????? ?????????
    public void verify(Member member){
        member.verified();
        login(member);
    }
    /* ???????????? ???, ????????? ??????*/
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
