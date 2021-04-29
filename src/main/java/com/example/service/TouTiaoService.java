package com.example.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.common.BizException;
import com.example.common.TouTiaoTypeEnum;
import com.example.model.Toutiao;
import com.example.model.ToutiaoContent;
import com.example.repository.TouTiaoContentRepository;
import com.example.repository.TouTiaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class TouTiaoService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TouTiaoRepository touTiaoRepository;
    @Autowired
    private TouTiaoContentRepository touTiaoContentRepository;

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


        Date date = new Date();
        List<Toutiao> list = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            JSONObject source = result.getJSONObject(i);
            Toutiao toutiao = JSONObject.parseObject(source.toJSONString(), Toutiao.class);
            toutiao.setCreateTime(date);
            toutiao.setUpdateTime(date);
            toutiao.setStatus(1);
            list.add(toutiao);
        }

//        mongoTemplate.insertAll(list);
        touTiaoRepository.saveAll(list);
        return true;
    }


    @Transactional(noRollbackFor = BizException.class)
    public Boolean content() {
        Query query = null;
        try {
            query = new Query(Criteria.where("createTime").gt(DateUtils.parseDate(LocalDate.now().toString(), "yyyy-MM-dd")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        query.addCriteria(Criteria.where("status").is(1));
        Pageable pageable = PageRequest.of(0, 1);
        List<Toutiao> toutiaos = mongoTemplate.find(query.with(pageable), Toutiao.class);

        if (CollectionUtils.isEmpty(toutiaos)) {
            log.info("获取新闻详情结束：toutiaos.size() = {}", toutiaos.size());
            return true;
        }

        Toutiao toutiao = toutiaos.get(0);
        String uniquekey = toutiao.getUniquekey();
        //标记处理
        Query query1 = new Query(Criteria.where("uniquekey").is(uniquekey));
        Update update = new Update().set("status", 2);
        mongoTemplate.updateFirst(query1,update, Toutiao.class);
        log.info("获取新闻详情:uniquekey = {}", uniquekey);

        Map<String, Object> map = new HashMap<>();
        map.put("key", juheKey);
        map.put("uniquekey", uniquekey);
        JSONObject forObject = restTemplate.getForObject(CONTENT_URL, JSONObject.class, map);
        System.err.println(forObject.toJSONString());

        Integer error_code = forObject.getInteger("error_code");
        if (0 != error_code) {
            String reason = forObject.getString("reason");
            log.error("获取新闻详情异常：msg={},uniquekey={}", reason, uniquekey);
            throw new BizException("请求新闻详情异常:msg={}" + reason);
        }

        Date date = new Date();
        JSONObject result = forObject.getJSONObject("result");
        ToutiaoContent detail = JSONObject.parseObject(result.getJSONObject("detail").toJSONString(), ToutiaoContent.class);
        detail.setContent(result.getString("content"));
        detail.setUniquekey(result.getString("uniquekey"));
        detail.setCreateTime(date);
        detail.setUpdateTime(date);

        touTiaoContentRepository.save(detail);

        return true;
    }


}
