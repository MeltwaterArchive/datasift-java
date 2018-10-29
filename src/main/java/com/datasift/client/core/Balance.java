package com.datasift.client.core;

import com.datasift.client.BaseDataSiftResult;
import com.datasift.client.DataSiftClient;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

/*
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class Balance extends BaseDataSiftResult {

    @JsonProperty
    protected BalanceData balance;

    public Balance() {
    }

    /*
     * @return The account's price plan
     */
    public String pricePlan() {
        return balance.plan;
    }

    public BalanceData getBalance() {
        return balance;
    }

    public void setBalance(BalanceData balance) {
        this.balance = balance;
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

    public static class AllowanceData {
        public String getName() {
            return name;
        }

        public List<String> getCategories() {
            return categories;
        }

        public double getCost() {
            return cost;
        }

        public double getDpuAllowance() {
            return dpuAllowance;
        }

        public double getRemainingDpus() {
            return remainingDpus;
        }

        public double getUsage() {
            return usage;
        }

        @JsonProperty
        String name;
        @JsonProperty
        List<String> categories;
        @JsonProperty
        double cost;
        @JsonProperty("dpu_allowance")
        double dpuAllowance;
        @JsonProperty("remaining_dpus")
        double remainingDpus;
        @JsonProperty
        double usage;

        public void setName(String name) {
            this.name = name;
        }

        public void setCategories(List<String> categories) {
            this.categories = categories;
        }

        public void setCost(double cost) {
            this.cost = cost;
        }

        public void setDpuAllowance(double dpuAllowance) {
            this.dpuAllowance = dpuAllowance;
        }

        public void setRemainingDpus(double remainingDpus) {
            this.remainingDpus = remainingDpus;
        }

        public void setUsage(double usage) {
            this.usage = usage;
        }
    }

    public static class BalanceData {
        @JsonProperty
        double threshold;
        @JsonProperty
        String plan;
        @JsonProperty
        List<AllowanceData> allowances;

        public double getThreshold() {
            return threshold;
        }

        public String getPlan() {
            return plan;
        }

        public List<AllowanceData> getAllowances() {
            return allowances;
        }

        public void setThreshold(double threshold) {
            this.threshold = threshold;
        }

        public void setPlan(String plan) {
            this.plan = plan;
        }

        public void setAllowances(List<AllowanceData> allowances) {
            this.allowances = allowances;
        }
    }
}
