package com.tcm.prescription.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/system")
@RequiredArgsConstructor
public class SystemController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/overview")
    public Map<String, Object> overview() {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        data.put("herbs", queryCount("select count(*) from tcm_herb"));
        data.put("formulas", queryCount("select count(*) from tcm_formula"));
        data.put("prescriptions", queryCount("select count(*) from tcm_prescription"));
        data.put("consultations", queryCount("select count(*) from tcm_consultation"));
        data.put("users", queryCount("select count(*) from tcm_user"));

        result.put("code", 0);
        result.put("data", data);
        return result;
    }

    private long queryCount(String sql) {
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count == null ? 0L : count;
    }
}
