package models

enum class Result {
    ERROR, SUCCESS
}

data class Response(val data: Any?, val msg: String = "", val result: Int = Result.ERROR.ordinal)