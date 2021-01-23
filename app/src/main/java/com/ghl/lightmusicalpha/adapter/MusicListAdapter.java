package com.ghl.lightmusicalpha.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ghl.lightmusicalpha.R;
import com.ghl.lightmusicalpha.pojo.MusicPOJO;

import java.io.PipedOutputStream;
import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.InnerHolder> {
  private static final String TAG = "info";

  private final Context mContext;
  private List<MusicPOJO> mData;

  public MusicListAdapter(Context context, List<MusicPOJO> musicListData) {
    this.mContext = context;
    this.mData = musicListData;
  }

  @NonNull
  @Override
  public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new InnerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_music_list_item, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
    holder.mMusicSingerName.setText(mData.get(position).getSingerName());
    holder.mMusicSongName.setText(mData.get(position).getSongName());
    // 加载图片
    if (!mData.get(position).getSongPic().equals("")) {
      Glide.with(mContext).load(mData.get(position).getSongPic()).into(holder.mMusicPic);
    } else {
      holder.mMusicPic.setImageResource(R.drawable.ncs);
    }
  }

  @Override
  public int getItemCount() {
    if (mData != null && mData.size() > 0) return mData.size();
    return 0;
  }

  public void scrollTopMoreData(List<MusicPOJO> tempData) {
    for (MusicPOJO tempDatum : tempData) {
      mData.add(tempDatum);
    }
    notifyDataSetChanged();
  }

  public void downPullMoreData(List<MusicPOJO> tempData) {
    for (MusicPOJO tempDatum : tempData) {
      mData.add(0, tempDatum);
    }
    notifyDataSetChanged();
  }

  public class InnerHolder extends RecyclerView.ViewHolder {
    private ImageView mMusicPic;
    private TextView mMusicSongName, mMusicSingerName;

    public InnerHolder(@NonNull View itemView) {
      super(itemView);
      mMusicPic = itemView.findViewById(R.id.music_list_item_pic);
      mMusicSongName = itemView.findViewById(R.id.music_list_item_song_name);
      mMusicSingerName = itemView.findViewById(R.id.music_list_item_singer_name);
    }
  }
}
