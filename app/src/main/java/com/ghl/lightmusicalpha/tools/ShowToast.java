package com.ghl.lightmusicalpha.tools;

import android.content.Context;
import android.widget.Toast;

public class ShowToast {
  private Context mContext;
  private Toast mToast;

  public void show(String message) {
    if (mToast == null) {
      mToast = Toast.makeText(mContext,message,Toast.LENGTH_SHORT);
    }else{
      mToast.setText(message);
    }
    mToast.show();
  }

  public ShowToast() {
  }

  public ShowToast(Context context) {
    this.mContext = context;
  }
}