const config = {
    floor: {
        size: { x: 34, y: 30                   }
    },
    player: {
        //position: { x: 0.5, y: 0.5 },		//CENTER
        position: { x: 0.10, y: 0.15 },		//INIT
        //position: { x: 0.8, y: 0.85 },		//END
        speed: 0.2
    },
     sonars: [
        {
            name: "sonar1",
            position: { x: 1.0, y: 0.24 },
            senseAxis: { x: true, y: false }
        },
        {
            name: "sonar2",
            position: { x: 1.0, y: 0.93},
            senseAxis: { x: true, y: false }
        } 
     ],
   movingObstacles: [
    ],
   staticObstacles: [
 /*  
        {
            name: "plasticBox",
            centerPosition: { x: 0.15, y: 1.0},
            size: { x: 0.24, y: 0.07}
        },

		
        {
            name: "table",
            centerPosition: { x: 0.60, y: 0.40},
            size: { x: 0.16, y: 0.14      }
		},
*/
        {
            name: "table12",
            centerPosition: { x: 0.64, y: 0.41 },
            size: { x: 0.05, y: 0.06      }
		},
		
        {
            name: "table1",
            centerPosition: { x: 0.33, y: 0.41},
            size: { x: 0.05, y: 0.06      }
		},

        {
            name: "wallUp",
            centerPosition: { x: 0.48, y: 0.97},
            size: { x: 0.89, y: 0.01}
        },
        {
            name: "gatein",
            centerPosition: { x: 0.066, y: 0.11},
            size: { x: 0.089, y: 0.01}
        },
        {
            name: "wallDown",
            centerPosition: { x: 0.54, y: 0.11},
            size: { x: 0.65, y: 0.01}
        },
        {
            name: "wallLeft",
            centerPosition: { x: 0.02, y: 0.45},
            size: { x: 0.01, y: 0.88}
        },
        {
            name: "wallRight",
            centerPosition: { x: 0.99, y: 0.5},
            size: { x: 0.01, y: 0.99}
        } 
    ]
}
 
export default config;
