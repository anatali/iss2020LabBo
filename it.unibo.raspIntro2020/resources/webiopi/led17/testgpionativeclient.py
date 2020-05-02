# -------------------------------------------------------------------------------
# testgpionativeclient.py
# Run this on Raspberry /home/pi/nat/WebIOPi-0.7.1/nathtdocs
# -------------------------------------------------------------------------------

from webiopi.clients import *
from time import sleep

client = PiHttpClient("192.168.1.6")
#client.setCredentials("webiopi", "raspberry")

# RPi native GPIO
gpio = NativeGPIO(client)
gpio.setFunction(17, "out")
state = True

for x in range(0, 6): 
    gpio.digitalWrite(17, state)     
    state = not state
    print("again ... ")
    sleep(1.0)
