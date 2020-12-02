#!/bin/sh

curl -X POST \
     -d'@test-data.json' \
     -H'Content-Type:application/json' \
     -H'ce-specversion:1.0' \
     -H'ce-type:faces.api.response' \
     -H'ce-source:/processor' \
     -H'ce-id:45c83279-c8a1-4db6-a703-b3768db93887' \
     -H'ce-time:2019-11-06T11:17:00Z' \
     http://localhost:8080/

