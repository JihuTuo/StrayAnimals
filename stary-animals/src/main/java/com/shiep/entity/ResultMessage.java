package com.shiep.entity;

import java.io.Serializable;
import java.util.List;
 
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
 
/**
 * 自定义响应结构
 */
public class ResultMessage implements Serializable {
 
    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();
 
    // 响应业务状态
    private Integer status;
 
    // 响应消息
    private String msg;
 
    // 响应中的数据
    private Object data;
 
    public static ResultMessage build(Integer status, String msg, Object data) {
        return new ResultMessage(status, msg, data);
    }
 
    public static ResultMessage ok(Object data) {
        return new ResultMessage(data);
    }
 
    public static ResultMessage ok() {
        return new ResultMessage(null);
    }
 
    public ResultMessage() {
 
    }
 
    public static ResultMessage build(Integer status, String msg) {
        return new ResultMessage(status, msg, null);
    }
 
    public ResultMessage(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    // 只返回数据的时候，状态码正常
    public ResultMessage(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }
 
//    public Boolean isOK() {
//        return this.status == 200;
//    }
 
    public Integer getStatus() {
        return status;
    }
 
    public void setStatus(Integer status) {
        this.status = status;
    }
 
    public String getMsg() {
        return msg;
    }
 
    public void setMsg(String msg) {
        this.msg = msg;
    }
 
    public Object getData() {
        return data;
    }
 
    public void setData(Object data) {
        this.data = data;
    }
 
    /**
     * 将json结果集转化为TaotaoResult对象
     * 
     * @param jsonData json数据
     * @param clazz TaotaoResult中的object类型
     * @return
     */
    public static ResultMessage formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, ResultMessage.class);
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (clazz != null) {
                if (data.isObject()) {
                    obj = MAPPER.readValue(data.traverse(), clazz);
                } else if (data.isTextual()) {
                    obj = MAPPER.readValue(data.asText(), clazz);
                }
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }
 
    /**
     * 没有object对象的转化
     * 
     * @param json
     * @return
     */
    public static ResultMessage format(String json) {
        try {
            return MAPPER.readValue(json, ResultMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
 
    /**
     * Object是集合转化
     * 
     * @param jsonData json数据
     * @param clazz 集合中的类型
     * @return
     */
    public static ResultMessage formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }
 
}