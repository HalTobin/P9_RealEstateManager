package com.openclassrooms.realestatemanager.data.repository

import android.util.Log
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.api.PositionStackApi
import com.openclassrooms.realestatemanager.model.Coordinates
import com.openclassrooms.realestatemanager.repository.CoordinatesRepository
import com.openclassrooms.realestatemanager.util.ErrorUtils
import com.openclassrooms.realestatemanager.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.*

class CoordinatesRepositoryImpl(private val retrofit: Retrofit) : CoordinatesRepository {

    private val positionStackApi = retrofit.create(PositionStackApi::class.java)

    override suspend fun getCoordinates(address: String): Flow<Coordinates?> {

        return flow {
            val result = getResponse(
                request = {
                    positionStackApi.getCoordinates(
                        BuildConfig.POSITIONSTACK_API_KEY,
                        address,
                        1
                    )
                },
                defaultErrorMessage = "Error fetching Coordinates"
            )

            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data?.let { coordinates ->
                        Log.i("COORDINATES", coordinates.toString())
                        emit(coordinates)
                    }
                }
                Result.Status.ERROR -> {
                    result.message?.let {
                        Log.i("COOORDINATES", "ERROR $it")
                    }
                }
                else -> {}
            }
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun <T> getResponse(
        request: suspend () -> Response<T>,
        defaultErrorMessage: String
    ): Result<T?> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) return Result.success(result.body())
            else {
                val errorResponse = retrofit.let { ErrorUtils.parseError(result, it) }
                Result.error(errorResponse?.message ?: defaultErrorMessage, errorResponse)
            }
        } catch (e: Throwable) {
            Result.error(e.stackTraceToString(), null)
        }
    }

}