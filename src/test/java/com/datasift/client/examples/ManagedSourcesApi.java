package com.datasift.client.examples;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.core.Stream;
import com.datasift.client.managedsource.ManagedSource;
import com.datasift.client.managedsource.sources.BaseSource;
import com.datasift.client.managedsource.sources.FacebookPage;

public class ManagedSourcesApi {
    private ManagedSourcesApi() throws InterruptedException {

        final DataSiftClient datasift = new DataSiftClient(config);

        FacebookPage source = new FacebookPage(config);
        String fbToken = "some-facebook-token";

        source.addOAutToken(fbToken, "name", 1381406400);
        //username or id is valid
        source.addPage("theguardian", "https://www.facebook.com/theguardian", "a name for this");
        //or use the ID of the facebook page
        //source.addPage("10513336322", "https://www.facebook.com/theguardian", "a name for this");

        //enable options using
        source.enableComments(true)
                .enableLikes(true)
                .enablePageLikes(false)
                .enablePostsByOthers(true);
        //or all at once
        source.setParams(true, true, true);
        ManagedSource managedSource = datasift.managedSource().create("My managed source", source).sync();
        if (managedSource.isSuccessful()) {
            //and now we can do filtering on this page e.g.
            Stream stream = datasift
                    .compile(String.format("interaction.content contains \"news\" AND source.id == \"%s\"",
                            managedSource.getId())).sync();

            System.out.println(managedSource);

            managedSource = datasift.managedSource().addAuth(managedSource.getId(), false, "a-new-token").sync();
            managedSource = datasift.managedSource().removeAuth(managedSource.getId(), "a-new-token").sync();

            BaseSource.ResourceParams resource = new BaseSource.ResourceParams();
            resource.set("id", "bbcnews");
            resource.set("param1", "value1");
            managedSource = datasift.managedSource().addResource(managedSource.getId(), false, resource).sync();
            String resourceId = managedSource.getResources().toArray(new BaseSource.ResourceParams[1])[0].getId();
            datasift.managedSource().removeResource(managedSource.getId(), resourceId).sync();
        }
    }

    public static void main(String... args) throws InterruptedException {
        new ManagedSourcesApi();
    }
}
