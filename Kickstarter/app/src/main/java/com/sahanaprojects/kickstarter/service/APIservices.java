package com.sahanaprojects.kickstarter.service;



import com.sahanaprojects.kickstarter.Model.KickstarterDetails;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIservices {


    //GET Method to retrieve data from the api
    @GET("/kickstarter")
    Call<List<KickstarterDetails>> getData();


}
