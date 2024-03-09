package com.example.talkey_android.data.domain.repository.remote

import com.example.talkey_android.data.domain.model.users.RegisterFromResponseModel
import com.example.talkey_android.data.domain.model.users.RegisterToRequestModel
import com.example.talkey_android.data.domain.repository.DataSource
import com.example.talkey_android.data.domain.repository.remote.mapper.users.RegisterFromResponseMapper
import com.example.talkey_android.data.domain.repository.remote.mapper.users.RegisterToRequestMappper
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

object RemoteDataSource : DataSource {

    //Aquí está la variable que iría en la inyección de dependencias
    private val apiCallService = ApiCallService(RetrofitClient.getApiServices())

    override suspend fun postRegister(registerToRequestModel: RegisterToRequestModel)
            : BaseResponse<RegisterFromResponseModel> {
        // TODO: incluir la variable en el when. Lo haré cuando vea
        //  que todo funciona bien, por si tengo que modificar algo
        val apiResult =
            apiCallService.postRegister(RegisterToRequestMappper().toRequest(registerToRequestModel))
        return when (apiResult) {
            is BaseResponse.Success -> BaseResponse.Success(
                RegisterFromResponseMapper().fromResponse(
                    apiResult.data
                )
            )

            is BaseResponse.Error -> BaseResponse.Error(apiResult.error)
        }
    }
}