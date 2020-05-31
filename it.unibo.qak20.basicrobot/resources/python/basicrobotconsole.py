##############################################################
# basicrobotconsole.py
##############################################################
import math
import time
import datetime
import paho.mqtt.client as paho

### broker="broker.hivemq.com"
brokerAddr="localhost"
#define callback
def on_message(client, userdata, message):
    #time.sleep(1)
    print("received message =",str(message.payload.decode("utf-8")))


 
 
def moveRobot( move ):
	msg = "msg(cmd,dispatch,py,basicrobot,r,1)"
	print("moveRobot  moveRobot= :" , msg  )
	client.publish("ctxbasicrobot/basicrobot",msg) 
  

def console() :  
    print("console  STARTS :"   )
    cmd =  str( input() )
    ## info("Console cmd= %s " % (cmd))
    #print("console  cmd= :" , cmd  )   # w,s,a,d,r,l,z,x
    while( cmd != "q"  ) :
        #print( cmd )
        moveRobot( cmd )
        cmd = str(input() )
###

client= paho.Client("basicrobotconsole")  
client.on_message=on_message            # Bind function to callback    

client.connect(brokerAddr)              #connect
print("connected to broker ", brokerAddr)
startTime     = time.time() 
#print( "startTime=" , time.localtime( startTime ) )
print( "startTime=" , startTime )

print("subscribing to unibo/polar")
client.subscribe("unibo/polar")      #subscribe

### print("collecting values; please wait ..." )
### client.loop_start()             #start loop to process received messages
### dataFile.write("START JOB robotCmdExec at " + str( datetime.datetime.now() ) + " \n")

 
console() 			