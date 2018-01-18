最近要做一个为视频设置封面的功能，这里展示一下简单的demo。

## demo效果
这里直接将选取的视频某一时间的bitmap显示在视频下方。上面是视频，下面是所获取那一帧的截图。

![](https://user-gold-cdn.xitu.io/2018/1/18/16108333349ac530?w=412&h=664&f=gif&s=2493220)

## 具体代码
这里的话主要是靠videoView来显示视频内容，seekBar来控制视频的进度,使用MediaMetadataRetriever来获取所选中进度的时间的视频画面。

### 布局代码

    <LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context="com.se.palegreen.ui.activity.UploadActivity">


        <VideoView
            android:id="@+id/vv_player"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>
        <SeekBar
            android:id="@+id/sb_select"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>
        <ImageView
            android:id="@+id/iv_head"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"/>
    </LinearLayout>
    
### 具体代码

    public class UploadActivity extends ActivitySupport {

	@BindView(R.id.vv_player)
	VideoView videoView;
	@BindView(R.id.sb_select)
	SeekBar sbVideo;
	@BindView(R.id.iv_head)
	ImageView ivHead;

	boolean isTouch = false;
	int totalTime;
	int currentTime;
	@Override
	protected int getLayoutResId() {
		return R.layout.activity_upload;
	}

	@Override
	protected void initData() {
		String mp4Path = Environment.getExternalStorageDirectory().getPath() + "/qwwz.mp4";
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		mmr.setDataSource(mp4Path);

		videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				totalTime = videoView.getDuration();//毫秒
			}
		});
		sbVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if(isTouch){
					currentTime = (int)(((float) progress / 100) * totalTime);
					videoView.seekTo(currentTime);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				isTouch = true;
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				isTouch = false;
				//获取第一帧图像的bitmap对象 单位是微秒
				Bitmap bitmap = mmr.getFrameAtTime((long) (currentTime * 1000), OPTION_PREVIOUS_SYNC);
				ivHead.setImageBitmap(bitmap);
			}
		});
		videoView.setVideoPath(mp4Path);
	}

}


## 问题
  很简单，这样就完成了gif所展示的内容。   <br />
  **但是也有明显不足之处**：seekBar的progress范围是0 - 100也就是视频获取截图的**最小时间单位是视频总时间长度 / 100**，这样的单位是满足不了长视频的截图要求的,这里还需要优化,可以通过一个自定义view来实现。
  
Demo地址：[https://github.com/JavaNoober/VideoScreen]()
