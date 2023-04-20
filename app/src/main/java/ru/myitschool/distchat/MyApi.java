package ru.myitschool.distchat;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyApi {
    @GET("/chat.php")
    Call<MyData> getData(@Query("x") int x, @Query("y") int y);
}
