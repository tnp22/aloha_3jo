package com.aloha.movie_project.controller;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.aloha.movie_project.domain.AuthList;
import com.aloha.movie_project.domain.Cinema;
import com.aloha.movie_project.domain.FileText;
import com.aloha.movie_project.domain.Files;
import com.aloha.movie_project.domain.Movie;
import com.aloha.movie_project.domain.Pagination;
import com.aloha.movie_project.domain.Theater;
import com.aloha.movie_project.domain.TheaterList;
import com.aloha.movie_project.domain.UserAuth;
import com.aloha.movie_project.domain.Users;
import com.aloha.movie_project.service.AuthListService;
import com.aloha.movie_project.service.FileService;
import com.aloha.movie_project.service.MovieService;
import com.aloha.movie_project.service.UserService;
import com.aloha.movie_project.service.cinema.CinemaService;
import com.aloha.movie_project.service.cinema.TheaterListService;
import com.aloha.movie_project.service.cinema.TheaterService;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AuthListService authListService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @Autowired
    private CinemaService cinemaService;

    @Autowired
    private TheaterService theaterService;

    @Autowired
    private TheaterListService theaterListService;


    /* ------------------------------------- 영화관 관 련------------------------------------- */

    /**
     * 관리자 페이지
     * @return
     */
    @GetMapping({"" , "/","/cinema/list","/cinema","/cinema/"})
    public String index(Model model
    ,@RequestParam(name = "page", required = false, defaultValue = "1") Integer page
    ,@RequestParam(name = "size", required = false, defaultValue = "6") Integer size
    ,@RequestParam(name = "search", required = false) String search) throws Exception {
        // 데이터 요청
        PageInfo<Cinema> pageInfo = null;
        if(search == null || search.equals("")){
            pageInfo = cinemaService.list(page, size);
        }
        else{
            pageInfo = cinemaService.list(page, size, search);
        }
        // 모델 등록
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("search", search);
        return "/admin/index";
    }

    /**
     * 시네마 생성 진입
     * @param model
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @GetMapping("/cinema/insert")
    public String cinemaInsert(Model model) throws Exception {
        List<AuthList> authList = authListService.list();
        model.addAttribute("authList", authList);
        return "/admin/cinema/insert";
    }

    /**
     * 시네마 생성
     * @param model
     * @param userAuth
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @PostMapping("/cinema/insert")
    public String cinemaInsert(Model model,
                              Cinema cinema) throws Exception {
        int result = cinemaService.insert(cinema);
        if(result>0){
            for (MultipartFile files : cinema.getMainFiles()) {
                Files file = new Files();
                file.setFile(files);
                file.setDivision("main");
                file.setFkTable("cinema");
                file.setFkId(cinema.getId());
                fileService.upload(file);
            }

            return "redirect:/admin/cinema/list";
        }
        return "redirect:/admin/cinema/list&error";
    }

    /**
     * 관리자 페이지
     * @return
     */
    @Secured("ROLE_SUPER")
    @GetMapping("/cinema/updateList")
    public String cinemaUpdateList(Model model
    ,@RequestParam(name = "page", required = false, defaultValue = "1") Integer page
    ,@RequestParam(name = "size", required = false, defaultValue = "6") Integer size
    ,@RequestParam(name = "search", required = false) String search) throws Exception {
        // 데이터 요청
        PageInfo<Cinema> pageInfo = null;
        if(search == null || search.equals("")){
            pageInfo = cinemaService.list(page, size);
        }
        else{
            pageInfo = cinemaService.list(page, size, search);
        }
        // 모델 등록
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("search", search);
        return "/admin/cinema/updateList";
    }

    /**
     * 영화관 선택
     * @param model
     * @param username
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @GetMapping("/cinema/select")
    public String cinemaSelect(Model model,@RequestParam("id") String id) throws Exception {
        Cinema cinema = cinemaService.select(id);
        model.addAttribute("cinema", cinema);
        return "/admin/cinema/select";
    }

    /**
     * 업데이트 화면 진입
     * @param model
     * @param movie
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @GetMapping("/cinema/update")
    public String cinemaUpdate(Model model,@RequestParam("id") String id) throws Exception {
        List<AuthList> authList = authListService.list();
        model.addAttribute("authList", authList);
        Cinema cinema = cinemaService.select(id);
        model.addAttribute("cinema", cinema);
        return "/admin/cinema/update";
    }


    /**
     * 업데이트 post
     * @param model
     * @param movie
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @PostMapping("/cinema/update")
    public String cinemaUpdate(Model model,Cinema cinema) throws Exception {
        int result = cinemaService.update(cinema);
        if(result>0){
            return "redirect:/admin/cinema/select?id="+cinema.getId();
        }
        return "redirect:/admin/cinema/update?id="+cinema.getId()+"&error";
    }


    /**
     * 풍경 삭제
     * @param model
     * @param username
     * @param no
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @GetMapping("/cinema/mainDelete")
    public String cinemaMainDelete(Model model,@RequestParam("mainId") String mainId
    ,@RequestParam("id") String id) throws Exception {
        // log.info(stilcutId+" 넘버!!!!!!!!!!!!!!!!!!!");
        int result = fileService.delete(mainId);
        if(result>0){
            return "redirect:/admin/cinema/update?id="+id;
        }
        return "redirect:/admin/cinema/update?id="+id+"&error";
    }

    /**
     * 메인이미지 변경
     * @param model
     * @param userAuth
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @PostMapping("/cinema/mainPlus")
    public String mainPlus(Model model,
                              Cinema cinema) throws Exception {
        // log.info(cinema.toString());
        fileService.delete(cinema.getFileId());
        for (MultipartFile files : cinema.getMainFiles()) {
            Files file = new Files();
            file.setFile(files);
            file.setDivision("main");
            file.setFkTable("cinema");
            file.setFkId(cinema.getId());
            fileService.upload(file);
        }
        return "redirect:/admin/cinema/update?id="+cinema.getId();
    }


    /* ------------------------------------- 영화관 끝------------------------------------- */


    /* ------------------------------------- 시어터 시작------------------------------------- */


    /**
     * 개별 영화관 진입
     * @return
     */
    //해당 아이디 권한 추가 요망
    @PreAuthorize("(hasRole('SUPER')) or ( #p1 != null and @TheaterService.isOwner(#p1,authentication.principal.user.authList))")
    @GetMapping("/cinema/enter")
    public String cinemaEnter(Model model, @RequestParam("id") String id
    ,@RequestParam(name = "page", required = false, defaultValue = "1") Integer page
    ,@RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) throws Exception {
        // 데이터 요청
        PageInfo<Theater> pageInfo = null;

        pageInfo = theaterService.list(page, size, id);
        
        // 모델 등록
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("cinema", cinemaService.select(id));
        model.addAttribute("search", id);
        return "/admin/theater/list";
    }

    /**
     * 상영관 생성 진입
     * @param model
     * @return
     * @throws Exception
     */
    @PreAuthorize("(hasRole('SUPER')) or ( #p1 != null and @TheaterService.isOwner(#p1,authentication.principal.user.authList))")
    @GetMapping("/theater/insert")
    public String theaterInsert(Model model,@RequestParam("id") String id,
                    @RequestParam(name = "fileName", required = false) String fileName) throws Exception {
        model.addAttribute("cinema", cinemaService.select(id));
        model.addAttribute("UUID", UUID.randomUUID().toString());
        model.addAttribute("fileName", fileName);
        return "/admin/theater/insert";
    }


    FileText ft = new FileText();

    /**
     * 시네마 생성
     * @param model
     * @param userAuth
     * @return
     * @throws Exception
     */
    @PreAuthorize("(hasRole('SUPER')) or ( #p1 != null and @TheaterService.isOwner(#p1,authentication.principal.user.authList))")
    @ResponseBody
    @PostMapping("/theater/insert")
    public String theaterInsert(Model model,
                              @RequestBody Theater theater) throws Exception {
        theater.setMap(theater.getId());
        theater.setMapSize(theater.getX() * theater.getY());

        log.info("*******맵 : " + theater);

        // 맵 위치 확인 로직 예시
        List<List<String>> mapData = theater.getMapData();
        // log.info("맵 위치 3.0 : " + mapData.get(3).get(0)); //출력결과 기본값 D_1 (4번째줄 첫번째값)

        // 2차원 리스트를 문자열로 변환
        StringBuilder sb = new StringBuilder();
        for (List<String> row : mapData) {
            if (sb.length() > 0) {
                sb.append("\n"); // 행 구분자 추가
            }
            sb.append(String.join(",", row)); // 내부 리스트를 문자열로 변환
        }
        String result = sb.toString();

        int count = 0;
        for (List<String> row : mapData) {
            for (String row1 : row) {
                if(row1.equals("null") || row1.equals("통로")){
                    count++;
                }
            }
        }
        
        int seat = theater.getMapSize() - count;
        theater.setSeat(seat);


        int rs = theaterService.insert(theater);

        /*****----------------------------------------------- */
        

        String path = "C:\\upload\\test"; // 파일 저장 경로
        String fileName = theater.getId();
        String fileName_1 = fileName+".txt"; // 파일 이름

        // text 파일로 저장
        ft.write(path, fileName_1, result);


    /*****----------------------------------------------- */

        if(rs>0){
            return "SUCCESS";
        }
        return "FAIL";
    }
































    /* ------------------------------------- 시어터 끝------------------------------------- */


    /* --------------------------------------상영 리스트 -------------------------------- */


    /**
     * 상영 리스트 진입
     * @return
     */
    //해당 아이디 권한 추가 요망
    @PreAuthorize("(hasRole('SUPER')) or ( #p1 != null and @TheaterService.isOwner(#p1,authentication.principal.user.authList))")
    @GetMapping("/theaterList/list")
    public String theaterListList(Model model, @RequestParam("id") String id
    ,@RequestParam(name = "search", required = false) String search
    ,@RequestParam(name = "page", required = false, defaultValue = "1") Integer page
    ,@RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) throws Exception {
        // 데이터 요청
        PageInfo<TheaterList> pageInfo = null;

        if(search == null || search.equals("")){
            pageInfo = theaterListService.list(page, size, id);
        }
        else
        {
            pageInfo = theaterListService.list(page, size, id, search);
            model.addAttribute("search", search);
        }
        
        
        // 모델 등록
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("cinema", cinemaService.select(id));
        model.addAttribute("id", id);
        return "/admin/theaterList/list";
    }


    /**
     * 상영 리스트 생성 진입
     * @return
     */
    //해당 아이디 권한 추가 요망
    @PreAuthorize("(hasRole('SUPER')) or ( #p1 != null and @TheaterService.isOwner(#p1,authentication.principal.user.authList))")
    @GetMapping("/theaterList/insert")
    public String theaterListInsert(Model model, @RequestParam("id") String id
    ,@RequestParam(name = "search", required = false) String search
    ) throws Exception {

        List<Theater> theaterLists = theaterService.list(id);
        List<Movie> movieList = null;
        if (search == null || search.isEmpty()) {
             movieList = movieService.list();
        }
        else{
            movieList = movieService.list(search);
            model.addAttribute("search", search);
        }
        
        // 모델 등록
        model.addAttribute("theaterLists", theaterLists);
        model.addAttribute("pageInfo", movieList);
        model.addAttribute("cinema", cinemaService.select(id));
        model.addAttribute("id", id);
        return "/admin/theaterList/insert";
    }

    /**
     * 상영 리스트  생성
     * @param model
     * @param userAuth
     * @return
     * @throws Exception
     */
    @PreAuthorize("(hasRole('SUPER')) or ( #p1 != null and @TheaterService.isOwner(#p1,authentication.principal.user.authList))")
    @PostMapping("/theaterList/insert")
    public String theaterListInsert(Model model, @RequestParam("cinemaId") String id,
                            TheaterList theaterList) throws Exception {
        int result = theaterListService.insert(theaterList);
        model.addAttribute("cinema", cinemaService.select(id));
        model.addAttribute("id", id);
        if(result>0){
            return "redirect:/admin/theaterList/list?id="+id;
        }
        return "redirect:/admin/theaterList/list?id=id&error";
    }

    /**
     * 상영 리스트 조회
     * @param model
     * @param userAuth
     * @return
     * @throws Exception
     */
    @PreAuthorize("(hasRole('SUPER')) or ( #p1 != null and @TheaterService.isOwner(#p1,authentication.principal.user.authList))")
    @GetMapping("/theaterList/select")
    public String theaterListSelect(Model model, @RequestParam("id") String id,
                    @RequestParam("theaterListId") String theaterListId) throws Exception {
        TheaterList theaterList = theaterListService.select(theaterListId);

        model.addAttribute("theaterList", theaterList);
        model.addAttribute("cinema", cinemaService.select(id));
        model.addAttribute("id", id);
        
        return "/admin/theaterList/Select";
    }

    /**
     * 상영 리스트 업데이트 진입
     * @param model
     * @param userAuth
     * @return
     * @throws Exception
     */
    @PreAuthorize("(hasRole('SUPER')) or ( #p1 != null and @TheaterService.isOwner(#p1,authentication.principal.user.authList))")
    @GetMapping("/theaterList/update")
    public String theaterListUpdate(Model model, @RequestParam("id") String id,
                    @RequestParam("theaterListId") String theaterListId) throws Exception {
        TheaterList theaterList = theaterListService.select(theaterListId);

        model.addAttribute("theaterList", theaterList);
        model.addAttribute("cinema", cinemaService.select(id));
        model.addAttribute("id", id);
        
        return "/admin/theaterList/Update";
    }










    /* --------------------------------------상영 리스트 끝 -------------------------------- */







    /* ------------------------------------- 영화 관련------------------------------------- */

    /**
     * 영화 리스트
     * @param model
     * @param page
     * @param size
     * @param search
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @GetMapping("/movie/list")
    public String movieList(Model model
                      ,@RequestParam(name = "page", required = false, defaultValue = "1") Integer page
                      ,@RequestParam(name = "size", required = false, defaultValue = "6") Integer size
                      ,@RequestParam(name = "search", required = false) String search) throws Exception {
        // 데이터 요청
        PageInfo<Movie> pageInfo = null;
        if(search == null || search.equals("")){
            pageInfo = movieService.list(page, size);
        }
        else{
            pageInfo = movieService.list(page, size, search);
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
        return "/admin/movie/list";
    }


    /**
     * 영화 선택
     * @param model
     * @param username
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @GetMapping("/movie/select")
    public String movieSelect(Model model,@RequestParam("id") String id) throws Exception {
        Movie movie = movieService.select(id);
        // log.info(movie.toString());
        model.addAttribute("movie", movie);
        return "/admin/movie/select";
    }

    /**
     * 영화 생성
     * @param model
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @GetMapping("/movie/insert")
    public String movieInsert(Model model) throws Exception {

        return "/admin/movie/insert";
    }

    /**
     * 영화 생성
     * @param model
     * @param userAuth
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @PostMapping("/movie/insert")
    public String movieInsert(Model model,
                              Movie movie) throws Exception {
        int result = movieService.insert(movie);
        if(result>0){
            for (MultipartFile files : movie.getMainFiles()) {
                Files file = new Files();
                file.setFile(files);
                file.setDivision("main");
                file.setFkTable("movie");
                file.setFkId(movie.getId());
    
                fileService.upload(file);
            }

            for (MultipartFile stilcut : movie.getStilcuts()) {
                Files stilfile = new Files();
                stilfile.setFile(stilcut);
                stilfile.setDivision("stilcut");
                stilfile.setFkTable("movie");
                stilfile.setFkId(movie.getId());
                fileService.upload(stilfile);
            }
            return "redirect:/admin/movie/list";
        }
        return "redirect:/admin/movie/list&error";
    }


    /**
     * 업데이트 화면 진입
     * @param model
     * @param movie
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @GetMapping("/movie/update")
    public String movieUpdate(Model model,@RequestParam("id") String id) throws Exception {
        Movie movie = movieService.select(id);
        model.addAttribute("movie", movie);
        return "/admin/movie/update";
    }

    /**
     * 업데이트 post
     * @param model
     * @param movie
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @PostMapping("/movie/update")
    public String movieUpdate(Model model,Movie movie) throws Exception {
        int result = movieService.update(movie);
        if(result>0){
            return "redirect:/admin/movie/select?id="+movie.getId();
        }
        return "redirect:/admin/movie/update?id="+movie.getId()+"&error";
    }


    /**
     * 스틸컷 삭제
     * @param model
     * @param username
     * @param no
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @GetMapping("/movie/stilcutDelete")
    public String stilcutDelete(Model model,@RequestParam("stilcutId") String stilcutId
    ,@RequestParam("id") String id) throws Exception {
        // log.info(stilcutId+" 넘버!!!!!!!!!!!!!!!!!!!");
        int result = fileService.delete(stilcutId);
        if(result>0){
            return "redirect:/admin/movie/update?id="+id;
        }
        return "redirect:/admin/movie/update?id="+id+"&error";
        
    }

    /**
     * 스틸컷 추가
     * @param model
     * @param userAuth
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @PostMapping("/movie/stilcutPlus")
    public String stilcutPlus(Model model,
                              Movie movie) throws Exception {
        log.info(movie.toString());
        for (MultipartFile stilcut : movie.getStilcuts()) {
            Files stilfile = new Files();
            stilfile.setFile(stilcut);
            stilfile.setDivision("stilcut");
            stilfile.setFkTable("movie");
            stilfile.setFkId(movie.getId());
            fileService.upload(stilfile);
        }
        return "redirect:/admin/movie/update?id="+movie.getId();
    }

    /**
     * 메인이미지 변경
     * @param model
     * @param userAuth
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @PostMapping("/movie/mainPlus")
    public String mainPlus(Model model,
                              Movie movie) throws Exception {
        log.info(movie.toString());
        int result = fileService.delete(movie.getFileId());
        if(result>0){
            for (MultipartFile files : movie.getMainFiles()) {
                Files file = new Files();
                file.setFile(files);
                file.setDivision("main");
                file.setFkTable("movie");
                file.setFkId(movie.getId());
                fileService.upload(file);
            }
        }
        return "redirect:/admin/movie/update?id="+movie.getId();
    }


    /* ------------------------------------- 영화 끝------------------------------------- */


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
     * 업데이트 post
     * @param model
     * @param user
     * @return
     * @throws Exception
     */
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
     * 유저 권한 삭제
     * @param model
     * @param username
     * @param no
     * @return
     * @throws Exception
     */
    @Secured("ROLE_SUPER")
    @GetMapping("/userManager/user/authDelete")
    public String userAuthDelete(Model model,@RequestParam("username") String username
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
