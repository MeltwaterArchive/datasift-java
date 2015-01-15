package com.datasift.client.examples;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.core.Stream;
import com.datasift.client.dynamiclist.DataSiftDynamicList;
import com.datasift.client.dynamiclist.DynamicList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christopher Gilbert <christopher.john.gilbert@gmail.com>
 */
public class DynamicListApi {
    private DynamicListApi() throws InterruptedException {
        DataSiftConfig config = new DataSiftConfig("username", "api-key");

        DataSiftClient datasift = new DataSiftClient(config);
        DataSiftDynamicList dynamiclist = new DataSiftDynamicList(config);

        DynamicList list = dynamiclist.create(DataSiftDynamicList.ListType.STRING, "example list").sync();
        if (list.isSuccessful()) {
            // create a stream that references the dynamic list, using the list_any operator
            Stream stream = datasift
                    .compile(String.format("interaction.content list_any \"%s\"", list.getId())).sync();

            // even after the stream was created, we can manipulate the list, even if the stream is running
            List<String> items = new ArrayList<>();
            items.add("keyword1");
            dynamiclist.add(list, items);

            System.out.println(list);
        }
    }

    public static void main(String... args) throws InterruptedException {
        new DynamicListApi();
    }
}
