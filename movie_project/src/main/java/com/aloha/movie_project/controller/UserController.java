package com.aloha.movie_project.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.aloha.movie_project.domain.CustomUser;
import com.aloha.movie_project.domain.Reserve;
import com.aloha.movie_project.domain.Users;
import com.aloha.movie_project.service.ReserveService;
import com.aloha.movie_project.service.ReviewService;
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

    @Autowired
    private ReserveService reserveService;

    @Autowired
    ReviewService reviewService;

    // @GetMapping("/encode")
    // public String encodePassword(@RequestParam String rawPassword) {
    // return passwordEncoder.encode(rawPassword);
    // }

    @GetMapping("/mypage")
    public String mypage(@AuthenticationPrincipal CustomUser authUser, Model model) {
        if (authUser != null) {
            String username = authUser.getUsername();
            String userId = authUser.getId(); // 사용자 고유 ID (UUID)

            // 예매 횟수 조회
            int movieCount = reserveService.selectReservationCountByUsername(username);

            // 리뷰 수 조회
            int reviewCount = reviewService.countUserReviews(userId);


            // 모델에 데이터 추가
            model.addAttribute("username", username);
            model.addAttribute("movieCount", movieCount);
            model.addAttribute("grade", "일반"); // 임의 데이터
            model.addAttribute("reviewCount", reviewCount); // 리뷰 수 추가

        } else {
            return "redirect:/login";
        }

        return "user/mypage";
    }

    @PostMapping("/mypage")
    public String myPage(@RequestParam("password") String password, HttpServletRequest request) {
        // 현재 로그인된 사용자의 정보를 가져오기
        String username = request.getUserPrincipal().getName();

        try {
            // DB에서 사용자 정보 가져오기
            Users user = userService.select(username);
            log.info("DB에서 가져온 사용자 비밀번호: " + user.getPassword());

            // user가 null이 아닌지 체크
            if (user != null) {
                // DB에서 가져온 암호화된 비밀번호와 입력된 비밀번호 비교
                if (passwordEncoder.matches(password, user.getPassword())) {
                    // 비밀번호가 맞으면 회원 수정 페이지로 이동
                    return "redirect:/user/mypageUpdate";
                } else {
                    // 비밀번호가 틀리면 다시 마이 페이지로
                    return "redirect:/user/mypage?error=invalidPassword";
                }
            } else {
                // 사용자 정보가 없으면 에러 처리
                return "redirect:/user/mypage?error=userNotFound";
            }
        } catch (Exception e) {
            // 예외 발생 시 에러 처리
            e.printStackTrace();
            return "redirect:/user/mypage?error=serverError";
        }
    }

    @GetMapping("/mypageUpdate")
    public String mypageUpdate(@AuthenticationPrincipal CustomUser authUser, Model model) throws Exception {
        if (authUser != null) {
            String username = authUser.getUsername();
            String profileUploadPath = "C:/upload/profiles/";

            // 프로필 이미지 파일 이름 생성
            String[] possibleExtensions = { "png", "jpg", "jpeg" };
            String profileImagePath = null;

            for (String ext : possibleExtensions) {
                File profileFile = new File(profileUploadPath + username + "." + ext);
                if (profileFile.exists()) {
                    profileImagePath = "/profiles/" + username + "." + ext;
                    break;
                }
            }

            // 기본 이미지 경로 설정
            if (profileImagePath == null) {
                profileImagePath = "/profiles/default.png";
            }

            model.addAttribute("username", username);
            model.addAttribute("encryptedPassword", authUser.getPassword());
            model.addAttribute("profileImage", profileImagePath);
        } else {
            return "redirect:/login";
        }

        return "user/mypageUpdate";
    }

    @PostMapping("/mypageUpdate")
    public String mypageUpdate(@AuthenticationPrincipal CustomUser authUser,
            @RequestParam(value = "file", required = false) MultipartFile file,
            Users updatedUser,
            Model model) throws Exception {
        log.info(":::::::::: 마이페이지 정보 수정 처리 ::::::::::");

        if (authUser != null) {
            String currentUsername = authUser.getUsername();
            log.info("현재 로그인 사용자: " + currentUsername);

            // 비밀번호 업데이트 로직
            String newPassword = updatedUser.getPassword();

            if (newPassword != null && !newPassword.trim().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(newPassword);
                updatedUser.setUsername(currentUsername);
                updatedUser.setPassword(encodedPassword);
                int updateResult = userService.updatePw(updatedUser);

                if (updateResult > 0) {
                    log.info("비밀번호 변경 성공!");
                    model.addAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다.");
                } else {
                    log.error("비밀번호 변경 실패!");
                    model.addAttribute("errorMessage", "비밀번호 변경에 실패했습니다.");
                }
            } else {
                log.warn("새로운 비밀번호가 입력되지 않았습니다.");
            }

            // 이메일 업데이트 로직
            String newEmail = updatedUser.getEmail();

            if (newEmail != null && !newEmail.trim().isEmpty()) {
                // 이메일 형식 유효성 검사 (간단한 예시)
                if (newEmail.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
                    updatedUser.setUsername(currentUsername);
                    updatedUser.setEmail(newEmail);
                    int emailUpdateResult = userService.updateEmail(updatedUser);

                    if (emailUpdateResult > 0) {
                        log.info("이메일 변경 성공!");
                        model.addAttribute("successMessage", "이메일이 성공적으로 변경되었습니다.");
                    } else {
                        log.error("이메일 변경 실패!");
                        model.addAttribute("errorMessage", "이메일 변경에 실패했습니다.");
                    }
                } else {
                    log.warn("유효하지 않은 이메일 형식: " + newEmail);
                    model.addAttribute("errorMessage", "유효한 이메일 주소를 입력해주세요.");
                }
            } else {
                log.warn("새로운 이메일이 입력되지 않았습니다.");
            }

            // 프로필 이미지 변경 처리
            if (file != null && !file.isEmpty()) {
                String profileUploadPath = "C:/upload/profiles/";
                String originalFilename = file.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

                List<String> validExtensions = Arrays.asList("png", "jpg", "jpeg");
                if (!validExtensions.contains(fileExtension)) {
                    model.addAttribute("errorMessage", "이미지 파일만 업로드 가능합니다. (PNG, JPG, JPEG)");
                    return "user/mypageUpdate";
                }

                String contentType = file.getContentType();
                if (!contentType.startsWith("image/")) {
                    model.addAttribute("errorMessage", "유효한 이미지 파일만 업로드 가능합니다.");
                    return "user/mypageUpdate";
                }

                String fileNameWithoutExtension = currentUsername;
                File existingFilePng = new File(profileUploadPath + fileNameWithoutExtension + ".png");
                File existingFileJpg = new File(profileUploadPath + fileNameWithoutExtension + ".jpg");

                if (existingFilePng.exists() && !existingFilePng.delete()) {
                    log.error("기존 PNG 파일 삭제 실패");
                }
                if (existingFileJpg.exists() && !existingFileJpg.delete()) {
                    log.error("기존 JPG 파일 삭제 실패");
                }

                String fileName = fileNameWithoutExtension + "." + fileExtension;
                File uploadFile = new File(profileUploadPath + fileName);
                try {
                    file.transferTo(uploadFile);
                    log.info("프로필 이미지 저장 성공: " + uploadFile.getAbsolutePath());
                    model.addAttribute("profileImage", "/profiles/" + fileName);
                } catch (IOException e) {
                    log.error("프로필 이미지 저장 실패", e);
                    model.addAttribute("errorMessage", "이미지 업로드 중 문제가 발생했습니다.");
                    return "user/mypageUpdate";
                }
            }
        } else {
            return "redirect:/login";
        }

        return "redirect:/user/mypageUpdate";
    }

    @GetMapping("/reservationList")
    public String reservationList(Model model) {
        // 예시 데이터: 실제로는 서비스 계층에서 데이터를 가져와야 합니다.
        List<Reserve> reservationList = Arrays.asList(
                new Reserve(
                        "영화 제목1", "상영관 1", "T001", "TL001", "서울",
                        "2024-12-20", "오후 7:00", 10000, "A1, A2", 2,
                        "UUID12345", "홍길동", "movie1.jpg"),
                new Reserve(
                        "영화 제목2", "상영관 2", "T002", "TL002", "부산",
                        "2024-12-21", "오후 6:00", 12000, "B3, B4", 2,
                        "UUID67890", "김철수", "movie2.jpg"));

        // 모델에 예약 목록 추가
        model.addAttribute("reservationList", reservationList);
        return "/user/reservationList"; // reservationList.html 파일을 렌더링
    }

}