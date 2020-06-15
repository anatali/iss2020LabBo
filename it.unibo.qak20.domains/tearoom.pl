%====================================================================================
% tearoom description   
%====================================================================================
context(ctxtearoom, "localhost",  "TCP", "8060").
 qactor( waiter, ctxtearoom, "it.unibo.waiter.Waiter").
