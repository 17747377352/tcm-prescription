package com.tcm.prescription.controller;

import com.tcm.prescription.entity.Herb;
import com.tcm.prescription.repository.HerbRepository;
import com.tcm.prescription.service.KimiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/herbs")
@RequiredArgsConstructor
public class HerbController {

    private final HerbRepository herbRepository;
    private final KimiService kimiService;

    @GetMapping("/search")
    public Map<String, Object> search(@RequestParam(defaultValue = "") String keyword,
                                      @RequestParam(required = false) String nature) {
        Map<String, Object> result = new HashMap<>();
        List<Herb> herbs;

        if (nature != null && !nature.isBlank()) {
            herbs = herbRepository.findByNature(nature);
            if (keyword != null && !keyword.isBlank()) {
                herbs = herbs.stream().filter(h -> h.getName() != null && h.getName().contains(keyword)).toList();
            }
        } else {
            herbs = herbRepository.findByNameContaining(keyword == null ? "" : keyword);
        }

        result.put("code", 0);
        result.put("data", herbs);
        result.put("total", herbs.size());
        return result;
    }

    @GetMapping("/list")
    public Map<String, Object> list() {
        Map<String, Object> result = new HashMap<>();
        List<Herb> herbs = herbRepository.findAll();
        result.put("code", 0);
        result.put("data", herbs);
        result.put("total", herbs.size());
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        herbRepository.findById(id).ifPresentOrElse(
                herb -> {
                    result.put("code", 0);
                    result.put("data", herb);
                },
                () -> {
                    result.put("code", 404);
                    result.put("message", "药材不存在");
                }
        );
        return result;
    }

    @PostMapping("/init")
    public Map<String, Object> initHerbs(@RequestParam(required = false) String herbs) {
        Map<String, Object> result = new HashMap<>();

        String defaultHerbs = """
            人参、黄芪、白术、茯苓、甘草、当归、川芎、白芍、熟地黄、生地黄、
            麦冬、天冬、玄参、丹参、三七、何首乌、枸杞子、女贞子、墨旱莲、桑葚、
            阿胶、鹿茸、冬虫夏草、蛤蚧、紫河车、杜仲、续断、骨碎补、补骨脂、益智仁、
            菟丝子、沙苑子、锁阳、肉苁蓉、巴戟天、淫羊藿、仙茅、韭菜子、核桃仁、黑芝麻、
            麻黄、桂枝、紫苏叶、荆芥、防风、羌活、独活、白芷、细辛、藁本
            """;

        String herbNames = herbs != null ? herbs : defaultHerbs;

        try {
            List<String> names = splitHerbNames(herbNames);
            int saved = 0;
            int total = 0;
            for (int i = 0; i < names.size(); i += 10) {
                List<String> batch = names.subList(i, Math.min(i + 10, names.size()));
                List<Herb> herbList = kimiService.fetchHerbsFromAI(String.join("、", batch));
                saved += kimiService.saveHerbs(herbList);
                total += herbList.size();
            }
            result.put("code", 0);
            result.put("message", "成功保存 " + saved + " 种药材");
            result.put("total", total);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "初始化失败: " + e.getMessage());
        }

        return result;
    }

    @GetMapping("/by-nature/{nature}")
    public Map<String, Object> getByNature(@PathVariable String nature) {
        Map<String, Object> result = new HashMap<>();
        List<Herb> herbs = herbRepository.findByNature(nature);
        result.put("code", 0);
        result.put("data", herbs);
        result.put("total", herbs.size());
        return result;
    }

    private List<String> splitHerbNames(String herbNames) {
        String normalized = herbNames.replace("\n", "、").replace(",", "、").replace("，", "、");
        List<String> list = new ArrayList<>();
        for (String item : normalized.split("、")) {
            String name = item.trim();
            if (!name.isEmpty()) list.add(name);
        }
        return list;
    }
}
