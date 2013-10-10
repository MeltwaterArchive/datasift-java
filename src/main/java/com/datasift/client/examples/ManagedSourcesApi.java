package com.datasift.client.examples;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.core.Stream;
import com.datasift.client.managedsource.ManagedSource;
import com.datasift.client.managedsource.sources.FacebookPage;

public class ManagedSourcesApi {
    private ManagedSourcesApi() throws InterruptedException {
        DataSiftConfig config = new DataSiftConfig("username", "api-key");

        final DataSiftClient datasift = new DataSiftClient(config);

        FacebookPage source = new FacebookPage(config);
        String fbToken = "long-lived-facebook-api-token";

        source.addOAutToken(fbToken, "name", 1381406400);
        //username or id is valid
        source.addPage("theguardian", "https://www.facebook.com/theguardian", "a name for this");
        //or use the ID of the facebook page
        //source.addPage("10513336322", "https://www.facebook.com/theguardian", "a name for this");

        //enable options using
        source.enableComments(true)
                .enableLikes(true)
                .enablePostsByOthers(true);
        //or all at once
        source.setParams(true, true, true);
        ManagedSource managedSource = datasift.managedSource().create("My managed source", source).sync();
        if (managedSource.isSuccessful()) {
            //and now we can do filtering on this page e.g.
            Stream stream = datasift.core()
                    .compile(String.format("interaction.content contains \"news\" AND source.id == \"%s\"",
                            managedSource.getId())).sync();

            System.out.println(managedSource);
        }
    }

    public static void main(String... args) throws InterruptedException {
        new ManagedSourcesApi();
    }
}
