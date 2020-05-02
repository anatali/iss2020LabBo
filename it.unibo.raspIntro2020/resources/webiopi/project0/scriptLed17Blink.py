#=================================================
#scriptLed17Blink.py
#=================================================
import webiopi
import datetime

GPIO  = webiopi.GPIO


LEDGPIO = 17 		# GPIO pin using BCM numbering

 
# setup function is automatically called at WebIOPi startup
def setup():
    # set the GPIO used by the light to output
    GPIO.setFunction(LEDGPIO, GPIO.OUT)
    # retrieve current datetime
    now = datetime.datetime.now()
    print("			scriptLed17Blink | SETUP at:" + str(now) )      
    blink()
	
def blink():
    state = True 
    for x in range(0, 6) :   
        GPIO.digitalWrite(LEDGPIO, state)
        state = not state
        webiopi.sleep(0.5)

''' 
# Looped by WebIOPi
def loop():
    webiopi.sleep(5)      
    blink()  
'''

# destroy function is called at WebIOPi shutdown
def destroy():
    GPIO.digitalWrite(LEDGPIO, GPIO.LOW)

