package com.datasift.client.push;

import com.datasift.client.DataSiftClient;
import com.datasift.client.exceptions.IllegalDataSiftPullFormat;
import com.datasift.client.exceptions.PushSubscriptionNotFound;
import com.datasift.client.stream.Interaction;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.higgs.http.client.readers.Reader;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.IOException;

/**
 * @author Courtney Robinson <courtney@crlog.info>
 */
public class PullReader extends Reader<String> {
    public static final String
            HEADER_NEXT_CURSOR = "X-DataSift-Cursor-Next",
            HEADER_CURRENT_CURSOR = "X-DataSift-Cursor-Current",
            HEADER_FORMAT = "X-DataSift-Format",
            FORMAT_ARRAY = "json_array",
            FORMAT_META = "json_meta",
            FORMAT_NEW_LINE = "json_new_line";
    protected String currentCursor, nextCursor, format;
    //    protected JsonFactory factory;
//    protected JsonParser parser;
    protected final PulledInteractions queue;
    private boolean done, headersSet;
    protected int status = -1;
    protected int backOff = 1;
    protected int successiveNoContent;

    public PullReader(PulledInteractions queue) {
        this.queue = queue;
//        factory = new JsonFactory();
//        try {
//            parser = factory.createParser(data);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //todo as the amount of data that can be requested with pull increased, memory may become an issue
        //replace current processing with Jackson's stream processing to improve memory efficiency
        /**
         JsonToken current;
         while ((current = parser.nextToken()) != JsonToken.END_OBJECT) {
         String fieldName = parser.getCurrentName();
         if (fieldName.equals("records")) {
         if (current == JsonToken.START_ARRAY) {
         // For each of the records in the array
         while (parser.nextToken() != JsonToken.END_ARRAY) {
         // read the record into a tree model,
         // this moves the parsing position to the end of it
         JsonNode node = parser.readValueAsTree();
         // And now we have random access to everything in the object
         System.out.println("field1: " + node.get("field1").getValueAsText());
         System.out.println("field2: " + node.get("field2").getValueAsText());
         }
         } else {
         System.out.println("Error: records should be an array: skipping.");
         parser.skipChildren();
         }
         } else {
         System.out.println("Unprocessed property: " + fieldName);
         parser.skipChildren();
         }
         }
         */
    }

    @Override
    public void onStatus(HttpResponseStatus status) {
        super.onStatus(status);
        this.status = status.code();
        checkResponseStatus();
        if (this.status == 204) {
            successiveNoContent++;
        }
    }

    protected boolean checkResponseStatus() {
        switch (this.status) {
            case 404:
                throw new PushSubscriptionNotFound(response);
                //403 and 429 are rate limit status
            case 403:
            case 429:
                backOff = 30;
                break;
            //in all cases below we want to back off
            case 503: //service or one of it's dependencies is unavailable
            case 500: // all hell broke loose
            case 204: // no data
                //exponentially back off up to 60 seconds
                backOff = backOff * 2;
                if (backOff > 60) {
                    backOff = 60;
                }
                break;
            case 200:
                backOff = 0;
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void onHeaders(HttpHeaders headers) {
        super.onHeaders(headers);
        checkHeaders();
        headersSet = true;
    }

    protected void checkHeaders() {
        HttpHeaders headers = response.getHeaders();
        if (headers != null && headers.names() != null) {
            for (String k : headers.names()) {
                if (HEADER_CURRENT_CURSOR.equalsIgnoreCase(k)) {
                    currentCursor = headers.get(k);
                } else if (HEADER_NEXT_CURSOR.equalsIgnoreCase(k)) {
                    nextCursor = headers.get(k);
                } else if (HEADER_FORMAT.equalsIgnoreCase(k)) {
                    format = headers.get(k);
                }
            }
        }
    }

    @Override
    public void data(ByteBuf data) {
        buffer.writeBytes(data);
        processData();
    }

    @Override
    public void done() {
        done = true;
        processData();
    }

    protected void processData() {
        checkHeaders();
        if (backOff == 0 && //if we got a 503,500 or 204 back off will not be 0
                //if status isn't a 204 and we're not backing off then we're in an invalid state if the rest holds true
                status != 204 &&
                headersSet && (format == null || format.isEmpty())) {
            throw new IllegalDataSiftPullFormat("The DataSift API failed to provide the format of the data. " +
                    "Please raise the issue with support", response);
        }

        if (format != null) {
            switch (format) {
                case FORMAT_NEW_LINE:
                    if (done) {
                        //chunked responses will cause new line to break
                        // early so only do it when he entire response is received
                        readLineByLine();
                    }
                    break;
                case FORMAT_ARRAY:
                    readArray();
                    break;
                case FORMAT_META:
                    readObject();
                    break;
                default:
                    throw new IllegalDataSiftPullFormat("DataSift format '" + format + "' is not supported", response);
            }
        }
    }

    protected void send(Interaction interaction) {
        queue.add(interaction);
    }

    protected void readObject() {
        //until we support JSON stream processing only do this when the entire response is read
        if (done) {
            String data = getDataAsString();
            try {
                ObjectNode meta = (ObjectNode) DataSiftClient.MAPPER.readTree(data);
                ArrayNode interactions = (ArrayNode) meta.get("interactions");
                for (JsonNode interaction : interactions) {
                    send(new Interaction(interaction));
                }
                buffer.discardReadBytes();
            } catch (IOException e) {
                log.warn("Failed to decode interactions", e);
            }
        }
    }

    protected void readArray() {
        //until we support JSON stream processing only do this when the entire response is read
        if (done) {
            String data = getDataAsString();
            try {
                ArrayNode interactions = (ArrayNode) DataSiftClient.MAPPER.readTree(data);
                for (JsonNode interaction : interactions) {
                    send(new Interaction(interaction));
                }
                buffer.discardReadBytes();
            } catch (IOException e) {
                log.warn("Failed to decode interactions", e);
            }
        }
    }

    protected void readLineByLine() {
        String line;
        try {
            while ((line = data.readLine()) != null) {
                //System.out.println(line);
                ObjectNode interaction = (ObjectNode) DataSiftClient.MAPPER.readTree(line);
                send(new Interaction(interaction));
            }
            buffer.discardReadBytes();
        } catch (IOException e) {
            log.info("Failed to decode interaction ", e);
        }
    }

    protected String getDataAsString() {
        String str = buffer.toString(0, buffer.writerIndex(), utf8);
        buffer.readerIndex(buffer.writerIndex());
        return str;
    }

    public void reset() {
        //only reset back off if the current request was successful and we've reached 1 minute back off
        if (status == 200 || backOff >= 60) {
            successiveNoContent = 0;
            backOff = 1;
        }
        currentCursor = nextCursor = format = null;
        done = false;
        status = -1;
        headersSet = false;
    }
}
