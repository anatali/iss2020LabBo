##############################
# coapBasicrobotClient.py
##############################

from coapthon.client.helperclient import HelperClient

 
host = "127.0.0.1"
port = 8020
path ="ctxbasicrobot/basicrobot"

client = HelperClient(server=(host, port))
response = client.get(path)
#print( "response" + response  )
client.stop()       