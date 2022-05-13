package ru.leadpogrommer.oop.snake.models

@kotlinx.serialization.Serializable
data class Level(val walls: ArrayList<ArrayList<Int>>, val snake: ArrayList<Point>)
