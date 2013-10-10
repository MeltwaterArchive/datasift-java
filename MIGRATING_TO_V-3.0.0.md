Breaking changes
===

Earlier versions of the DataSift library are incompatible with 3.x.x. Put bluntly 3.0.0 is a complete re-design. In order to continue delivering better features and performance some architectural changes had to be made that made backwards compatibility very difficult and in some cases impractical.

Features
===

The new library introduces a few things that will need getting used to if you've been a long time user of the old client. In particular:

* All HTTP requests are now asynchronous by default.
* All HTTP responses which are wrapped in "FutureData<T>" can be made synchronous by calling the sync() method.
* Previous versions of the were oriented around a "user" object. Version 3+ changes to be oriented around a DataSiftClient object. Which at a minimum makes more features feel semantically correct
* The old client used HTTP to stream data, this pushed the envelope for what HTTP should do...just a bit, when we introduced multi-streams we're sure we were breaking some unwritten rule. To remedy this, the new client uses WebSockets for all streaming, in fact the new client only does multi-streams internally but the API hides this so that you can just subscribe to one or as many streams as you like without worrying about anything else.
* All features that were missing in the previous client have been added to 3.0.0
* Every DataSift result type has common methods to check if the request was successful, if authentication failed, rate limits and so on.
* Where it was previously set so that strings are used for various things that never change, the new client defines proper types as either enums or data classes. Essentially we've tried to make as many things type safe where it seemed appropriate.
* Where parameters are optional an overloaded version as been provided so that you don't have to pass null etc for params you don't want to specify
* A lot more verification is done in the client on parameters before making a request, saving you the round trip and possibly valuable DPUs and rate limit requests.
* All currently supported [push connectors](http://dev.datasift.com/docs/push/connectors) have a type safe way of generating the parameters required.

Maven
===

There has been a ticket open to have the client in maven for a very long time, for various reasons this has been on hold. The new version has removed all obstacles that delayed that happening. The client is now and will continue to be in maven central. Add it to your project using:

```xml

<dependency>
    <groupId>com.datasift.client</groupId>
    <artifactId>datasift-java</artifactId>
    <version>3.0.0-Alpha1</version>
</dependency>

```

Code: Basics
===

__Note that the examples in this migration guide use .sync() to present a synchronous comparison of the old and new client, however by default v3+ is asynchronous.__
Code examples are available in [src/main/java/com/datasift/client/examples](src/main/java/com/datasift/client/examples).

In prior versions of the client you would begin with this:

```java

User user = new User(Config.username, Config.api_key);

```
In version 3+ you begin by providing a configuration object to the DataSift client. From there the client instance gives you access to the rest of the DataSift API. This is organized in a similar way to the [DataSift REST API documentation](http://dev.datasift.com/docs/rest-api) except where it didn't make sense to do so.

```java

DataSiftConfig config = new DataSiftConfig("username", "api-key");

DataSiftClient datasift = new DataSiftClient(config);

```

### Meet DataSiftResult

All API response objects extend DataSiftResult. This base class comes with a few useful methods that is generally useful to know about any request/response for example.

```java

DataSiftResult result = datasift.core().compile(csdl).sync();
//is successful returns true if a response hasn't explicitly been marked as failed,
//there is a valid response, no exceptions are set and the response status is between 200 - 399
if (result.isSuccessful()) {
    //if true an exception may have caused the request to fail, inspect the cause if available
    if (result.failureCause() != null) { //may not be an exception
        result.failureCause().printStackTrace();
    }
    return;
}
//is true if isSuccessful() == true and the response status is not 401
result.isAuthorizationSuccesful();
//allows access to the response object which you can get the request and JSON string response from
result.getResponse();
//gets the rate limit DataSift returned with the response, use it to keep track of usage
result.rateLimit();
//returns the cost of executing the request which produced this result
result.rateLimitCost();
//what's left of your rate limit quota
result.rateLimitRemaining();

```

This means the client provides access to

Core
---

* datasift.__core()__.validate(...)
* datasift.__core()__.compile(...)
* datasift.__core()__.usage(...)
* datasift.__core()__.dpu(...)
* datasift.__core()__.balance(...)

###  Validate: <3.0.0

```java

Definition def = user.createDefinition(csdl);
try{
def.compile();

}catch(EInvalidData e){
    //try to determine what went wrong here...
}

```

###  Validate: 3.0.0+

```java

//synchronously validate a CSDL
Validation validation = datasift.core().validate(csdl).sync();
if (validation.isValid()) {
    //CSDL is valid...
}

```

### Compile: <3.0.0


```java

Definition def = user.createDefinition(csdl);
def.compile();

```

### Compile: 3.0.0+

```java

//notice .sync(), this makes the compile operation block until the HTTP request is complete
Stream stream = datasift.core().compile(csdl).sync();

//alternatively will return immediately and allows you to be notified when the result is available if you subscribe to onData(...)
FutureData<Stream> stream = datasift.core().compile(csdl);
//as in
stream.onData(new FutureResponse<Stream>() {
    public void apply(Stream data) {
        System.out.println(data);
    }
});

```

###  Usage: <3.0.0

```java

User user = new User(Config.username, Config.api_key);
user.getUsage();

```

###  Usage: 3.0.0+

```java

datasift.core().usage().sync();

```

###  DPU: <3.0.0

```java

Definition def = user.createDefinition(...);
def.getDPUBreakdown();

```
###  DPU: 3.0.0+

```java

Stream stream = Stream.fromString("13e9347e7da32f19fcdb08e297019d2e");
Dpu dpu = datasift.core().dpu(stream).sync();

```

###  Balance: <3.0.0

__no implemented__

###  Balance: 3.0.0+

```java

Balance balance = datasift.core().balance().sync();

```

Live Streams
---

### Streaming: <3.0.0

```java

try {
    // Authenticate
    System.out.println("Creating user...");
    _user = new User(Config.username, Config.api_key);

    // Get the hash list from the parameters
    System.out.println("Building hash list...");

    // Create the consumer
    System.out.println("Getting the consumer...");
    StreamConsumer consumer = StreamConsumer.factory(_user, StreamConsumer.TYPE_WS, this);

    // Subscribe
    for (String hash : hashes) {
        boolean tryagain = true;
        while (tryagain) {
            try {
                consumer.subscribe(hash);
                System.out.println("Subscribing to \"" + hash + "\"...");
                tryagain = false;
                Thread.sleep(100);
            } catch (EAPIError e) {
                if (!e.getMessage().contains("not connected")) {
                    throw e;
                }
            }
        }
    }

    // And start consuming
    System.out.println("Consuming...");
    System.out.println("--");
    consumer.consume();
} catch (EInvalidData e) {
    System.out.print("InvalidData: ");
    System.out.println(e.getMessage());
} catch (ECompileFailed e) {
    System.out.print("CompileFailed: ");
    System.out.println(e.getMessage());
} catch (EAccessDenied e) {
    System.out.print("AccessDenied: ");
    System.out.println(e.getMessage());
} catch (EAPIError e) {
    System.out.print("APIError: ");
    System.out.println(e.getMessage());
} catch (InterruptedException e) {
    System.out.print("InterruptedException: ");
    System.out.println(e.getMessage());
}

```

### Streaming: 3.0.0+


```java

    public static void main(String... args) throws InterruptedException {
        DataSiftConfig config = new DataSiftConfig("username", "api-key");
        final DataSiftClient datasift = new DataSiftClient(config);

        Stream stream = Stream.fromString("13e9347e7da32f19fcdb08e297019d2e");

        //handle exceptions that can't necessarily be linked to a specific stream
        datasift.liveStream().onError(new ErrorHandler());

        //handle delete message
        datasift.liveStream().onStreamEvent(new DeleteHandler());

        //process interactions
        datasift.liveStream().subscribe(new Subscription(stream));
        //process interactions for another stream
        datasift.liveStream().subscribe(new Subscription(Stream.fromString("another-stream-hash")));

        //at some point later if you want unsubscribe
        datasift.liveStream().unsubscribe(stream);
    }

    public static class Subscription extends StreamSubscription {
        public Subscription(Stream stream) {
            super(stream);
        }

        public void onDataSiftLogMessage(DataSiftMessage di) {
            //di.isWarning() is also available
            System.out.println((di.isError() ? "Error" : di.isInfo() ? "Info" : "Warning") + ":\n" + di);
        }

        public void onMessage(Interaction i) {
            System.out.println("INTERACTION:\n" + i);
        }
    }

    public static class DeleteHandler extends StreamEventListener {
        public void onDelete(DeletedInteraction di) {
            //go off and delete the interaction if you have it stored. This is a strict requirement!
            System.out.println("DELETED:\n " + di);
        }
    }

    public static class ErrorHandler extends ErrorListener {
        public void exceptionCaught(Throwable t) {
            t.printStackTrace();
            //do something useful...
        }
    }

```

You can use the live streams API to consume a stream in real time by using something similar to:

```java

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


```

Notice that we provide onError and onStreamEvent listeners before subscribing, that's because they're required but you only need to provide them once and then you can subscript to as many streams as you like....

### onError

Subscribes a callback to listen for exceptions that may occur during streaming.
When exceptions occur it is unlikely we'll know which stream/subscription caused the exception so instead of notifying all stream subscribers of the same exception this provides a way to get the error just once.

### onStreamEvent

Twitter sends us a notification whenever one of their users deletes a Tweet. We pass these notifications on to you as part of your stream. If you are storing Tweets you must take account of all of these delete messages in order to comply with Twitter's Terms of Service. For more information on deletes see the DataSift documentation on [Twitter Delete Messages](http://dev.datasift.com/docs/resources/twitter-deletes)

Push
--

* datasift.__push()__.validate(...)
* datasift.__push()__.create(...)
* datasift.__push()__.pause(...)
* datasift.__push()__.resume(...)
* datasift.__push()__.update(...)
* datasift.__push()__.stop(...)
* datasift.__push()__.delete(...)
* datasift.__push()__.log(...)
* datasift.__push()__.get(...)
* datasift.__push()__.pull(...)

### Push: <3.0.0

```java

try {
    User user = new User("username", "api-key");
    // Create the PushDefinition
    PushDefinition pushDef = user.createPushDefinition();
    pushDef.setOutputType("s3");

    // Now add the output_type-specific args from the command line
    pushDef.setOutputParam("auth.access_key", "aws-key");
    pushDef.setOutputParam("auth.secret_key", "aws-secret");
    pushDef.setOutputParam("bucket", "bucket-name");
    pushDef.setOutputParam("directory", "directory");
    pushDef.setOutputParam("acl", "private");
    pushDef.setOutputParam("delivery_frequency", "0");
    pushDef.setOutputParam("max_size", "10485760");
    pushDef.setOutputParam("file_prefix", "DataSiftJava-");
    // Subscribe the definition to the hash
    PushSubscription pushSub = pushDef.subscribeStreamHash(hash, name);
} catch (EInvalidData e) {
    System.err.println("EInvalidData: " + e.getMessage());
} catch (EAPIError e) {
    System.err.println("EAPIError: " + e.getMessage());
} catch (EAccessDenied e) {
    System.err.println("EAccessDenied: " + e.getMessage());
}

```

### Push: 3.0.0+

```java

S3 s3 = PushConnectors
        .s3()
        .accessKey(settings.getS3AccessKey())
        .secretKey(settings.getS3SecretKey())
        .bucket("apitests")
        .directory("java-client")
        .acl("private")
        .deliveryFrequency(0)
        .maxSize(10485760)
        .filePrefix("DataSiftJava-");

PushValidation pv = datasift.push().validate(s3).sync();

if(pv.isSuccessful()){
 //valid push configuration submitted
 PushSubscription subscription = datasift.push().create(s3, stream, name).sync();
 //change name or other config
 s3.bucket("new-bucket");
 PushSubscription updatedSubscription = datasift.push().update(subscription.getId(), s3, "New name").sync();
 PushSubscription pausedSubscription = datasift.push().pause(subscription.getId()).sync();
 PushSubscription activeSubscription = datasift.push().resume(pausedSubscription.getId()).sync();
}

```

Historics
---

* datasift.__historics()__.prepare(...)
* datasift.__historics()__.start(...)
* datasift.__historics()__.stop(...)
* datasift.__historics()__.status(...)
* datasift.__historics()__.update(...)
* datasift.__historics()__.delete(...)
* datasift.__historics()__.get(...)


### Historics: <3.0.0

```java

        try {
            User user = new User("username", "api-key");
            // Create the Historic
            Historic historic = user.createHistoric(stream_hash, start, end, sources, sample, name);
            historic.prepare();
            historic.start();
            historic.stop();
            historic.delete();
            user.getHistoric("id...");
            user.listHistorics();
        } catch (EInvalidData e) {
            System.err.println("EInvalidData: " + e.getMessage());
        } catch (EAccessDenied e) {
            System.err.println("EAccessDenied: " + e.getMessage());
        } catch (EAPIError eapiError) {
            System.err.println("EAPIError: " + eapiError.getMessage());
        }

```

### Historics: 3.0.0+

```java

DataSiftConfig config = new DataSiftConfig("zcourts", "44067e0ff342b76b52b36a63eea8e21a");
DataSiftClient datasift = new DataSiftClient(config);
Stream stream = Stream.fromString("13e9347e7da32f19fcdb08e297019d2e");

S3 s3 = PushConnectors
        .s3()
        .accessKey("s3-access-key")
        .secretKey("s3-access-key")
        .bucket("apitests")
        .directory("java-client")
        .acl("private")
        .deliveryFrequency(0)
        .maxSize(10485760)
        .filePrefix("DataSiftJava-");

DateTime fiveHrsAgo = DateTime.now().minusHours(5);
DateTime fourHrsAgo = fiveHrsAgo.plusHours(1);
String name = "My awesome Historics";
//prepare our query
PreparedHistoricsQuery query = datasift.historics().prepare(stream.hash(), fiveHrsAgo, fourHrsAgo, name).sync();
//have to create a push subscription to the newly create historics before starting
datasift.push().create(s3, stream, "Subscription name").sync();
DataSiftResult historics = datasift.historics().start(query).sync();

//stop a query
datasift.historics().stop(query, "some reason").sync();
//delete a query
datasift.historics().delete(query).sync();
//get a single historics query
datasift.historics().get(query.getId()).sync();
//get your list of historics
datasift.historics().list().sync();


```

Historics preview
---

* datasift.__preview()__.create(...)
* datasift.__preview()__.get(...)


### Push: <3.0.0

__not implemented__


### Push: 3.0.0+

```java

DataSiftConfig config = new DataSiftConfig("username", "api-key");

final DataSiftClient datasift = new DataSiftClient(config);
Stream stream = Stream.fromString("13e9347e7da32f19fcdb08e297019d2e");
DateTime fiveHrsAgo = DateTime.now().minusHours(5);
HistoricsPreview preview = datasift.preview().create(fiveHrsAgo, stream,
        new String[]{ "interaction.author.link,targetVol,hour;interaction.type,freqDist,10" }).sync();
//can also later get the preview data
datasift.preview().get(preview);
//or
datasift.preview().get(preview.id());


```

Managed sources
---

* datasift.__source()__.create(...)
* datasift.__source()__.update(...)
* datasift.__source()__.delete(...)
* datasift.__source()__.log(...)
* datasift.__source()__.get(...)
* datasift.__source()__.stop(...)
* datasift.__source()__.start(...)

### Managed sources: <3.0.0

__not implemented__

### Managed sources: 3.0.0+


```java

//Instagram, GooglePlus and Yammer also available
FacebookPage source = new FacebookPage(config);
String fbToken = "facebook-long-lived-access-token";

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
    Stream stream = datasift.core().compile(String.format("interaction.content contains \"news\" AND source.id == \"%s\"",managedSource.getId())).sync();
    System.out.println(managedSource);
}

```