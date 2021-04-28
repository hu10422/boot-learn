package com.example.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.common.BizException;
import com.example.common.TouTiaoTypeEnum;
import com.example.model.Toutiao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TouTiaoService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${juhe.key}")
    private String juheKey;


    private String INDEX_URL = "http://v.juhe.cn/toutiao/index?type={type}&key={key}&page={page}&page_size={page_size}";

    private String CONTENT_URL = "http://v.juhe.cn/toutiao/content?key={key}&uniquekey={uniquekey}";


    public Boolean index() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", juheKey);
        map.put("type", TouTiaoTypeEnum.TOP.getType());
        map.put("page", 1);
        map.put("page_size", 30);

        JSONObject forObject = restTemplate.getForObject(INDEX_URL, JSONObject.class, map);
        if (forObject == null) {
            throw new BizException("请求异常");
        }
        log.debug("req_url={},forObject={}", INDEX_URL, forObject.toJSONString());

        Integer error_code = forObject.getInteger("error_code");
        if (0 != error_code) {
            String reason = forObject.getString("reason");
            log.error("获取图书目录异常：msg={}", reason);
            throw new BizException("请求异常:" + reason);
        }

        JSONArray result = forObject.getJSONObject("result").getJSONArray("data");
        if (CollectionUtils.isEmpty(result)) {
            return true;
        }


        List<Toutiao> list = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            JSONObject source = result.getJSONObject(i);
            Toutiao toutiao = JSONObject.parseObject(source.toJSONString(), Toutiao.class);//source.toJavaObject(Toutiao.class);
            list.add(toutiao);
        }

        mongoTemplate.insertAll(list);
        return true;
    }


    public Boolean content() {
        Query query = new Query(Criteria.where("date").gt(LocalDate.now().toString()));
        List<Toutiao> toutiaos = mongoTemplate.find(query, Toutiao.class);

        System.err.println("-------------------------------------------------");
        System.err.println(LocalDate.now().toString());
        System.err.println(toutiaos.size());
        for (Toutiao toutiao : toutiaos) {
            System.err.println(JSONObject.toJSONString(toutiao));
        }

        return false;
    }


}
