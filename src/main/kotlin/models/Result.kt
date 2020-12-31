package models

enum class Result {
    ERROR, SUCCESS
}

data class Response<T>(val data: T, val msg: String = "", val result: Int = Result.ERROR.ordinal)