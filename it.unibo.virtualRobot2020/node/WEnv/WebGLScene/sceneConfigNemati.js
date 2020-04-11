const config = {
    floor: {
        size: {x: 20, y: 20}
    },
    player: {
        position: {x: 0.15, y: 0.15},		
        speed: 0.2
    },
    sonars: [],
    movingObstacles: [],
    staticObstacles: [
        {
            name: "littlemockobstacle1",
            centerPosition: {x: 0.4, y: 0.72},
            size: {x: 0.07, y: 0.03}
        },
        {
            name: "littlemockobstacle2",
            centerPosition: {x: 0.35, y: 0.45},
            size: {x: 0.07, y: 0.03}
        },
        {
            name: "littlemockobstacle3",
            centerPosition: {x: 0.4, y: 0.35},
            size: {x: 0.07, y: 0.03}
        },
        {
            name: "wallUp",
            centerPosition: {x: 0.5, y: 1},
            size: {x: 1, y: 0}
        },
        {
            name: "wallDown",
            centerPosition: {x: 0.5, y: 0},
            size: {x: 1, y: 0}
        },
        {
            name: "wallLeft",
            centerPosition: {x: 0, y: 0.5},
            size: {x: 0, y: 1}
        },
        {
            name: "wallRight",
            centerPosition: { x: 1, y: 0.5},
            size: {x: 0, y: 1}
        }
    ]
}

export default config;
