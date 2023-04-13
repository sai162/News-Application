package com.example.newsapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface {
//    a424b0fc93e04830af5352a3b9e8218e
    private RecyclerView newsRV,categoryRV;
    private ProgressBar loadingPB;
    private ArrayList<Articles> articlesArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;
    ArrayList<CategoryRVModel> categoryRVModalArrayList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsRV=findViewById(R.id.idRVNews);
        categoryRV=findViewById((R.id.idRVCategories));
        loadingPB=findViewById((R.id.idPBLoading));
        articlesArrayList=new ArrayList<>();
        categoryRVModalArrayList=new ArrayList<>();
        newsRVAdapter=new NewsRVAdapter(articlesArrayList,this);
        categoryRVAdapter=new CategoryRVAdapter(categoryRVModalArrayList,this,this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategories();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();;

    }
private void getCategories(){
     categoryRVModalArrayList.add(new CategoryRVModel("All","https://media.webdunia.com/_media/hi/img/category/article/hindi-news.jpeg"));
    categoryRVModalArrayList.add(new CategoryRVModel("Technology","https://images.freeimages.com/images/large-previews/01a/technology-background-1632715.jpg"));
    categoryRVModalArrayList.add(new CategoryRVModel("Science","https://static.vecteezy.com/system/resources/previews/000/160/988/original/science-and-chemical-vector-designs.jpg"));
    categoryRVModalArrayList.add(new CategoryRVModel("Sports","https://th.bing.com/th/id/R.52a56076c1bf71fc447ca7091c803dfb?rik=eb8gziVZwrFvsw&riu=http%3a%2f%2fwww.gannett-cdn.com%2f-mm-%2fe608829ebae8065be1f12121f3064425fd09714f%2fc%3d0-216-1733-1516%2flocal%2f-%2fmedia%2f2015%2f10%2f12%2fCherryHill%2fB9319237644Z.1_20151012233524_000_GHOC7FU8Q.1-0.jpg&ehk=IilSYUfAGcl4fCpzbOf6Z8uyFINgCMLXq6ewnWZ48kQ%3d&risl=&pid=ImgRaw&r=0"));
    categoryRVModalArrayList.add(new CategoryRVModel("General","https://pbs.twimg.com/profile_banners/2731316564/1411990867/1500x500"));
    categoryRVModalArrayList.add(new CategoryRVModel("Business","https://th.bing.com/th/id/OIP._AFOYTukkie2FBZBQR3ruwHaE8?pid=ImgDet&rs=1"));
    categoryRVModalArrayList.add(new CategoryRVModel("Entertainment","https://th.bing.com/th/id/OIP.yhzm7WQJ3Fvd6CP8ZO9rawHaE8?pid=ImgDet&rs=1"));
   categoryRVModalArrayList.add(new CategoryRVModel("Health","https://th.bing.com/th/id/R.2ed06c0f19a77db3cfdddbae75d47685?rik=%2bGxaD7qalCVgCQ&riu=http%3a%2f%2fevespolitics.com%2fwp-content%2fuploads%2f2014%2f06%2fhealthcare.jpg&ehk=Digvuo9txd1JZNjPd1VVMf%2fo6QDIrZELl7RFyj2BwfI%3d&risl=&pid=ImgRaw&r=0"));
categoryRVAdapter.notifyDataSetChanged();
    }
    private void getNews(String category){
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL="https://newsapi.org/v2/top-headlines?country=in&category="+category+"&apiKey=a424b0fc93e04830af5352a3b9e8218e";
        String url="https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow.com&sortBy=pulbishedAt&language=en&apiKey=a424b0fc93e04830af5352a3b9e8218e";
        String BASE_URL="https://newsapi.org/";
        Retrofit retrofit=new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitAPI retrofitAPI=retrofit.create(RetrofitAPI.class);
        Call<NewsModel> call;
if(category.equals("All")){
    call=retrofitAPI.getAllNews(url);
}
else{
    call= retrofitAPI.getNewsByCategory(categoryURL);
}
call.enqueue(new Callback<NewsModel>(){
    @Override
    public void onResponse(Call<NewsModel> call, Response<NewsModel> response){
      NewsModel newsModel=response.body();
      loadingPB.setVisibility(View.GONE);
      ArrayList<Articles> articles=newsModel.getArticles();
      for(int i=0;i<articles.size();i++){
          articlesArrayList.add(new Articles(articles.get(i).getTitle(),articles.get(i).getDescription(),articles.get(i).getUrlToImage(),articles.get(i).getUrl(),articles.get(i).getContent()));
      newsRVAdapter.notifyDataSetChanged();
      }
    }

    @Override
    public void onFailure(Call<NewsModel> call, Throwable t) {
Toast.makeText(MainActivity.this,"Fail to get News", Toast.LENGTH_SHORT).show();
    }
});
    }
    @Override
    public void onCategoryClick(int position) {
String category=categoryRVModalArrayList.get(position).getCategory();
getNews(category);
    }
}