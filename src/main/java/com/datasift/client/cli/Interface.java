package com.datasift.client.cli;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.DataSiftResult;
import com.datasift.client.core.Stream;
import com.datasift.client.core.Usage;
import com.datasift.client.push.OutputType;
import com.datasift.client.push.PushSubscription;
import com.datasift.client.push.connectors.BaseConnector;
import com.datasift.client.push.connectors.PushConnector;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.higgs.http.client.HttpRequestBuilder;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.datasift.client.cli.Parser.CliArguments;
import static com.datasift.client.cli.Parser.CliSwitch;

public class Interface {
    private static final ObjectMapper mapper = new ObjectMapper();

    private Interface() {
    }

    public static void main(String[] args) {
        List<CliSwitch> switches = new ArrayList<>();
        switches.add(new CliSwitch("a", "auth", true, "Auth is required in the form username:api_key"));
        switches.add(new CliSwitch("c", "command", true));
        CliSwitch endpoint = new CliSwitch("e", "endpoint");
        endpoint.setDefault("core");
        switches.add(endpoint);
        switches.add(new CliSwitch("p", "param"));
        CliArguments parsedArgs = Parser.parse(args, switches);

        Map<String, String> auth = parsedArgs.map("a");
        if (auth == null || auth.size() == 0) {
            System.out.println("Auth must be provided in the form '-a[uth] username api_key'");
            System.exit(0);
        }

        Map.Entry<String, String> authVals = auth.entrySet().iterator().next();
        DataSiftConfig config = new DataSiftConfig(authVals.getKey(), authVals.getValue());
        DataSiftClient dataSift = new DataSiftClient(config);
        switch (parsedArgs.get("e")) {
            case "core":
                executeCore(dataSift, parsedArgs.get("c"), parsedArgs.map("p"));
                break;
            case "push":
                executePush(dataSift, parsedArgs.get("c"), parsedArgs.map("p"));
                break;
            case "historics":
                executeHistorics(dataSift, parsedArgs.get("c"), parsedArgs.map("p"));
                break;
            case "preview":
                executePreview(dataSift, parsedArgs.get("c"), parsedArgs.map("p"));
                break;
            case "sources":
                executeSources(dataSift, parsedArgs.get("c"), parsedArgs.map("p"));
                break;
        }

        HttpRequestBuilder.shutdown();
    }

    private static void require(String[] args, Map<String, String> params) {
        List<String> missing = new ArrayList<>();
        for (String arg : args) {
            if (!params.containsKey(arg)) {
                missing.add(arg);
            }
        }
        if (missing.size() > 0) {
            System.out.println("The following arguments are required : " + Arrays.toString(missing.toArray()));
            System.exit(0);
        }
    }

    private static void executeCore(DataSiftClient dataSift, String endpoint, Map<String, String> params) {
        switch (endpoint) {
            case "validate":
                require(new String[]{"csdl"}, params);
                printResponse(dataSift.validate(params.get("csdl")).sync());
                break;
            case "compile":
                require(new String[]{"csdl"}, params);
                printResponse(dataSift.compile(params.get("csdl")).sync());
                break;
            case "usage":
                String period = params.get("period");
                if (period == null) {
                    printResponse(dataSift.usage().sync());
                } else {
                    printResponse(dataSift.usage(Usage.Period.fromStr(period)).sync());
                }
                break;
            case "dpu":
                require(new String[]{"hash"}, params);
                printResponse(dataSift.dpu(Stream.fromString(params.get("hash"))).sync());
                break;
            case "balance":
                printResponse(dataSift.balance().sync());
                break;
        }
    }

