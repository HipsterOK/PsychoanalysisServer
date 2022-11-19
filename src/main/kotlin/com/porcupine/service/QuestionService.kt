package com.porcupine.service

import com.porcupine.model.Question
import com.porcupine.model.QuestionRequest
import com.porcupine.model.Questions
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toSet

class QuestionService {

    private val database = Database.connect(
        url = "jdbc:postgresql://localhost:5438/postgres",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "postgres"
    )

    fun createQuestion(questionRequest: QuestionRequest): Boolean {
        val newQuestion = Question {
            text = questionRequest.text
        }

        val affectedRecordsNumber =
            database.sequenceOf(Questions)
                .add(newQuestion)

        return affectedRecordsNumber == 1
    }

    fun findAllQuestions(): Set<Question> =
        database.sequenceOf(Questions).toSet()

    fun findQuestionById(QuestionId: Long): Question? =
        database.sequenceOf(Questions)
            .find { Question -> Question.id eq QuestionId }

    fun updateQuestionById(questionId: Long, questionRequest: QuestionRequest): Boolean {
        val foundQuestion = findQuestionById(questionId)
        foundQuestion?.text = questionRequest.text

        val affectedRecordsNumber = foundQuestion?.flushChanges()

        return affectedRecordsNumber == 1
    }

    fun deleteQuestionById(questionId: Long): Boolean {
        val foundQuestion = findQuestionById(questionId)

        val affectedRecordsNumber = foundQuestion?.delete()

        return affectedRecordsNumber == 1
    }
}