package com.wxsk.vr.mine.controller.response.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import java.util.HashMap;
import java.util.Map;

/**
 * 地块挖掘收益
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class LandAreaHarvestVo {

    /**
     * 名称
     * */
    @JsonProperty("name")
    private String name;

    /**
     * 收益类型
     * */
    @JsonProperty("type")
    private Byte type;

    /**
     * 收益总数
     * */
    @JsonProperty("amount")
    private Double amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "LandAreaHarvestVo{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", amount=" + amount +
                '}';
    }

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        Double d = (double)1;
        map.put("a",d);
        System.out.println(mapper.writeValueAsString(map));

    }
}
