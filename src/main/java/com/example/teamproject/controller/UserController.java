package com.example.teamproject.controller;

import com.example.teamproject.dto.MemberForm;
import com.example.teamproject.entity.User;
import com.example.teamproject.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("/")
    public String index() {
        return "members/login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "members/login";  // login.mustache 페이지로 이동
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        // 이메일과 비밀번호로 회원 찾기
        Optional<User> memberOpt = userRepository.findByEmail(email);

        if (memberOpt.isPresent()) {
            User member = memberOpt.get();

            // 비밀번호 검증 (단순한 예시로 일치 여부만 확인)
            if (member.getPassword().equals(password)) {
                // 로그인 성공 시 회원 정보를 세션에 저장하는 등의 작업을 할 수 있음
                model.addAttribute("member", member);
                session.setAttribute("id", member.getId());
                return "redirect:/members/" + member.getId();  // 로그인 성공 후 회원 상세 페이지로 리디렉션
            } else {
                // 비밀번호 불일치
                model.addAttribute("error", "비밀번호가 맞지 않습니다.");
                return "members/login";  // 로그인 페이지로 돌아감
            }
        } else {
            // 이메일이 존재하지 않음
            model.addAttribute("error", "이메일이 존재하지 않습니다.");
            return "members/login";  // 로그인 페이지로 돌아감
        }
    }

    // 기존의 회원가입, 회원 조회, 수정 등의 메서드는 그대로 유지

    @GetMapping("/members/new")
    public String newMemberForm(){
        return "members/new";
    }

    /*@GetMapping("/signup")
    public String signup() {
        return "/members/new";
    }*/

    //회원가입
    @PostMapping("/join")
    public String join(MemberForm memberForm) {
        memberForm.logInfo();

        // MemberForm을 Member 엔티티로 변환
        User member = memberForm.toEntity();

        // 회원 저장
        User saved = userRepository.save(member);

        // 회원가입 후 로그인 페이지로 리디렉션
        return "redirect:/login"; // 회원가입 후 로그인 페이지로 리디렉션
    }

    //데이터 조회 요청
    @GetMapping("/members/{id}")
    public String show(@PathVariable("id") Long id, Model model) {

        Optional<User> member = userRepository.findById(id);//여기확인
        model.addAttribute("member", member.get());
        /*
         */
/*
        Member member2 = memberRepository.findById(id).orElse(null);
        member2.logInfo();
        model.addAttribute("member",member2);
*/
/*
        model.addAttribute("member", memberRepository.findById(id)); */

        return "members/show";
    }

    //멤버리스트
    @GetMapping("/members")
    public String index(Model model) {

        List<User> memberList = userRepository.findAll();
        model.addAttribute("memberList", memberList);
        return "members/index";
    }

    @GetMapping("/members/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        User memberEntity = userRepository.findById(id).orElse(null);
        model.addAttribute("member", memberEntity);

        return "members/edit";
    }

    @GetMapping("/members/{id}/medicine")
    public String medicine(@PathVariable("id") Long id, Model model) {
        Optional<User> member = userRepository.findById(id);
        if (member.isPresent()) {
            model.addAttribute("member", member.get());
            return "members/medicine";  // 약품 수정 페이지로 이동
        }else {
            return "redirect:/members";  // 만약 회원이 없다면 멤버 목록 페이지로 리디렉션
        }
    }


    // 약품 정보 수정 처리 (mupdate)
    @PostMapping("/members/mupdate")
    public String mupdate(MemberForm memberForm) {
        log.info("약품 정보 수정: {}", memberForm.toString());

        User memberEntity = memberForm.toEntity();
        log.info("수정된 회원 엔티티: {}", memberEntity.toString());

        User target = userRepository.findById(memberEntity.getId()).orElse(null);

        if (target != null) {
            userRepository.save(target);  // 업데이트된 정보 저장
        }

        // 회원 상세 페이지로 리디렉션
        return "redirect:/members/" + target.getId();
    }


    @PostMapping("/members/update")
    public String update(MemberForm memberForm) {
        log.info(memberForm.toString());

        User memberEntity = memberForm.toEntity();

        User target = userRepository.findById(memberEntity.getId()).orElse(null);

        if (target != null) {
            userRepository.save(memberEntity);
        }
        //상세페이지로 redirect
        return "redirect:/members/" + target.getId();

    }

    @GetMapping("/members/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr){
        log.info("삭제 요청이 들어왔습니다.");
        //1.삭제 대상을 가져옴
        User target = userRepository.findById(id).orElse(null);
        log.info(target.toString());
        //2. 대상 삭제
        if(target != null){
            userRepository.delete(target);
            rttr.addFlashAttribute("msg","삭제되었습니다.");
        }
        //3. 결과 페이지로 리다이렉트
        return "redirect:/members";
    }

    @GetMapping("/members/{id}/mdelete")
    public String mdelete(@PathVariable Long id, RedirectAttributes rttr) {
        log.info("복용 중인 약품을 0으로 설정하는 요청이 들어왔습니다.");

        // 1. 삭제 대상이 아닌, 'medicine' 값을 0으로 설정할 대상을 가져옴
        User target = userRepository.findById(id).orElse(null);
        if (target != null) {
            // 2. medicine 값을 0으로 설정
            userRepository.save(target);  // 변경된 엔티티 저장
            log.info("회원의 복용 중인 약품을 0으로 변경했습니다: {}", target);
        } else {
            log.error("회원 정보를 찾을 수 없습니다. id: {}", id);
        }

        // 3. 해당 회원 상세 페이지로 리디렉션
        rttr.addFlashAttribute("msg", "복용 중인 약품 정보가 삭제되었습니다.");
        return "redirect:/members/" + id;
    }


}
