import webiopi
import datetime
import time
import serial

GPIO = webiopi.GPIO

LIGHT = 17       # GPIO pin using BCM numbering
 
ser = serial.Serial('/dev/ttyUSB0', 115200)  

# setup function is automatically called at WebIOPi startup
def setup():
    # set the GPIO used by the light to output
    GPIO.setFunction(LIGHT, GPIO.OUT)
    # retrieve current datetime
    now = datetime.datetime.now()
    print("			script0 | SETUP at:" + str(now) )       
    state = True
    for x in range(0, 6):   
	    GPIO.digitalWrite(LIGHT, state)
	    state = not state
	    # gives CPU some time before looping again
	    webiopi.sleep(0.5)
    #ser = serial.Serial('/dev/ttyUSB0', 115200) 
    time.sleep(1) #give the Arduino time to reboot
    print("CONNECTED TO ARDUINO ... " + str( ser.isOpen() ) )
 

# A macro without args which return nothing
@webiopi.macro
def PrintTime():
	webiopi.debug("PrintTime: " + time.asctime())

# Towrds robot commands
def forwardToArduino( msg ):
	ser.write( msg.encode() )  
	ser.flushOutput()

@webiopi.macro
def do_h():
	forwardToArduino("h")
@webiopi.macro
def do_w():
	forwardToArduino("w")
@webiopi.macro
def do_s():
	forwardToArduino("s")
@webiopi.macro
def do_a():
	forwardToArduino("a")
@webiopi.macro
def do_d():
	forwardToArduino("d")
@webiopi.macro
def do_z():
	forwardToArduino("z")
@webiopi.macro
def do_x():
	forwardToArduino("x")
@webiopi.macro
def do_r():
	forwardToArduino("r")
@webiopi.macro
def do_l():
	forwardToArduino("l")

 
# destroy function is called at WebIOPi shutdown
def destroy():
    GPIO.digitalWrite(LIGHT, GPIO.LOW)

     