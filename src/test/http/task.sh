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

TASK_ID=$(echo $CREATE_RES | jq -r '.id')


echo "Get Task by ID: $TASK_ID"
http --check-status GET "$BASE_URL/tasks/$TASK_ID"


echo ""


echo "Upadte Task ID: $TASK_ID"
http --body PATCH "$BASE_URL/tasks/$TASK_ID" \
    title="Demo Task Random ($TASK_ID)" \
    description="Task updated via script ($TASK_ID)" \
    status:=1
echo ""

echo "Delete Task by ID: $TASK_ID"
http --check-status DELETE "$BASE_URL/tasks/$TASK_ID"


echo ""

echo "Get Task by ID after delete: $TASK_ID"
http --check-status GET "$BASE_URL/tasks/$TASK_ID"

echo ""



echo "Flow completed."