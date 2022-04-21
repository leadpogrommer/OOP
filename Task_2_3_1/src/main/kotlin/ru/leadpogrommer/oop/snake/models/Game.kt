package ru.leadpogrommer.oop.snake.models

import kotlin.random.Random


data class Point(val x: Int, val y: Int){
    operator fun plus(other: Point): Point{
        return Point(x + other.x, y + other.y)
    }
}
data class Snake(val points: MutableList<Point>, var direction: SnakeDirection)


class SnakeGame(val width: Int, val height: Int){

    val field = Array(height){
        Array(width){
            CellContents.EMPTY
        }
    }

    var gameOver = false
        private set
    private val snake = Snake(mutableListOf(Point(10, 2), Point(11, 2)), SnakeDirection.LEFT)
    private var foodLocation = Point(2, 2)
    var score = 0
        private set

    private fun bakeField(){
        for(y in 0 until height){
            for(x in 0 until width){
                if(field[y][x] == CellContents.SNAKE || field[y][x] == CellContents.FOOD){
                    field[y][x] = CellContents.EMPTY
                }
            }
        }
        field[foodLocation.y][foodLocation.x] = CellContents.FOOD
        for(point in snake.points){
            field[point.y][point.x] = CellContents.SNAKE
        }
    }

    fun tick(){
        if(gameOver)return
        val nextLocation = snake.points[0] + snake.direction.delta
        if(nextLocation.x < 0 || nextLocation.x >= width || nextLocation.y < 0 || nextLocation.y >= height ){
            gameOver = true;
            return
        }
        val nextCell = field[nextLocation.y][nextLocation.x]
        if (nextCell == CellContents.WALL || nextCell == CellContents.SNAKE){
            gameOver = true
        }else{
            snake.points.add(0, nextLocation)
            if(nextCell == CellContents.FOOD){
                placeFood()
                score++
            }else{
                snake.points.removeAt(snake.points.size - 1)
            }
            bakeField()
        }

    }

    private fun placeFood(){
        var result: Point
        do {
            result = Point(Random.nextInt(width), Random.nextInt(height))
        } while (field[result.y][result.x] != CellContents.EMPTY )
        foodLocation  = result
    }

    var snakeDirection
        get() = snake.direction
        set(value) {
            snake.direction = value
        }

}

enum class CellContents{
    EMPTY,
    FOOD,
    SNAKE,
    WALL
}

enum class SnakeDirection(val delta: Point){
    UP(Point(0, -1)),
    DOWN(Point(0, 1)),
    LEFT(Point(-1, 0)),
    RIGHT(Point(1, 0)),
}