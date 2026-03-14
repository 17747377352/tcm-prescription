package com.tcm.prescription.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.tcm.prescription.entity.Herb;
import com.tcm.prescription.repository.HerbRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class KimiService {
    
    @Value("${kimi.api-key}")
    private String apiKey;
    
    @Value("${kimi.base-url}")
    private String baseUrl;
    
    @Value("${kimi.model}")
    private String model;
    
    private final HerbRepository herbRepository;
    
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();
    
    /**
     * 调用 Kimi API 获取中药材数据
     */
    public List<Herb> fetchHerbsFromAI(String herbNames) {
        String prompt = buildHerbDataPrompt(herbNames);
        String response = callKimiAPI(prompt);
        return parseHerbData(response);
    }
    
    /**
     * 构建获取中药材数据的 Prompt
     */
    private String buildHerbDataPrompt(String herbNames) {
        return """
            请为以下中药材生成结构化数据，以JSON数组格式返回。每个药材包含以下字段：
            
            字段说明：
            - name: 药材名称
            - name_latin: 拉丁名
            - name_alias: 别名数组，如["黄芪","黄参"]
            - source: 来源(植物/动物/矿物及具体部位)
            - processing: 常见炮制规格
            - nature: 四气(寒/热/温/凉/平)
            - flavor: 五味数组，如["甘","微苦"]
            - meridian: 归经数组，如["脾","肺"]
            - direction: 升降浮沉
            - efficacy: 主要功效描述
            - indications: 主治病症
            - pharmacology: 现代药理作用
            - toxicity_level: 毒性等级(0无毒/1小毒/2有毒/3大毒)
            - dosage_min: 最小用量(克)
            - dosage_max: 最大用量(克)
            - contraindications: 禁忌人群
            - classic_formulas: 含该药材的经典方剂数组，最多5个
            
            需要获取数据的中药材：
            %s
            
            请严格按照JSON格式返回，不要添加任何其他文字说明。
            """.formatted(herbNames);
    }
    
    /**
     * 调用 Kimi API
     */
    private String callKimiAPI(String prompt) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", model);
            requestBody.put("temperature", 0.3);
            requestBody.put("max_tokens", 8000);
            
            JSONArray messages = new JSONArray();
            JSONObject systemMsg = new JSONObject();
            systemMsg.put("role", "system");
            systemMsg.put("content", "你是一位专业的中医药学专家，精通《中国药典》《中药学》《方剂学》等经典著作，能够准确提供中药材的专业数据。");
            messages.add(systemMsg);
            
            JSONObject userMsg = new JSONObject();
            userMsg.put("role", "user");
            userMsg.put("content", prompt);
            messages.add(userMsg);
            
            requestBody.put("messages", messages);
            
            Request request = new Request.Builder()
                    .url(baseUrl + "/chat/completions")
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(requestBody.toJSONString(), MediaType.parse("application/json")))
                    .build();
            
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    log.error("Kimi API调用失败: {}", response.code());
                    throw new RuntimeException("API调用失败: " + response.code());
                }
                
                String responseBody = response.body().string();
                JSONObject jsonResponse = JSON.parseObject(responseBody);
                return jsonResponse.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");
            }
        } catch (IOException e) {
            log.error("调用Kimi API异常", e);
            throw new RuntimeException("调用AI服务失败", e);
        }
    }
    
    /**
     * 解析AI返回的中药材数据
     */
    private List<Herb> parseHerbData(String content) {
        List<Herb> herbs = new ArrayList<>();
        
        try {
            // 提取JSON部分
            String jsonStr = content;
            if (content.contains("```json")) {
                jsonStr = content.substring(content.indexOf("["), content.lastIndexOf("]") + 1);
            } else if (content.contains("[")) {
                jsonStr = content.substring(content.indexOf("["), content.lastIndexOf("]") + 1);
            }
            
            JSONArray array = JSON.parseArray(jsonStr);
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Herb herb = new Herb();
                herb.setName(obj.getString("name"));
                herb.setNameLatin(obj.getString("name_latin"));
                herb.setNameAlias(obj.getString("name_alias"));
                herb.setSource(obj.getString("source"));
                herb.setProcessing(obj.getString("processing"));
                herb.setNature(obj.getString("nature"));
                herb.setFlavor(obj.getString("flavor"));
                herb.setMeridian(obj.getString("meridian"));
                herb.setDirection(obj.getString("direction"));
                herb.setEfficacy(obj.getString("efficacy"));
                herb.setIndications(obj.getString("indications"));
                herb.setPharmacology(obj.getString("pharmacology"));
                herb.setToxicityLevel(obj.getInteger("toxicity_level"));
                
                if (obj.getBigDecimal("dosage_min") != null) {
                    herb.setDosageMin(obj.getBigDecimal("dosage_min"));
                }
                if (obj.getBigDecimal("dosage_max") != null) {
                    herb.setDosageMax(obj.getBigDecimal("dosage_max"));
                }
                herb.setDosageUnit("g");
                herb.setContraindications(obj.getString("contraindications"));
                herb.setClassicFormulas(obj.getString("classic_formulas"));
                herb.setStatus(1);
                
                herbs.add(herb);
            }
        } catch (Exception e) {
            log.error("解析药材数据失败: {}", e.getMessage());
        }
        
        return herbs;
    }
    
    /**
     * 批量保存中药材数据
     */
    public int saveHerbs(List<Herb> herbs) {
        int saved = 0;
        for (Herb herb : herbs) {
            if (herbRepository.findByName(herb.getName()).isEmpty()) {
                herbRepository.save(herb);
                saved++;
                log.info("保存药材: {}", herb.getName());
            }
        }
        return saved;
    }
}