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
import javafx.event.EventHandler
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.util.Duration
import ru.leadpogrommer.oop.snake.models.Cell
import ru.leadpogrommer.oop.snake.models.SnakeDirection
import ru.leadpogrommer.oop.snake.models.SnakeGame
import tornadofx.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import ru.leadpogrommer.oop.snake.models.Level

class GameController: Controller(), EventHandler<KeyEvent> {
    private val timeline = Timeline(60.0)
    val field = SimpleObjectProperty<Array<Array<Cell>>>()
    val fieldWidth = SimpleIntegerProperty()
    val fieldHeight = SimpleIntegerProperty()
    val gameOver = SimpleBooleanProperty(false)
    val score = SimpleIntegerProperty(0)
    val levels: List<Level> = Json.decodeFromString<List<Level>>(javaClass.getResource("/levels.json").readText())


    private var game = SnakeGame(levels.random())

    // TODO: replace this with bindings
    private fun setProperties(){
        field.set(game.field.copyOf())
        gameOver.set(game.gameOver)
        score.set(game.score)
        fieldWidth.set(game.width)
        fieldHeight.set(game.height)
    }

    fun setDirection(d: SnakeDirection){
        game.snakeDirection = d
    }
    init {

        timeline.cycleCount = Animation.INDEFINITE
        timeline.keyFrames.add(KeyFrame(Duration.seconds(0.5), {
            game.tick()
            setProperties()
            timeline.rate = game.timeScale
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
        game = SnakeGame(levels.random())
        setProperties()
    }

    override fun handle(event: KeyEvent?) {
        event ?: return
        KeyEvent.KEY_PRESSED
        when(event.eventType){
            KeyEvent.KEY_PRESSED -> {
                when(event.code){
                    KeyCode.A -> SnakeDirection.LEFT
                    KeyCode.D -> SnakeDirection.RIGHT
                    KeyCode.W -> SnakeDirection.UP
                    KeyCode.S -> SnakeDirection.DOWN
                    else -> null
                }?.let { direction ->
                    setDirection(direction)
                }
            }
        }
    }
}