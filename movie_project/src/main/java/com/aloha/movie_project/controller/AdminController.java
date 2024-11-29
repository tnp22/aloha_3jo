package com.aloha.movie_project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aloha.movie_project.domain.AuthList;
import com.aloha.movie_project.service.AuthListService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AuthListService authListService;

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


    @GetMapping("/userManager/user/list")
    public String userList() {
        return "/admin/userManager/user/list";
    }

    @GetMapping("/userManager/user/insert")
    public String userInsert() {
        return "/admin/userManager/user/insert";
    }



    @Secured("ROLE_ADMIN")
    @GetMapping("/userManager/auth/list")
    public String authList(Model model) throws Exception {
        List<AuthList> authList = null;
        authList = authListService.list();
        model.addAttribute("AuthList", authList);
        return "/admin/userManager/auth/list";
    }

    @GetMapping("/userManager/auth/insert")
    public String authInsert() {



        return "/admin/userManager/auth/insert";
    }

    @GetMapping("/reviewManager/list")
    public String reviewList() {
        return "/admin/reviewManager/list";
    }

    @GetMapping("/reviewManager/auth/insert")
    public String reviewInsert() {
        return "/admin/reviewManager/insert";
    }
    
    
}
