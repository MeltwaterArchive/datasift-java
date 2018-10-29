package com.datasift.client.core;

import com.datasift.client.BaseDataSiftResult;
import com.datasift.client.DataSiftClient;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.HashMap;

/*
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class Dpu extends BaseDataSiftResult {
    @JsonProperty
    private double dpu;
    @JsonProperty
    private DpuDetails detail;

    public double getDpu() {
        return dpu;
    }

    public DpuDetails getDetail() {
        return detail;
    }

    @Override
    public String toString() {
        try {
            return DataSiftClient.MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Unable to generate string representation of this response/result";
        }
    }

    public static class DpuDetails extends HashMap<String, Object> {
        //TODO try to revise a more predictable structure
    }
}
