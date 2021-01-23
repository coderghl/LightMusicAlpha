package com.ghl.lightmusicalpha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;

import com.ghl.lightmusicalpha.adapter.MusicListAdapter;
import com.ghl.lightmusicalpha.pojo.MusicPOJO;
import com.ghl.lightmusicalpha.tools.OkHttpHelper;
import com.ghl.lightmusicalpha.tools.ShowToast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
  private static final String TAG = "info";

  private RecyclerView rv_music_list;
  private RelativeLayout rl_music_controller;
  private SwipeRefreshLayout sw_music_list_down_pull;

  private MusicListAdapter mMusicListAdapter;

  private List<MusicPOJO> mMusicListData = new ArrayList<>();
  private int maxPage = 83;
  private int currentPage = 1;
  private ShowToast mToast;
  private MyHandler mHandler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initData(); // 请求数据
    initView(); // 初始化控件
    initListener(); // 初始化事件
  }

  private void initListener() {
    // 监听musicList下滑事件
    rv_music_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        // 当recycler停止滚动时判断目前是否滚动到倒数第五个，如果到了则继续请求更多
        if (manager != null) {
          if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
            int totalItemCount = manager.getItemCount();
            // 请求更多
            if (totalItemCount - lastVisibleItem < 5) {
              currentPage++;
              if (maxPage > currentPage) {
                scrollTopMoreData();
              }
            }
          }
        }
      }
    });

    // 下拉刷新
    sw_music_list_down_pull.setColorSchemeColors(Color.parseColor("#ff4081"));
    sw_music_list_down_pull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        currentPage++;
        if (currentPage < maxPage) {
          downPullMoreData();
        }
      }
    });
  }

  // 第一次进来时数据
  private void initData() {
    OkHttpHelper.requestGet(Constants.GET_DATA, new Callback() {
      @Override
      public void onFailure(@NotNull Call call, @NotNull IOException e) {
        Log.d(TAG, "onFailure: " + e);
      }

      @Override
      public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        if (response.code() == 200) {
          String data = response.body().string();
          try {

            JSONArray jsonArray = new JSONArray(data);

            for (int i = 0; i < jsonArray.length(); i++) {
              JSONObject jsonObject = jsonArray.getJSONObject(i);
              // 获取数据
              int id = jsonObject.getInt("id");
              String song_name = jsonObject.getString("song_name");
              String singer_name = jsonObject.getString("singer_name");
              String song_pic = jsonObject.getString("song_pic");
              String song_url = jsonObject.getString("song_url");

              mMusicListData.add(new MusicPOJO(id, song_name, singer_name, song_url, song_pic));
            }
            // 这时的mMusicListData数据已经有了，可以填充到musicList当中去了
            mHandler.sendEmptyMessage(Constants.GET_DATA_SUCCESS_OVER);
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      }
    });
  }

  // 初始化控件
  private void initView() {
    mToast = new ShowToast(this);
    rv_music_list = findViewById(R.id.rv_music_list);
    rl_music_controller = findViewById(R.id.rl_music_controller);
    sw_music_list_down_pull = findViewById(R.id.sw_music_list_down_pull);
    mHandler = new MyHandler();
  }

  // 上啦加载更多
  private void scrollTopMoreData() {
    List<MusicPOJO> tempData = new ArrayList<>();
    OkHttpHelper.requestGet("http://101.200.40.169:11008/GetMusicDatasByName/t/" + currentPage + "/20", new Callback() {
      @Override
      public void onFailure(@NotNull Call call, @NotNull IOException e) {
        Log.d(TAG, "onFailure: " + e);
      }

      @Override
      public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        if (response.code() == 200) {
          String data = response.body().string();
          try {

            JSONArray jsonArray = new JSONArray(data);

            for (int i = 0; i < jsonArray.length(); i++) {
              JSONObject jsonObject = jsonArray.getJSONObject(i);
              // 获取数据
              int id = jsonObject.getInt("id");
              String song_name = jsonObject.getString("song_name");
              String singer_name = jsonObject.getString("singer_name");
              String song_pic = jsonObject.getString("song_pic");
              String song_url = jsonObject.getString("song_url");
              tempData.add(new MusicPOJO(id, song_name, singer_name, song_url, song_pic));
            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
          // 通知上啦加载更多完成
          Message message = new Message();
          message.what = Constants.SCROLL_TOP_MORE_SUCCESS;
          message.obj = tempData;
          mHandler.sendMessage(message);
        }
      }
    });
  }

  // 下拉刷新
  private void downPullMoreData() {
    List<MusicPOJO> tempData = new ArrayList<>();
    OkHttpHelper.requestGet("http://101.200.40.169:11008/GetMusicDatasByName/t/" + currentPage + "/20", new Callback() {
      @Override
      public void onFailure(@NotNull Call call, @NotNull IOException e) {
        Log.d(TAG, "onFailure: " + e);
      }

      @Override
      public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        if (response.code() == 200) {
          String data = response.body().string();
          try {

            JSONArray jsonArray = new JSONArray(data);

            for (int i = 0; i < jsonArray.length(); i++) {
              JSONObject jsonObject = jsonArray.getJSONObject(i);
              // 获取数据
              int id = jsonObject.getInt("id");
              String song_name = jsonObject.getString("song_name");
              String singer_name = jsonObject.getString("singer_name");
              String song_pic = jsonObject.getString("song_pic");
              String song_url = jsonObject.getString("song_url");
              tempData.add(new MusicPOJO(id, song_name, singer_name, song_url, song_pic));
            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
          // 通知上啦加载更多完成
          Message message = new Message();
          message.what = Constants.DOWN_PULL_MORE_SUCCESS;
          message.obj = tempData;
          mHandler.sendMessage(message);
        }
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    initAnimation(); // 控件出场动画
  }

  // 初始化动画
  private void initAnimation() {
    MusicListAnimation();

    // 底部播放器出场动画
    Animation musicControllerAnimation = AnimationUtils.loadAnimation(this, R.anim.move_in);
    rl_music_controller.setAnimation(musicControllerAnimation);
    musicControllerAnimation.start();
  }

  // musicList出场动画
  private void MusicListAnimation() {
    Animation musicListAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
    LayoutAnimationController musicListAnimationController = new LayoutAnimationController(musicListAnimation);  // 控制动画出场顺序
    musicListAnimationController.setDelay(0.2f);  // 设置每个item之间的动画间隔时间
    rv_music_list.setLayoutAnimation(musicListAnimationController);
  }

  // 线程通信的Handler
  private class MyHandler extends Handler {
    @Override
    public void handleMessage(@NonNull Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case Constants.GET_DATA_SUCCESS_OVER:
          // 给musicList设置布局管理器以及适配器
          rv_music_list.setLayoutManager(new LinearLayoutManager(MainActivity.this));
          mMusicListAdapter = new MusicListAdapter(MainActivity.this, mMusicListData);
          rv_music_list.setAdapter(mMusicListAdapter);
          MusicListAnimation();
          break;
        case Constants.SCROLL_TOP_MORE_SUCCESS:
          if (mMusicListAdapter != null) {
            List<MusicPOJO> tempData = (List<MusicPOJO>) msg.obj;
            mMusicListAdapter.scrollTopMoreData(tempData);
          }
          break;
        case Constants.DOWN_PULL_MORE_SUCCESS:
          if (mMusicListAdapter != null) {
            List<MusicPOJO> tempData = (List<MusicPOJO>) msg.obj;
            mMusicListAdapter.downPullMoreData(tempData);
            sw_music_list_down_pull.setRefreshing(false);
            mToast.show("刷新完成");
          }
          break;
      }
    }
  }

  ;
}