#include <Arduino.h>
#include <Wire.h>
//#include <SoftwareSerial.h>
#include <NewPing.h>


/*
 * -----------------------------------
 * Connections and settings
 * -----------------------------------
 */

//SONAR PIN
const int trigPin = 10;           // Pin trig of HC-SR04
const int echoPin = 9;            // Pin echo of HC-SR04
const int MAX_DISTANCE = 350;     // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm
const int MIN_DISTANCE = 12;      // The distance (in cm) at which we want sonar to signal the collision

//PWM CAPABLE PIN TO CONTROL MOTOR SPEED: 3, 5, 6, 9, 10, 11
//Motor DX
const int motorPin1A  = 5;        // Pin 2 of L293D
const int motorPin1B  = 4;        // Pin 7 of L293D
const int motorPin1EN  = 3;       // Pin 1 of L293D

//Motor SX
const int motorPin2A  = 8;        // Pin 15 of L293D
const int motorPin2B  = 7;        // Pin 10 of L293D
const int motorPin2EN  = 6;       // Pin 9 of L293D

//LED PIN
const int ledRED = A3;            //We are using analog pin as digital out in order to keeps digital pins free for further expansions 
const int ledGREEN = A4;
const int ledBLUE = A5;


// ----------------------

int last_speed_DX = 0;
int last_speed_SX = 0;
 
void runDX(int speed);
void runSX(int speed);
void moveBot(int direction, int speed);

NewPing sonar(trigPin, echoPin, MAX_DISTANCE); // NewPing setup of pins and maximum distance.

int input;
int count;

float rotLeftTime  = 0.58;
float rotRightTime = 0.58;
float rotStepTime  = 0.058;


void remoteCmdExecutor();



/*
 * -----------------------------------
 * Obstacles
 * -----------------------------------
 */

float calcDistance()
{
  int iterations = 5;
  float duration = sonar.ping_median(iterations);
  return (duration / 2) * 0.0343;
}

void lookAtSonar()
{
    float sonar = calcDistance();
    //emit sonar data but with a reduced frequency
    if( count++ > 10 ){ Serial.println(sonar);  count = 0; }
    if((sonar) <= (MIN_DISTANCE)){ //very near
        if(((input)==(119))){
            moveBot(1,0);  //Stop
            setLed('R');
            Serial.println("OBSTACLE FROM ARDUINO");
            /*
            _delay(0.3);
            moveBot(2,100);
            _delay(1);
            moveBot(2,0);
            */
       }
    }
}




/*
 * -----------------------------------
 * delay
 * -----------------------------------
 */
void _loop(){
}
 
void _delay(float seconds){
    long endTime = millis() + seconds * 1000;
    while(millis() < endTime)_loop();
}

/*
 * -----------------------------------
 * Interpreter
 * -----------------------------------
 */

 /*
  * WARNING: the modification is not permanent
  * This is useful just for tuning
  */
void configureRotationTime(){
  char dir  = Serial.read();
  float v   = Serial.parseFloat();  
  Serial.println( dir == 'l' );
  if( dir == 'l' ) rotLeftTime  = v;
  if( dir == 'r' ) rotRightTime = v;
  if( dir == 'z' ) rotStepTime  = v;
  if( dir == 'x' ) rotStepTime  = v;
  Serial.println( rotLeftTime );
}
void remoteCmdExecutor(){
    if((Serial.available()) > (0  )){
        input = Serial.read();
        //Serial.println(input);
        switch( input ){
          case 99  : configureRotationTime(); break;  //c... | cl0.59 or cr0.59  or cx0.005 or cz0.005
          case 119 : moveBot(1,150); break;  //w
          case 115 : moveBot(2,150); break;  //s
          case 97  : moveBot(3,150); break;  //a
          case 122 : rotateLeftStep();  break;  //z
          case 120 : rotateRightStep();  break; //x
          case 100 : moveBot(4,150); break;  //d
          case 104 : moveBot(1,0);  break;  //h
          case 114 : rotateRight90();  break;  //r
          case 108 : rotateLeft90(); break;    //l
          case 102 : moveBot(1,0); break;  //f
          default  : moveBot(1,0);
        }
    }
}

void rotateLeft90( ){
  moveBot(3,150);
  _delay( rotLeftTime );
  moveBot(1,0);
}
void rotateRight90(){ 
  //Serial.println("rotateRight90");
  moveBot(4,150);
  _delay( rotRightTime );
  moveBot(1,0);
}

