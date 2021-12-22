package com.example.test.API;

import com.example.test.model.FriendItem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {

    @GET("search")
    Call<FriendItem> getItemDetails(@Query("key") String key,
                                    @Query("channelId") String channelId,
                                    @Query("part") String part,
                                    @Query("order") String order,
                                    @Query("maxResult") String maxResult,
                                    @Query("type") String type);
}



/*https://www.googleapis.com/youtube/v3
        /search?  ->end point

        paraneters :
        key=AIzaSyD0HhKqtuUPkcJjViED8bs53FfjXKSxYRU
        &channelId=UCsOW9TPy2TKkqCchUHL04Fg
        &part=snippet -> this required to get list of videos
        &order=date  -> order depends on newest date
        &maxResult=20
        &type=video  -> to get only video*/

