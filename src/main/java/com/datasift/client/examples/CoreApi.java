package com.datasift.client.examples;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.FutureData;
import com.datasift.client.FutureResponse;
import com.datasift.client.core.Dpu;
import com.datasift.client.core.Stream;
import com.datasift.client.core.Validation;
import com.datasift.client.stream.DataSiftMessage;
import com.datasift.client.stream.DeletedInteraction;
import com.datasift.client.stream.ErrorListener;
import com.datasift.client.stream.Interaction;
import com.datasift.client.stream.StreamEventListener;
import com.datasift.client.stream.StreamSubscription;

public class CoreApi {
    private CoreApi() {
    }

    public static void main(String... args) throws InterruptedException {
        DataSiftConfig config = new DataSiftConfig("username", "api-key");
        //or
        config = new DataSiftConfig().auth("zcourts", "44067e0ff342b76b52b36a63eea8e21a");

        final DataSiftClient datasift = new DataSiftClient(config);
        String csdl = "interaction.content contains \"some string\"";
        //both sync and async processing are supported by calling "sync" on any FutureDate object

        //synchronously validate a CSDL
        Validation validation = datasift.core().validate(csdl).sync();
        if (validation.hasFailed()) {
            //if true an exception may have caused the request to fail, inspect the cause if available
            if (validation.failureCause() != null) { //may not be an exception
                validation.failureCause().printStackTrace();
            }
            return;
        }
        System.out.println(validation);
        if (validation.isSuccessful()) {
            //we now know it's valid so asynchronously compile the CSDL and obtain a stream
            FutureData<Stream> stream = datasift.core().compile(csdl);
            FutureData<Dpu> dpus = datasift.core().dpu(stream);
            dpus.onData(new FutureResponse<Dpu>() {
                public void apply(Dpu data) {
                    System.out.println(data);
                }
            });
        }
        System.out.println(datasift.core().balance().sync());
        System.out.println(datasift.core().usage().sync());

        datasift.liveStream().onError(new ErrorListener() {
            public void exceptionCaught(Throwable t) {
                t.printStackTrace();
            }
        });

        datasift.liveStream().onStreamEvent(new StreamEventListener() {
            public void onDelete(DeletedInteraction di) {
                System.out.println("DELETED:\n " + di);
            }
        });

        Stream stream = Stream.fromString("13e9347e7da32f19fcdb08e297019d2e");

        datasift.liveStream().subscribe(new StreamSubscription(stream) {
            public void onDataSiftLogMessage(DataSiftMessage di) {
                //di.isWarning() is also available
                System.out.println((di.isError() ? "Error" : di.isInfo() ? "Info" : "Warning") + ":\n" + di);
            }

            public void onMessage(Interaction i) {
                System.out.println("INTERACTION:\n" + i);
            }
        });
    }
}
