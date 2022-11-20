package model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object Test1s : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class Test1(
        val id: Int,
        val name: String,
)

@Serializable
data class NewTest1(
        val id: Int? = null,
        val name: String,
)