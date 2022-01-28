package com.example.newsapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface {
// API key: 693b95cb550a4cb5ac004aa32cc76564
    private RecyclerView newsRV,categoryRV;
    private ProgressBar progressBar;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRVModal> categoryRVModalArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsRV=findViewById(R.id.idNewsRV);
        categoryRV=findViewById(R.id.idCategoriesRV);
        progressBar=findViewById(R.id.idPBLoading);
        articlesArrayList=new ArrayList<>();
        categoryRVModalArrayList=new ArrayList<>();
        newsRVAdapter=new NewsRVAdapter(articlesArrayList,this);
        categoryRVAdapter=new CategoryRVAdapter(categoryRVModalArrayList,this,this::onCategoryCLick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategories();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();
    }
    private void getCategories(){
        categoryRVModalArrayList.add(new CategoryRVModal("All","https://media.istockphoto.com/photos/abstract-digital-news-concept-picture-id1290904409?b=1&k=20&m=1290904409&s=170667a&w=0&h=6khncht98kwYG-l7bdeWfBNs_GGcG1pDqzLb6ZXhh7I="));
        categoryRVModalArrayList.add(new CategoryRVModal("Technology","https://images.unsplash.com/photo-1451187863213-d1bcbaae3fa3?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1780&q=80"));
        categoryRVModalArrayList.add(new CategoryRVModal("Science","https://media.istockphoto.com/photos/high-school-girl-looking-through-microscope-at-school-picture-id1278973856?b=1&k=20&m=1278973856&s=170667a&w=0&h=kgZqSU0MWljS9b0BUzD9zsW2-7vHS8RlFdI_J8xW-Bo="));
        categoryRVModalArrayList.add(new CategoryRVModal("Sports","https://media.istockphoto.com/photos/various-sport-equipments-on-grass-picture-id949190736?s=612x612"));
        categoryRVModalArrayList.add(new CategoryRVModal("General","https://media.istockphoto.com/photos/map-pin-icon-with-network-polygon-graphic-abstract-background-picture-id1251263484?b=1&k=20&m=1251263484&s=170667a&w=0&h=2FN-xZ3F9G_NDV3_Of60SGMo3cf3FSWsaz-JbrwGMr4="));
        categoryRVModalArrayList.add(new CategoryRVModal("Business","https://media.istockphoto.com/photos/hand-arranging-wood-block-stacking-as-step-stair-on-paper-pink-picture-id1169974807?k=20&m=1169974807&s=612x612&w=0&h=KccRfwcOD73JeL8XTxPFwIy-37REt6T2mG6IDD5oJuE="));
        categoryRVModalArrayList.add(new CategoryRVModal("Entertainment","https://media.istockphoto.com/photos/media-concept-smart-tv-picture-id503685712?b=1&k=20&m=503685712&s=170667a&w=0&h=01OPzR5XIyg3HQF5ziFyD566oMIE9InO8oB6GdbQe_M="));
        categoryRVModalArrayList.add(new CategoryRVModal("Health","https://media.istockphoto.com/photos/medical-technology-background-picture-id1255646957?b=1&k=20&m=1255646957&s=170667a&w=0&h=0DXiK-OvNRevgEABMJyuu4_ED1aFmHVsFiQc1b-NdLg="));
        categoryRVAdapter.notifyDataSetChanged();
    }

    private void getNews(String category){
        progressBar.setVisibility(View.VISIBLE);
        articlesArrayList.clear(); // clears and add specfic categories
        String categoryURL="https://newsapi.org/v2/top-headlines?country=in&category="+category+"&apiKey=693b95cb550a4cb5ac004aa32cc76564";
        String url="https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=693b95cb550a4cb5ac004aa32cc76564";
        String BASE_URL="https://newsapi.org/";

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI= retrofit.create(RetrofitAPI.class);
        Call<NewsModal> call;
        if(category.equals("All")){
            call= retrofitAPI.getAllNews(url);
        }
        else{
            call=retrofitAPI.getNewsByCategory(categoryURL);
        }
    call.enqueue(new Callback<NewsModal>() {
        @Override
        public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {
            NewsModal newsModal=response.body();
            progressBar.setVisibility(View.GONE);
            ArrayList<Articles> articles=newsModal.getArticles();
            for(int i=0;i<articles.size();i++){
                articlesArrayList.add(new Articles(articles.get(i).getTitle(),articles.get(i).getDescription(),articles.get(i).getUrlToImage(),articles.get(i).getContent(),articles.get(i).getUrl()));
            }
            newsRVAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(Call<NewsModal> call, Throwable t) {
            Toast.makeText(MainActivity.this,"Failure to get API",Toast.LENGTH_SHORT).show();
        }
    });
    }
    public void onCategoryCLick(int position) {
   String category=categoryRVModalArrayList.get(position).getCategory();
   getNews(category);
    }
}

