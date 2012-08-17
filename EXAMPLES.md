# DataSift Java Client Library Examples

The examples have been designed to demonstrate the most common
use cases. This file contains a brief description of each example.

Please note that running these examples will incur DPU costs against your account.

Unless stated otherwise all of the examples use the username and API key in Config.java, so you'll probably want to set those before building the jar file.

## ConsumeStream

This example shows the basic structure of a streaming client. It takes a stream hash on the command line and consumes it, displaying the details of events as they occur.

    java -cp datasift-java-2.1.0 org.datasift.examples.ConsumeStream 0a4c11d2e90ddee8483e2c68061cbbf5

ConsumeStreamWS file does the same but uses a websocket connection instead of HTTP, and can take multiple stream hashes on the command line which it consumes using a single connection.

## Deletes

Correct handling of delete notifications is a requirement of your data usage agreement with DataSift. This example demonstrates how delete notifications will be received by your code.

    java -cp datasift-java-2.1.0 org.datasift.examples.Deletes

DeletesWS file does the same but using a websocket connection instead of HTTP.

## DPUBreakdown

All usage of the DataSift platform incurs costs in units of DPU. This example shows how to retrieve a breakdown of the DPU cost for a stream defined by the CSDL in the file given on the command line.

    java -cp datasift-java-2.1.0 org.datasift.examples.DPUBreakdown csdl.txt

## Football

You might want to collect a sample of data from a given stream, and that's what the football example demonstrates. The stream looks for anything containing the word "football" and outputs them as they are received. Once it has received ten interactions it stops the consumer and exits.

    java -cp datasift-java-2.1.0 org.datasift.examples.Football

## TwitterTrack

Once upon a time, when the Twitter streaming API was barely out of diapers, they introduced a feature that enabled you to receive everything that mentioned one or more words. This example implements that functionality.

Call the script with the words or phrases you're interested in as command line arguments and it will display matching tweets as they are received.

    java -cp datasift-java-2.1.0 org.datasift.examples.TwitterTrack olympics london2012 "london 2012" "boris johnson" locog

## Historics

In the root of the project you'll find a shell script called historics. This wraps up calling the Historics examples to make them easier to use. The actual examples can be found in src/org/datasift/examples/historics.

To use it you pass in your username and API key, followed by the command you want to run and the arguments that command expects.

    ./historics <username> <api_key> <command> <arg1> <arg2> .. <argn>

### Historics commands

* **CreateFromCSDL**

   Creates a new historic query from a file containing CSDL. Call this command without any arguments for usage information, or refer to the usage function in the code.

        ./historics your_username your_api_key CreateFromCSDL \
            csdl.txt "2012-08-01 12:00:00" "2012-08-01 13:00:00" twitter \
            "nexus 7" 100

* **CreateFromHash**

  Creates a new historic query from a stream hash. Call this command without any arguments for usage information, or refer to the usage function in the code.

        ./historics your_username your_api_key CreateFromHash \
            0a4c11d2e90ddee8483e2c68061cbbf5 "2012-08-01 12:00:00" \
            "2012-08-01 13:00:00" twitter "nexus 7" 100

* **Delete**

  Delete one or more Historics queries.

        ./historics your_username your_api_key Delete 58a83a9bdc2880de7fe6

* **List**

  List the Historics queries in your account.

        ./historics your_username your_api_key List

* **Start**

  Start a Historics query.

        ./historics your_username your_api_key Start 58a83a9bdc2880de7fe6

* **Stop**

  Stop a Historics query.

        ./historics your_username your_api_key Stop 58a83a9bdc2880de7fe6

* **View**

  View the details of a Historics query.

        ./historics your_username your_api_key View 58a83a9bdc2880de7fe6

## push

In the root of the project you'll find a shell script called push. This wraps up calling the Push examples to make them easier to use. The actual examples can be found in src/org/datasift/examples/push.

To use it you pass in your username and API key, followed by the command you want to run and the arguments that command expects.

    ./push <username> <api_key> <command> <arg1> <arg2> .. <argn>

### Push commands

* **PushFromHash**

  Creates a new Push subscription from a stream hash. Call this command without any arguments for usage information, or refer to the usage function in the code.

        ./push your_username your_api_key PushFromHash http stream \
            0a4c11d2e90ddee8483e2c68061cbbf5 \"Nexus 7 Push\" \
            delivery_frequency=10 url=http://www.example.com/push_endpoint \
            auth.type=basic auth.username=myuser auth.password=mypassword

* **PushStreamFromCSDL**

  Creates a new stream from the supplied CSDL, and creates and activates a Push subscription to receive the data. Call this command without any arguments for usage information, or refer to the usage function in the code.

        ./push your_username your_api_key PushFromHash http stream \
            0a4c11d2e90ddee8483e2c68061cbbf5 \"Nexus 7 Push\" \
            delivery_frequency=10 url=http://www.example.com/push_endpoint \
            auth.type=basic auth.username=myuser auth.password=mypassword

* **PushHistoricFromCSDL**

  Creates a new Historics query from the supplied CSDL, creates a Push subscription to receive the data, and starts the Historics query.

        ./push your_username your_api_key PushHistoricFromCSDL \
            csdl.txt 20120801120000 20120801130000 twitter 100 \
            "Nexus 7 Historic Push" http delivery_frequency=10 \
            url=http://www.example.com/push_endpoint auth.type=none

* **Delete**

  Deletes one or more existing Push subscriptions.

        ./push your_username your_api_key Delete \
            20cf023d838fcfc573a5d991f1b8a911

* **List**

  Lists the Push subscriptions in your account.

        ./push your_username your_api_key List

* **Pause**

  Pause one or more Push subscriptions in your account.

        ./push your_username your_api_key Pause \
            20cf023d838fcfc573a5d991f1b8a911

* **Resume**

  Resume one or more Push subscriptions in your account.

        ./push your_username your_api_key Resume \
            20cf023d838fcfc573a5d991f1b8a911

* **Stop**

  Stops one or more Push subscriptions in your account.

        ./push your_username your_api_key Stop \
            20cf023d838fcfc573a5d991f1b8a911

* **View**

  View the details of one or more Push subscriptions in your account.

        ./push your_username your_api_key View \
            20cf023d838fcfc573a5d991f1b8a911

* **ViewLog**

  View recent log entries for your Push subscriptions. If no subscription ID is passed then all log entries are shown, otherwise only log entries relating to that subscription are retrieved.

        ./push your_username your_api_key ViewLog

        ./push your_username your_api_key ViewLog \
            20cf023d838fcfc573a5d991f1b8a911
