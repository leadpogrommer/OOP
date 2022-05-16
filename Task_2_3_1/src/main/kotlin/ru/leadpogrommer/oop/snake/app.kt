package ru.leadpogrommer.oop.snake

import javafx.stage.Stage
import ru.leadpogrommer.oop.snake.views.GameScreen
import tornadofx.App

class SnakeApp : App(GameScreen::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        stage.isResizable = true
        stage.width = 800.0
        stage.height = 600.0
    }
}