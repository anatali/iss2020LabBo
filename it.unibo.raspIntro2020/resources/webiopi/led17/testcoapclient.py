# -------------------------------------------------------------------------------
# testcoapclient.py
# Run this on Raspberry /home/pi/nat/cam-robot/WebIOPi-0.7.1/nat
# -------------------------------------------------------------------------------

from webiopi.protocols.coap import *
from time import sleep

client = COAPClient()
client.sendRequest(COAPPost("coap://192.168.1.6/GPIO/17/function/out"))
state = True

for x in range(0, 6): 
    response = client.sendRequest(COAPPost("coap://192.168.1.6/GPIO/17/value/%d" % state))
    if response:
        print("POST | Received response:\n%s" % response)
        state = not state
    else:
        print("No response received")
    response = client.sendRequest(COAPGet("coap://192.168.1.7/GPIO/17/value" ))
    if response:
        print("GET | Received response: %s" % response.payload)
    else:
        print("No response received")
    sleep(1.0)
