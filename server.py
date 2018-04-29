import socket
import sys
import json
import threading
import unicodedata

p_mssg = " "
tup1 = ()


def processEntry(data, addr):
    user_exists = 0
    user_ok = 0
    d = data.replace('\\', ' ')  # format string
    d_json = json.loads(d)
    d_json['id'] = addr      # change id to ip address
    # verify that usernames are unique
    data = json.load(open('example.json'))
    if d_json['action'] == 'e':  # if a new user is registering
        print("new user registering....")
        for d in data['messages']:
            if (d['user'] == d_json['user']) and (d_json['action'] == 'e'):
                user_exists = 1
            elif (d['user'] != d_json['user']) and (d_json['action'] == 'e'):
                user_ok = 1
        if user_exists == 1:
            s.sendto("Username already exist", d_json['id'])
        elif user_ok == 1:
            s.sendto("Username valid", d_json['id'])
            with open('example.json', 'a+') as f:  # adds user to json
                f.seek(0, 2)
                f.truncate()
                f.seek(-2, 2)
                f.truncate()
                f.write(' , ')
                json.dump(d_json, f)
                f.write(']}')
    #  TODO update each users must recent action
    elif d_json['action'] != 'f':
        with open('example.json', 'a+') as f:
            f.seek(0, 2)
            f.truncate()
            f.seek(-2, 2)
            f.truncate()
            f.write(' , ')
            json.dump(d_json, f)
            f.write(']}')
        solve_action(d_json)
    elif d_json['action'] == 'f':
        solve_action(d_json)


def give_users(d_json):  # gives list of users connected to the chat
    user_array = " "
    data = json.load(open('example.json'))
    for d in data['messages']:
        user = d['user'] + ", "
        user_array += user
    print("give_users", user_array, d_json['id'])
    s.sendto(user_array, d_json['id'])


def broadcast_message(d_json):
    print("*BROADCAST MESSAGE")
    data = json.load(open('example.json'))
    for d in data['messages']:
        print("send to", d_json['text'], d['id'])
        s.sendto(d_json['text'], d['user'])


def private_message(d_json):
    user_exists = 0
    user_ok = 0
    count = 0
    error_mssg = "User doesn't exist"
    text = d_json['text']
    array_text = text.split(',')
    global p_mssg
    p_mssg = array_text[0]
    recipient_pm = array_text[1]
    data = json.load(open('example.json'))
    for d in data['messages']:
        count += 1
        if recipient_pm == d['user']:
            list1 = [0, 0]
            list1[0] = d['id'][0]
            list1[1] = d_json['id'][1]
            modify_ip = list1[0]
            modify_ip = modify_ip.encode("utf-8")
            p_mssg = p_mssg.encode("utf-8")
            list1[0] = modify_ip
            global tup1
            tup1 = tuple(list1)
            # print("d_json_0", type(d_json['id'][0]))
            # print("tuple_0", type(list1[0]))
            # print("d_json_1", type(d_json['id'][1]))
            # print("tuple_1", type(list1[1]))
            # print("d_json", type(d_json['id']))
            # print("tuple", type(tup1))

            # print("Private message sent: ", p_mssg, tuple(d['id']))
            # print("Private message sent: ", p_mssg, tuple(tup))
            # print("Private message sent: ", p_mssg, d_json['id'])
            print("Private message sent: ", p_mssg, tup1)

            # s.sendto(p_mssg, tup1)
        else:
            user_ok += 1
    if user_ok == count:
        print("error")
        s.sendto(error_mssg, d_json['id'])


def resend_message(d_json):
    print("mssg", p_mssg, "addr", tup1)
    if p_mssg != " ":
        print("entro")
        global tup1
        li = list(tup1)
        li[1] = d_json['id'][1]
        tup1 = tuple(li)
        print("mssg", p_mssg, "addr", tup1)
        s.sendto(p_mssg, tup1)
        # global p_mssg
        # p_mssg = " "


def exit(d_json):
    print("*EXIT")
    s.sendto("exit", d_json['id'])


def solve_action(d_json):
    action = d_json['action']
    print(action)
    if action == 'a':
        broadcast_message(d_json)
    elif action == 'b':
        give_users(d_json)
    elif action == 'c':
        private_message(d_json)
    elif action == 'd':
        exit(d_json)
    elif action == 'e':
        print("new user saved")
    elif action == 'f':
        resend_message(d_json)



# Datagram (udp) socket
def server_thread():
    # HOST = ' '  # Symbolic name meaning all available interfaces
    # PORT = 8888  # Arbitrary non-privileged port

    #  now keep talking with the client
    while 1:
        print("i'm talkin to client")
        # receive data from client (data, addr)
        d = s.recvfrom(1024)
        data = d[0]
        addr = d[1]
        print(d[0])
        if not data:
            print("no data")
            break

        reply = 'OK...' + data
        processEntry(data, addr)
        print("*mssg", p_mssg, "*addr", tup1)
        # s.sendto(reply, addr)
        # print 'Message[' + addr[0] + ':' + str(addr[1]) + '] - ' + data.strip()

    s.close()


HOST = ''  # Symbolic name meaning all available interfaces
PORT = 8888  # Arbitrary non-privileged port

try:
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)  #  Make Socket Reusable
    s.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1) #  Allow incoming broadcasts
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

threads = list()
for i in range(3):
    t = threading.Thread(target=server_thread)
    # t.daemon = True
    threads.append(t)
    t.start()

