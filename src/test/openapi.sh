#!/bin/bash

http POST https://api.openai.com/v1/chat/completions \
  Authorization:"Bearer $API_KEY" \
  Content-Type:application/json \
  model="gpt-4" \
  messages:='[{"role": "user", "content": "Erzeuge mir eine java junit test klasse."}]' \
  -v

#http https://api.openai.com/v1/models \
#  Authorization:"Bearer $API_KEY" \
