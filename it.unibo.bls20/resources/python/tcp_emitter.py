##############################################################
# tcp_emitter.py
##############################################################
import socket
import time

port = 8080
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

alarmFire     = "msg(alarm,event,python,none,alarm(firetcp),1)"
alarmTsunami  = "msg(alarm,event,python,none,alarm(tsunamitcp),2)"
onPressed     = "msg(onPressed,event,python,none,onPressed(python),2)"  

ledon         = "msg(turnOn, dispatch,python,led,turnOn(a),2)"
ledoff        = "msg(turnOff,dispatch,python,led,turnOff(a),3)"

def connect(port) :
    server_address = ('localhost', port)
    sock.connect(server_address)    
    print("CONNECTED " , server_address)

def emit( message ) :
    print("emit ", message)
    msg = message + "\n"
    byt=msg.encode()    #required in Python3
    sock.send(byt)

def forward( message ) :
    print("forward ", message)
    msg = message + "\n"
    byt=msg.encode()    #required in Python3
    sock.send(byt)

def work() :
    ## emit( onPressed ) 
    ## emit( alarmFire ) 
    forward( ledon )
    time.sleep(1)
    forward( ledoff )
 

def terminate() :
    sock.close()
    print("BYE")

###########################################    
connect(port)
work()
terminate()  
