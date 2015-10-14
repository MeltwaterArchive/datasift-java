package com.datasift.client.cli;

import com.datasift.client.DataSiftAPIResult;
import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.DataSiftResult;
import com.datasift.client.pylon.PylonQueryParameters;
import com.datasift.client.pylon.PylonQuery;
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

    private static String getOrDefault(Map<String, String> map, String key, String defaultValue) {
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            return defaultValue;
        }
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
                URI url = new URI(!u.startsWith("http") && !u.startsWith("https") ? "http://" + u : u);
                config.host(url.getHost());
                config.ingestionHost(url.getHost());
                config.setSslEnabled(url.getScheme() != null && url.getScheme().equals("https"));
                config.port(url.getPort() > -1 ? url.getPort() : config.isSslEnabled() ? 443 : 80);
            }
            DataSiftClient dataSift = new DataSiftClient(config);
            HashMap<String, String> params = parsedArgs.map("p");
            if (params == null) {
                params = new HashMap<String, String>();
            }
            switch (parsedArgs.get("e")) {
                case "core":
                    executeCore(dataSift, parsedArgs.get("c"), params);
                    break;
                case "push":
                    executePush(dataSift, parsedArgs.get("c"), params);
                    break;
                case "historics":
                    executeHistorics(dataSift, parsedArgs.get("c"), params);
                    break;
                case "preview":
                    executePreview(dataSift, parsedArgs.get("c"), params);
                    break;
                case "sources":
                    executeSources(dataSift, parsedArgs.get("c"), params);
                    break;
                case "pylon":
                    executeAnalysis(dataSift, parsedArgs.get("c"), params);
                    break;
                case "odp":
                    executeODP(dataSift, parsedArgs.get("c"), params);
                    break;
                case "identity":
                    executeIdentity(dataSift, parsedArgs.get("c"), params);
                    break;
                case "token":
                    executeToken(dataSift, parsedArgs.get("c"), params);
                    break;
                case "limit":
                    executeLimit(dataSift, parsedArgs.get("c"), params);
                    break;
            }
        } catch (Exception ex) {
            DataSiftAPIResult res = new DataSiftAPIResult();
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
            System.exit(0);
        } catch (Exception ex) {
            DataSiftAPIResult res = new DataSiftAPIResult();
            res.failed(ex);
            System.out.println(mapper.writeValueAsString(res));
            System.exit(0);
        } finally {
            HttpRequestBuilder.group().shutdownGracefully(0, 0, TimeUnit.MILLISECONDS);
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
                PylonQueryParameters map = null;
                String p = params.get("parameters");
                if (p != null && !p.isEmpty()) {
                    map = mapper.readValue(p, PylonQueryParameters.class);
                }
                PylonQuery analysis = new PylonQuery(params.get("hash"), map, params.get("filter"),
                        params.get("start") == null ? null : Integer.parseInt(params.get("start")),
                        params.get("end") == null ? null : Integer.parseInt(params.get("end")));
                printResponse(dataSift.pylon().analyze(analysis).sync());
                break;
            case "compile":
                printResponse(dataSift.pylon().compile(params.get("csdl")).sync());
                break;
            case "get":
                String hash = params == null ? null : params.get("hash");
                printResponse(hash == null ? dataSift.pylon().get().sync() : dataSift.pylon().get(hash).sync());
                break;
            case "start":
                printResponse(dataSift.pylon().start(params.get("hash")).sync());
                break;
            case "stop":
                printResponse(dataSift.pylon().stop(params.get("hash")).sync());
                break;
            case "tags":
                printResponse(dataSift.pylon().tags(params.get("hash")).sync());
                break;
            case "validate":
                printResponse(dataSift.pylon().validate(params.get("csdl")).sync());
                break;
        }
    }

    private static void executeODP(DataSiftClient dataSift, String command, HashMap<String, String> params)
            throws IOException {
        require(new String[]{"sourceid"}, params);
        require(new String[]{"data"}, params);
        String sourceId = params.get("sourceid");
        String data = params.get("data");

        if (command != null && command == "batch") {
            printResponse(dataSift.odp().batch(sourceId, data).sync());
        } else {
            System.out.println("ODP ingestion upload mode not supported via CLI");
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

    private static void executeIdentity(DataSiftClient dataSift, String endpoint, HashMap<String, String> params)
            throws IOException {
        switch (endpoint) {
            case "list":
                String label = getOrDefault(params, "label", null);
                int page = Integer.parseInt(getOrDefault(params, "page", "0"));
                int perpage = Integer.parseInt(getOrDefault(params, "per_page", "0"));
                printResponse(dataSift.account().list(label, page, perpage).sync());
                break;
            case "get":
                printResponse(dataSift.account().get(params.get("id")).sync());
                break;
            case "create":
                String createlabel = params.get("label");
                Boolean active = getOrDefault(params, "status", "inactive").equals("active");
                Boolean master = getOrDefault(params, "master", "false").equals("true");
                printResponse(dataSift.account().create(createlabel, active, master).sync());
                break;
            case "update":
                String targetid = params.get("id");
                String updatelabel = getOrDefault(params, "label", null);
                String updateactivitystring = getOrDefault(params, "status", null);
                Boolean updateactivity = null;
                if (updateactivitystring != null) {
                    if (updateactivitystring.equals("active")) {
                        updateactivity = true;
                    } else if (updateactivitystring.equals("inactive")) {
                        updateactivity = false;
                    }
                }
                String updatemasterstring = getOrDefault(params, "master", null);
                Boolean updatemaster = null;
                if (updatemasterstring != null) {
                    if (updatemasterstring.equals("true")) {
                        updatemaster = true;
                    } else if (updatemasterstring.equals("false")) {
                        updatemaster = false;
                    }
                }
                printResponse(dataSift.account().update(targetid, updatelabel, updateactivity, updatemaster).sync());
                break;
            case "delete":
                printResponse(dataSift.account().delete(params.get("id")).sync());
                break;
        }
    }

    private static void executeToken(DataSiftClient dataSift, String endpoint, HashMap<String, String> params)
            throws IOException {
        switch (endpoint) {
            case "list":
                String identityid = getOrDefault(params, "identity_id", null);
                int page = Integer.parseInt(getOrDefault(params, "page", "0"));
                int perpage = Integer.parseInt(getOrDefault(params, "per_page", "0"));
                printResponse(dataSift.account().listTokens(identityid, page, perpage).sync());
                break;
            case "get":
                printResponse(dataSift.account().getToken(params.get("identity_id"), params.get("service")).sync());
                break;
            case "create":
                String createidentity = params.get("identity_id");
                String createservice = params.get("service");
                String createtoken = params.get("token");
                printResponse(dataSift.account().createToken(createidentity, createservice, createtoken).sync());
                break;
            case "update":
                String targetid = params.get("identity_id");
                String targetservice = params.get("service");
                String newtoken = params.get("token");
                printResponse(dataSift.account().updateToken(targetid, targetservice, newtoken).sync());
                break;
            case "delete":
                printResponse(dataSift.account().deleteToken(params.get("identity_id"), params.get("service")).sync());
                break;
        }
    }
        private static void executeLimit(DataSiftClient dataSift, String endpoint, HashMap<String, String> params)
            throws IOException {
        switch (endpoint) {
            case "list":
                String service = params.get("service");
                int page = Integer.parseInt(getOrDefault(params, "page", "0"));
                int perpage = Integer.parseInt(getOrDefault(params, "per_page", "0"));
                printResponse(dataSift.account().listLimits(service, page, perpage).sync());
                break;
            case "get":
                printResponse(dataSift.account().getLimit(params.get("identity_id"), params.get("service")).sync());
                break;
            case "create":
                String createidentity = params.get("identity_id");
                String createservice = params.get("service");
                Long createallowance = Long.parseLong(params.get("total_allowance"));
                printResponse(dataSift.account().createLimit(createidentity, createservice, createallowance).sync());
                break;
            case "update":
                String updateidentity = params.get("identity_id");
                String updateservice = params.get("service");
                Long updateallowance = Long.parseLong(params.get("total_allowance"));
                printResponse(dataSift.account().updateLimit(updateidentity, updateservice, updateallowance).sync());
                break;
            case "delete":
                printResponse(dataSift.account().deleteLimit(params.get("identity_id"), params.get("service")).sync());
                break;
        }
    }
}
