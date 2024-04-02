package com.example.talkey_android.data.domain.repository.remote.response

import com.example.talkey_android.BuildConfig
import com.example.talkey_android.data.domain.model.error.ErrorModel
import com.example.talkey_android.data.domain.repository.remote.response.error.ErrorResponse
import com.google.gson.Gson
import retrofit2.Response

abstract class BaseApiCallService {
    suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): BaseResponse<T> {
        val response: Response<T>
        return try {
            response = call.invoke()
            if (!response.isSuccessful) {
                val errorResponse = mapErrorResponse(response)
                BaseResponse.Error(errorResponse)
            } else {
                response.body()?.let { body ->
                    BaseResponse.Success(body)
                } ?: BaseResponse.Error(mapErrorResponse(response))
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            BaseResponse.Error(mapErrorResponse(throwable))
        }
    }

    private fun <T> mapErrorResponse(response: Response<T>): ErrorModel {
        val errorBody = response.errorBody()?.string()
        val errorData = try {
            val parsedData = Gson().fromJson(errorBody, ErrorResponse::class.java)
            if (response.code() == 401) {
                parsedData.errorCode = 401.toString()

                parsedData.error = response.message()
            }
            parsedData
        } catch (exception: java.lang.Exception) {
            exception.printStackTrace()
            null
        }
        return ErrorModel(
            errorData?.error ?: "",
            errorData?.errorCode ?: "0",
            errorData?.message ?: ""
        )
    }

    private fun mapErrorResponse(throwable: Throwable): ErrorModel {
        return if (BuildConfig.DEBUG) {
            ErrorModel(
                "UNKNOWN mapErrorResponse",
                "UNKNOWN mapErrorResponse",
                throwable.message ?: "UNKNOWN mapErrorResponse"
            )
        } else {
            ErrorModel(
                "Hay problemas de conexión",
                "0",
                "Vuelve a intentarlo más tarde"
            )
        }
    }
}