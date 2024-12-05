package com.aloha.movie_project.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aloha.movie_project.domain.FileText;
import com.aloha.movie_project.domain.addMap;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/admin")
public class mapController {

    FileText ft = new FileText();

    /**
     * addMap 맵 이동
     * 
     * @return
     */
    @GetMapping("/addMap")
    public String addMap() {
        return "/admin/addMap";
    }

    // 맵 저장하기
    @ResponseBody
    @PostMapping("/addMap")
    public String addMap(@RequestBody addMap map) {
        map.setMapSize(map.getX() * map.getY());

        log.info("*******맵 : " + map);

        // 맵 위치 확인 로직 예시
        List<List<String>> mapData = map.getMapData();
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

        String path = "C:\\upload\\test"; // 파일 저장 경로
        String fileName = "test.txt"; // 파일 이름

        // text 파일로 저장
        ft.write(path, fileName, result);

        return "SUCCESS";
    }

    // 불러오기
    @PutMapping("/addMap")
    public ResponseEntity<Map<String, Object>> readMap() {
        String path = "C:\\upload\\test"; // 파일 저장 경로
        String fileName = "test.txt"; // 파일 이름

        // 파일 읽기
        String result = ft.read(path, fileName);
        System.out.println(result);

        // String을 List<List<String>>으로 변환
        List<List<String>> mapData = new ArrayList<>();
        String[] rows = result.split("\n");
        for (String row : rows) {
            List<String> rowList = Arrays.asList(row.split(","));
            mapData.add(rowList);
        }

        // 응답 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("readMapData", mapData);

        return ResponseEntity.ok(response); // JSON 데이터 반환
    }

}
