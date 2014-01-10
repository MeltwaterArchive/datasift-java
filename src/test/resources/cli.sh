#!/bin/bash

set -e

DU=${1:-$DU}
DK=${2:-$DK}

datasift() {
  java -classpath "./*:./lib/*:./lib/log4j.properties" com.datasift.client.cli.Interface -a $DU $DK "$@"
}

cp ./log4j.properties ../../../lib
cd ../../..
mvn package -DskipTests
cd target

# Core API
datasift -c dpu -p hash 123456
datasift -c usage -p hash 123456
datasift -c usage -p period hour
datasift -c usage -p period day
datasift -c balance
datasift -c validate -p csdl '{ wrong }'
datasift -c validate -p csdl 'source.id == "42"'
datasift -c compile -p csdl 'source.id == "42"'
datasift -c pull -p subscription_id 42

# Managed Sources
src=$(datasift -e sources -c create \
     -p source_type instagram -p name api \
     -p auth '[{"parameters":{"value":"TOKEN"}}]' \
     -p resources '[{"parameters":{"value":"cats","type":"tag"}}]' \
     -p parameters '{"comments":true,"likes":false}')
datasift -e sources -c start -p source_id $src
datasift -e sources -c stop -p source_id $src
datasift -e sources -c get
datasift -e sources -c get -p source_type instagram
datasift -e sources -c get -p source_type facebook_page
datasift -e sources -c get -p source_type instagram -p page 2
datasift -e sources -c get -p source_id $src
datasift -e sources -c log -p source_id $src -p page 2 -p per_page 1
datasift -e sources -c delete -p source_id $src

# Historics Preview (dummy values, all of these cause errors)
datasift -e preview -c create -p stream 42 -p start 124 -p parameters '["target","analysis"]' -p sources '["twitter"]' -p end 1325549800
datasift -e preview -c get -p preview_id 42

# Historics (dummy values, all of these cause errors)
datasift -e historics -c prepare -p stream 42 -p start 42 -p end 43 -p name test -p sources '["twitter"]' -p sample 0.01
datasift -e historics -c start -p historics_id 42
datasift -e historics -c update -p name test -p historics_id 42
datasift -e historics -c stop -p historics_id 42
datasift -e historics -c stop -p historics_id 42 -p reason "just do it"
datasift -e historics -c status -p start 42 -p end 42
datasift -e historics -c status -p start 42 -p end 42 -p sources '["facebook"]'
datasift -e historics -c status -p start 42 -p end 42 -p sources '["twitter"]'
datasift -e historics -c update -p historics_id 42 -p name "new name"
datasift -e historics -c delete -p historics_id 42
datasift -e historics -c get
datasift -e historics -c get -p maximum 3 -p with_estimate true

# Push (mostly dummy values)
datasift -e push -c validate -p output_type http -p output_params '{"format":"json","delivery_frequency":0,"url":"http://datasift.com"}'
datasift -e push -c create -p output_type http -p output_params '{"format":"json","delivery_frequency":0,"url":"http://datasift.com"}' -p from_hash 4242 -p stream_or_id 42 -p name test
datasift -e push -c create -p output_type http -p output_params '{"format":"json","delivery_frequency":0,"url":"http://datasift.com"}' -p from_hash 4242 -p stream_or_id 42 -p name test -p initial_status 42
datasift -e push -c create -p output_type http -p output_params '{"format":"json","delivery_frequency":0,"url":"http://datasift.com"}' -p from_hash 4242 -p stream_or_id 42 -p name test -p initial_status 42 -p start 84
datasift -e push -c create -p output_type http -p output_params '{"format":"json","delivery_frequency":0,"url":"http://datasift.com"}' -p from_hash 4242 -p stream_or_id 42 -p name test -p initial_status 42 -p end 84
datasift -e push -c pause -p subscription_id 42
datasift -e push -c stop -p subscription_id 42
datasift -e push -c delete -p subscription_id 42
datasift -e push -c log -p subscription_id 42
datasift -e push -c log
datasift -e push -c log -p page 2 -p per_page 1
datasift -e push -c log -p order_by request_time
datasift -e push -c log -p order_dir desc
datasift -e push -c log -p order_dir asc
datasift -e push -c get -p stream 42
datasift -e push -c get -p historics_id 42
datasift -e push -c get -p include_finished false
datasift -e push -c get -p include_finished true
datasift -e push -c create -p name test-api -p from_hash true -p stream_or_id 14d12c0c828fd84509c29dea44591e5e -p output_type pull -p output_params '{"format": "json_new_line"}'