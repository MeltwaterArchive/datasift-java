DataSift API
============

This is the official Java library for accessing DataSift. See the examples
folder for some simple example usage. The build file can build javadoc
documentation and a JAR distribution file.

The unit tests should be run with junit.

All examples and tests use the username and API key in Config.java.

Requirements
------------

The following libraries are included in the lib folder.

* HttpClient from the Apache Software Foundation (4.1.1 included)
  http://hc.apache.org/httpcomponents-client-ga/

* Junit (4.9b2 included)
  http://www.junit.org/


License
-------

All code contained in this repository is Copyright 2011 MediaSift Ltd.

This code is released under the BSD license. Please see the LICENSE file for more details.

Changelog
---------

* v.1.2.1 Compile-time checks (TBD)

  The callback methods in the abstract StreamConsumer class are now abstract,
  to avoid errors at runtime.

* v.1.2.0 Twitter Compliance (2012-02-26)

  The StreamConsumer now defines an onDeleted(Interaction interaction) method to handle
  DELETE requests from Twitter (@see http://dev.datasift.com/docs/twitter-deletes).

  NB: You must implement this method in your code if you extend StreamConsumer.

