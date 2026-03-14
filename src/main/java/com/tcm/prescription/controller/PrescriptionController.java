package com.tcm.prescription.controller;

import com.tcm.prescription.entity.Herb;
import com.tcm.prescription.repository.HerbRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/prescription")
@RequiredArgsConstructor
public class PrescriptionController {

    private final HerbRepository herbRepository;
    private final JdbcTemplate jdbcTemplate;

    @PostMapping("/check-compatibility")
    public Map<String, Object> checkCompatibility(@RequestBody Map<String, Object> payload) {
        List<Map<String, Object>> herbItems = (List<Map<String, Object>>) payload.getOrDefault("herbs", Collections.emptyList());
        List<String> herbNames = herbItems.stream().map(item -> String.valueOf(item.get("name"))).toList();

        Map<String, Object> result = new HashMap<>();
        List<String> warnings = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        Map<String, List<String>> shiBaFan = Map.of(
                "甘草", List.of("甘遂", "大戟", "芫花", "海藻"),
                "乌头", List.of("贝母", "瓜蒌", "半夏", "白蔹", "白及"),
                "藜芦", List.of("人参", "沙参", "丹参", "玄参", "苦参", "细辛", "芍药")
        );

        Map<String, List<String>> shiJiuWei = Map.of(
                "硫黄", List.of("朴硝"),
                "水银", List.of("砒霜"),
                "狼毒", List.of("密陀僧"),
                "巴豆", List.of("牵牛"),
                "丁香", List.of("郁金"),
                "川乌", List.of("犀角"),
                "草乌", List.of("犀角"),
                "牙硝", List.of("三棱"),
                "官桂", List.of("石脂"),
                "人参", List.of("五灵脂")
        );

        for (String herbA : herbNames) {
            if (shiBaFan.containsKey(herbA)) {
                for (String herbB : herbNames) {
                    if (!Objects.equals(herbA, herbB) && shiBaFan.get(herbA).contains(herbB)) {
                        errors.add(String.format("【十八反】%s 与 %s 相反，禁止同用", herbA, herbB));
                    }
                }
            }
        }

        for (String herbA : herbNames) {
            if (shiJiuWei.containsKey(herbA)) {
                for (String herbB : herbNames) {
                    if (!Objects.equals(herbA, herbB) && shiJiuWei.get(herbA).contains(herbB)) {
                        warnings.add(String.format("【十九畏】%s 与 %s 相畏，建议避免同用", herbA, herbB));
                    }
                }
            }
        }

        for (Map<String, Object> item : herbItems) {
            String herbName = String.valueOf(item.get("name"));
            BigDecimal dosage = new BigDecimal(String.valueOf(item.getOrDefault("dosage", "0")));
            herbRepository.findByName(herbName).ifPresent(herb -> appendDosageWarnings(warnings, herbName, dosage, herb));
        }

        Map<String, Object> data = new HashMap<>();
        data.put("safe", errors.isEmpty());
        data.put("errors", errors);
        data.put("warnings", warnings);
        data.put("score", Math.max(0, 100 - errors.size() * 30 - warnings.size() * 8));

        result.put("code", 0);
        result.put("data", data);
        return result;
    }

    @PostMapping("/save-demo")
    public Map<String, Object> saveDemoPrescription(@RequestBody Map<String, Object> payload) {
        String prescriptionNo = "RX" + System.currentTimeMillis();
        String type = String.valueOf(payload.getOrDefault("type", "手动抓配"));
        String composition = String.valueOf(payload.getOrDefault("composition", "[]"));

        jdbcTemplate.update("insert into tcm_prescription(user_id, prescription_no, type, composition, total_doses, usage, precautions, review_status, status) values (?,?,?,?,?,?,?,?,?)",
                1L, prescriptionNo, "AI开方".equals(type) ? 1 : 2, composition, 7, "水煎服，每日1剂", "遵医嘱", 1, 1);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("message", "保存成功");
        result.put("data", Map.of("prescriptionNo", prescriptionNo));
        return result;
    }

    @GetMapping("/history")
    public Map<String, Object> history() {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("select id, prescription_no, type, composition, total_doses, usage, created_at from tcm_prescription order by id desc limit 100");
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", rows);
        return result;
    }

    private void appendDosageWarnings(List<String> warnings, String herbName, BigDecimal dosage, Herb herb) {
        if (herb.getToxicityLevel() != null && herb.getToxicityLevel() > 0) {
            String level = switch (herb.getToxicityLevel()) {
                case 1 -> "小毒";
                case 2 -> "有毒";
                case 3 -> "大毒";
                default -> "无毒";
            };
            warnings.add(String.format("【毒性提示】%s 为%s药材，用量需严格控制(%s-%sg)",
                    herbName, level, herb.getDosageMin(), herb.getDosageMax()));
        }
        if (herb.getDosageMax() != null && dosage.compareTo(herb.getDosageMax()) > 0) {
            warnings.add(String.format("【超量提示】%s 当前剂量%sg，超过推荐上限%sg", herbName, dosage, herb.getDosageMax()));
        }
        if (herb.getDosageMin() != null && dosage.compareTo(herb.getDosageMin()) < 0) {
            warnings.add(String.format("【低量提示】%s 当前剂量%sg，低于常用下限%sg", herbName, dosage, herb.getDosageMin()));
        }
    }
}
