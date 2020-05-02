# -------------------------------------------------------------------------------
# testgpionative.py
# Run this on Raspberry /home/pi/nat/WebIOPi-0.7.1/nathtdocs
# -------------------------------------------------------------------------------
from time import sleep
try:
    import _webiopi.GPIO as GPIO
except:
    print("testgpionative import warning " )
    pass

channel=17

print("testgpionative | GPIO="+str(GPIO))
print("testgpionative | see https://github.com/wuestkamp/raspberry-gpio-python ")
print("testgpionative | GPIO.LOW="+str(GPIO.LOW))

GPIO.setup(channel, GPIO.OUT, GPIO.LOW) #or GPIO.setup(channel, GPIO.OUT)

sleep(1.0)
state = GPIO.HIGH

for x in range(0, 6): 
    v = GPIO.digitalRead(channel)
    #print("testgpionative | read %s=%s", %channel, %str(v))
    GPIO.digitalWrite(channel, state)     
    state = not state
    sleep(0.5)

v = GPIO.digitalRead(channel)
print("testgpionative | read 17="+ str(v))

