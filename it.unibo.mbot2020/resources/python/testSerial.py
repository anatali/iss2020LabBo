import serial
import  time

##ser = serial.Serial('/dev/ttyUSB0', 115200) 
ser = serial.Serial('COM20', 115200) 

if ser.isOpen():
    try:
        time.sleep(2)  		#give the Arduino time to reboot

        numOfLines = 0
        print("read data ")
        while True:
            response = ser.readline().decode()
            print("read data: " + response)
            numOfLines = numOfLines + 1
            if (numOfLines >= 5):
              break
        #ser.close()
    except:
        print( "error communicating...  "  )
else:
    print( "cannot open serial port " )

def moveRobot( move ):
    ser.write( move.encode()  )
     
def console() :  
    print("console>", end = ''   )
    cmd =  str( input() )
    #print("console  cmd= :" , cmd  )   # w,s,a,d,r,l,z,x
    while( cmd != "q"  ) :
        #print( cmd )
        moveRobot( cmd )
        print("console>", end = '' )
        #read() #blocking ...
        cmd = str(input() )

console()    