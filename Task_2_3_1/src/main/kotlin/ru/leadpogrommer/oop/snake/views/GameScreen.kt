package ru.leadpogrommer.oop.snake.views

import javafx.geometry.VPos
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Border
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.TextAlignment
import ru.leadpogrommer.oop.snake.controllers.GameController
import ru.leadpogrommer.oop.snake.models.*
import tornadofx.*

const val TILE_WIDTH = 20.0
const val TILE_HEIGHT = 20.0

class GameScreen : View() {
    private val gameController: GameController by inject()


    override val root = hbox {


        addEventHandler(KeyEvent.KEY_PRESSED, gameController)
            canvas{
            this.widthProperty().bind(gameController.fieldWidth.multiply(TILE_WIDTH))
            this.heightProperty().bind(gameController.fieldHeight.multiply(TILE_HEIGHT))
                gameController.field.addListener{ _, _, field ->

                    for(y in 0 until field.size){
                        for(x in 0 until field[y].size){
                            this.graphicsContext2D.fill = when(field[y][x]){
                                is EmptyCell -> Color.LIGHTGREEN
                                is FoodCell -> Color.RED
                                is SnakeCell-> Color.YELLOW
                                is WallCell -> Color.BLACK
                            }
                            this.graphicsContext2D.fillRect(TILE_WIDTH*x, TILE_HEIGHT*y, TILE_WIDTH, TILE_HEIGHT)
                            val cell = field[y][x]
                            if(cell is FoodCell){
                                this.graphicsContext2D.fill = Color.CYAN
                                val text = "${cell.value}"
                                this.graphicsContext2D.textAlign = TextAlignment.CENTER
                                this.graphicsContext2D.textBaseline = VPos.CENTER
                                this.graphicsContext2D.fillText("${cell.value}", TILE_WIDTH*(x+0.5), TILE_HEIGHT*(y+0.5))
                            }
                        }
                    }
                }
                style {
                    borderColor += box(Color.BLACK)
                }
        }

        // spacer
        region {
            hgrow = Priority.ALWAYS
        }

        vbox {
            label(gameController.gameOver.stringBinding{
                "gameOver: $it"
            })
            label(gameController.score.stringBinding{
                "Score: $it"
            })
            button("(Re)start") {
                action {
                    gameController.reset()
                    gameController.start()
                }
            }
        }
    }
}