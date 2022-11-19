package com.porcupine.route

import com.porcupine.model.ErrorResponse
import com.porcupine.model.Question
import com.porcupine.model.QuestionRequest
import com.porcupine.model.QuestionResponse
import com.porcupine.service.QuestionService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private fun Question?.toQuestionResponse(): QuestionResponse? =
    this?.let { QuestionResponse(it.id!!, it.text) }

fun Application.configureQuestionRoutes() {
    routing {
                        route("/question") {
            val questionService = QuestionService()
            createQuestion(questionService)
            getAllQuestionsRoute(questionService)
            getQuestionByIdRoute(questionService)
            updateQuestionByIdRoute(questionService)
            deleteQuestionByIdRoute(questionService)
        }
    }
}

fun Route.createQuestion(questionService: QuestionService) {
    post {
        val request = call.receive<QuestionRequest>()

        val success = questionService.createQuestion(questionRequest = request)

        if (success)
            call.respond(HttpStatusCode.Created)
        else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Cannot create question"))
    }
}

fun Route.getAllQuestionsRoute(questionService: QuestionService) {
    get {
        val questions = questionService.findAllQuestions()
            .map(Question::toQuestionResponse)

        call.respond(message = questions)
    }
}

fun Route.getQuestionByIdRoute(questionService: QuestionService) {
    get("/{id}") {
        val id: Long = call.parameters["id"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid id"))

        questionService.findQuestionById(id)
            ?.let { foundQuestion -> foundQuestion.toQuestionResponse() }
            ?.let { response -> call.respond(response) }
            ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("Question with id [$id] not found"))
    }
}

fun Route.updateQuestionByIdRoute(questionService: QuestionService) {
    patch("/{id}") {
        val id: Long = call.parameters["id"]?.toLongOrNull()
            ?: return@patch call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid id"))

        val request = call.receive<QuestionRequest>()
        val success = questionService.updateQuestionById(id, request)

        if (success)
            call.respond(HttpStatusCode.NoContent)
        else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Cannot update question with id [$id]"))
    }
}

fun Route.deleteQuestionByIdRoute(questionService: QuestionService) {
    delete("/{id}") {
        val id: Long = call.parameters["id"]?.toLongOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid id"))

        val success = questionService.deleteQuestionById(id)

        if (success)
            call.respond(HttpStatusCode.NoContent)
        else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Cannot delete question with id [$id]"))
    }
}