void rotateLeftStep(){
  moveBot(3,150);
  _delay( rotStepTime );
  moveBot(1,0);
}
void rotateRightStep(){ 
  //Serial.println("rotateRight90");
  moveBot(4,150);
  _delay( rotStepTime );
  moveBot(1,0);
}


/*
 * -----------------------------------
 * alternative run DX and SX
 * -----------------------------------
 */
void runDX(int motorSpeed)
{
  motorSpeed = motorSpeed > 255 ? 255 : motorSpeed;
  motorSpeed = motorSpeed < -255 ? -255 : motorSpeed;

  if(last_speed_DX != motorSpeed)
  {
    last_speed_DX = motorSpeed;
  }
  else
  {
    return;
  }

  if(motorSpeed >= 0)
  {
    //Set DX motor forward (clockwise)
    digitalWrite(motorPin1A, HIGH);
    digitalWrite(motorPin1B, LOW);

    //Set speed
    analogWrite(motorPin1EN, motorSpeed);
   
  }
  else
  {
    //Set DX motor backward (counter-clockwise)
    digitalWrite(motorPin1A, LOW);
    digitalWrite(motorPin1B, HIGH);
    
    //Set speed
    analogWrite(motorPin1EN, -motorSpeed);
  }
}


void runSX(int motorSpeed)
{
  motorSpeed = motorSpeed > 255 ? 255 : motorSpeed;
  motorSpeed = motorSpeed < -255 ? -255 : motorSpeed;

  if(last_speed_SX != motorSpeed)
  {
    last_speed_SX = motorSpeed;
  }
  else
  {
    return;
  }

  if(motorSpeed >= 0)
  {
    //Set SX motor forward (counter-clockwise)
    digitalWrite(motorPin2A, LOW);
    digitalWrite(motorPin2B, HIGH);

    //Set speed
    analogWrite(motorPin2EN, motorSpeed);
   
  }
  else
  {
    //Set DX motor backward (clockwise)
    digitalWrite(motorPin2A, HIGH);
    digitalWrite(motorPin2B, LOW);
    
    //Set speed
    analogWrite(motorPin2EN, -motorSpeed);
  }
}



/*
 * -----------------------------------
 * Moving
 * -----------------------------------
 */
void moveBot(int direction, int speed)
{
      int leftSpeed  = 0;
      int rightSpeed = 0;
      
      if(direction == 1){ //forward
          leftSpeed = speed;
          rightSpeed = speed;
      }else if(direction == 2){ //backward
          leftSpeed = -speed;
          rightSpeed = -speed;
      }else if(direction == 3){ //left
          leftSpeed = -speed;
          rightSpeed = speed;
      }else if(direction == 4){ //right
          leftSpeed = speed;
          rightSpeed = -speed;
      }

      runDX(rightSpeed);
      runSX(leftSpeed);
}


/*
 * -----------------------------------
 * LED Stuff
 * -----------------------------------
 * 
 * We're using a simple RGB common cathode LED (or three differend LEDs) 
 * but with Adafruit_NeoPixel lib is it possible to use WS2812, WS2811 
 * and SK6812 LED, like on the mbot.
 * 
 * Very very simple: 4 possible state= Off, Red on, Green on, Blue on
 * 
 */
 void setLed(char cmd)
 {
  //O-R-G-B
  switch(cmd)
  {
    case 'O': digitalWrite(ledRED, LOW); digitalWrite(ledGREEN, LOW); digitalWrite(ledBLUE, LOW); break;
    case 'R': digitalWrite(ledRED, HIGH); digitalWrite(ledGREEN, LOW); digitalWrite(ledBLUE, LOW); break;
    case 'G': digitalWrite(ledRED, LOW); digitalWrite(ledGREEN, HIGH); digitalWrite(ledBLUE, LOW); break;
    case 'B': digitalWrite(ledRED, LOW); digitalWrite(ledGREEN, LOW); digitalWrite(ledBLUE, HIGH); break;
    default: digitalWrite(ledRED, LOW); digitalWrite(ledGREEN, LOW); digitalWrite(ledBLUE, LOW);
  }
 }



/*
 * -----------------------------------
 * setup
 * -----------------------------------
 */
void setup(){
    Serial.begin(115200);
    Serial.println("uniboControl start");
}

void loop(){
    setLed('G');
    remoteCmdExecutor();
    lookAtSonar();
    _loop();
}
