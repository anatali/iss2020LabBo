%===========================================
% tearoomkb.pl
%===========================================


pos( barman,       5, 0 ).
pos( teatable1,    2, 2 ).
pos( teatable2,    4, 2 ).
pos( entrancedoor, 1, 4 ).
pos( exitdoor,     6, 4 ).


%% ------------------------------------------ 
%% See teatables.qak
%% ------------------------------------------ 
teatable( 1, clean ).
teatable( 2, clean ).
teatable( "1", cleanasstr ).
teatable( "2", cleanasstr ).

stateOfTeatables( [teatable1(V1),teatable2(V2)] ) :-
	teatable( 1, V1 ),
	teatable( 2, V2 ).

	
engageTable(N)	 :-
	stdout <- println( tearoomkb_engageTable(N) ),
	retract( teatable( N, clean ) ),
	!,
	assert( teatable( N, engaged ) ).
engageTable(N).	
	
	
cleanTable(N)	 :-
	stdout <- println( tearoomkb_cleanTable(N) ),
	retract( teatable( N, engaged ) ),
	!,
	assert( teatable( N, clean ) ).
cleanTable(N).	