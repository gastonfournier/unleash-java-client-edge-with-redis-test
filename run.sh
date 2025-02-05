#!/bin/bash
echo "Start up Unleash server and edge"
docker compose up -d

echo "Waiting for Unleash to start"
while ! curl --output /dev/null --silent --head --fail http://localhost:4242/health; do
  echo "Waiting for Unleash to start..."
  sleep 1
done

export FLAG_NAME="test-flag"
echo "Creating a test flag: ${FLAG_NAME}"
curl --location --request POST 'http://localhost:4242/api/admin/projects/default/features' \
    --header 'Authorization: *:*.unleash-insecure-admin-api-token' \
    --header 'Content-Type: application/json' \
    --data-raw "{
  \"type\": \"release\",
  \"name\": \"${FLAG_NAME}\",
  \"description\": \"\",
  \"impressionData\": false
}"

set -e
echo "Enabling the test flag: ${FLAG_NAME}"
curl --location --request POST \
    --header 'Authorization: *:*.unleash-insecure-admin-api-token' \
    "http://localhost:4242/api/admin/projects/default/features/${FLAG_NAME}/environments/development/on"

echo "Wait 2 seconds so the new flag change can be propagated to the edge"
sleep 2

echo "Runing test with java"
cd java-cli
./gradlew run
cd ..

echo ""
echo "=================================================="
echo "Test completed, you can see the results in the output above, expect to see the message: \"${FLAG_NAME} was true: 10000 of 10000\""
echo "You can access Unleash at http://localhost:4242 and Edge at http://localhost:3063." 
echo "To shut down containers run 'docker compose down'"
echo "If you want to test the java sample again run 'FLAG_NAME=\"${FLAG_NAME}\" ./gradlew run' in the java-cli directory"
echo "=================================================="