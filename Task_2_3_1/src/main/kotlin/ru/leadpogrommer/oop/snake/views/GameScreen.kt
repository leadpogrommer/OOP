package ru.leadpogrommer.oop.snake.views

import javafx.geometry.VPos
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Priority
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
        canvas {
            this.widthProperty().bind(gameController.fieldWidth.multiply(TILE_WIDTH))
            this.heightProperty().bind(gameController.fieldHeight.multiply(TILE_HEIGHT))
            gameController.field.addListener { _, _, field ->

                for (y in 0 until field.size) {
                    for (x in 0 until field[y].size) {
                        val cell = field[y][x]
                        this.graphicsContext2D.fill = when (cell) {
                            is EmptyCell -> Color.LIGHTGREEN
                            is FoodCell -> foodValueToColor(cell.value)
                            is SnakeCell -> Color.YELLOW
                            is WallCell -> Color.BLACK
                        }
                        this.graphicsContext2D.fillRect(TILE_WIDTH * x, TILE_HEIGHT * y, TILE_WIDTH, TILE_HEIGHT)
                        if (cell is FoodCell) {
                            this.graphicsContext2D.fill = Color.CYAN
                            this.graphicsContext2D.textAlign = TextAlignment.CENTER
                            this.graphicsContext2D.textBaseline = VPos.CENTER
                            this.graphicsContext2D.fillText(
                                "${cell.value}",
                                TILE_WIDTH * (x + 0.5),
                                TILE_HEIGHT * (y + 0.5)
                            )
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
            label(gameController.gameOver.stringBinding {
                "gameOver: $it"
            })
            label(gameController.score.stringBinding {
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

    private fun foodValueToColor(value: Int): Color {
        val minHue = 30.0
        val minValue = 1.0
        val maxValue = MAX_FOOD_VALUE
        val hue = (1 - ((value.toDouble() - minValue) / (maxValue - minValue))) * minHue
        return Color.hsb(hue, 1.0, 1.0)
    }
}