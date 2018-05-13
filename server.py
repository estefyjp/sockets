import socket
import sys
import json
import threading
import unicodedata
import os


p_mssg = " "
b_mssg = " "
tup1 = ()
max_count = 0
max_count_b = 0
times_mssg_broadcasted = 0
list_broadcast_ip = []
list_broadcast_port = []
flag_private = 0
flag_broadcast = 0
total_users = 0

#       *********************           chat                    ***************


def processEntry(data, addr):
    user_exists = 0
    user_ok = 0
    if data.startswith('hello'):
        listen_servers()
    else:
        d = data.replace('\\', ' ')  # format string
        d_json = json.loads(d)
        d_json['id'] = addr      # change id to ip address
        # verify that usernames are unique
        data = json.load(open('example.json'))
        if d_json['action'] == 'e':  # if a new user is registering
            print("new user registering....")
            print("address", addr, type(addr))
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
    global b_mssg
    global list_broadcast_ip
    global total_users
    count_ip = 0
    print("*BROADCAST MESSAGE")
    data = json.load(open('example.json'))
    print("_________", len(data['messages'][0]))
    total_users = len(data['messages'][0])

    # for d in data['messages']:
        # if d['id'][0] not in list_broadcast_ip:
            # list_broadcast_ip.append(d['id'][0])
    # print("_________", list_broadcast_ip)
    b_mssg = d_json['text']
    # s.sendto(d_json['text'], d['user'])


def private_message(d_json):
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
            list1[0] = d['id'][0]  # ip
            list1[1] = d_json['id'][1]  # this doesn't work because it's the port from case c, not from the always reading thread
            modify_ip = list1[0]
            modify_ip = modify_ip.encode("utf-8")
            p_mssg = p_mssg.encode("utf-8")
            list1[0] = modify_ip
            global tup1
            tup1 = tuple(list1)
            print("Private message sent: ", p_mssg, tup1)
        else:
            user_ok += 1
    if user_ok == count:  # let client know user doesn't exist
        print("error")
        s.sendto(error_mssg, d_json['id'])


def resend_broadcast(d_json):
    global list_broadcast_port
    global b_mssg
    global times_mssg_broadcasted
    global total_users
    if b_mssg != " ":
        flag = 0
        count_ports = 0
        # for i in range(10):

        # if d_json['id'][1] not in list_broadcast_port:
                # count_ports += 1
                # x = len(list_broadcast_port)
                # if count_ports == x:
            # list_broadcast_port.append(d_json['id'][1])
            # print("ports:", list_broadcast_port)
        if d_json['id'][0] not in list_broadcast_ip:
            list_broadcast_ip.append(d_json['id'][0])
            flag = 1
        if flag == 1:
                # if max_count_b < 2:
                    li2 = d_json['id']
                    tup_broadcast = tuple(li2)
                    message_for_client = b_mssg
                    print("___mssg", message_for_client, "addr", tup_broadcast)
                    # global max_count_b
                    # max_count_b += 1
                    times_mssg_broadcasted += 1
                    s.sendto(message_for_client, tup_broadcast)
        #  con elif se cicla
        if times_mssg_broadcasted == total_users:
            b_mssg = " "


def resend_message(d_json):
    print("mssg", p_mssg, "addr", tup1)
    if p_mssg != " ":
        if max_count < 2:
            print("entro")
            global tup1
            li = list(tup1)
            li[1] = d_json['id'][1]
            tup1 = tuple(li)
            message_for_client = p_mssg
            print("mssg", message_for_client, "addr", tup1)
            global max_count
            max_count += 1
            s.sendto(message_for_client, tup1)
        else:
            global p_mssg
            p_mssg = " "


def exit(d_json):
    print("*EXIT")
    s.sendto("exit", d_json['id'])


def solve_action(d_json):
    global flag_broadcast
    global flag_private
    global list_broadcast_ip
    action = d_json['action']
    print(action)
    if action == 'a':
        list_broadcast_ip = []
        flag_broadcast = 1
        flag_private = 0
        broadcast_message(d_json)
    elif action == 'b':
        flag_broadcast = 0
        flag_private = 0
        give_users(d_json)
    elif action == 'c':
        flag_private = 1
        flag_broadcast = 0
        private_message(d_json)
    elif action == 'd':
        exit(d_json)
    elif action == 'e':
        print("new user saved")
        flag_broadcast = 0
        flag_private = 0
    elif action == 'f' and flag_private == 1:  # read private message
        if p_mssg == " ":
            print("cambio de count")
            global max_count
            max_count = 0
        resend_message(d_json)
    elif action == 'f' and flag_broadcast == 1:  # read broadcast message
        # if b_mssg == " ":
            # print("cambio de count")
            # global max_count_b
            # max_count_b = 0
        resend_broadcast(d_json)


#       *********************          ENDOF chat                    **********


def other_servers_send():
    server_addr = ('192.168.1.68', 8888)
    server_hello_mssg = "hello server 1 active"
    for i in range(0, 3):
        s.sendto(server_hello_mssg, server_addr)


def listen_servers():
    for i in range(0, 3):
        d_server = s.recvfrom(1024)
        data_s = d_server[0]
        addr_s = d_server[1]
        if not data_s:
            print("no data")
            break
        print(data_s)


# Datagram (udp) socket
def server_thread():
    # HOST = ' '  # Symbolic name meaning all available interfaces
    # PORT = 8888  # Arbitrary non-privileged port

    #  now keep talking with the client
    while 1:
        # print("i'm talkin to client")
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


with open('example.json', 'a+') as f:  # refresh json every time server starts
    f.seek(121, 0)
    f.truncate()
    f.write(']}')

threads = list()
for i in range(3):
    t = threading.Thread(target=server_thread)
    # t.daemon = True
    threads.append(t)
    t.start()

t_servers_send = threading.Thread(target=other_servers_send)
t_servers_send.start()

t_servers_read = threading.Thread(target=listen_servers)
t_servers_read.start()
