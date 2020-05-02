import serial
import  time


#possible timeout values:
#    1. None: wait forever, block call
#    2. 0: non-blocking mode, return immediately
#    3. x, x is bigger than 0, float allowed, timeout block call
#ser.port = "/dev/ttyUSB7"
#ser.port = "/dev/ttyS2"

'''
ser.port     = "/dev/ttyUSB0"
ser.baudrate = 115200
ser.bytesize = serial.EIGHTBITS     #.EIGHTBITS 	#number of bits per bytes
ser.parity   = serial.PARITY_NONE   #PARITY_NONE 	#set parity check: no parity
ser.stopbits = serial.STOPBITS_ONE  #STOPBITS_ONE 	#number of stop bits

#ser.timeout = None          		#block read
#ser.timeout  = 1            	    #non-block read
#ser.timeout = 2              		#timeout block read
#ser.xonxoff  = False     			#disable software flow control
#ser.rtscts   = False     			#disable hardware (RTS/CTS) flow control
#ser.dsrdtr   = False       			#disable hardware (DSR/DTR) flow control
#ser.writeTimeout = 2    			#timeout for write

 

try: 
    print( "open serial port: " + str( ser.port )  )
    ser = serial.Serial('/dev/ttyUSB0', 115200) 
    #ser.open()
except: 
    print( "error open serial port: "   )
    exit()
    
    
the Arduino is designed to reset everytime a serial connection is made over the USB interface    
'''

ser = serial.Serial('/dev/ttyUSB0', 115200) 
if ser.isOpen():

    try:
        
        #ser.flushInput() 	#flush input buffer, discarding all its contents
        #ser.flushOutput()	#flush output buffer, aborting current output 
                 			#and discard all that is in buffer

        time.sleep(2)  		#give the Arduino time to reboot
        #write data
        print("write data ")
        ser.write( "z".encode()  ) #"z\r\n".encode()
        time.sleep(1)  #give the serial port sometime to receive the data
        ser.flushOutput()
        print("write z ")
        ser.write( "x".encode()  )  
        time.sleep(0.5)  #give the serial port sometime to receive the data
        ser.flushOutput()

        numOfLines = 0
        print("read data ")
        while True:
            response = ser.readline().decode()
            print("read data: " + response)
            numOfLines = numOfLines + 1
            if (numOfLines >= 5):
              break
        ser.close()
    except:
        print( "error communicating...  "  )

else:
    print( "cannot open serial port " )
