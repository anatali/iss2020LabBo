##############################################################
# tcp_emitter.py
##############################################################
import socket
import time

port = 8095
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

alarmFire     = "msg(alarm,event,python,none,alarm(firetcp),1)"
alarmTsunami  = "msg(alarm,event,python,none,alarm(tsunamitcp),2)"
onPressed     = "msg(onPressed,event,python,none,onPressed(python),2)"  
def connect(port) :
    server_address = ('localhost', port)
    sock.connect(server_address)    
    print("CONNECTED " , server_address)

def emit( message ) :
    print("emit ", message)
    msg = message + "\n"
    byt=msg.encode()    #required in Python3
    sock.send(byt)

def work() :
    emit( onPressed ) 
    time.sleep(1)
 

def terminate() :
    sock.close()
    print("BYE")

###########################################    
connect(port)
work()
terminate()  
