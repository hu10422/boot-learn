package com.example.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.common.BizException;
import com.example.model.Catalog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class GoodBookService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${juhe.key}")
    private String juheKey;

    private String CATALOG_URL = "http://apis.juhe.cn/goodbook/catalog?key={key}&dtype=json";
    private String QUERY_URL = "http://apis.juhe.cn/goodbook/query?key={key}&catalog_id=246&rn=10&rn=10";

    public Boolean catalog() {
        Map<String, String> map = new HashMap<>();
        map.put("key", juheKey);
        JSONObject forObject = restTemplate.getForObject(CATALOG_URL, JSONObject.class, map);
        if (forObject == null) {
            throw new BizException("请求异常");
        }
        log.info("req_url={},forObject={}", CATALOG_URL, forObject.toJSONString());

        Integer resultCode = forObject.getInteger("resultcode");
        if (200 != resultCode) {
            String reason = forObject.getString("reason");
            log.error("获取图书目录异常：msg={}", reason);
            throw new BizException("请求异常:" + reason);
        }

        JSONArray result = forObject.getJSONArray("result");
        if (CollectionUtils.isEmpty(result)){
            return true;
        }

        List<Catalog> list = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            JSONObject jsonObject = result.getJSONObject(i);
            Catalog catalog = jsonObject.toJavaObject(Catalog.class);
            list.add(catalog);
        }
        mongoTemplate.insertAll(list);
        return true;
    }

//    public Boolean query() {
////        mongoTemplate.
//
//    }

}
