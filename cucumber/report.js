$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("src/test/resources/datasift-client-tests/ValidateCSDL.feature");
formatter.feature({
  "id": "core",
  "description": "",
  "name": "Core",
  "keyword": "Feature",
  "line": 1
});
formatter.scenarioOutline({
  "id": "core;",
  "description": "",
  "name": "",
  "keyword": "Scenario Outline",
  "line": 3,
  "type": "scenario_outline"
});
formatter.step({
  "name": "CSDL to validate like \u0027interaction.content contains \"moo\"",
  "keyword": "Given ",
  "line": 4
});
formatter.step({
  "name": "I call the validate endpoint",
  "keyword": "When ",
  "line": 5
});
formatter.step({
  "name": "I should get a validation with a dpu cost",
  "keyword": "Then ",
  "line": 6
});
formatter.uri("src/test/resources/datasift-client-tests/core.feature");
formatter.feature({
  "id": "core",
  "description": "",
  "name": "Core",
  "keyword": "Feature",
  "line": 1
});
formatter.scenarioOutline({
  "id": "core;",
  "description": "",
  "name": "",
  "keyword": "Scenario Outline",
  "line": 3,
  "type": "scenario_outline"
});
formatter.step({
  "name": "CSDL to validate like \u003cCSDL\u003e",
  "keyword": "Given ",
  "line": 4
});
formatter.step({
  "name": "I call the validate endpoint",
  "keyword": "When ",
  "line": 5
});
formatter.step({
  "name": "I should get a validation with a dpu cost",
  "keyword": "Then ",
  "line": 6
});
formatter.examples({
  "id": "core;;",
  "description": "",
  "name": "",
  "keyword": "Examples",
  "line": 8,
  "rows": [
    {
      "id": "core;;;1",
      "cells": [
        "CSDL"
      ],
      "line": 9
    },
    {
      "id": "core;;;2",
      "cells": [
        "\u0027interaction.sample \u003c 1\u0027"
      ],
      "line": 10
    }
  ]
});
formatter.scenario({
  "id": "core;;;2",
  "description": "",
  "name": "",
  "keyword": "Scenario Outline",
  "line": 10,
  "type": "scenario"
});
formatter.step({
  "name": "CSDL to validate like \u0027interaction.sample \u003c 1\u0027",
  "keyword": "Given ",
  "line": 4,
  "matchedColumns": [
    0
  ]
});
formatter.step({
  "name": "I call the validate endpoint",
  "keyword": "When ",
  "line": 5
});
formatter.step({
  "name": "I should get a validation with a dpu cost",
  "keyword": "Then ",
  "line": 6
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({
  "location": "ValidateCSDLSteps.i_call_the_validate_endpoint()"
});
formatter.result({
  "status": "skipped"
});
formatter.match({
  "location": "ValidateCSDLSteps.i_should_get_a_validation_with_a_dpu_cost()"
});
formatter.result({
  "status": "skipped"
});
formatter.scenarioOutline({
  "id": "core;",
  "description": "",
  "name": "",
  "keyword": "Scenario Outline",
  "line": 12,
  "type": "scenario_outline"
});
formatter.step({
  "name": "CSDL to compile like \u003cCSDL\u003e",
  "keyword": "Given ",
  "line": 13
});
formatter.step({
  "name": "I call the compile endpoint",
  "keyword": "When ",
  "line": 14
});
formatter.step({
  "name": "I should get a compilation with a hash",
  "keyword": "Then ",
  "line": 15
});
formatter.examples({
  "id": "core;;",
  "description": "",
  "name": "",
  "keyword": "Examples",
  "line": 17,
  "rows": [
    {
      "id": "core;;;1",
      "cells": [
        "CSDL"
      ],
      "line": 18
    },
    {
      "id": "core;;;2",
      "cells": [
        "\u0027interaction.sample \u003c 1\u0027"
      ],
      "line": 19
    }
  ]
});
formatter.scenario({
  "id": "core;;;2",
  "description": "",
  "name": "",
  "keyword": "Scenario Outline",
  "line": 19,
  "type": "scenario"
});
formatter.step({
  "name": "CSDL to compile like \u0027interaction.sample \u003c 1\u0027",
  "keyword": "Given ",
  "line": 13,
  "matchedColumns": [
    0
  ]
});
formatter.step({
  "name": "I call the compile endpoint",
  "keyword": "When ",
  "line": 14
});
formatter.step({
  "name": "I should get a compilation with a hash",
  "keyword": "Then ",
  "line": 15
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.scenario({
  "id": "core;",
  "description": "",
  "name": "",
  "keyword": "Scenario",
  "line": 21,
  "type": "scenario"
});
formatter.step({
  "name": "I call the usage endpoint",
  "keyword": "When ",
  "line": 22
});
formatter.step({
  "name": "I should get back account usage",
  "keyword": "Then ",
  "line": 23
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.scenarioOutline({
  "id": "core;",
  "description": "",
  "name": "",
  "keyword": "Scenario Outline",
  "line": 25,
  "type": "scenario_outline"
});
formatter.step({
  "name": "a hash like \u003chash\u003e",
  "keyword": "Given ",
  "line": 26
});
formatter.step({
  "name": "I call the dpu endpoint",
  "keyword": "When ",
  "line": 27
});
formatter.step({
  "name": "I should get a dpu cost",
  "keyword": "Then ",
  "line": 28
});
formatter.examples({
  "id": "core;;",
  "description": "",
  "name": "",
  "keyword": "Examples",
  "line": 30,
  "rows": [
    {
      "id": "core;;;1",
      "cells": [
        "hash"
      ],
      "line": 31
    },
    {
      "id": "core;;;2",
      "cells": [
        "\u0027c8ad285e200a0e9a4b8737ee43676599\u0027"
      ],
      "line": 32
    }
  ]
});
formatter.scenario({
  "id": "core;;;2",
  "description": "",
  "name": "",
  "keyword": "Scenario Outline",
  "line": 32,
  "type": "scenario"
});
formatter.step({
  "name": "a hash like \u0027c8ad285e200a0e9a4b8737ee43676599\u0027",
  "keyword": "Given ",
  "line": 26,
  "matchedColumns": [
    0
  ]
});
formatter.step({
  "name": "I call the dpu endpoint",
  "keyword": "When ",
  "line": 27
});
formatter.step({
  "name": "I should get a dpu cost",
  "keyword": "Then ",
  "line": 28
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.uri("src/test/resources/datasift-client-tests/managedSources.feature");
formatter.feature({
  "id": "managed-sources",
  "description": "",
  "name": "Managed Sources",
  "keyword": "Feature",
  "line": 1
});
formatter.scenarioOutline({
  "id": "managed-sources;",
  "description": "",
  "name": "",
  "keyword": "Scenario Outline",
  "line": 3,
  "type": "scenario_outline"
});
formatter.step({
  "name": "A facebook page like \u003cpage_id\u003e",
  "keyword": "Given ",
  "line": 4
});
formatter.step({
  "name": "I call the source create endpoint",
  "keyword": "When ",
  "line": 5
});
formatter.step({
  "name": "I should get back a valid facebook managed sources page",
  "keyword": "Then ",
  "line": 6
});
formatter.examples({
  "id": "managed-sources;;",
  "description": "",
  "name": "",
  "keyword": "Examples",
  "line": 8,
  "rows": [
    {
      "id": "managed-sources;;;1",
      "cells": [
        "page_id"
      ],
      "line": 9
    },
    {
      "id": "managed-sources;;;2",
      "cells": [
        "bbcnews"
      ],
      "line": 10
    },
    {
      "id": "managed-sources;;;3",
      "cells": [
        "applemusic"
      ],
      "line": 11
    },
    {
      "id": "managed-sources;;;4",
      "cells": [
        "Channel4News"
      ],
      "line": 12
    },
    {
      "id": "managed-sources;;;5",
      "cells": [
        "SPORTbible"
      ],
      "line": 13
    }
  ]
});
formatter.scenario({
  "id": "managed-sources;;;2",
  "description": "",
  "name": "",
  "keyword": "Scenario Outline",
  "line": 10,
  "type": "scenario"
});
formatter.step({
  "name": "A facebook page like bbcnews",
  "keyword": "Given ",
  "line": 4,
  "matchedColumns": [
    0
  ]
});
formatter.step({
  "name": "I call the source create endpoint",
  "keyword": "When ",
  "line": 5
});
formatter.step({
  "name": "I should get back a valid facebook managed sources page",
  "keyword": "Then ",
  "line": 6
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.scenario({
  "id": "managed-sources;;;3",
  "description": "",
  "name": "",
  "keyword": "Scenario Outline",
  "line": 11,
  "type": "scenario"
});
formatter.step({
  "name": "A facebook page like applemusic",
  "keyword": "Given ",
  "line": 4,
  "matchedColumns": [
    0
  ]
});
formatter.step({
  "name": "I call the source create endpoint",
  "keyword": "When ",
  "line": 5
});
formatter.step({
  "name": "I should get back a valid facebook managed sources page",
  "keyword": "Then ",
  "line": 6
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.scenario({
  "id": "managed-sources;;;4",
  "description": "",
  "name": "",
  "keyword": "Scenario Outline",
  "line": 12,
  "type": "scenario"
});
formatter.step({
  "name": "A facebook page like Channel4News",
  "keyword": "Given ",
  "line": 4,
  "matchedColumns": [
    0
  ]
});
formatter.step({
  "name": "I call the source create endpoint",
  "keyword": "When ",
  "line": 5
});
formatter.step({
  "name": "I should get back a valid facebook managed sources page",
  "keyword": "Then ",
  "line": 6
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.scenario({
  "id": "managed-sources;;;5",
  "description": "",
  "name": "",
  "keyword": "Scenario Outline",
  "line": 13,
  "type": "scenario"
});
formatter.step({
  "name": "A facebook page like SPORTbible",
  "keyword": "Given ",
  "line": 4,
  "matchedColumns": [
    0
  ]
});
formatter.step({
  "name": "I call the source create endpoint",
  "keyword": "When ",
  "line": 5
});
formatter.step({
  "name": "I should get back a valid facebook managed sources page",
  "keyword": "Then ",
  "line": 6
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.scenario({
  "id": "managed-sources;",
  "description": "",
  "name": "",
  "keyword": "Scenario",
  "line": 15,
  "type": "scenario"
});
formatter.step({
  "name": "the facebook \"bbcnews\"",
  "keyword": "Given ",
  "line": 16
});
formatter.step({
  "name": "I call the source create endpoint",
  "keyword": "When ",
  "line": 17
});
formatter.step({
  "name": "I should get a valid source back",
  "keyword": "Then ",
  "line": 18
});
formatter.step({
  "name": "I should be able to start the source I just created",
  "keyword": "Then ",
  "line": 19
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.scenario({
  "id": "managed-sources;",
  "description": "",
  "name": "",
  "keyword": "Scenario",
  "line": 21,
  "type": "scenario"
});
formatter.step({
  "name": "the facebook \"bbcnews\"",
  "keyword": "Given ",
  "line": 22
});
formatter.step({
  "name": "I call the source create endpoint",
  "keyword": "When ",
  "line": 23
});
formatter.step({
  "name": "I should get a valid source back",
  "keyword": "Then ",
  "line": 24
});
formatter.step({
  "name": "I should be able to start the source I just created",
  "keyword": "Then ",
  "line": 25
});
formatter.step({
  "name": "I should be able to stop the source I just started",
  "keyword": "Then ",
  "line": 26
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.scenario({
  "id": "managed-sources;",
  "description": "",
  "name": "",
  "keyword": "Scenario",
  "line": 28,
  "type": "scenario"
});
formatter.step({
  "name": "the facebook \"bbcnews\"",
  "keyword": "Given ",
  "line": 29
});
formatter.step({
  "name": "I call the source create endpoint",
  "keyword": "When ",
  "line": 30
});
formatter.step({
  "name": "I should get a valid source back",
  "keyword": "Then ",
  "line": 31
});
formatter.step({
  "name": "I should be able to delete the source I just created",
  "keyword": "Then ",
  "line": 32
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.scenario({
  "id": "managed-sources;",
  "description": "",
  "name": "",
  "keyword": "Scenario",
  "line": 34,
  "type": "scenario"
});
formatter.step({
  "name": "the facebook \"bbcnews\"",
  "keyword": "Given ",
  "line": 35
});
formatter.step({
  "name": "I call the source create endpoint",
  "keyword": "When ",
  "line": 36
});
formatter.step({
  "name": "I should get a valid source back",
  "keyword": "Then ",
  "line": 37
});
formatter.step({
  "name": "I should be able to get the source I just created",
  "keyword": "Then ",
  "line": 38
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.scenario({
  "id": "managed-sources;",
  "description": "",
  "name": "",
  "keyword": "Scenario",
  "line": 40,
  "type": "scenario"
});
formatter.step({
  "name": "the facebook \"bbcnews\"",
  "keyword": "Given ",
  "line": 41
});
formatter.step({
  "name": "I call the source create endpoint",
  "keyword": "When ",
  "line": 42
});
formatter.step({
  "name": "I should get a valid source back",
  "keyword": "Then ",
  "line": 43
});
formatter.step({
  "name": "I should be able to update the source I just created with a new page \"applemusic\"",
  "keyword": "Then ",
  "line": 44
});
formatter.step({
  "name": "I should be able to get the source and it should have the two pages I added",
  "keyword": "Then ",
  "line": 45
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.scenario({
  "id": "managed-sources;",
  "description": "",
  "name": "",
  "keyword": "Scenario",
  "line": 47,
  "type": "scenario"
});
formatter.step({
  "name": "the facebook \"bbcnews\"",
  "keyword": "Given ",
  "line": 48
});
formatter.step({
  "name": "I call the source create endpoint",
  "keyword": "When ",
  "line": 49
});
formatter.step({
  "name": "I should get a valid source back",
  "keyword": "Then ",
  "line": 50
});
formatter.step({
  "name": "I should be able to add a new authentication token to the source I just created",
  "keyword": "Then ",
  "line": 51
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.scenario({
  "id": "managed-sources;",
  "description": "",
  "name": "",
  "keyword": "Scenario",
  "line": 53,
  "type": "scenario"
});
formatter.step({
  "name": "the facebook \"bbcnews\"",
  "keyword": "Given ",
  "line": 54
});
formatter.step({
  "name": "I call the source create endpoint",
  "keyword": "When ",
  "line": 55
});
formatter.step({
  "name": "I should get a valid source back",
  "keyword": "Then ",
  "line": 56
});
formatter.step({
  "name": "I should be able to remove an authentication token from the source I just created",
  "keyword": "Then ",
  "line": 57
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.uri("src/test/resources/datasift-client-tests/pylon/analyze.feature");
formatter.feature({
  "id": "post-/pylon/analyze",
  "description": "",
  "name": "POST /pylon/analyze",
  "keyword": "Feature",
  "line": 1
});
formatter.scenario({
  "id": "post-/pylon/analyze;analyze-with-valid-parameters",
  "description": "",
  "name": "Analyze with valid parameters",
  "keyword": "Scenario",
  "line": 3,
  "type": "scenario"
});
formatter.step({
  "name": "that the request body is valid JSON",
  "keyword": "Given ",
  "line": 4,
  "doc_string": {
    "value": "  {\n    \"id\": \"1234\",\n    \"parameters\": [\n      {\n        \"analysis_type\": \"freqDist\",\n        \"parameters\": {\n          \"target\": \"fb.author.gender\",\n          \"threshold\": 0\n        }\n      }\n    ],\n    \"start\": 1453191346,\n    \"end\": 1453191346,\n    \"filter\": \"interaction.sample \u003c\u003d 100\"\n  }",
    "line": 5,
    "content_type": ""
  }
});
formatter.step({
  "name": "I make a \"POST\" request to \"/v1.3/pylon/analyze\"",
  "keyword": "When ",
  "line": 22
});
formatter.step({
  "name": "the response status code should be \"200\"",
  "keyword": "Then ",
  "line": 23
});
formatter.step({
  "name": "the response body contains the JSON data",
  "keyword": "And ",
  "line": 24,
  "doc_string": {
    "value": "  {\n     \"truncated\":true,\n     \"interations\":10,\n     \"unique_authors\":20,\n     \"results\": [\n         {\n           \"male\" : 30,\n           \"female\": 40,\n           \"unknown\": 10\n         }\n     ]\n  }",
    "line": 25,
    "content_type": ""
  }
});
formatter.match({
  "location": "TestRest.that_the_request_body_is_valid_JSON(String)"
});
formatter.result({
  "duration": 118584000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "POST",
      "offset": 10
    },
    {
      "val": "/v1.3/pylon/analyze",
      "offset": 28
    }
  ],
  "location": "TestRest.i_make_a_request_to(String,String)"
});
formatter.result({
  "duration": 1604000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "200",
      "offset": 36
    }
  ],
  "location": "TestRest.the_response_status_code_should_be(String)"
});
formatter.result({
  "duration": 42000,
  "status": "passed"
});
formatter.match({
  "location": "TestRest.the_response_body_contains_the_JSON_data(String)"
});
formatter.result({
  "duration": 12000,
  "status": "passed"
});
formatter.scenario({
  "id": "post-/pylon/analyze;analyze-without-id",
  "description": "",
  "name": "Analyze without id",
  "keyword": "Scenario",
  "line": 40,
  "type": "scenario"
});
formatter.step({
  "name": "that the request body is valid JSON",
  "keyword": "Given ",
  "line": 41,
  "doc_string": {
    "value": "  {\n    \"parameters\": [\n      {\n        \"analysis_type\": \"freqDist\",\n        \"parameters\": {\n          \"target\": \"fb.author.gender\",\n          \"threshold\": 0\n        }\n      }\n    ],\n    \"start\": 1453191346,\n    \"end\": 1453191346,\n    \"filter\": \"interaction.sample \u003c\u003d 100\"\n  }",
    "line": 42,
    "content_type": ""
  }
});
formatter.step({
  "name": "I make a \"POST\" request to \"/v1.3/pylon/analyze\"",
  "keyword": "When ",
  "line": 58
});
formatter.step({
  "name": "the response status code should be \"400\"",
  "keyword": "Then ",
  "line": 59
});
formatter.step({
  "name": "the response body contains the JSON data",
  "keyword": "And ",
  "line": 60,
  "doc_string": {
    "value": "  {\n     \"error\":\"No id was supplied.\"\n  }",
    "line": 61,
    "content_type": ""
  }
});
formatter.match({
  "location": "TestRest.that_the_request_body_is_valid_JSON(String)"
});
formatter.result({
  "duration": 27000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "POST",
      "offset": 10
    },
    {
      "val": "/v1.3/pylon/analyze",
      "offset": 28
    }
  ],
  "location": "TestRest.i_make_a_request_to(String,String)"
});
formatter.result({
  "duration": 177000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "400",
      "offset": 36
    }
  ],
  "location": "TestRest.the_response_status_code_should_be(String)"
});
formatter.result({
  "duration": 37000,
  "status": "passed"
});
formatter.match({
  "location": "TestRest.the_response_body_contains_the_JSON_data(String)"
});
formatter.result({
  "duration": 17000,
  "status": "passed"
});
formatter.uri("src/test/resources/datasift-client-tests/pylon/get.feature");
formatter.feature({
  "id": "get-/pylon/get",
  "description": "",
  "name": "GET /pylon/get",
  "keyword": "Feature",
  "line": 1
});
formatter.scenario({
  "id": "get-/pylon/get;get-with-recording-id",
  "description": "",
  "name": "Get with recording_id",
  "keyword": "Scenario",
  "line": 3,
  "type": "scenario"
});
formatter.step({
  "name": "a mock exists at \"/account/identity\" it should return \"200\" with the body:",
  "keyword": "Given ",
  "line": 4,
  "doc_string": {
    "value": "  {\n    \"count\": 1,\n    \"page\": 1,\n    \"pages\": 2,\n    \"per_page\": 2,\n    \"subscriptions\": [\n      {\n        \"id\": \"1234\",\n        \"volume\": 123,\n        \"start\": 1453191346,\n        \"end\": 1453191346,\n        \"status\": \"running\",\n        \"name\": \"abc\",\n        \"reached_capacity\": true,\n        \"identity_id\": \"121334234a\",\n        \"hash\": \"83fa8c8f21c44698be111fa0c1372a40\",\n        \"remaining_index_capacity\": 112,\n        \"remaining_account_capacity\": 211\n      }\n    ]\n  }",
    "line": 5,
    "content_type": ""
  }
});
formatter.step({
  "name": "that the request body is valid JSON",
  "keyword": "And ",
  "line": 28,
  "doc_string": {
    "value": "  {\n    \"id\": \"1234\",\n    \"volume\": 123,\n    \"start\": 1453191346,\n    \"end\": 1453191346,\n    \"status\": \"running\",\n    \"name\": \"abc\",\n    \"reached_capacity\": true,\n    \"identity_id\": \"121334234a\",\n    \"hash\": \"83fa8c8f21c44698be111fa0c1372a40\",\n    \"remaining_index_capacity\": 112,\n    \"remaining_account_capacity\": 211\n  }",
    "line": 29,
    "content_type": ""
  }
});
formatter.step({
  "name": "the mocks are created",
  "keyword": "And ",
  "line": 44
});
formatter.step({
  "name": "that the request body is valid JSON",
  "keyword": "And ",
  "line": 45,
  "doc_string": {
    "value": "  {\n    \"id\": \"1234\",\n    \"volume\": 123,\n    \"start\": 1453191346,\n    \"end\": 1453191346,\n    \"status\": \"running\",\n    \"name\": \"abc\",\n    \"reached_capacity\": true,\n    \"identity_id\": \"121334234a\",\n    \"hash\": \"83fa8c8f21c44698be111fa0c1372a40\",\n    \"remaining_index_capacity\": 112,\n    \"remaining_account_capacity\": 211\n  }",
    "line": 46,
    "content_type": ""
  }
});
formatter.step({
  "name": "I make a \"GET\" request to \"/v1.3/pylon/get?id\u003d1234\"",
  "keyword": "When ",
  "line": 61
});
formatter.step({
  "name": "the response status code should be \"200\"",
  "keyword": "Then ",
  "line": 62
});
formatter.step({
  "name": "the response body contains the JSON data",
  "keyword": "And ",
  "line": 63,
  "doc_string": {
    "value": "  {\n    \"count\": 1,\n    \"page\": 1,\n    \"pages\": 2,\n    \"per_page\": 2,\n    \"subscriptions\": [\n      {\n        \"id\": \"1234\",\n        \"volume\": 123,\n        \"start\": 1453191346,\n        \"end\": 1453191346,\n        \"status\": \"running\",\n        \"name\": \"abc\",\n        \"reached_capacity\": true,\n        \"identity_id\": \"121334234a\",\n        \"hash\": \"83fa8c8f21c44698be111fa0c1372a40\",\n        \"remaining_index_capacity\": 112,\n        \"remaining_account_capacity\": 211\n      }\n    ]\n  }",
    "line": 64,
    "content_type": ""
  }
});
formatter.match({
  "arguments": [
    {
      "val": "/account/identity",
      "offset": 18
    },
    {
      "val": "200",
      "offset": 55
    }
  ],
  "location": "TestRest.a_mock_exists_at_it_should_return_with_the_body(String,String,String)"
});
formatter.result({
  "duration": 1843000,
  "status": "pending",
  "error_message": "cucumber.api.PendingException: TODO: implement me\n\tat com.datasift.client.behavioural.TestRest.a_mock_exists_at_it_should_return_with_the_body(TestRest.java:16)\n\tat ✽.Given a mock exists at \"/account/identity\" it should return \"200\" with the body:(src/test/resources/datasift-client-tests/pylon/get.feature:4)\n"
});
formatter.match({
  "location": "TestRest.that_the_request_body_is_valid_JSON(String)"
});
formatter.result({
  "status": "skipped"
});
formatter.match({
  "location": "TestRest.the_mocks_are_created()"
});
formatter.result({
  "status": "skipped"
});
formatter.match({
  "location": "TestRest.that_the_request_body_is_valid_JSON(String)"
});
formatter.result({
  "status": "skipped"
});
formatter.match({
  "arguments": [
    {
      "val": "GET",
      "offset": 10
    },
    {
      "val": "/v1.3/pylon/get?id\u003d1234",
      "offset": 27
    }
  ],
  "location": "TestRest.i_make_a_request_to(String,String)"
});
formatter.result({
  "status": "skipped"
});
formatter.match({
  "arguments": [
    {
      "val": "200",
      "offset": 36
    }
  ],
  "location": "TestRest.the_response_status_code_should_be(String)"
});
formatter.result({
  "status": "skipped"
});
formatter.match({
  "location": "TestRest.the_response_body_contains_the_JSON_data(String)"
});
formatter.result({
  "status": "skipped"
});
formatter.uri("src/test/resources/datasift-client-tests/pylon/sample.feature");
formatter.feature({
  "id": "post-/pylon/sample",
  "description": "",
  "name": "POST /pylon/sample",
  "keyword": "Feature",
  "line": 1
});
formatter.scenario({
  "id": "post-/pylon/sample;sample-with-valid-parameters",
  "description": "",
  "name": "Sample with valid parameters",
  "keyword": "Scenario",
  "line": 3,
  "type": "scenario"
});
formatter.step({
  "name": "that the request body is valid JSON",
  "keyword": "Given ",
  "line": 4,
  "doc_string": {
    "value": "  {\n      \"id\": \"1234\",\n      \"count\": 3,\n      \"start\": 1453191346,\n      \"end\": 1453191346,\n      \"filter\": \"interaction.sample \u003c\u003d 100\"\n  }",
    "line": 5,
    "content_type": ""
  }
});
formatter.step({
  "name": "I make a \"POST\" request to \"/v1.3/pylon/sample\"",
  "keyword": "When ",
  "line": 14
});
formatter.step({
  "name": "the response status code should be \"200\"",
  "keyword": "Then ",
  "line": 15
});
formatter.step({
  "name": "the response body contains the JSON data",
  "keyword": "And ",
  "line": 16,
  "doc_string": {
    "value": "  {\n    \"remaining\": 1234,\n    \"reset_at\": 1453191346,\n    \"interactions\": [\n      {\n        \"interaction\": {\n          \"subtype\": \"reshare\",\n          \"content\": \"baz the map could,\"\n        },\n        \"fb\": {\n          \"media_type\": \"post\",\n          \"content\": \"baz the map could, \",\n          \"language\": \"en\",\n          \"topic_ids\": [\n            565634324\n          ]\n        }\n      }\n    ]\n  }",
    "line": 17,
    "content_type": ""
  }
});
formatter.match({
  "location": "TestRest.that_the_request_body_is_valid_JSON(String)"
});
formatter.result({
  "duration": 34000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "POST",
      "offset": 10
    },
    {
      "val": "/v1.3/pylon/sample",
      "offset": 28
    }
  ],
  "location": "TestRest.i_make_a_request_to(String,String)"
});
formatter.result({
  "duration": 74000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "200",
      "offset": 36
    }
  ],
  "location": "TestRest.the_response_status_code_should_be(String)"
});
formatter.result({
  "duration": 44000,
  "status": "passed"
});
formatter.match({
  "location": "TestRest.the_response_body_contains_the_JSON_data(String)"
});
formatter.result({
  "duration": 15000,
  "status": "passed"
});
formatter.uri("src/test/resources/datasift-client-tests/pylon/start.feature");
formatter.feature({
  "id": "put-/pylon/start",
  "description": "",
  "name": "PUT /pylon/start",
  "keyword": "Feature",
  "line": 1
});
formatter.scenario({
  "id": "put-/pylon/start;start-with-name-and-hash",
  "description": "",
  "name": "Start with name and hash",
  "keyword": "Scenario",
  "line": 3,
  "type": "scenario"
});
formatter.step({
  "name": "that the request body is valid JSON",
  "keyword": "Given ",
  "line": 4,
  "doc_string": {
    "value": "  {\n    \"name\": \"abc\",\n    \"hash\": \"83fa8c8f21c44698be111fa0c1372a40\"\n  }",
    "line": 5,
    "content_type": ""
  }
});
formatter.step({
  "name": "I make a \"PUT\" request to \"/v1.3/pylon/start\"",
  "keyword": "When ",
  "line": 11
});
formatter.step({
  "name": "the response status code should be \"200\"",
  "keyword": "Then ",
  "line": 12
});
formatter.step({
  "name": "the response body contains the JSON data",
  "keyword": "And ",
  "line": 13,
  "doc_string": {
    "value": "  {\n    \"id\": \"1234\"\n  }",
    "line": 14,
    "content_type": ""
  }
});
formatter.match({
  "location": "TestRest.that_the_request_body_is_valid_JSON(String)"
});
formatter.result({
  "duration": 49000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "PUT",
      "offset": 10
    },
    {
      "val": "/v1.3/pylon/start",
      "offset": 27
    }
  ],
  "location": "TestRest.i_make_a_request_to(String,String)"
});
formatter.result({
  "duration": 101000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "200",
      "offset": 36
    }
  ],
  "location": "TestRest.the_response_status_code_should_be(String)"
});
formatter.result({
  "duration": 55000,
  "status": "passed"
});
formatter.match({
  "location": "TestRest.the_response_body_contains_the_JSON_data(String)"
});
formatter.result({
  "duration": 21000,
  "status": "passed"
});
formatter.uri("src/test/resources/datasift-client-tests/pylon/stop.feature");
formatter.feature({
  "id": "put-/pylon/stop",
  "description": "",
  "name": "PUT /pylon/stop",
  "keyword": "Feature",
  "line": 1
});
formatter.scenario({
  "id": "put-/pylon/stop;stop-with-id",
  "description": "",
  "name": "Stop with id",
  "keyword": "Scenario",
  "line": 3,
  "type": "scenario"
});
formatter.step({
  "name": "that the request body is valid JSON",
  "keyword": "Given ",
  "line": 4,
  "doc_string": {
    "value": "  {\n    \"id\": \"1234\"\n  }",
    "line": 5,
    "content_type": ""
  }
});
formatter.step({
  "name": "I make a \"PUT\" request to \"/v1.3/pylon/stop\"",
  "keyword": "When ",
  "line": 10
});
formatter.step({
  "name": "the response status code should be \"204\"",
  "keyword": "Then ",
  "line": 11
});
formatter.match({
  "location": "TestRest.that_the_request_body_is_valid_JSON(String)"
});
formatter.result({
  "duration": 27000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "PUT",
      "offset": 10
    },
    {
      "val": "/v1.3/pylon/stop",
      "offset": 27
    }
  ],
  "location": "TestRest.i_make_a_request_to(String,String)"
});
formatter.result({
  "duration": 64000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "204",
      "offset": 36
    }
  ],
  "location": "TestRest.the_response_status_code_should_be(String)"
});
formatter.result({
  "duration": 45000,
  "status": "passed"
});
formatter.uri("src/test/resources/datasift-client-tests/pylon/tags.feature");
formatter.feature({
  "id": "get-/pylon/tags",
  "description": "",
  "name": "GET /pylon/tags",
  "keyword": "Feature",
  "line": 1
});
formatter.scenario({
  "id": "get-/pylon/tags;tags-with-recording-id",
  "description": "",
  "name": "Tags with recording_id",
  "keyword": "Scenario",
  "line": 3,
  "type": "scenario"
});
formatter.step({
  "name": "that the request body is valid JSON",
  "keyword": "Given ",
  "line": 4,
  "doc_string": {
    "value": "  {\n    \"id\": \"1234\"\n  }",
    "line": 5,
    "content_type": ""
  }
});
formatter.step({
  "name": "I make a \"GET\" request to \"/v1.3/pylon/tags\"",
  "keyword": "When ",
  "line": 10
});
formatter.step({
  "name": "the response status code should be \"200\"",
  "keyword": "Then ",
  "line": 11
});
formatter.step({
  "name": "the response body contains the JSON data",
  "keyword": "And ",
  "line": 12,
  "doc_string": {
    "value": "  [\n      \"interaction.tag_tree.automotive.media\",\n      \"interaction.tag_tree.motogp.manufacturer\",\n      \"interaction.tag_tree.motogp.rider\"\n  ]",
    "line": 13,
    "content_type": ""
  }
});
formatter.match({
  "location": "TestRest.that_the_request_body_is_valid_JSON(String)"
});
formatter.result({
  "duration": 28000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "GET",
      "offset": 10
    },
    {
      "val": "/v1.3/pylon/tags",
      "offset": 27
    }
  ],
  "location": "TestRest.i_make_a_request_to(String,String)"
});
formatter.result({
  "duration": 118000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "200",
      "offset": 36
    }
  ],
  "location": "TestRest.the_response_status_code_should_be(String)"
});
formatter.result({
  "duration": 41000,
  "status": "passed"
});
formatter.match({
  "location": "TestRest.the_response_body_contains_the_JSON_data(String)"
});
formatter.result({
  "duration": 15000,
  "status": "passed"
});
formatter.uri("src/test/resources/datasift-client-tests/pylon/update.feature");
formatter.feature({
  "id": "put-/pylon/update",
  "description": "",
  "name": "PUT /pylon/update",
  "keyword": "Feature",
  "line": 1
});
formatter.scenario({
  "id": "put-/pylon/update;update-with-valid-parameters",
  "description": "",
  "name": "Update with valid parameters",
  "keyword": "Scenario",
  "line": 3,
  "type": "scenario"
});
formatter.step({
  "name": "that the request body is valid JSON",
  "keyword": "Given ",
  "line": 4,
  "doc_string": {
    "value": "  {\n    \"id\": \"1234\",\n    \"name\": \"abc\",\n    \"hash\": \"83fa8c8f21c44698be111fa0c1372a40\"\n  }",
    "line": 5,
    "content_type": ""
  }
});
formatter.step({
  "name": "I make a \"PUT\" request to \"/v1.3/pylon/update\"",
  "keyword": "When ",
  "line": 12
});
formatter.step({
  "name": "the response status code should be \"204\"",
  "keyword": "Then ",
  "line": 13
});
formatter.match({
  "location": "TestRest.that_the_request_body_is_valid_JSON(String)"
});
formatter.result({
  "duration": 26000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "PUT",
      "offset": 10
    },
    {
      "val": "/v1.3/pylon/update",
      "offset": 27
    }
  ],
  "location": "TestRest.i_make_a_request_to(String,String)"
});
formatter.result({
  "duration": 61000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "204",
      "offset": 36
    }
  ],
  "location": "TestRest.the_response_status_code_should_be(String)"
});
formatter.result({
  "duration": 50000,
  "status": "passed"
});
formatter.scenario({
  "id": "put-/pylon/update;update-without-id",
  "description": "",
  "name": "Update without id",
  "keyword": "Scenario",
  "line": 15,
  "type": "scenario"
});
formatter.step({
  "name": "that the request body is valid JSON",
  "keyword": "Given ",
  "line": 16,
  "doc_string": {
    "value": "  {\n    \"name\": \"abc\",\n    \"hash\": \"83fa8c8f21c44698be111fa0c1372a40\"\n  }",
    "line": 17,
    "content_type": ""
  }
});
formatter.step({
  "name": "I make a \"PUT\" request to \"/v1.3/pylon/update\"",
  "keyword": "When ",
  "line": 23
});
formatter.step({
  "name": "the response status code should be \"400\"",
  "keyword": "Then ",
  "line": 24
});
formatter.step({
  "name": "the response body contains the JSON data",
  "keyword": "And ",
  "line": 25,
  "doc_string": {
    "value": "  {\n    \"error\": \"No id was supplied.\"\n  }",
    "line": 26,
    "content_type": ""
  }
});
formatter.match({
  "location": "TestRest.that_the_request_body_is_valid_JSON(String)"
});
formatter.result({
  "duration": 27000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "PUT",
      "offset": 10
    },
    {
      "val": "/v1.3/pylon/update",
      "offset": 27
    }
  ],
  "location": "TestRest.i_make_a_request_to(String,String)"
});
formatter.result({
  "duration": 85000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "400",
      "offset": 36
    }
  ],
  "location": "TestRest.the_response_status_code_should_be(String)"
});
formatter.result({
  "duration": 41000,
  "status": "passed"
});
formatter.match({
  "location": "TestRest.the_response_body_contains_the_JSON_data(String)"
});
formatter.result({
  "duration": 19000,
  "status": "passed"
});
formatter.scenario({
  "id": "put-/pylon/update;update-without-hash",
  "description": "",
  "name": "Update without hash",
  "keyword": "Scenario",
  "line": 32,
  "type": "scenario"
});
formatter.step({
  "name": "that the request body is valid JSON",
  "keyword": "Given ",
  "line": 33,
  "doc_string": {
    "value": "  {\n    \"id\": \"1234\",\n    \"name\": \"abc\"\n  }",
    "line": 34,
    "content_type": ""
  }
});
formatter.step({
  "name": "I make a \"PUT\" request to \"/v1.3/pylon/update\"",
  "keyword": "When ",
  "line": 40
});
formatter.step({
  "name": "the response status code should be \"400\"",
  "keyword": "Then ",
  "line": 41
});
formatter.step({
  "name": "the response body contains the JSON data",
  "keyword": "And ",
  "line": 42,
  "doc_string": {
    "value": "  {\n    \"error\": \"No hash was supplied.\"\n  }",
    "line": 43,
    "content_type": ""
  }
});
formatter.match({
  "location": "TestRest.that_the_request_body_is_valid_JSON(String)"
});
formatter.result({
  "duration": 28000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "PUT",
      "offset": 10
    },
    {
      "val": "/v1.3/pylon/update",
      "offset": 27
    }
  ],
  "location": "TestRest.i_make_a_request_to(String,String)"
});
formatter.result({
  "duration": 82000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "400",
      "offset": 36
    }
  ],
  "location": "TestRest.the_response_status_code_should_be(String)"
});
formatter.result({
  "duration": 52000,
  "status": "passed"
});
formatter.match({
  "location": "TestRest.the_response_body_contains_the_JSON_data(String)"
});
formatter.result({
  "duration": 18000,
  "status": "passed"
});
formatter.scenario({
  "id": "put-/pylon/update;update-without-name",
  "description": "",
  "name": "Update without name",
  "keyword": "Scenario",
  "line": 49,
  "type": "scenario"
});
formatter.step({
  "name": "that the request body is valid JSON",
  "keyword": "Given ",
  "line": 50,
  "doc_string": {
    "value": "  {\n    \"id\": \"1234\",\n    \"hash\": \"83fa8c8f21c44698be111fa0c1372a40\"\n  }",
    "line": 51,
    "content_type": ""
  }
});
formatter.step({
  "name": "I make a \"PUT\" request to \"/v1.3/pylon/update\"",
  "keyword": "When ",
  "line": 57
});
formatter.step({
  "name": "the response status code should be \"400\"",
  "keyword": "Then ",
  "line": 58
});
formatter.step({
  "name": "the response body contains the JSON data",
  "keyword": "And ",
  "line": 59,
  "doc_string": {
    "value": "  {\n    \"error\": \"No name was supplied.\"\n  }",
    "line": 60,
    "content_type": ""
  }
});
formatter.match({
  "location": "TestRest.that_the_request_body_is_valid_JSON(String)"
});
formatter.result({
  "duration": 33000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "PUT",
      "offset": 10
    },
    {
      "val": "/v1.3/pylon/update",
      "offset": 27
    }
  ],
  "location": "TestRest.i_make_a_request_to(String,String)"
});
formatter.result({
  "duration": 65000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "400",
      "offset": 36
    }
  ],
  "location": "TestRest.the_response_status_code_should_be(String)"
});
formatter.result({
  "duration": 57000,
  "status": "passed"
});
formatter.match({
  "location": "TestRest.the_response_body_contains_the_JSON_data(String)"
});
formatter.result({
  "duration": 16000,
  "status": "passed"
});
});