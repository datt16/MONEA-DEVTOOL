package io.github.datt16.monea_devtool.models

class SensorData(
    val id: String = "",
    val name: String = "",
    val color: String = "",
    val description: String = "",
    val position: Position = Position(0, 0),
    val status: Status = Status(0, "")
) {
    override fun toString(): String {
        return "id:$id, name:$name, color:$color, pos:(${position.x}, ${position.y}), desc:$description, s-code:${status.code}"
    }
}
