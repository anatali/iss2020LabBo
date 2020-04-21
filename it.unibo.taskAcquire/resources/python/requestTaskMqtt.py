
###########################################
## requestTaskMqtt.py
###########################################
import time
import paho.mqtt.client as paho
 
brokerAddr     = "broker.hivemq.com"

actorName      = "taskdeploy"
topic          = "unibo/qak/taskdeploy"  
reqTemplate    = "msg(getmeatask, request, SURNAME, taskdeploy, getmeatask(SURNAME, MATR,MAIL),1)"

def emit( cmd ) :
    message = eventTemplate.replace("CMDVAL", cmd)
    #print("emit event via MQTT", message)
    msg = message + "\n"
    #byt = msg.encode()     
    #tb  = topic.encode()
    client.publish(topic, msg)

def request( surname, badge, mail ) :
    message = reqTemplate.replace("SURNAME", surname).replace("MATR", badge).replace("MAIL", mail)
    print("request via MQTT", message)
    msg = message + "\n"
    client.publish(topic, msg)

def on_message(client, userdata, message):
    print("message received " ,str(message.payload.decode("utf-8")))
    print("message topic=",message.topic)
    print("message qos=",message.qos)
    print("message retain flag=",message.retain)
    

def sendMyRequest():
    surname = "studentPython1"
    client.subscribe("unibo/qak/"+surname, qos=0)
    badge   = "0000123456"
    mail    = "'studentPython1.firstname1@studio.unibo.it'"	 
    request( surname, badge, mail )
#################################################   
client= paho.Client("sender")      
client.connect(brokerAddr)              #connect
print("connected to broker ", brokerAddr)
client.on_message=on_message
client.loop_start()
 
sendMyRequest()

time.sleep(2)
print("BYE " )