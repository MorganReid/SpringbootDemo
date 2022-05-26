package com.example.demo.controller;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static org.elasticsearch.common.unit.TimeValue.*;

/**
 * @author: hujun
 * @date: 2021/10/26  16:41
 */
@RestController
public class MyESController {

    @Autowired
    private RestHighLevelClient myesClient;



    public IndexResponse save() throws IOException {

        IndexRequest request = new IndexRequest("111");
        request.source("111", XContentType.JSON);
        request.timeout(timeValueMinutes(16000));
        return myesClient.index(request, RequestOptions.DEFAULT);
    }

}

