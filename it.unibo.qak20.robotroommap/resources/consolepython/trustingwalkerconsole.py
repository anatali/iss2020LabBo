##############################################################
# trustingwalkerconsole.py
##############################################################

import paho.mqtt.client as paho

### brokerAddr ="broker.hivemq.com"
brokerAddr ="localhost"

#define callback
def on_message(client, userdata, message):
    print("received message =",str(message.payload.decode("utf-8")))

def requestMovecell( x,y ):
    msg = "msg(movetoCell,request,py,trustingwalker,movetoCell("+x+","+y+"),1)"
    print("requestMovecell:" , msg  )
    client.publish("unibo/qak/trustingwalker",msg) 

def forwardMsg( move ):
    msg = "msg(cmd,dispatch,py,trustingwalker,cmd("+str(move)+"),1)"
    print("forwardMsg  forwardMsg= :" , msg  )
    client.publish("unibo/qak/trustingwalker",msg) 

def console() :  
    print("console  STARTS :"   )
    cmd =  str( input() )
    while( cmd != "q"  ) :
        #print( cmd )
        if( cmd == "g" ) :
            requestMovecell("5","3")
        if( cmd == "b" ) :
            requestMovecell("0","0")
        cmd = str(input() )
##################################################################

client= paho.Client("pyconsole")  
client.on_message=on_message            # Bind function to callback    

client.connect(brokerAddr)              #connect
print("connected to broker ", brokerAddr)

print("subscribing to unibo/polar")
client.subscribe("unibo/polar")      #subscribe

print("starting the client loop to receive sonar data" )
client.loop_start()                  #start loop to process received messages
 
console()