package com.datasift.client.cli;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.DataSiftResult;
import com.datasift.client.core.Stream;
import com.datasift.client.core.Usage;
import com.datasift.client.push.PushConnectors;
import com.datasift.client.push.connectors.BigQuery;
import com.datasift.client.push.connectors.CouchDB;
import com.datasift.client.push.connectors.DynamoDB;
import com.datasift.client.push.connectors.ElasticSearch;
import com.datasift.client.push.connectors.FTP;
import com.datasift.client.push.connectors.Http;
import com.datasift.client.push.connectors.MongoDB;
import com.datasift.client.push.connectors.PushConnector;
import com.datasift.client.push.connectors.Redis;
import com.datasift.client.push.connectors.S3;
import com.datasift.client.push.connectors.SFTP;
import com.datasift.client.push.connectors.SplunkEnterprise;
import com.datasift.client.push.connectors.SplunkStorm;
import com.datasift.client.push.connectors.SplunkStormRest;
import com.datasift.client.push.connectors.ZoomData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.higgs.http.client.HttpRequestBuilder;
import io.netty.handler.codec.http.HttpMethod;
import org.joda.time.DateTime;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
        switches.add(new CliSwitch("e", "endpoint", true));
        CliSwitch command = new CliSwitch("c", "command");
        command.setDefault("core");
        switches.add(command);
        switches.add(new CliSwitch("p", "param"));
        CliArguments parsedArgs = Parser.parse(args, switches);

        String[] auth = parsedArgs.get("a").split(":");
        if (auth.length != 2) {
            System.out.println("Auth must be in the form username:api_key");
            System.exit(0);
        }

        DataSiftConfig config = new DataSiftConfig(auth[0], auth[1]);
        DataSiftClient dataSift = new DataSiftClient(config);
        switch (parsedArgs.get("c")) {
            case "core":
                executeCore(dataSift, parsedArgs.get("e"), parsedArgs.map("p"));
                break;
            case "push":
                executePush(dataSift, parsedArgs.get("e"), parsedArgs.map("p"));
                break;
            case "historics":
                executeHistorics(dataSift, parsedArgs.get("e"), parsedArgs.map("p"));
                break;
            case "preview":
                executePreview(dataSift, parsedArgs.get("e"), parsedArgs.map("p"));
                break;
            case "sources":
                executeSources(dataSift, parsedArgs.get("e"), parsedArgs.map("p"));
                break;

        }

        HttpRequestBuilder.shutdown();
    }

    private static void executeCore(DataSiftClient dataSift, String endpoint, HashMap<String, String> params) {
        switch (endpoint) {
            case "validate":
                printResponse(dataSift.validate(params.get("csdl")).sync());
                break;
            case "compile":
                printResponse(dataSift.compile(params.get("csdl")).sync());
                break;
            case "usage":
                String period = params.get("period");
                if (period == null) {
                    printResponse(dataSift.usage().sync());
                } else {
                    printResponse(dataSift.usage(Usage.Period.valueOf(period)).sync());
                }
                break;
            case "dpu":
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
        PushConnector connector = getPushConnector(params);
        switch (endpoint) {
            case "validate":
                printResponse(dataSift.push().validate(connector).sync());
                break;
            case "create":
                printResponse(dataSift.push().create(connector, Stream.fromString(params.get("hash")), params.get("name")).sync());
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
                    printResponse(dataSift.push().pull(params.get("id"), Integer.parseInt(params.get("size")), params.get("page")).sync());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                break;
        }
    }

    private static PushConnector getPushConnector(HashMap<String, String> params) {
        PushConnector connector = null;
        switch (params.get("output_type")) {
            case "bigquery":
                BigQuery query = PushConnectors.bigQuery();

                query.authClientId(params.get("auth.client_id"));
                query.authKeyFile(params.get("auth.key_file"));
                query.authServiceAccount(params.get("auth.service_account"));
                query.datasetId(params.get("dataset_id"));
                query.projectId(params.get("project_id"));
                query.tableId(params.get("table_id"));

                connector = query;
                break;
            case "couchdb":
                CouchDB couchDB = PushConnectors.couchDB();
                couchDB.auth(params.get("auth.username"), params.get("auth.password"));
                couchDB.dbName(params.get("db_name"));
                couchDB.format(CouchDB.CouchDBFormat.valueOf(params.get("format")));
                try {
                    URI uri = new URI(params.get("url"));
                    couchDB.url(uri.getHost(), uri.getPort());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                couchDB.useSSL(params.get("use_ssl"));
                couchDB.verifySSL(params.get("verify_ssl"));

                connector = couchDB;
                break;
            case "dynamodb":
                DynamoDB dynamoDB = PushConnectors.dynamoDB();
                dynamoDB.accessKey(params.get("auth.access_key"));
                dynamoDB.region(new DynamoDB.Region(params.get("region")));
                dynamoDB.secretKey(params.get("auth.secret_key"));
                dynamoDB.table(params.get("table"));

                connector = dynamoDB;
                break;

            case "elasticsearch":
                ElasticSearch elasticSearch = PushConnectors.elasticSearch();
                elasticSearch.format(ElasticSearch.ElasticSearchFormat.valueOf(params.get("format")));
                elasticSearch.index(params.get("index"));
                elasticSearch.type(params.get("type"));
                try {
                    URI uri = new URI(params.get("url"));
                    elasticSearch.url(uri.getHost(), uri.getPort());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                connector = elasticSearch;
                break;

            case "ftp":
                FTP ftp = PushConnectors.ftp();
                try {
                    URI uri = new URI(params.get("url"));
                    ftp.url(uri.getHost(), uri.getPort());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                ftp.auth(params.get("auth.username"), params.get("auth.password"));
                try {
                    ftp.deliveryFrequency(Integer.parseInt(params.get("delivery_frequency")));
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                ftp.directory(params.get("directory"));
                ftp.filePrefix(params.get("file_prefix"));
                ftp.format(FTP.FTPFormat.valueOf(params.get("format")));
                ftp.markInProgress(Boolean.parseBoolean(params.get("mark_in_progress")));
                try {
                    ftp.maxSize(Integer.parseInt(params.get("max_size")));
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                ftp.directory(params.get("directory"));

                connector = ftp;
                break;
            case "http":
                Http http = PushConnectors.http();
                try {
                    http.maxSize(Integer.parseInt(params.get("max_size")));
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                http.auth(params.get("auth.username"), params.get("auth.password"));
                if (params.get("auth.username") != null) {
                http.authType(new Http.AuthType(params.get("value")));
                }
                try {
                    http.deliveryFrequency(Integer.parseInt(params.get("delivery_frequency")));
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                http.format(Http.HttpFormat.valueOf("format"));
                http.method(HttpMethod.valueOf("method"));
                http.url(params.get("url"));
                http.useGzip(Boolean.parseBoolean(params.get("use_gzip")));
                http.verifySSL(Boolean.parseBoolean(params.get("verify_ssl")));

                connector = http;
                break;

            case "mongodb":
                MongoDB mongoDB = PushConnectors.mongoDB();
                mongoDB.auth(params.get("auth.username"), params.get("auth.password"));
                mongoDB.collectionName(params.get("collection_name"));
                mongoDB.dbName(params.get("db_name"));
                mongoDB.format(MongoDB.MongoDBFormat.valueOf("format"));
                try {
                    URI uri = new URI(params.get("url"));
                    mongoDB.url(uri.getHost(), uri.getPort());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                connector = mongoDB;

                break;

            case "redis":
                Redis redis = PushConnectors.redis();
                try {
                    redis.port(Integer.parseInt(params.get("port")));
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                redis.host(params.get("host"));
                redis.database(params.get("database"));
                redis.format(Redis.RedisFormat.valueOf("format"));
                redis.list(params.get("list"));
                redis.password(params.get("auth.password"));

                connector = redis;

                break;

            case "s3":
                S3 s3 = PushConnectors.s3();
                s3.accessKey(params.get("auth.access_key"));
                s3.acl(params.get("acl"));
                s3.bucket(params.get("bucket"));
                try {
                    s3.deliveryFrequency(Integer.parseInt(params.get("delivery_frequency")));
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                s3.directory(params.get("directory"));
                s3.filePrefix(params.get("file_prefix"));
                s3.format(S3.S3OutputFormat.valueOf("format"));
                try {
                    s3.maxSize(Integer.parseInt(params.get("max_size")));
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                s3.secretKey(params.get("auth.secret_key"));

                connector = s3;
                break;

            case "sftp":
                SFTP sftp = PushConnectors.sftp();
                try {
                    sftp.maxSize(Integer.parseInt(params.get("max_size")));
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                sftp.filePrefix(params.get("file_prefix"));
                sftp.directory(params.get("directory"));
                sftp.auth(params.get("auth.username"), params.get("auth.password"));
                try {
                    sftp.deliveryFrequency(Integer.parseInt(params.get("delivery_frequency")));
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                sftp.format(SFTP.SFTPFormat.valueOf("format"));
                sftp.markInProgress(Boolean.parseBoolean(params.get("mark_in_progress")));
                try {
                    URI uri = new URI(params.get("url"));
                    sftp.url(uri.getHost(), uri.getPort());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                connector = sftp;

                break;
            case "splunkstormrest":
                SplunkStormRest splunkStormRest = PushConnectors.splunkStormRest();
                splunkStormRest.accessToken(params.get("auth.access_token"));
                splunkStormRest.apiHostname(params.get("api_hostname"));
                splunkStormRest.format(SplunkStormRest.SplunkStormRestFormat.valueOf("format"));
                splunkStormRest.projectId(params.get("project_id"));

                connector = splunkStormRest;
                break;

            case "splunkstorm":
                SplunkStorm splunkStorm = PushConnectors.splunkStorm();
                try {
                    URI uri = new URI(params.get("url"));
                    splunkStorm.url(uri.getHost(), uri.getPort());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                splunkStorm.format(SplunkStorm.SplunkStormFormat.valueOf(params.get("format")));

                connector = splunkStorm;
                break;

            case "splunk":
                SplunkEnterprise splunk = PushConnectors.splunkEnterprise();
                try {
                    splunk.port(Integer.parseInt(params.get("port")));
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                splunk.host(params.get("host"));
                try {
                    URI uri = new URI(params.get("url"));
                    splunk.url(uri.getHost(), uri.getPort());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                splunk.password(params.get("password"));
                splunk.username(params.get("username"));

                connector = splunk;

                break;

            case "zoomdata":
                ZoomData zoomData = PushConnectors.zoomdata();
                zoomData.username(params.get("auth.username"));
                zoomData.password(params.get("auth.password"));
                try {
                    URI uri = new URI(params.get("url"));
                    zoomData.url(uri.getHost(), uri.getPort());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                zoomData.source(params.get("source"));

                connector = zoomData;
                break;

        }

        return connector;
    }

    private static void executeHistorics(DataSiftClient dataSift, String endpoint, HashMap<String, String> params) {
        switch (endpoint) {
            case "prepare":
                printResponse(dataSift.historics().prepare(params.get("hash"), DateTime.parse(params.get("start")), DateTime.parse(params.get("end")), params.get("name")).sync());
                break;
            case "start":
                printResponse(dataSift.historics().start(params.get("id")).sync());
                break;
            case "stop":
                printResponse(dataSift.historics().stop(params.get("id"), params.get("reason")).sync());
                break;
            case "status":
                printResponse(dataSift.historics().status(new DateTime(Long.parseLong(params.get("start"))), new DateTime(Long.parseLong(params.get("end")))).sync());
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
                printResponse(dataSift.preview().create(new DateTime(Long.parseLong(params.get("start"))), Stream.fromString(params.get("hash")), params.get("params").split(",")).sync());
                break;
            case "get":
                printResponse(dataSift.preview().get(params.get("id")).sync());
                break;
        }
    }

    private static void executeSources(DataSiftClient dataSift, String endpoint, HashMap<String, String> params) {
        switch (endpoint) {
            case "create":
                //printResponse(dataSift.managedSource().create(params.get("name"), new DateTime(Long.parseLong(params.get("start"))), ).sync());
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
