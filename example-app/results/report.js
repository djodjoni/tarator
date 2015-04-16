$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("features/test_sample.feature");
formatter.feature({
  "id": "sample-feature",
  "line": 2,
  "description": "",
  "name": "Sample Feature",
  "keyword": "Feature"
});
formatter.scenario({
  "id": "sample-feature;test-section-1",
  "line": 5,
  "type": "scenario",
  "description": "",
  "name": "test Section 1",
  "keyword": "Scenario"
});
formatter.step({
  "line": 7,
  "name": "we are at section 1 screen",
  "keyword": "Given "
});
formatter.step({
  "line": 8,
  "name": "press example action",
  "keyword": "When "
});
formatter.step({
  "line": 9,
  "name": "we must pass",
  "keyword": "Then "
});
formatter.match({
  "location": "BaseTest.we_are_at_section_screen(int)",
  "arguments": [
    {
      "val": "1",
      "offset": 18
    }
  ]
});
formatter.result({
  "status": "passed",
  "duration": 1324279787
});
formatter.match({
  "location": "BaseTest.press_example_action()"
});
formatter.result({
  "status": "passed",
  "duration": 183106
});
formatter.match({
  "location": "BaseTest.we_must_pass()"
});
formatter.result({
  "status": "passed",
  "duration": 122070
});
formatter.scenario({
  "id": "sample-feature;test-scenario2",
  "line": 12,
  "type": "scenario",
  "description": "",
  "name": "test scenario2",
  "keyword": "Scenario"
});
formatter.step({
  "line": 14,
  "name": "we are at section 2 screen",
  "keyword": "Given "
});
formatter.step({
  "line": 15,
  "name": "press example action",
  "keyword": "When "
});
formatter.step({
  "line": 16,
  "name": "we must fail",
  "keyword": "Then "
});
formatter.match({
  "location": "BaseTest.we_are_at_section_screen(int)",
  "arguments": [
    {
      "val": "2",
      "offset": 18
    }
  ]
});
formatter.result({
  "status": "passed",
  "duration": 1108764648
});
formatter.match({
  "location": "BaseTest.press_example_action()"
});
formatter.result({
  "status": "passed",
  "duration": 213623
});
formatter.match({
  "location": "BaseTest.we_must_fail()"
});
formatter.result({
  "status": "passed",
  "duration": 213623
});
});