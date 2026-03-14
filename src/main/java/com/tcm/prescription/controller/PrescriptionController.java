package com.tcm.prescription.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.tcm.prescription.entity.Herb;
import com.tcm.prescription.repository.HerbRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/prescription")
@RequiredArgsConstructor
public class PrescriptionController {
    
    private final HerbRepository herbRepository;
    
    /**
     * 配伍禁忌检查
     */
    @PostMapping("/check-compatibility")
    public Map<String, Object> checkCompatibility(@RequestBody List<String> herbNames) {
        Map<String, Object> result = new HashMap<>();
        List<String> warnings = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        
        // 十八反检查规则
        Map<String, List<String>> shiBaFan = Map.of(
            "甘草", List.of("甘遂", "大戟", "芫花", "海藻"),
            "乌头", List.of("贝母", "瓜蒌", "半夏", "白蔹", "白及"),
            "藜芦", List.of("人参", "沙参", "丹参", "玄参", "苦参", "细辛", "芍药")
        );
        
        // 十九畏检查规则
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
        
        // 检查十八反
        for (String herbA : herbNames) {
            if (shiBaFan.containsKey(herbA)) {
                for (String herbB : herbNames) {
                    if (shiBaFan.get(herbA).contains(herbB)) {
                        errors.add(String.format("【十八反】%s 与 %s 相反，禁止同用", herbA, herbB));
                    }
                }
            }
        }
        
        // 检查十九畏
        for (String herbA : herbNames) {
            if (shiJiuWei.containsKey(herbA)) {
                for (String herbB : herbNames) {
                    if (shiJiuWei.get(herbA).contains(herbB)) {
                        warnings.add(String.format("【十九畏】%s 与 %s 相畏，建议避免同用", herbA, herbB));
                    }
                }
            }
        }
        
        // 检查毒性药材剂量
        for (String herbName : herbNames) {
            herbRepository.findByName(herbName).ifPresent(herb -> {
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
            });
        }
        
        result.put("code", 0);
        result.put("errors", errors);
        result.put("warnings", warnings);
        result.put("safe", errors.isEmpty());
        return result;
    }
}