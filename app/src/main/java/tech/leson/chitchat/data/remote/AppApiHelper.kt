package tech.leson.chitchat.data.remote

import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single
import tech.leson.chitchat.data.model.api.request.UserRequest
import tech.leson.chitchat.data.model.api.response.ServerResponse
import javax.inject.Inject

class AppApiHelper @Inject constructor(private val userRequest: UserRequest) : ApiHelper {

    override fun register(registerData: JsonObject): Single<ServerResponse> =
        userRequest.register(registerData)

    override fun login(loginData: JsonObject): Single<ServerResponse> = userRequest.login(loginData)

    override fun getUser(token: String): Single<ServerResponse> = userRequest.getUser(token)

    override fun update(token: String, updateData: JsonObject): Single<ServerResponse> =
        userRequest.update(token, updateData)

    override fun changeUsername(
        token: String,
        changeUsernameData: JsonObject,
    ): Single<ServerResponse> = userRequest.changeUsername(token, changeUsernameData)

    override fun changePassword(
        token: String,
        changePasswordData: JsonObject,
    ): Single<ServerResponse> = userRequest.changePassword(token, changePasswordData)

    override fun logout(token: String): Single<ServerResponse> = userRequest.logout(token)

    override fun search(token: String, searchData: JsonObject): Single<ServerResponse> =
        userRequest.search(token, searchData)

    override fun getUserByUsername(token: String, username: String): Single<ServerResponse> =
        userRequest.getUserByUsername(token, username)

}
