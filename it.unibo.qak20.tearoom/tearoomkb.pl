%===========================================
% tearoomkb.pl
%===========================================

%% ------------------------------------------ 
%% Positions
%% ------------------------------------------ 
pos( barman,       5, 0 ).
pos( teatable1,    2, 2 ).
pos( teatable2,    4, 2 ).
pos( entrancedoor, 1, 4 ).
pos( exitdoor,     6, 4 ).


%% ------------------------------------------ 
%% Teatables
%% ------------------------------------------ 
%% busy
%% free		(not busy and not clean)
%% dirty	(not clean)
%% clean	(not dirty)
%% available (free and clean)	

teatable( 1, clean ).
teatable( 2, clean ).


numfreetables(N) :-
	findall( N,teatable( N,clean ), NList),
	%% stdout <- println( tearoomkb_numfreetables(NList) ),
	length(NList,N).

stateOfTeatables( [teatable1(V1),teatable2(V2)] ) :-
	teatable( 1, V1 ),
	teatable( 2, V2 ).

	
engageTable(N)	 :-
	%% stdout <- println( tearoomkb_engageTable(N) ),
	retract( teatable( N, clean ) ),
	!,
	assert( teatable( N, engaged ) ).
engageTable(N).	
	
	
cleanTable(N)	 :-
	%% stdout <- println( tearoomkb_cleanTable(N) ),
	retract( teatable( N, engaged ) ),
	!,
	assert( teatable( N, clean ) ).
cleanTable(N).	

%% ------------------------------------------ 
%% Waiter
%% ------------------------------------------ 

%%  athome
%%	serving( CLIENTID )
%%	movingto( CELL )
%%	cleaning( table(N) )

waiter( athome ).	


%% ------------------------------------------ 
%% ServiceDesk
%% ------------------------------------------ 
%% idle
%% preparing( CLIENTID )
%% ready( CLIENTID )

servicedesk( idle ).

%% ------------------------------------------ 
%% Room as a whole
%% ------------------------------------------ 
roomstate(  waiter(S), stateOfTeatables(V), servicedesk(D) ):-
	 waiter(S), stateOfTeatables(V), servicedesk(D).
	 
	