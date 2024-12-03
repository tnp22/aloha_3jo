package com.aloha.movie_project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aloha.movie_project.domain.AuthList;
import com.aloha.movie_project.domain.Pagination;
import com.aloha.movie_project.domain.UserAuth;
import com.aloha.movie_project.domain.Users;
import com.aloha.movie_project.service.AuthListService;
import com.aloha.movie_project.service.UserService;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AuthListService authListService;

    @Autowired
    private UserService userService;


    /* ------------------------------------- 영화관 관 련------------------------------------- */

    /**
     * 관리자 페이지
     * @return
     */
    @Secured("ROLE_SUPER")
    @GetMapping({"" , "/","/cinema/list","/cinema","/cinema/"})
    public String index() {
        log.info("/admin");
        return "/admin/index";
    }

    @Secured("ROLE_SUPER")
    @GetMapping("/cinema/insert")
    public String cinemaInsert(Model model) throws Exception {
        List<AuthList> authList = authListService.list();
        model.addAttribute("authList", authList);
        return "/admin/cinema/insert";
    }

    /* ------------------------------------- 영화관 끝------------------------------------- */

    /* ------------------------------------- 유저 리스트 관 련------------------------------------- */

    /**
     * 유저 리스트
     * @param model
     * @param page
     * @param size
     * @param search
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @GetMapping("/userManager/user/list")
    public String userList(Model model
                      ,@RequestParam(name = "page", required = false, defaultValue = "1") Integer page
                      ,@RequestParam(name = "size", required = false, defaultValue = "30") Integer size
                      ,@RequestParam(name = "search", required = false) String search) throws Exception {
        // 데이터 요청
        PageInfo<Users> pageInfo = null;
        if(search == null || search.equals("")){
            pageInfo = userService.list(page, size);
        }
        else{
            pageInfo = userService.list(page, size, search);
        }
        Pagination pagination = new Pagination();
        pagination.setPage(page);
        pagination.setSize(size);
        pagination.setTotal( pageInfo.getTotal());

        // 모델 등록
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        // 뷰 페이지 지정
        return "/admin/userManager/user/list";
    }

    /**
     * 유저 선택
     * @param model
     * @param username
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @GetMapping("/userManager/user/select")
    public String userSelect(Model model,@RequestParam("username") String username) throws Exception {
        Users user = userService.select(username);
        model.addAttribute("user", user);
        return "/admin/userManager/user/select";
    }

    /**
     * 업데이트 화면 진입
     * @param model
     * @param username
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @GetMapping("/userManager/user/update")
    public String userUpdate(Model model,@RequestParam("username") String username) throws Exception {
        Users user = userService.select(username);
        List<AuthList> authList = authListService.list();
        model.addAttribute("user", user);
        model.addAttribute("authList", authList);
        return "/admin/userManager/user/update";
    }

    /**
     * 유저 권한 삭제
     * @param model
     * @param username
     * @param no
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @GetMapping("/userManager/user/authDelete")
    public String userUpdate(Model model,@RequestParam("username") String username
    ,@RequestParam("no") int no) throws Exception {
        int result = userService.deleteAuth(no);
        // log.info(no+" 넘버!!!!!!!!!!!!!!!!!!!");
        if(result>0){
            return "redirect:/admin/userManager/user/update?username="+username;
        }
        return "redirect:/admin/userManager/user/update?username="+username+"&error";
        
    }

    /**
     * 유저 권한 추가
     * @param model
     * @param userAuth
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @PostMapping("/userManager/user/authPlus")
    public String userAuthPlus(Model model,
                              UserAuth userAuth) throws Exception {
        int result = userService.insertAuth(userAuth);
        if(result>0){
            return "redirect:/admin/userManager/user/update?username="+userAuth.getUserId();
        }
        return "redirect:/admin/userManager/user/update?username="+userAuth.getUserId()+"&error";
    }


    @Secured("ROLE_SUPER")
    @PostMapping("/userManager/user/update")
    public String userUpdate(Model model,Users user) throws Exception {
        int result = userService.update(user);
        if(result>0){
            return "redirect:/admin/userManager/user/select?username="+user.getUsername();
        }
        return "redirect:/admin/userManager/user/update?username="+user.getUsername()+"&error";
    }

    /**
     * 유저 휴먼 전환
     * @param id
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @GetMapping("/userManager/user/sleep")
    public String userSleep(@RequestParam("username") String username) throws Exception {
        Users user = userService.select(username);
        if(user.isEnabled()){
            user.setEnabled(false);
        }
        else{
            user.setEnabled(true);
        }
        int result = userService.update(user);
        if( result > 0 ) {
            return "redirect:/admin/userManager/user/list";
        }
        return "redirect:/admin/userManager/user/list?error";
    }

    /* ------------------------------------- 유저 리스트 끝------------------------------------- */

    // @Secured("ROLE_ADMIN")
    // @GetMapping("/userManager/auth/list")
    // public String authList(Model model) throws Exception {
    //     List<AuthList> authList = null;
    //     authList = authListService.list();
    //     model.addAttribute("AuthList", authList);
    //     return "/admin/userManager/auth/list";
    // }

/* ------------------------------------- 권 한 관 련------------------------------------- */

    /**
     * 권한 목록 조회 화면
     * @return
     * @throws Exception 
     */
    @Secured("ROLE_SUPER")
    @GetMapping("/userManager/auth/list")
    public String authList(Model model
                      ,@RequestParam(name = "page", required = false, defaultValue = "1") Integer page
                      ,@RequestParam(name = "size", required = false, defaultValue = "10") Integer size
                      ,@RequestParam(name = "search", required = false) String search) throws Exception {
        // 데이터 요청
        PageInfo<AuthList> pageInfo = null;
        if(search == null || search.equals("")){
            pageInfo = authListService.list(page, size);
        }
        else{
            pageInfo = authListService.list(page, size, search);
        }
        Pagination pagination = new Pagination();
        pagination.setPage(page);
        pagination.setSize(size);
        pagination.setTotal( pageInfo.getTotal());

        // 모델 등록
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        // 뷰 페이지 지정
        return "/admin/userManager/auth/list";
    }

    /**
     * 권한 생성 진입
     * @return
     */
    @Secured("ROLE_SUPER")
    @GetMapping("/userManager/auth/insert")
    public String authInsert() {

        return "/admin/userManager/auth/insert";
    }

    /**
     * 권한 생성
     * @param authList
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @PostMapping("/userManager/auth/insert")
    public String postMethodName(AuthList authList) throws Exception {
        log.info("authList : " + authList);
        int result = authListService.insert(authList);
        if( result > 0 ) {
            return "redirect:/admin/userManager/auth/list";
        }
        return "redirect:/admin/userManager/auth/list?error";
    }

    /**
     * 권한 삭제
     * @param no
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @GetMapping("/userManager/auth/delete")
    public String authDelete(@RequestParam("no") int no) throws Exception {
        int result = authListService.delete(no);
        if( result > 0 ) {
            return "redirect:/admin/userManager/auth/list";
        }
        return "redirect:/admin/userManager/auth/list?error";
    }


    /* ------------------------------------- 권 한 리스트 끝------------------------------------- */
    
    @Secured("ROLE_SUPER")
    @GetMapping("/reviewManager/list")
    public String reviewList() {
        return "/admin/reviewManager/list";
    }
    @Secured("ROLE_SUPER")
    @GetMapping("/reviewManager/auth/insert")
    public String reviewInsert() {
        return "/admin/reviewManager/insert";
    }
    
    
}
