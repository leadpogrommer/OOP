package ru.leadpogrommer.oop.snake.controllers

import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.beans.binding.Binding
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.util.Duration
import ru.leadpogrommer.oop.snake.models.CellContents
import ru.leadpogrommer.oop.snake.models.SnakeDirection
import ru.leadpogrommer.oop.snake.models.SnakeGame
import tornadofx.*

class GameController: Controller() {
    private var game = SnakeGame(30, 20)
    private val timeline = Timeline(60.0)
    val field = SimpleObjectProperty<Array<Array<CellContents>>>()
    val gameOver = SimpleBooleanProperty(false)
    val score = SimpleIntegerProperty(0)

    // TODO: replace this with bindings
    private fun setProperties(){
        field.set(game.field.copyOf())
        gameOver.set(game.gameOver)
        score.set(game.score)
    }

    fun setDirection(d: SnakeDirection){
        game.snakeDirection = d
    }
    init {

        timeline.cycleCount = Animation.INDEFINITE
        timeline.keyFrames.add(KeyFrame(Duration.seconds(0.5), {
            game.tick()
            setProperties()
        }))
    }

    fun pause() {
        timeline.pause()
    }
    fun start(){
        timeline.play()
    }

    fun reset(){
        timeline.stop()
        game = SnakeGame(30, 20)
        setProperties()
    }
}