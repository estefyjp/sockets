import json

data = {}
data = json.load(open('example.json'))

for d in data['messages']:
    print(d['user'])
