#!/bin/bash
set -e

BASE_URL="http://localhost:8080"

echo "Creating a task..."
CREATE_RES=$(
http --body POST $BASE_URL/tasks \
    title="Demo Task Random" \
    description="Task created via script" \
)

echo "Response:"
echo $CREATE_RES | jq .

echo ""
echo "Fetching all tasks"
http GET $BASE_URL/tasks

echo ""
echo "Flow completed."