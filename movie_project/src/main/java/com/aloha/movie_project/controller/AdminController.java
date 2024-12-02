package com.aloha.movie_project.controller;

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

    /**
     * 관리자 페이지
     * @return
     */
    @GetMapping({"" , "/","/cinema/list","/cinema","/cinema/"})
    public String index() {
        log.info("/admin");
        return "/admin/index";
    }
    @GetMapping("/cinema/insert")
    public String cinemaInsert() {
        return "/admin/cinema/insert";
    }

    @Secured("ROLE_SUPER")
    @GetMapping("/userManager/user/list")
    public String userList(Model model
                      ,@RequestParam(name = "page", required = false, defaultValue = "1") Integer page
                      ,@RequestParam(name = "size", required = false, defaultValue = "10") Integer size
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

    @Secured("ROLE_SUPER")
    @GetMapping("/userManager/user/insert")
    public String userInsert() {
        return "/admin/userManager/user/insert";
    }

    @Secured("ROLE_SUPER")
    @GetMapping("/userManager/user/select")
    public String userSelect(Model model,@RequestParam("username") String username) throws Exception {
        Users user = userService.select(username);
        model.addAttribute("user", user);
        return "/admin/userManager/user/select";
    }

    @Secured("ROLE_SUPER")
    @GetMapping("/userManager/user/sleep")
    public String userSleep(@RequestParam("id") String id) throws Exception {
        //int result = userService.sleep(no);
        int result = 0;
        if( result > 0 ) {
            return "redirect:/admin/userManager/user/list";
        }
        return "redirect:/admin/userManager/user/list?error";
    }


    // @Secured("ROLE_ADMIN")
    // @GetMapping("/userManager/auth/list")
    // public String authList(Model model) throws Exception {
    //     List<AuthList> authList = null;
    //     authList = authListService.list();
    //     model.addAttribute("AuthList", authList);
    //     return "/admin/userManager/auth/list";
    // }

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

    @Secured("ROLE_SUPER")
    @GetMapping("/userManager/auth/insert")
    public String authInsert() {

        return "/admin/userManager/auth/insert";
    }

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
    @Secured("ROLE_SUPER")
    @GetMapping("/userManager/auth/delete")
    public String authDelete(@RequestParam("no") int no) throws Exception {
        int result = authListService.delete(no);
        if( result > 0 ) {
            return "redirect:/admin/userManager/auth/list";
        }
        return "redirect:/admin/userManager/auth/list?error";
    }
    
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
