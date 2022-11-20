package web

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import service.Test1Service

fun Route.widget(test1Service: Test1Service) {

    route("/test1") {

        get {
            call.respond(test1Service.getAllTest1())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalStateException("Must provide id")
            val test1 = test1Service.getTest1(id)
            if (test1 == null) call.respond(HttpStatusCode.NotFound)
            else call.respond(test1)
        }

    }

    webSocket("/updates") {
        try {
            test1Service.addChangeListener(this.hashCode()) {
                sendSerialized(it)
            }
            for (frame in incoming) {
                if (frame.frameType == FrameType.CLOSE) {
                    break
                } else if (frame is Frame.Text) {
                    call.application.environment.log.info("Received websocket message: {}", frame.readText())
                }
            }
        } finally {
            test1Service.removeChangeListener(this.hashCode())
        }
    }
}