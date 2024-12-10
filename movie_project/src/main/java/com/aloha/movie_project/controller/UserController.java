package com.aloha.movie_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aloha.movie_project.domain.CustomUser;
import com.aloha.movie_project.domain.Users;
import com.aloha.movie_project.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/encode")
    public String encodePassword(@RequestParam String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }  
            
    @GetMapping("/mypageMain")
    public String mypageMain(Model model) {
        model.addAttribute("username", "홍길동");
        model.addAttribute("grade", "일반 등급");
        model.addAttribute("movieCount", 2);
        model.addAttribute("reviewCount", 1);
        return "user/mypageMain"; // mypageMain.html 파일을 반환
    }

    @PostMapping("/update-info")
    public String updateUserInfo(@RequestParam("password") String password, HttpServletRequest request) {
        // 비밀번호 검증 로직
        String correctPassword = "1234"; // 실제 비밀번호를 DB에서 가져오는 로직으로 수정해야 함

        if (password.equals(correctPassword)) {
            // 비밀번호가 맞으면 회원 수정 페이지로 이동
            return "redirect:/user/mypage"; // 회원 수정 페이지로 리다이렉트
        } else {
            // 비밀번호가 틀리면 다시 마이 페이지로
            return "redirect:/user/mypageMain"; // 마이 페이지로 리다이렉트
        }
    }

    @GetMapping("/mypage")
    public String mypage(@AuthenticationPrincipal CustomUser authUser, Model model) throws Exception {
        if (authUser != null) {
            // 로그인된 사용자의 아이디를 모델에 추가
            String username = authUser.getUsername();
            model.addAttribute("username", username);
        } else {
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }
        return "user/mypage"; // 경로를 수정
    }

    @PostMapping("/mypage")
    public String updateMypage(@AuthenticationPrincipal CustomUser authUser,
                                Users updatedUser,
                                Model model) throws Exception {
        log.info(":::::::::: 마이페이지 정보 수정 처리 ::::::::::");
        
        if (authUser != null) {
            String currentUsername = authUser.getUsername();
            log.info("현재 로그인 사용자: " + currentUsername);
    
            // 기존 비밀번호와 비교 또는 새로운 비밀번호 업데이트 로직
            String newPassword = updatedUser.getPassword();
            
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                // 비밀번호 암호화 및 업데이트
                String encodedPassword = passwordEncoder.encode(newPassword);
                updatedUser.setUsername(currentUsername); // 현재 사용자 설정
                updatedUser.setPassword(encodedPassword);
                int updateResult = userService.updatePw(updatedUser); // 비밀번호 업데이트
    
                if (updateResult > 0) {
                    log.info("비밀번호 변경 성공!");
                    model.addAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다.");
                } else {
                    log.error("비밀번호 변경 실패!");
                    model.addAttribute("errorMessage", "비밀번호 변경에 실패했습니다.");
                }
            } else {
                log.warn("새로운 비밀번호가 입력되지 않았습니다.");
                model.addAttribute("errorMessage", "비밀번호를 입력해주세요.");
            }
        } else {
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }
        return "user/mypage"; // 마이페이지로 리다이렉트
    }
    
    @PostMapping("/mypage/password")
    public String updatePassword()
    {
                    return null;   
       
    }
}
