package com.example.talkey_android.data.domain.repository.remote

import com.example.talkey_android.data.domain.model.users.RegisterRequestModel
import com.example.talkey_android.data.domain.model.users.RegisterResponseModel
import com.example.talkey_android.data.domain.repository.DataSource
import com.example.talkey_android.data.domain.repository.remote.mapper.users.RegisterRequestMappper
import com.example.talkey_android.data.domain.repository.remote.mapper.users.RegisterResponseMapper
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

object RemoteDataSource : DataSource {

    //Aquí está la variable que iría en la inyección de dependencias
    private val apiCallService = ApiCallService(RetrofitClient.getApiServices())

    override suspend fun postRegister(registerRequestModel: RegisterRequestModel)
            : BaseResponse<RegisterResponseModel> {
        // TODO: incluir la variable en el when. Lo haré cuando vea
        //  que todo funciona bien, por si tengo que modificar algo
        val apiResult =
            apiCallService.postRegister(RegisterRequestMappper().toRequest(registerRequestModel))
        return when (apiResult) {
            is BaseResponse.Success -> BaseResponse.Success(
                RegisterResponseMapper().fromResponse(
                    apiResult.data
                )
            )

            is BaseResponse.Error -> BaseResponse.Error(apiResult.error)
        }
    }
}