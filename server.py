import socket
import sys
import json


def processEntry(data, addr):
    d = data.replace('\\', ' ')
    d_json = json.loads(d)
    d_json['id'] = addr[0]
    print(d_json)
    solve_action(d_json)
    with open('example.json', 'a+') as f:
        f.seek(0, 2)
        f.truncate()
        f.seek(-2, 2)
        f.truncate()
        f.write(' , ')
        json.dump(d_json, f)
        f.write(']}')


def give_users():
    print("*GIVE USERS")
    user_array = []
    data = json.load(open('example.json'))
    for d in data['messages']:
        user_array = d['user']
        print("user_array", user_array)
    #s.sendto(user_array, d['id'])


def broadcast_message(d_json):
    print("*BROADCAST MESSAGE")
    data = json.load(open('example.json'))
    for d in data['messages']:
        print("send to", d_json['text'], d['id'])
        #s.sendto(d_json['text'], d['user'])


def private_message(d_json):
    print("*PRIVATE MESSAGE")
    s.sendto(d_json['text'], d_json['id'])


def exit(d_json):
    print("*EXIT")
    s.sendto("exit", d_json['id'])


def solve_action(d_json):
    action = d_json['action']
    print(action)
    if action == 'a':
        broadcast_message(d_json)
    elif action == 'b':
        give_users()
    elif action == 'c':
        private_message(d_json)
    elif action == 'd':
        exit(d_json)


HOST = ''   # Symbolic name meaning all available interfaces
PORT = 8888 # Arbitrary non-privileged port

# Datagram (udp) socket
try:

    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    print 'Socket created'
except socket.error, msg:
    print 'Failed to create socket. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
    sys.exit()


# Bind socket to local host and port
try:
    s.bind((HOST, PORT))
except socket.error, msg:
    print 'Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
    sys.exit()

print 'Socket bind complete'

#now keep talking with the client
while 1:
    # receive data from client (data, addr)
    d = s.recvfrom(1024)
    data = d[0]
    addr = d[1]

    if not data:
        break

    reply = 'OK...' + data
    #processEntry(data, addr)

    s.sendto(reply, addr)
    print 'Message[' + addr[0] + ':' + str(addr[1]) + '] - ' + data.strip()

s.close()
