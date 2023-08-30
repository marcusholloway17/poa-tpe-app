package com.example.acceuil_recp;

import retrofit2.Call;
import retrofit2.http.GET;

public class SpinnerInterface {

    static String JSONURL = "https://api.vaccitest.lik.tg/api/";

    @GET("points")
    Call<String> getJSONString() {
        return getJSONString();
    }
}
