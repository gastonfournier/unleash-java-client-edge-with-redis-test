## About this repository
This test setup validates 10000 isEnabled calls agasint Unleash using the Java SDK. The SDK is connected to Edge which is using Redis as a cache and connecting to an upstream Unleash server. 

The test is run in a docker container and the results are output in stdout.

### About the test
The test will create a feature flag, enable it and then run 10000 isEnabled calls. The results are output in stdout.

Then it will disable the feature flag and run 10000 isEnabled calls again. The results are output in stdout.

Expect to see the flag evaluated 10000 times, all being true, and then another 10000 times, all being false.

### Important notice:
This setup is only intended for testing. Some of the settings are not secure and should not be used in production.

Edge is configured in a way that will sync quickly with upstream, which is not recommended in production. This is done to speed up the test.

## How to run the test
1. Clone this repository
2. Run the following command in the root of the repository
```bash
./run.sh
```

## Results
Besides the results in the output, you can explore edge metrics at http://localhost:3063/internal_backstage/prometheus.

You can also access Unleash at http://localhost:4242 and check the metrics for the feature flag that should show 10000 isEnabled calls, all being true.

The test will output the following results:
```bash
ClientFeatureResponse: status=CHANGED httpStatus=200
Number of toggles from edge: 1
test-flag -> true
ClientFeatureResponse: status=NOT_CHANGED httpStatus=304
Flag name: test-flag
Waiting to send metrics
ClientFeatureResponse: status=NOT_CHANGED httpStatus=304
test-flag was true: 10000 of 10000

BUILD SUCCESSFUL in 2s
3 actionable tasks: 1 executed, 2 up-to-date
Disabling the test flag: test-flag
Wait 2 seconds so the new flag change can be propagated to the edge
Runing test with java

> Task :run
11:17:56.288 [main] INFO  i.g.r.FeatureBackupHandlerFile -- Unleash will try to load feature toggle states from temporary backup
ClientFeatureResponse: status=CHANGED httpStatus=200
Number of toggles from edge: 1
test-flag -> false
ClientFeatureResponse: status=NOT_CHANGED httpStatus=304
Flag name: test-flag
Waiting to send metrics
ClientFeatureResponse: status=NOT_CHANGED httpStatus=304
test-flag was true: 0 of 10000

BUILD SUCCESSFUL in 2s
3 actionable tasks: 1 executed, 2 up-to-date

==================================================
Test completed, you can see the results in the output above, expect to see the message: "test-flag was true: 10000 of 10000"
You can access Unleash at http://localhost:4242 and Edge at http://localhost:3063.
To shut down containers run 'docker compose down'
If you want to test the java sample again run 'FLAG_NAME="test-flag" ./gradlew run' in the java-cli directory
==================================================
```