package routes

import copyToSuspend
import database.queries.ImageQueries
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import models.Response
import models.Result
import models.UploadResponse
import toMB
import user
import java.io.File

fun Route.uploadRoutes() {

    authenticate {
        route("upload") {
            post("") {
                val multipart = call.receiveMultipart()
                val fileNames = arrayListOf<String>()
                var size: Long = 0
                var files = 0
                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            fileNames.add(part.value)
                        }
                        is PartData.FileItem -> {
                            files++
                            val ext = File(part.originalFileName!!).extension
                            val file = File(Constants.UPLOAD_FILE_PATH, "uploads-${System.currentTimeMillis()}.$ext")
                            part.streamProvider().use { input ->
                                file.outputStream().buffered().use { output -> size += input.copyToSuspend(output) }
                            }
                        }
                        else -> {
                            call.respond(
                                Response(
                                    null,
                                    "Upload Failed",
                                    Result.ERROR.ordinal,
                                )
                            )
                        }
                    }
                    part.dispose()
                }
//                ImageQueries.addImages(call.user.id, fileNames)
                call.respond(
                    Response(
                        UploadResponse(fileNames, size.toMB(), files),
                        "Upload Successfully",
                        Result.SUCCESS.ordinal,
                    )
                )
            }
        }
    }

}