##############################################################
# robotboundaryusage.py
##############################################################
import socket
import time

robotboundaryPort = 8018
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

startMsg    = "msg(start,  dispatch,python,robotboundary,start(0),1)"
stopMsg     = "msg(stop,   dispatch,python,robotboundary,stop(0),1)" 
resumeMsg   = "msg(resume, dispatch,python,robotboundary,resume(0),1)"  
## obstacleEv  = "msg(collision, event,python,robotboundary,collision(wall),1)"  
 
def connect(port) :
    server_address = ('localhost', port)
    sock.connect(server_address)    
    print("CONNECTED WITH robotboundary" , server_address)

def forward( message ) :
    print("forward ", message)
    msg = message + "\n"
    byt=msg.encode()    #required in Python3
    sock.send(byt)

def work() :
    forward( startMsg ) 
    time.sleep(1)
    """
    forward( resumeMsg ) 
    time.sleep(1)
    forward( stopMsg )
    time.sleep(1)
    forward( resumeMsg )
    time.sleep(1)
    forward( obstacleEv )
    time.sleep(1)
    forward( obstacleEv )
    time.sleep(1)
    forward( obstacleEv )
    time.sleep(1)
    forward( obstacleEv )
    """
def read() :
    BUFFER_SIZE = 1024
    data = sock.recv(BUFFER_SIZE)
    print( "received data:", data )

def terminate() :
    #sock.close()
    print("BYE")

###########################################    
connect(robotboundaryPort)
work()
##read()
terminate()  
