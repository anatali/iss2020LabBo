from protocols.coap import *
#from utils.logger import logToFile
import time


'''
-----------------------------------------------------------------------
This is the code of userDocs/CoapWebiopi.ipyn  
The goal is to move the robot by sending CoAP Post commands
Requires 
	coap.py (taken from WebIoPi) that in its turn requires:
	utils (copied from WebIoPi)
-----------------------------------------------------------------------
'''

client = COAPClient()
 
def moveRobot( move ):
    response = client.sendRequest(COAPPost("coap://192.168.1.7/macros/do_"+move))
    if response:
         print("POST | Received response:\n%s" % response)

def blinckLed():
    client.sendRequest(COAPPost("coap://192.168.1.7/GPIO/17/function/out"))
    state = True
    for x in range(0, 2): 
        response = client.sendRequest(COAPPost("coap://192.168.1.7/GPIO/17/value/%d" % state))
        if response:
            print("POST | Received response:\n%s" % response)
            state = not state
        else:
            print("No response received")
        response = client.sendRequest(COAPGet("coap://192.168.1.7/GPIO/17/value" ))
        if response:
            print("GET | Received response: %s" %  response.payload.decode("utf-8") )
        else:
            print("No response received")
        time.sleep(1.0)

def console() :  
    print("console  STARTS :"   )
    cmd =  str( input() )
    info("Console cmd= %s " % (cmd))
    #print("console  cmd= :" , cmd  )   # w,s,a,d,r,l,z,x
    while( cmd != "q"  ) :
        #print( cmd )
        moveRobot( cmd )
        #read() #blocking ...
        cmd = str(input() )
        
#moveRobot('r')   
#blinckLed()
#console()
response = client.sendRequest(COAPPost("coap://192.168.1.7/GPIO/17/value/0"  ))
if response:
	print("POST | Received response:\n%s" % response)
else:
	print("POST | No response received")


response = client.sendRequest(COAPGet("coap://192.168.1.7/GPIO/17/value" ))
if response:
	print("GET | Received response: %s" %  response.payload.decode("utf-8") )
else:
	print("GET | No response received")
