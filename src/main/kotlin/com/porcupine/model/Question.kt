package com.porcupine.model

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

interface Question : Entity<Question> {
    companion object : Entity.Factory<Question>()

    val id: Long?
    var text: String
}

object Questions : Table<Question>("question") {
    val id = long("id").primaryKey().bindTo(Question::id)
    val text = varchar("text").bindTo(Question::text)
}