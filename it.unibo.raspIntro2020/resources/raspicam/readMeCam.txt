sudo raspi-config
interface options
P1 camera

raspistill -o image.jpg

raspivid -o video.h264 -t 5000		//in msecs

--------------------------------------------------------------------------
Network
--------------------------------------------------------------------------
sudo route add default gw 192.168.1.1 wlan0

--------------------------------------------------------------------------
mjpg-streamer server (from osoyoo)
--------------------------------------------------------------------------
In mjpgstreamerserver:
wget  http://osoyoo.com/driver/mjpg-streamer.tar.gz

tar  -xzvf   mjpg-streamer.tar.gz

sudo apt-get install libv4l-dev libjpeg8-dev -y
sudo apt-get install subversion -y

//sed is a nifty utility that allows you to search and replace text in a file using a short 1-liner
sed -i 's/V4L2_PIX_FMT_MJPEG/V4L2_PIX_FMT_YUYV/g' /home/pi/nat/mjpgstreamerserver/mjpg-streamer/plugins/input_uvc/input_uvc.c
cd  mjpg-streamer && make all

===============================================================================
FROM GITHUB
https://github.com/cncjs/cncjs/wiki/Setup-Guide:-Raspberry-Pi-%7C-MJPEG-Streamer-Install-&-Setup-&-FFMpeg-Recording
===============================================================================

# Update & Install Tools
sudo apt-get update -y
sudo apt-get upgrade -y
sudo apt-get install build-essential imagemagick libv4l-dev libjpeg-dev cmake -y

sudo apt-get install cmake libjpeg8-dev
# Clone Repo in /tmp
cd /tmp
git clone https://github.com/jacksonliam/mjpg-streamer.git
cd mjpg-streamer/mjpg-streamer-experimental

# Make
make
sudo make install
RUN

export LD_LIBRARY_PATH=.

./mjpg_streamer -o "output_http.so -w ./www" -i "input_raspicam.so"
 http://192.168.1.10:8080/stream.html 

DOPO make install:
/usr/local/bin/mjpg_streamer -i "input_uvc.so -r 1280x720 -d /dev/video0 -f 30" -o "output_http.so -p 8080 -w /usr/local/share/mjpg-streamer/www"


--------------------------------------------------------------------------
WEBIOPI
the old installation method does not work for the Raspberry Pi 3. 
See https://github.com/doublebind/raspi
--------------------------------------------------------------------------
In nat/webiopi:
Download WebIOPi-0.7.1.tar.gz
tar xvzf WebIOPi-0.7.1.tar.gz
cd  WebIOPi-0.7.1 
wget https://raw.githubusercontent.com/doublebind/raspi/master/webiopi-pi2bplus.patch
patch -p1 -i webiopi-pi2bplus.patch
sudo ./setup.sh
Do you want to access WebIOPi over the Internet? [y/n]” ---> n
reboot
python 2.7.13
.....................................
DEBUG MODE
sudo webiopi -d -c /etc/webiopi/config

sudo /etc/init.d/webiopi start
sudo /etc/init.d/webiopi stop

sudo update-rc.d webiopi defaults   AUTOMATIC Startup
sudo update-rc.d webiopi remove  	AUTOMATIC Startup remove

http://192.168.1.10:8000/
username: webiopi
pswwd   : raspberry

mkdir myproject
cd myproject
mkdir html
mkdir python
You then need to create script.py and place it in myproject/python
and create index.html and place in myproject/html

You then need to edit /etc/webiopi/config as sudo  with gedit or your favourite editor, command is:

sudo gedit /etc/webiopi/config
Once open look for these lines starting myscript and doc-root. Modify as follows:
myscript = /home/pi/nat/webiopi/myproject/python/script.py
doc-root = /home/pi/nat/webiopi/myproject/html
Save the file and exit

Now start webiopi in the terminal:
sudo webiopi -c /etc/webiopi/config

From another computer or tablet or phone on your home network
enter the pi address and port 8000 and you should be able to see the LED
page as per tutorial

