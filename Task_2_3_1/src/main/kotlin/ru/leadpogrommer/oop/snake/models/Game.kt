package ru.leadpogrommer.oop.snake.models

import kotlin.random.Random

@kotlinx.serialization.Serializable
data class Point(val x: Int, val y: Int){
    operator fun plus(other: Point): Point{
        return Point(x + other.x, y + other.y)
    }
}
data class Snake(val points: MutableList<Point>, var direction: SnakeDirection)


class SnakeGame(level: Level){
    val width = level.walls[0].size
    val height= level.walls.size

    val field = Array(height){y->
        Array<Cell>(width){x->
            if(level.walls[y][x] == 0)EmptyCell()
            else WallCell()
        }
    }

    var gameOver = false
        private set
    private val snake = Snake(level.snake.toMutableList(), SnakeDirection.LEFT)

    var score = 0
        private set

    val timeScale
        get() = 1.0 + 5*(1.0 - (1.0/(score/3.0+1.0)))

    private var foodAcc = 0

    init {
        for(point in snake.points){
            field[point.y][point.x] = SnakeCell()
        }
        placeFood()
    }

    fun tick(){
        if(gameOver)return
        val nextLocation = snake.points[0] + snake.direction.delta
        if(nextLocation.x < 0 || nextLocation.x >= width || nextLocation.y < 0 || nextLocation.y >= height ){
            gameOver = true;
            return
        }
        val nextCell = field[nextLocation.y][nextLocation.x]
        if (nextCell is WallCell || nextCell is SnakeCell){
            gameOver = true
        }else{
            snake.points.add(0, nextLocation)
            field[nextLocation.y][nextLocation.x] = SnakeCell()
            if(nextCell is FoodCell){
                placeFood()
                score += nextCell.value
                foodAcc += nextCell.value
            }
            if(foodAcc == 0){
                val removedPoint = snake.points.removeAt(snake.points.size - 1)
                field[removedPoint.y][removedPoint.x] = EmptyCell()
            }else{
                foodAcc--
            }
        }

    }

    private fun placeFood(){
        var result: Point
        do {
            result = Point(Random.nextInt(width), Random.nextInt(height))
        } while (!(field[result.y][result.x]  is EmptyCell))
        field[result.y][result.x] = FoodCell(Random.nextInt(4)+1)
    }

    var snakeDirection
        get() = snake.direction
        set(value) {
            // snake cannot turn backwards
            if(value.delta + snake.direction.delta == Point(0, 0))return
            snake.direction = value
        }

}



enum class SnakeDirection(val delta: Point){
    UP(Point(0, -1)),
    DOWN(Point(0, 1)),
    LEFT(Point(-1, 0)),
    RIGHT(Point(1, 0)),
}