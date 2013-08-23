package com.datasift.client;

import com.datasift.client.core.Dpu;
import com.datasift.client.core.Stream;
import com.datasift.client.core.Validation;

public class Main {
    public static void main(String... args) throws InterruptedException {
        DataSiftConfig config = new DataSiftConfig("username", "api-key");
        //or
        config = new DataSiftConfig().auth("zcourts", "44067e0ff342b76b52b36a63eea8e21a");
        final DataSiftClient datasift = new DataSiftClient(config);
        String csdl = "interaction.content contains \"some string\"";
        //both sync and async processing are supported by calling "sync" on any FutureDate object

        //synchronously validate a CSDL
        Validation validation = datasift.core().validate(csdl).sync();
        System.out.println(validation);
        if (validation.isSuccessful()) {
            //we now know it's valid so asynchronously compile the CSDL and obtain a stream
            datasift.core().compile(csdl)
                    .onData(new FutureResponse<Stream>() {
                        public void apply(Stream stream) {
                            System.out.println(stream.hash());
                            FutureData<Dpu> dpus = datasift.core().dpu(stream);
                            dpus.onData(new FutureResponse<Dpu>() {
                                public void apply(Dpu data) {
                                    System.out.println(data);
                                }
                            });
                        }
                    });
        }
        System.out.println(datasift.core().balance().sync());
        System.out.println(datasift.core().usage().sync());
        //clean up and free up any resources -  ONLY WHEN  FINISHED
        //A java.nio.channels.ClosedChannelException may occur if you try to make an API call after calling shutdown
        //datasift.shutdown();
    }
}