from coapthon.client.helperclient import HelperClient

 
host = "127.0.0.1"
port = 8018
path ="ctxbasicrobot/basicrobot"

client = HelperClient(server=(host, port))
response = client.get(path)
#print( "response" + response  )
client.stop()       