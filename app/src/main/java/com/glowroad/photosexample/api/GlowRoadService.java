package com.glowroad.photosexample.api;

import com.glowroad.photosexample.entites.ContactDetails;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mahesh Kumar on 8/14/2018.
 */
public interface GlowRoadService {

    /**
     * Get All Product Details
     *
     * @param method  Url For the method typer search
     * @param shirts Type of BRand Should Be Returned
     * @return list of Product Details
     */
    @GET("services/rest/")
    Single<ContactDetails> getUsers(@Query("method") String method, @Query("api_key") String apiKey,
                                    @Query("format") String format, @Query("nojsoncallback") int perPage,
                                    @Query("text") String shirts,
                                    @Query("extras") String url);

    static GlowRoadService getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.flickr.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(GlowRoadService.class);
    }

}
