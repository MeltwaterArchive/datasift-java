DataSift API
============

This is the official Java library for accessing DataSift. See the examples
folder for some simple example usage. The build file can build javadoc
documentation and a JAR distribution file.

The unit tests should be run with junit.

All examples and tests use the username and API key in Config.java.

Download the JAR file at https://github.com/datasift/datasift-java/downloads

Note that we use [git flow](https://github.com/nvie/gitflow) to manage development.

Requirements
------------

The following libraries are included in the lib folder.

* HttpClient from the Apache Software Foundation (4.1.1 included)
  http://hc.apache.org/httpcomponents-client-ga/

* Trove (3.0.3 included)
  http://trove.starlight-systems.com/

* Junit (4.9b2 included)
  http://www.junit.org/

The library will use SSL connections by default. While we recommend using SSL
you may disable it if required by passing false as the third parameter when
creating a user, or by calling user.enableSSL(false) on the user object.

License
-------

All code contained in this repository is Copyright 2011-2012 MediaSift Ltd.

This code is released under the BSD license. Please see the LICENSE file for
more details.

Changelog
---------

* v.2.2.0 Finalised support for Historics and addedsupport for Push
          delivery (2012-08-17)

* v.2.1.1 Fixed some bugs in the websocket reconnection logic (2012-07-26)

* Added the develop branch as required by git flow (2012-08-15)

* v.2.0.0 Added onConnect, onDisconnect and onStatus events, improved
          efficiency within the WebSocket library, and support for the
          historics API (2012-06-22). Please note: Though support has been
          added for the the Historics API, the API itself is not yet publicly
          available. Access will be made available over the coming months. 

  The IStreamConsumerEvents and IMultiStreamConsumerEvents interfaces now
  contain callbacks for onConnect, onDisconnect and onStatus events. These
  MUST be implemented in your event handler.

* v.1.3.4 Added SSL support for HTTP streams (2012-05-15)

  This is enabled by default and can be disabled by passing false as the third
  parameter to the User constructor, or calling enableSSL(false) on the User
  object.

* v.1.3.3 Subscriptions are no longer lost on reconnection (2012-05-15)

* v.1.3.2 Added User-Agent header to the StreamConsumers (2012-03-13)

* v.1.3.1 Do not reconnect when a 4xx response is received (2012-03-08)

* v.1.3.0 Stream warning/error handling + WS disconnection changes (2012-03-08)

  The IStreamConsumerEvents and IMultiStreamConsumerEvents interfaces now
  contain callbacks for onWarning and onError events. These MUST be implemented
  in your event handler.
  
  The WebSocket consumer has been modified to request that the server
  unsubscribe from all streams and disconnect rather than simply closing
  the connection. This change is largely transparent except that you'll get
  an error notification acknowledging the request and indicating that the
  server is about to disconnect. The client will then wait up to 30 seconds
  for the server to actually disconnect before closing the socket itself. This
  should never happen as the server will usually disconnect immediately after
  sending that message.

* v.1.2.1 Clarification (2012-02-27)

  Modified the football example to display the IDs of interactions so you can
  visually match deletion notifications with the original tweet.
  
  NB: If you are using an implementation of IStreamConsumerEvents to handle
  events, you must implement the new onDeleted method or the code will throw
  an exception when a delete notification is received. The multi-stream
  equivalent, IMultiStreamConsumerEvents, also contains a new onDeleted
  method which must be implemented.

* v.1.2.0 Twitter Compliance (2012-02-26)

  The StreamConsumer now defines an onDeleted(Interaction interaction) method
  to handle DELETE requests from Twitter.
  (@see http://dev.datasift.com/docs/twitter-deletes)

  NB: You must implement this method in your code if you extend StreamConsumer.

