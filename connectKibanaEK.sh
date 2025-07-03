#!/bin/bash

# Setup Elasticsearch passwords
# Run this after starting Elasticsearch for the first time

echo "Setting up Elasticsearch passwords..."

# Wait for Elasticsearch to be ready
echo "Waiting for Elasticsearch to be ready..."
until curl -s -u elastic:${ELASTIC_PASSWORD} http://localhost:9200/_cluster/health > /dev/null; do
    echo "Waiting for Elasticsearch..."
    sleep 5
done

echo "Elasticsearch is ready!"

# Set kibana_system password
echo "Setting kibana_system password..."
curl -s -X POST -u elastic:${ELASTIC_PASSWORD} \
  -H "Content-Type: application/json" \
  http://localhost:9200/_security/user/kibana_system/_password \
  -d "{\"password\":\"${KIBANA_PASSWORD}\"}"

echo -e "\n\nPasswords set successfully!"
echo "You can now access:"
echo "  - Elasticsearch: http://localhost:9200 (user: elastic, password: from .env)"
echo "  - Kibana: http://localhost:5601 (user: elastic, password: from .env)"