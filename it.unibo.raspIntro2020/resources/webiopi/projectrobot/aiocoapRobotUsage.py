import logging
import asyncio

from aiocoap import *

logging.basicConfig(level=logging.INFO)
 
async def init(): 
    context = await Context.create_client_context()

async def work(): 
    request = Message(code=POST, payload="", uri="coap://192.168.1.8/macros/do_r")
    response = await context.request(request).response
    print('Result: %s\n%r'%(response.code, response.payload))

def console() :  
    print("console  STARTS :"   )
    init()
    cmd =  str( input() )
    print("Console cmd= %s " % (cmd))
    #print("console  cmd= :" , cmd  )   # w,s,a,d,r,l,z,x
    while( cmd != "q"  ) :
        print( cmd )
        work(  )
        #read() #blocking ...
        cmd = str(input() )

if __name__ == "__main__":
    asyncio.get_event_loop().run_until_complete(console())    