%====================================================================================
% roomexplore description   
%====================================================================================
context(ctxroomexplore, "localhost",  "TCP", "8038").
context(ctxbasicrobot, "192.168.1.22",  "TCP", "8020").
 qactor( roomexplorer, ctxroomexplore, "it.unibo.roomexplorer.Roomexplorer").
