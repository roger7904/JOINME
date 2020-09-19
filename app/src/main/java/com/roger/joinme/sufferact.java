//package com.roger.joinme;
//
//import android.os.Bundle;
//import android.widget.SearchView;
//
//import java.util.ArrayList;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//public class sufferact extends AppCompatActivity implements SearchView.OnQueryTextListener{
//
//    private SearchView search;
//    private RecyclerView recycle;
//    private ArrayList<data> datalist = new ArrayList<>();
//    private String[] titleList = null;
//    private DataAdapter dataAdapter;
//    private String[] urlList = null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        initView();
//        initSet();
//    }
//
//    private void initView(){
//        search = (SearchView) findViewById(R.id.search);
//        recycle = (RecyclerView) findViewById(R.id.recycle);
//    }
//
//    private void initSet() {
//        //RecyclerView設定
//        dataAdapter = new DataAdapter(this);
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recycle.setLayoutManager(llm);
//        search.setOnQueryTextListener(this);
//        search.setIconifiedByDefault(false); //是否要點選搜尋圖示後再打開輸入框
//        search.setFocusable(false);
//        search.requestFocusFromTouch();      //要點選後才會開啟鍵盤輸入
//        search.setSubmitButtonEnabled(false);//輸入框後是否要加上送出的按鈕
//        search.setQueryHint("請輸入android搜尋"); //輸入框沒有值時要顯示的提示文字
//
//        titleList = new String[]{}; //存活動名稱
//
//        urlList = new String[]{}; //存圖片
//
//        //兩筆假資料size()一樣才進入迴圈
//        if (titleList.length == urlList.length)
//            for (int i = 0; i < (urlList.length); i++) {
//                data data = new data();//製作單筆資料
//                data.setTitle(titleList[i]);
//                data.setImageUrl(urlList[i]);
//                datalist.add(data);//把單筆資料加入陣列
//            }
//
//        dataAdapter.setData(datalist);//把陣列塞入adapter
//        recycle.setAdapter(dataAdapter);//把adapter塞入recyclerview
//    }
//
//    private void initListener() {
//
//    }
//
//
//    @Override
//    public boolean onQueryTextChange(final String newText) {
//        //篩選 寫在adapter內
//        dataAdapter.getFilter().filter(newText);
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextSubmit(String s) {
//        return false;
//    }
//}
