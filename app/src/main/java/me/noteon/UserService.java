package me.noteon;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {


@POST("/{formID}")
Call<UserResponse> sendData(@Path("formID") String formID, @Body UserRequest userRequest);

}
