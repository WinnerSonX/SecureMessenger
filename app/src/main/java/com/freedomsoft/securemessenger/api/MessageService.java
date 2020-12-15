package com.freedomsoft.securemessenger.api;

import com.freedomsoft.securemessenger.api.models.Message;
import com.freedomsoft.securemessenger.api.models.MessageRequest;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface MessageService {

    @PUT("/api/messages/send")
    Single<Response<ResponseBody>> send(@Body MessageRequest message);

    @GET("/api/messages/get")
    Single<Response<List<MessageRequest>>> get(@Query("from_id") String fromId, @Query("to_id") String toId, @Query("after_id")Long afterId);
}
