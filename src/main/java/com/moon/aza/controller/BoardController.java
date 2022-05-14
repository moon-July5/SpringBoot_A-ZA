package com.moon.aza.controller;

import com.moon.aza.dto.BoardForm;
import com.moon.aza.dto.LikesDTO;
import com.moon.aza.dto.PageRequestDTO;
import com.moon.aza.entity.Board;
import com.moon.aza.entity.Member;
import com.moon.aza.entity.TemporaryBoard;
import com.moon.aza.service.BoardService;
import com.moon.aza.service.LikesService;
import com.moon.aza.service.TemporaryBoardService;
import com.moon.aza.support.CurrentMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/board")
@Controller
public class BoardController {
    private final BoardService boardService;
    private final LikesService likesService;
    private final TemporaryBoardService temporaryBoardService;

    @PostMapping("/temporary-register")
    public String temporaryRegister(@ModelAttribute BoardForm boardForm, @CurrentMember Member member ,
                                    RedirectAttributes redirectAttributes){
        TemporaryBoard temporaryBoard = temporaryBoardService.register(boardForm);
        redirectAttributes.addFlashAttribute("success", "성공적으로 저장되었습니다.");
        redirectAttributes.addFlashAttribute("member", member);

        return "redirect:/board/register";
    }

    @DeleteMapping("/temporary-remove/{id}")
    public @ResponseBody ResponseEntity<Long> temporaryRemove(@PathVariable("id") Long id,
                                                              @CurrentMember Member member){
        temporaryBoardService.remove(id);
        Long count = temporaryBoardService.getCount(member.getId());
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    /* 글 작성 페이지 */
    @GetMapping("/register")
    public void boardRegister(@CurrentMember Member member, Long id, Model model){
        log.info("/board/register");
        model.addAttribute("member",member);
        model.addAttribute(new BoardForm());

        model.addAttribute("result", temporaryBoardService.getList(member.getId()));
        model.addAttribute("count", temporaryBoardService.getCount(member.getId()));
        if(id!=null){
            BoardForm boardForm = temporaryBoardService.temporaryBoardLoad(id);
            model.addAttribute(new BoardForm(boardForm.getTitle(), boardForm.getContents()));
            model.addAttribute("tid",id);
        }
    }

    @GetMapping({"/read","/modify"})
    public void boardRead(@CurrentMember Member member, Long id, PageRequestDTO pageRequestDTO, Model model){
        log.info("id : "+id);
        BoardForm boardForm = boardService.read(id);
        model.addAttribute("boardForm", boardForm);
        if(member != null) {
            model.addAttribute(member);
            // 추천을 누른 사용자 확인
            Boolean likes = likesService.findLike(member.getId(), id);
            model.addAttribute("likes", likes);
        } else{
            model.addAttribute("likes",false);
        }
    }

    @PostMapping("/register")
    public String register(@ModelAttribute BoardForm boardForm, Long tid){
        Board board = boardService.register(boardForm);

        if(tid!=null){
            temporaryBoardService.remove(tid);
        }

        return "redirect:/board";
    }

    @PutMapping("/modify/{id}")
    public String modify(@ModelAttribute BoardForm boardForm, PageRequestDTO pageRequestDTO,
                         RedirectAttributes redirectAttributes){
        boardService.modify(boardForm);

        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("id", boardForm.getId());

        return "redirect:/board/read";
    }
    @DeleteMapping("/remove/{id}")
    public String remove(@PathVariable("id") Long id){
        boardService.remove(id);
        return "redirect:/board";
    }
    @PostMapping("/{boardId}/likes")
    public @ResponseBody ResponseEntity<Boolean> likes(@RequestBody LikesDTO likesDTO, Model model){
        log.info("LikesDTO : "+likesDTO);
        Boolean likes = likesService.pushLike(likesDTO);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }
}
