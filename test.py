import json
with open('example.json', 'a+') as f:  # adds user to json
    f.seek(121, 0)
    f.truncate()
    f.write(']}')
