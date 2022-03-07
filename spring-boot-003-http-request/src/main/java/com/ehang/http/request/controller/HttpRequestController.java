package com.ehang.http.request.controller;

import com.alibaba.fastjson.JSON;
import com.ehang.http.request.controller.dto.GetObjRequestDTO;
import com.ehang.http.request.controller.dto.PostBodyDTO;
import com.ehang.http.request.controller.dto.PutBodyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * @author LENOVO
 * @title: HttpRequestController
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/3/3 21:04
 */
@RestController
public class HttpRequestController {
    Logger logger = LoggerFactory.getLogger(HttpRequestController.class);

    /**
     * Get 请求
     * 一般用于少量参数的批量获取
     *
     * @param param1
     * @param name
     * @return
     */
    @GetMapping()
    public String get(@RequestParam String param1, @RequestParam("param2") String name) {
        // http://127.0.0.1:8083?param1=p1&param2=zhangsan
        // @RequestParam 用于接受url?后面的参数
        // @RequestParam("param2") String name 表示将url参数中的param2值赋给变量name

        logger.info("param1:{} name:{}", param1, name);
        return "get request success!!!";
    }

    /**
     * 对象接受请求参数
     * 一般用于多请求参数的接口
     *
     * @param requestDTO
     * @return
     */
    @GetMapping("/obj")
    public String getObj(GetObjRequestDTO requestDTO) {
        // http://127.0.0.1:8083/obj?name=zhangsan&age=20

        logger.info("requestDTO:{}", JSON.toJSONString(requestDTO));
        // 输出日志：requestDTO:{"age":20,"name":"zhangsan"}
        return "get request success!!!";
    }

    /**
     * 请请求地址中获取值  @PathVariable
     * 一般用于指定ID的数据
     *
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public String get(@PathVariable Integer id) {
        // http://127.0.0.1:8083/get/1
        logger.info("id:{}", id);
        // 输出 id:1
        return "get request success!!!";
    }

    /**
     * POST请求
     * 一般用于提交数据
     *
     * @param postBodyDTO
     * @return
     */
    @PostMapping()
    public String post(@RequestBody PostBodyDTO postBodyDTO) {
        // 地址：127.0.0.1:8083/
        // body数据：{"name":"zhangsan","age":20}
        logger.info("postBodyDTO:{}", JSON.toJSONString(postBodyDTO));
        // 输出 postBodyDTO:{"age":20,"name":"zhangsan"}
        return "post request success!!!";
    }

    /**
     * PUT请求
     * 一般用于修改数据
     *
     * @param putBodyDTO
     * @return
     */
    @PutMapping("/{id}")
    public String put(@PathVariable Integer id, @RequestBody PutBodyDTO putBodyDTO) {
        // PUT请求地址：127.0.0.1:8083/1
        // body数据：{"name":"zhangsan","age":20}
        logger.info("id:{} PutBodyDTO:{}", id, JSON.toJSONString(putBodyDTO));
        // 输出 id:1 putBodyDTO:{"age":20,"name":"zhangsan"}
        return "put request success!!!";
    }

    /**
     * DELETE请求
     * 一般用于删除操作
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        // DELETE请求地址：127.0.0.1:8083/1
        logger.info("删除id:{}", id);
        return "delete request success!!!";
    }
}
