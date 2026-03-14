package com.tcm.prescription.controller;

import com.tcm.prescription.entity.Herb;
import com.tcm.prescription.repository.HerbRepository;
import com.tcm.prescription.service.KimiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/herbs")
@RequiredArgsConstructor
public class HerbController {
    
    private final HerbRepository herbRepository;
    private final KimiService kimiService;
    
    /**
     * 搜索药材
     */
    @GetMapping("/search")
    public Map<String, Object> search(@RequestParam String keyword) {
        Map<String, Object> result = new HashMap<>();
        List<Herb> herbs = herbRepository.findByNameContaining(keyword);
        result.put("code", 0);
        result.put("data", herbs);
        return result;
    }
    
    /**
     * 获取药材详情
     */
    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        herbRepository.findById(id).ifPresentOrElse(
            herb -> {
                result.put("code", 0);
                result.put("data", herb);
            },
            () -> result.put("code", 404)
        );
        return result;
    }
    
    /**
     * 初始化中药材数据 - 从AI获取
     */
    @PostMapping("/init")
    public Map<String, Object> initHerbs(@RequestParam(required = false) String herbs) {
        Map<String, Object> result = new HashMap<>();
        
        // 默认初始化50种常用中药材
        String defaultHerbs = """
            人参、黄芪、白术、茯苓、甘草、当归、川芎、白芍、熟地黄、生地黄、
            麦冬、天冬、玄参、丹参、三七、何首乌、枸杞子、女贞子、墨旱莲、桑葚、
            阿胶、鹿茸、冬虫夏草、蛤蚧、紫河车、杜仲、续断、骨碎补、补骨脂、益智仁、
            菟丝子、沙苑子、锁阳、肉苁蓉、巴戟天、淫羊藿、仙茅、韭菜子、核桃仁、黑芝麻、
            麻黄、桂枝、紫苏叶、荆芥、防风、羌活、独活、白芷、细辛、藁本
            """;
        
        String herbNames = herbs != null ? herbs : defaultHerbs;
        
        try {
            List<Herb> herbList = kimiService.fetchHerbsFromAI(herbNames);
            int saved = kimiService.saveHerbs(herbList);
            
            result.put("code", 0);
            result.put("message", "成功保存 " + saved + " 种药材");
            result.put("total", herbList.size());
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "初始化失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 按功效分类获取药材
     */
    @GetMapping("/by-nature/{nature}")
    public Map<String, Object> getByNature(@PathVariable String nature) {
        Map<String, Object> result = new HashMap<>();
        List<Herb> herbs = herbRepository.findByNature(nature);
        result.put("code", 0);
        result.put("data", herbs);
        return result;
    }
}