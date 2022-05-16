package ru.leadpogrommer.oop.snake.models

sealed class Cell
class EmptyCell : Cell()
class WallCell : Cell()
class SnakeCell : Cell()
class FoodCell(val value: Int) : Cell()