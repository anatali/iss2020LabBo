##############################
# coapdemoCaller..py
# Tanganelli (DOES NOT WORK!!!)
##############################

from coapthon.client.helperclient import HelperClient

 
host = "localhost"
port = 8077
path ="ctxblsledalone/led"
fullpath = "coap://localhost:8077/ctxblsledalone/led"
client = HelperClient(server=(host, port))
print( "client" + str(client)  )
response = client.get(fullpath)
#print( "response" + response  )
#client.stop()       