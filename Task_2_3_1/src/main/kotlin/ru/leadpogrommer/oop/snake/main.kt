package ru.leadpogrommer.oop.snake

//import ru.leadpogrommer.oop.snake.models.CellStateModel
import javafx.beans.property.SimpleStringProperty
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.HBox
import javafx.scene.media.Media
import javafx.scene.media.MediaView
import javafx.scene.media.MediaPlayer
import javafx.scene.paint.Color
import javafx.scene.text.Font
//import javafx.scene.me
import javafx.stage.Stage
import ru.leadpogrommer.oop.snake.controllers.GameController
import ru.leadpogrommer.oop.snake.models.CellContents
import ru.leadpogrommer.oop.snake.models.SnakeDirection
import tornadofx.*


class TitleScreen : View() {
    val game: GameController by inject()

//    init {
//        game.gameOver.addListener { _, old, new ->
//            if(!old && new){
//                find<GameOverScreen>().player.play()
//                replaceWith<GameOverScreen>()
//            }
//        }
//    }



    override val root = hbox {
        addEventHandler(KeyEvent.KEY_PRESSED){
             when(it.code){
                KeyCode.A -> SnakeDirection.LEFT
                KeyCode.D -> SnakeDirection.RIGHT
                KeyCode.W -> SnakeDirection.UP
                KeyCode.S -> SnakeDirection.DOWN
                else -> null
            }?.let { direction ->
                game.setDirection(direction)
            }
        }
        canvas(30*20.0, 20*20.0){
            game.field.addListener{ _, _, field ->
                for(y in 0 until field.size){
                    for(x in 0 until field[y].size){
                        this.graphicsContext2D.fill = when(field[y][x]){
                            CellContents.EMPTY -> Color.LIGHTGREEN
                            CellContents.FOOD -> Color.RED
                            CellContents.SNAKE -> Color.YELLOW
                            CellContents.WALL -> Color.BLACK
                        }
                        this.graphicsContext2D.fillRect(20.0*x, 20.0*y, 20.0, 20.0)
                    }
                }
            }
        }
        vbox {
            label(game.gameOver.stringBinding{
                "gameOver: $it"
            })
            label(game.score.stringBinding{
                "Score: $it"
            })
            button("(Re)start") {
                action {
                    game.reset()
                    game.start()
                }
            }
        }
    }
}

//class GameOverScreen(): View(){
//    val game: GameController by inject()
//
////    init {
////        game.gameOver.addListener { _, old, new ->
////            if(!old && new){
//////                println("FFFUUU")
//////                player.play()
////            }
////        }
////
////    }
//
//    // tornadofx sucks
//    val player = MediaPlayer(Media(javaClass.getResource("/video.mp4")!!.toExternalForm()))
//    val mediaView = MediaView(player)
//
//    init {
//        player.setOnError {
//            println(player.error)
//        }
//    }
//
//    override val root = stackpane{
//        mediaView.attachTo(this)
//        button("Go back"){
//            action {
//                player.stop()
//                replaceWith<TitleScreen>()
//
//            }
//
//        }
//    }
//}


class SnakeApp : App(TitleScreen::class) {
    val game: GameController by inject()

    override fun start(stage: Stage) {
        super.start(stage)
        stage.isResizable = false
    }
}

fun main() {
    launch<SnakeApp>()
}