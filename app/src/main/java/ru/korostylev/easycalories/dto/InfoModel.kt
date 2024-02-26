package ru.korostylev.easycalories.dto

data class InfoModel(
    var loading: Boolean = false,
    var successResponse: Boolean = true,
    var responseCode: String = "",
    var isError: Boolean = false,
    var errorResponse: String = ""
)