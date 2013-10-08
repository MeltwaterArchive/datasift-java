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

Code examples are available in [src/main/java/com/datasift/client/examples](src/main/java/com/datasift/client/examples).

You begin by providing a configuration object to the DataSift client. From there the client instance gives you access to the rest of the DataSift API. This is organized in a similar way to the [DataSift REST API documentation](http://dev.datasift.com/docs/rest-api) except where it didn't make sense to do so.

```java

DataSiftConfig config = new DataSiftConfig("username", "api-key");

DataSiftClient datasift = new DataSiftClient(config);

```

This means the client provides access to

Core
---

* datasift.__core()__.validate(...)
* datasift.__core()__.compile(...)
* datasift.__core()__.usage(...)
* datasift.__core()__.dpu(...)
* datasift.__core()__.balance(...)

Live Streams
---
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

Historics
---

* datasift.__historics()__.prepare(...)
* datasift.__historics()__.start(...)
* datasift.__historics()__.stop(...)
* datasift.__historics()__.status(...)
* datasift.__historics()__.update(...)
* datasift.__historics()__.delete(...)
* datasift.__historics()__.get(...)

Historics preview
---

* datasift.__preview()__.create(...)
* datasift.__preview()__.get(...)

Managed sources
---

* datasift.__source()__.create(...)
* datasift.__source()__.update(...)
* datasift.__source()__.delete(...)
* datasift.__source()__.log(...)
* datasift.__source()__.get(...)
* datasift.__source()__.stop(...)
* datasift.__source()__.start(...)