    private static void printResponse(DataSiftResult result) {
        int status = result.getResponse().status();
        Map<String, String> headers = new HashMap<>();
        for (Map.Entry<String, List<String>> h : result.getResponse().headers().entrySet()) {
            headers.put(h.getKey(), h.getValue() == null || h.getValue().size() == 0 ? null : h.getValue().get(0));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("body", result.getResponse().data());
        response.put("status", status);
        response.put("headers", headers);
        try {
            System.out.println(mapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private static void executePush(DataSiftClient dataSift, String endpoint, HashMap<String, String> params) {
        PushConnector connector = null;
        try {
            Map<String, Object> args = mapper.readValue(params.get("output_type"),
                    new TypeReference<HashMap<String, Object>>() {
                    });
            connector = BaseConnector.fromMap(new OutputType<>(params.get("output_type")), args);
        } catch (IOException e) {
            System.out.println("output_type must be a valid JSON object received : " + params.get("output_type"));
            System.exit(0);
        }
        switch (endpoint) {
            case "validate":
                printResponse(dataSift.push().validate(connector).sync());
                break;
            case "create":
                printResponse(dataSift.push().create(connector, Stream.fromString(params.get("hash")),
                        params.get("name")).sync());
                break;
            case "pause":
                printResponse(dataSift.push().pause(params.get("id")).sync());
                break;
            case "resume":
                printResponse(dataSift.push().resume(params.get("id")).sync());
                break;
            case "update":
                printResponse(dataSift.push().update(params.get("id"), connector).sync());
                break;
            case "stop":
                printResponse(dataSift.push().stop(params.get("id")).sync());
                break;
            case "delete":
                printResponse(dataSift.push().delete(params.get("id")).sync());
                break;
            case "log":
                try {
                    printResponse(dataSift.push().log(params.get("id"), Integer.parseInt(params.get("page"))).sync());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                break;
            case "get":
                printResponse(dataSift.push().get(params.get("id")).sync());
                break;
            case "pull":
                try {
                    printResponse(dataSift.push().pull(PushSubscription.fromString(params.get("id")),
                            Integer.parseInt(params.get("size")),
                            params.get("page")).sync());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                break;
        }
    }

    private static void executeHistorics(DataSiftClient dataSift, String endpoint, HashMap<String, String> params) {
        switch (endpoint) {
            case "prepare":
                printResponse(dataSift.historics().prepare(params.get("hash"), DateTime.parse(params.get("start")),
                        DateTime.parse(params.get("end")), params.get("name")).sync());
                break;
            case "start":
                printResponse(dataSift.historics().start(params.get("id")).sync());
                break;
            case "stop":
                printResponse(dataSift.historics().stop(params.get("id"), params.get("reason")).sync());
                break;
            case "status":
                printResponse(dataSift.historics().status(new DateTime(Long.parseLong(params.get("start"))),
                        new DateTime(Long.parseLong(params.get("end")))).sync());
                break;
            case "update":
                printResponse(dataSift.historics().update(params.get("id"), params.get("name")).sync());
                break;
            case "delete":
                printResponse(dataSift.historics().delete(params.get("id")).sync());
                break;
            case "get":
                printResponse(dataSift.historics().get(params.get("id")).sync());
                break;
        }
    }

    private static void executePreview(DataSiftClient dataSift, String endpoint, HashMap<String, String> params) {
        switch (endpoint) {
            case "create":
                printResponse(dataSift.preview().create(new DateTime(Long.parseLong(params.get("start"))),
                        Stream.fromString(params.get("hash")), params.get("params").split(",")).sync());
                break;
            case "get":
                printResponse(dataSift.preview().get(params.get("id")).sync());
                break;
        }
    }

    private static void executeSources(DataSiftClient dataSift, String endpoint, HashMap<String, String> params) {
        switch (endpoint) {
            case "create":
                //DataSource
                //new DateTime(Long.parseLong(params.get("start")))
                //printResponse(dataSift.managedSource().create(params.get("name"),  ).sync());
                break;
            case "update":
                //printResponse(dataSift.managedSource().update(params.get("name"), ).sync());
                break;
            case "delete":
                printResponse(dataSift.managedSource().delete(params.get("id")).sync());
                break;
            case "log":
                printResponse(dataSift.managedSource().log(params.get("id")).sync());
                break;
            case "get":
                printResponse(dataSift.managedSource().get(params.get("id")).sync());
                break;
            case "stop":
                printResponse(dataSift.managedSource().stop(params.get("id")).sync());
                break;
            case "start":
                printResponse(dataSift.managedSource().start(params.get("id")).sync());
                break;
        }
    }
}
