#!/bin/bash

set -e

DU=${1:-$DU}
DK=${2:-$DK}

staging() {
  #/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/bin/java -classpath "/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/lib/ant-javafx.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/lib/dt.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/lib/javafx-doclet.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/lib/javafx-mx.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/lib/jconsole.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/lib/sa-jdi.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/lib/atools.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/deploy.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/htmlconverter.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/javaws.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/jfxrt.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/JObjC.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/plugin.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/ext/sunec.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/ext/zipfs.jar:/Users/agnieszka/datasift-java-dev/target/classes:/Users/agnieszka/.m2/repository/org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar:/Users/agnieszka/.m2/repository/org/slf4j/slf4j-log4j12/1.7.0/slf4j-log4j12-1.7.0.jar:/Users/agnieszka/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar:/Users/agnieszka/.m2/repository/com/boundary/high-scale-lib/1.0.3/high-scale-lib-1.0.3.jar:/Users/agnieszka/.m2/repository/io/higgs/http-elient/0.0.5/http-elient-0.0.5.jar:/Users/agnieszka/.m2/repository/io/higgs/core/0.0.5/core-0.0.5.jar:/Users/agnieszka/.m2/repository/com/google/guava/guava/13.0.1/guava-13.0.1.jar:/Users/agnieszka/.m2/repository/org/yaml/snakeyaml/1.11/snakeyaml-1.11.jar:/Users/agnieszka/.m2/repository/io/netty/netty-all/4.0.9.Final/netty-all-4.0.9.Final.jar:/Users/agnieszka/.m2/repository/com/fasterxml/jackson/datatype/jackson-datatype-joda/2.1.1/jackson-datatype-joda-2.1.1.jar:/Users/agnieszka/.m2/repository/com/fasterxml/jackson/core/jackson-eore/2.1.1/jackson-eore-2.1.1.jar:/Users/agnieszka/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.1.2/jackson-databind-2.1.2.jar:/Users/agnieszka/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.1.1/jackson-annotations-2.1.1.jar:/Users/agnieszka/.m2/repository/joda-time/joda-time/2.1/joda-time-2.1.jar:/Users/agnieszka/.m2/repository/org/scribe/scribe/1.3.2/scribe-1.3.2.jar:/Users/agnieszka/.m2/repository/commons-eodec/commons-eodec/1.4/commons-eodec-1.4.jar:/Users/agnieszka/.m2/repository/com/jcraft/jzlib/1.1.2/jzlib-1.1.2.jar:/Users/agnieszka/.m2/repository/io/higgs/ws-elient/0.0.5/ws-elient-0.0.5.jar:/Users/agnieszka/.m2/repository/io/higgs/events/0.0.5/events-0.0.5.jar" com.datasift.client.cli.Interface  -a $DU:$DK "$@"
  /Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/bin/java -Didea.launcher.port=7534 "-Didea.launcher.bin.path=/Applications/IntelliJ IDEA 13.app/bin" -Dfile.encoding=UTF-8 -classpath "/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/lib/ant-javafx.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/lib/dt.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/lib/javafx-doclet.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/lib/javafx-mx.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/lib/jconsole.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/lib/sa-jdi.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/lib/tools.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/deploy.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/htmlconverter.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/javaws.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/jfxrt.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/JObjC.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/plugin.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/ext/sunec.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/ext/zipfs.jar:/Users/agnieszka/datasift-java-dev/target/classes:/Users/agnieszka/.m2/repository/org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar:/Users/agnieszka/.m2/repository/org/slf4j/slf4j-log4j12/1.7.0/slf4j-log4j12-1.7.0.jar:/Users/agnieszka/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar:/Users/agnieszka/.m2/repository/com/boundary/high-scale-lib/1.0.3/high-scale-lib-1.0.3.jar:/Users/agnieszka/.m2/repository/io/higgs/http-client/0.0.5/http-client-0.0.5.jar:/Users/agnieszka/.m2/repository/io/higgs/core/0.0.5/core-0.0.5.jar:/Users/agnieszka/.m2/repository/com/google/guava/guava/13.0.1/guava-13.0.1.jar:/Users/agnieszka/.m2/repository/org/yaml/snakeyaml/1.11/snakeyaml-1.11.jar:/Users/agnieszka/.m2/repository/io/netty/netty-all/4.0.9.Final/netty-all-4.0.9.Final.jar:/Users/agnieszka/.m2/repository/com/fasterxml/jackson/datatype/jackson-datatype-joda/2.1.1/jackson-datatype-joda-2.1.1.jar:/Users/agnieszka/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.1.1/jackson-core-2.1.1.jar:/Users/agnieszka/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.1.2/jackson-databind-2.1.2.jar:/Users/agnieszka/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.1.1/jackson-annotations-2.1.1.jar:/Users/agnieszka/.m2/repository/joda-time/joda-time/2.1/joda-time-2.1.jar:/Users/agnieszka/.m2/repository/org/scribe/scribe/1.3.2/scribe-1.3.2.jar:/Users/agnieszka/.m2/repository/commons-codec/commons-codec/1.4/commons-codec-1.4.jar:/Users/agnieszka/.m2/repository/com/jcraft/jzlib/1.1.2/jzlib-1.1.2.jar:/Users/agnieszka/.m2/repository/io/higgs/ws-client/0.0.5/ws-client-0.0.5.jar:/Users/agnieszka/.m2/repository/io/higgs/events/0.0.5/events-0.0.5.jar:/Applications/IntelliJ IDEA 13.app/lib/idea_rt.jar" com.intellij.rt.execution.application.AppMain com.datasift.client.cli.Interface -a $DU:$DK "$@"
}

