package com.elhazent.education.rxjava.network;

import com.elhazent.education.rxjava.model.ResponseGithub;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Nu'man Nashif Annawwaf.
 * github : https://github.com/elhazent
 * linkedin : https://www.linkedin.com/in/elhazent/
 */
public interface ApiService {

    /*
   Fungsi @Path disini adalah untuk mengisi value yang sudah kita set.
   Contoh : {username} disini nantinya akan diisi dengan kebutuhan yang disesuaikan.
   Observable disini ialah dari RxJava. Karena pada contoh disini kita akan menggabungkan
   Retrofit dengan RxJava.
    */

    @GET("users/{username}/repos")
    Observable<List<ResponseGithub>> requestRepos(@Path("username") String username);
}
