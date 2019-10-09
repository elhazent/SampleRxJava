package com.elhazent.education.rxjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elhazent.education.rxjava.model.Repo;
import com.elhazent.education.rxjava.model.ResponseGithub;
import com.elhazent.education.rxjava.network.ApiService;
import com.elhazent.education.rxjava.network.InitRetrofit;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Nu'man Nashif Annawwaf.
 * github : https://github.com/elhazent
 * linkedin : https://www.linkedin.com/in/elhazent/
 */
public class MainActivity extends AppCompatActivity {


    ProgressBar pbLoading;
    RecyclerView rvRepos;
    EditText etUsername;

    /*
        inisialisasi file pembantu yang sudah kita buat.
        BaseApiService : untuk persoalan request API
        ReposAdapter : untuk kebutuhan adapter RecyclerView
         */
    ApiService mApiService;
    ReposAdapter mRepoAdapter;


    List<Repo> repoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*
        inisialisasi adapter dan recyclerview
         */
        pbLoading = findViewById(R.id.pbLoading);
        rvRepos = findViewById(R.id.rvRepos);
        etUsername = findViewById(R.id.etUsername);

        mApiService = InitRetrofit.providerApiService();

        mRepoAdapter = new ReposAdapter(this, repoList, null);
        rvRepos.setLayoutManager(new LinearLayoutManager(this));
        rvRepos.setItemAnimator(new DefaultItemAnimator());
        rvRepos.setHasFixedSize(true);
        rvRepos.setAdapter(mRepoAdapter);

        etUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*
                EditorInfo.IME_ACTION_SEARCH ini berfungsi untuk men-set keyboard kamu
                agar enter di keyboard menjadi search.
                 */
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String username = etUsername.getText().toString();
                    requestRepos(username);
                    return true;
                }
                return false;
            }
        });


    }

    /*
    Fungsi untuk berkomunikasi dengan API Server menggunakan library Retrofit dan RxJava.
     */
    private void requestRepos(String username) {
        pbLoading.setVisibility(View.VISIBLE);

        mApiService.requestRepos(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ResponseGithub>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<ResponseGithub> responseRepos) {
                        /**
                        onNext disini ketika data sudah masuk dan biasanya kita memasukan data API
                        ke lokal ataupun sesuai kebutuhan kamu. Di contoh ini data dari API Server dimasukan
                        dalam List repoList.
                         **/
                        for (int i = 0; i < responseRepos.size(); i++) {
                            String name = responseRepos.get(i).getName();
                            String description = responseRepos.get(i).getDescription();

                            repoList.add(new Repo(name, description));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        pbLoading.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Berhasil mengambil data", Toast.LENGTH_SHORT).show();

                        mRepoAdapter = new ReposAdapter(MainActivity.this, repoList, null);
                        rvRepos.setAdapter(mRepoAdapter);
                        mRepoAdapter.notifyDataSetChanged();
                    }
                });
    }
}
