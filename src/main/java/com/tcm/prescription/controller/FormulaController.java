package com.tcm.prescription.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/formulas")
@RequiredArgsConstructor
public class FormulaController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(defaultValue = "") String keyword) {
        String sql = "select id, name, source, principle, indication, status, created_at from tcm_formula where (? = '' or name like concat('%', ?, '%')) order by id desc limit 200";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, keyword, keyword);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", rows);
        return result;
    }
}
