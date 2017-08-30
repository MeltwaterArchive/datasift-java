package com.datasift.client.mock.datasift;

import com.datasift.client.core.Balance;
import com.datasift.client.core.Usage;
import io.higgs.http.server.HttpResponse;
import io.higgs.http.server.resource.MediaType;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.joda.time.DateTime;

import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import java.util.HashMap;
import java.util.Map;

@Path("v1.4")
public class MockCoreApi {
    Map<String, String> headers = new HashMap<>();
    private double dpu = -1f;
    private DateTime createdAt = DateTime.now();
    private String compileHash;
    private String expectedCsdl;
    private DateTime start, end;
    private Usage.UsageStream streams;
    private double credit = -1d;
    private String plan = "";
    private double remaining_dpus = -1d;
    private Usage.Period expectedPeriod;

    @Path("compile")
    public Map<String, Object> compile(@FormParam("csdl") String csdl, HttpResponse response) {
        Map<String, Object> data = compileOrValidate(csdl, response);
        data.put("hash", compileHash);
        return data;
    }

    @Path("validate")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> validate(@FormParam("csdl") String csdl, HttpResponse response) {
        return compileOrValidate(csdl, response);
    }

    @Path("usage")
    public Map<String, Object> usage(@QueryParam("period") String period) {
        if (expectedPeriod != null) {
            if (!expectedPeriod.toString().equalsIgnoreCase(period)) {
                throw new WebApplicationException(HttpResponseStatus.BAD_REQUEST.code());
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("end", end);
        map.put("streams", streams);
        return map;
    }

    @Path("dpu")
    public Map<String, Object> dpu() {
        Map<String, Object> map = new HashMap<>();
        map.put("dpu", dpu);
        return map;
    }

    @Path("balance")
    public Map<String, Balance.BalanceData> balance() {
        Map<String, Balance.BalanceData> map = new HashMap<>();
        Balance.BalanceData balanceData = new Balance.BalanceData();
        balanceData.setCredit(credit);
        balanceData.setPlan(plan);
        balanceData.setRemainingDpus(remaining_dpus);

        map.put("balance", balanceData);
        return map;
    }

    public void setDpu(double dpu) {
        this.dpu = dpu;
    }

    public void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setCompileHash(String compileHash) {
        this.compileHash = compileHash;
    }

    public void setExpectedCsdl(String expectedCsdl) {
        this.expectedCsdl = expectedCsdl;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setStart(DateTime start) {
        this.start = start;
    }

    public void setEnd(DateTime end) {
        this.end = end;
    }

    public void setStreams(Usage.UsageStream streams) {
        this.streams = streams;
    }

    private Map<String, Object> compileOrValidate(String csdl, HttpResponse response) {
        if (!expectedCsdl.equals(csdl)) {
            throw new WebApplicationException(HttpResponseStatus.BAD_REQUEST.code());
        }
        return hashDpuAndCreatedAt(response);
    }

    private Map<String, Object> hashDpuAndCreatedAt(HttpResponse response) {
        for (Map.Entry<String, String> v : headers.entrySet()) {
            response.headers().add(v.getKey(), v.getValue());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("created_at", createdAt);
        map.put("dpu", dpu);
        return map;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public void setRemaining_dpus(double remaining_dpus) {
        this.remaining_dpus = remaining_dpus;
    }

    public void setExpectedPeriod(Usage.Period expectedPeriod) {
        this.expectedPeriod = expectedPeriod;
    }
}