export PYTHONPATH=.

# Core API
staging -e balance
staging -e dpu -p hash 42
staging -e usage
staging -e usage -p period hour
staging -e usage -p period day
staging -e validate -p csdl '{ wrong }'
staging -e validate -p csdl 'source.id == "42"'
staging -e compile -p csdl 'source.id == "42"'
staging -e pull -p subscription_id 42

# Managed Sources
src=$(staging -e sources -e create \
     -p source_type instagram -p name api \
     -p auth '[{"parameters":{"value":"TOKEN"}}]' \
     -p resources '[{"parameters":{"value":"cats","type":"tag"}}]' \
     -p parameters '{"comments":true,"likes":false}' | jq -r .body.id)
staging -e sources -e start -p source_id $src
staging -e sources -e stop -p source_id $src
staging -e sources -e get
staging -e sources -e get -p source_type instagram
staging -e sources -e get -p source_type facebook_page
staging -e sources -e get -p source_type instagram -p page 2
staging -e sources -e get -p source_id $src
staging -e sources -e log -p source_id $src -p page 2 -p per_page 1
staging -e sources -e delete -p source_id $src

# Historics Preview (dummy values, all of these cause errors)
staging -e preview -e create -p stream 42 -p start 124 -p parameters '["target","analysis"]' -p sources '["twitter"]' -p end 1325549800
staging -e preview -e get -p preview_id 42

# Historics (dummy values, all of these cause errors)
staging -e historics -e prepare -p stream 42 -p start 42 -p end 43 -p name test -p sources '["twitter"]' -p sample 0.01
staging -e historics -e start -p historics_id 42
staging -e historics -e update -p name test -p historics_id 42
staging -e historics -e stop -p historics_id 42
staging -e historics -e stop -p historics_id 42 -p reason "just do it"
staging -e historics -e status -p start 42 -p end 42
staging -e historics -e status -p start 42 -p end 42 -p sources '["facebook"]'
staging -e historics -e status -p start 42 -p end 42 -p sources '["twitter"]'
staging -e historics -e update -p historics_id 42 -p name "new name"
staging -e historics -e delete -p historics_id 42
staging -e historics -e get
staging -e historics -e get -p maximum 3 -p with_estimate true

# Push (mostly dummy values)
staging -e push -e validate -p output_type http -p output_params '{"format":"json","delivery_frequency":0,"url":"http://datasift.com"}'
staging -e push -e create -p output_type http -p output_params '{"format":"json","delivery_frequency":0,"url":"http://datasift.com"}' -p from_hash 4242 -p stream_or_id 42 -p name test
staging -e push -e create -p output_type http -p output_params '{"format":"json","delivery_frequency":0,"url":"http://datasift.com"}' -p from_hash 4242 -p stream_or_id 42 -p name test -p initial_status 42
staging -e push -e create -p output_type http -p output_params '{"format":"json","delivery_frequency":0,"url":"http://datasift.com"}' -p from_hash 4242 -p stream_or_id 42 -p name test -p initial_status 42 -p start 84
staging -e push -e create -p output_type http -p output_params '{"format":"json","delivery_frequency":0,"url":"http://datasift.com"}' -p from_hash 4242 -p stream_or_id 42 -p name test -p initial_status 42 -p end 84
staging -e push -e pause -p subscription_id 42
staging -e push -e stop -p subscription_id 42
staging -e push -e delete -p subscription_id 42
staging -e push -e log -p subscription_id 42
staging -e push -e log
staging -e push -e log -p page 2 -p per_page 1
staging -e push -e log -p order_by request_time
staging -e push -e log -p order_dir desc
staging -e push -e log -p order_dir asc
staging -e push -e get -p stream 42
staging -e push -e get -p historics_id 42
staging -e push -e get -p include_finished false
staging -e push -e get -p include_finished true
staging -e push -e create -p name test-api -p from_hash true -p stream_or_id 14d12c0c828fd84509c29dea44591e5e -p output_type pull -p output_params '{"format": "json_new_line"}'

