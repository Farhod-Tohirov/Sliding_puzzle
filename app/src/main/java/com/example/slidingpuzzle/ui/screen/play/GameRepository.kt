package com.example.slidingpuzzle.ui.screen.play

import com.example.slidingpuzzle.model.Coordination

class GameRepository : GameContract.Model {
    private var spaceCoordination =
        Coordination(3, 3)
    private var stepCount = 0
    private var timeCount = ""

    override var space: Coordination
        get() = spaceCoordination
        set(value) {
            spaceCoordination = value
        }

    override var step: Int
        get() = stepCount
        set(value) {
            stepCount = value
        }

    override var time: String
        get() = timeCount
        set(value) {
            timeCount = value
        }
}