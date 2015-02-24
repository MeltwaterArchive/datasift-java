package com.datasift.client.cli;

import com.datasift.client.BaseDataSiftResult;
import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.DataSiftResult;
import com.datasift.client.analysis.AnalysisQueryParameters;
import com.datasift.client.analysis.AnalyzeQuery;
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
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.datasift.client.cli.Parser.CliArguments;
import static com.datasift.client.cli.Parser.CliSwitch;

public class Interface {
    private static final ObjectMapper mapper = new ObjectMapper();

    private Interface() {
    }

    public static void main(String[] args) throws JsonProcessingException {
        try {
            List<CliSwitch> switches = new ArrayList<>();
            switches.add(new CliSwitch("a", "auth", true, "Auth is required in the form username:api_key"));
            switches.add(new CliSwitch("c", "command", true));
            CliSwitch endpoint = new CliSwitch("e", "endpoint");
            endpoint.setDefault("core");
            switches.add(endpoint);
            switches.add(new CliSwitch("p", "param"));
            switches.add(new CliSwitch("u", "url"));
            CliArguments parsedArgs = Parser.parse(args, switches);

            Map<String, String> auth = parsedArgs.map("a");
            if (auth == null || auth.size() == 0) {
                System.out.println("Auth must be provided in the form '-a[uth] username api_key'");
                System.exit(0);
            }

            Map.Entry<String, String> authVals = auth.entrySet().iterator().next();
            DataSiftConfig config = new DataSiftConfig(authVals.getKey(), authVals.getValue());
            String u = parsedArgs.get("u");
            if (u != null) {
                URI url = new URI(u.startsWith("http") ? u : "http://" + u);
                config.host(url.getHost());
                config.port(url.getPort());
                config.setSslEnabled(url.getScheme() != null && url.getScheme().equals("https"));
            }
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
                case "analysis":
                    executeAnalysis(dataSift, parsedArgs.get("c"), parsedArgs.map("p"));
                    break;
            }
        } catch (Exception ex) {
            BaseDataSiftResult res = new BaseDataSiftResult();
            res.failed(ex);
            printResponse(res);
        } finally {
            HttpRequestBuilder.group().shutdownGracefully(0, 0, TimeUnit.MILLISECONDS);
        }
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

    private static void executeCore(DataSiftClient dataSift, String endpoint, Map<String, String> params)
            throws JsonProcessingException {
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

    private static void printResponse(DataSiftResult result) throws JsonProcessingException {
        try {
            Map<String, Object> response = new HashMap<>();
            Map<String, String> headers = new HashMap<>();
            if (result.getResponse() != null) {
                for (Map.Entry<String, List<String>> h : result.getResponse().headers().entrySet()) {
                    headers.put(h.getKey(), h.getValue() == null || h.getValue().size() == 0 ?
                            null : h.getValue().get(0));
                }
                int status = result.getResponse().status();
                response.put("status", status);
            } else {
                response.put("error", "Invalid response, null");
            }
            response.put("body", result);
            response.put("headers", headers);
            System.out.println(mapper.writeValueAsString(response));
        } catch (Exception ex) {
            BaseDataSiftResult res = new BaseDataSiftResult();
            res.failed(ex);
            System.out.println(mapper.writeValueAsString(res));
        } finally {
            HttpRequestBuilder.group().shutdownGracefully(0, 0, TimeUnit.MILLISECONDS);
            System.exit(0);
        }
    }

    private static void executePush(DataSiftClient dataSift, String endpoint, HashMap<String, String> params)
            throws IOException {
        PushConnector connector;
        Map<String, Object> args = mapper.readValue(params.get("output_type"),
                new TypeReference<HashMap<String, Object>>() {
                });
        connector = BaseConnector.fromMap(new OutputType<>(params.get("output_type")), args);
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
                printResponse(dataSift.push().log(params.get("id"), Integer.parseInt(params.get("page"))).sync());
                break;
            case "get":
                printResponse(dataSift.push().get(params.get("id")).sync());
                break;
            case "pull":
                printResponse(dataSift.push().pull(PushSubscription.fromString(params.get("id")),
                        Integer.parseInt(params.get("size")),
                        params.get("page")).sync());
                break;
        }
    }

    private static void executeHistorics(DataSiftClient dataSift, String endpoint, HashMap<String, String> params)
            throws JsonProcessingException {
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

    private static void executeAnalysis(DataSiftClient dataSift, String endpoint, HashMap<String, String> params)
            throws IOException {
        switch (endpoint) {
            case "analyze":
                AnalysisQueryParameters map = null;
                String p = params.get("parameters");
                if (p != null && !p.isEmpty()) {
                    map = mapper.readValue(p, AnalysisQueryParameters.class);
                }
                AnalyzeQuery analysis = new AnalyzeQuery(params.get("hash"), map, params.get("filter"),
                        params.get("start") == null ? null : Integer.parseInt(params.get("start")),
                        params.get("end") == null ? null : Integer.parseInt(params.get("end")));
                printResponse(dataSift.analysis().analyze(analysis).sync());
                break;
            case "compile":
                printResponse(dataSift.analysis().compile(params.get("csdl")).sync());
                break;
            case "get":
                String hash = params == null ? null : params.get("hash");
                printResponse(hash == null ? dataSift.analysis().get().sync() : dataSift.analysis().get(hash).sync());
                break;
            case "start":
                printResponse(dataSift.analysis().start(params.get("hash")).sync());
                break;
            case "stop":
                printResponse(dataSift.analysis().stop(params.get("hash")).sync());
                break;
            case "tags":
                printResponse(dataSift.analysis().tags(params.get("hash")).sync());
                break;
            case "validate":
                printResponse(dataSift.analysis().validate(params.get("csdl")).sync());
                break;
        }
    }

    private static void executePreview(DataSiftClient dataSift, String endpoint, HashMap<String, String> params)
            throws JsonProcessingException {
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

    private static void executeSources(DataSiftClient dataSift, String endpoint, HashMap<String, String> params)
            throws JsonProcessingException {
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
