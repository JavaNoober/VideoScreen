package com.noob.videoscreen;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
	VideoView videoView;
	SeekBar sbVideo;
	ImageView ivHead;

	boolean isTouch = false;
	int totalTime;
	int currentTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		videoView = findViewById(R.id.vv_player);
		sbVideo = findViewById(R.id.sb_select);
		ivHead = findViewById(R.id.iv_head);
		initData();
	}

	protected void initData() {
		String mp4Path = Environment.getExternalStorageDirectory().getPath() + "/qwwz.mp4";
		final MediaMetadataRetriever mmr = new MediaMetadataRetriever();
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
				Bitmap bitmap= mmr.getFrameAtTime((long) (currentTime * 1000), MediaMetadataRetriever
						.OPTION_PREVIOUS_SYNC);
				ivHead.setImageBitmap(bitmap);
			}
		});
		videoView.setVideoPath(mp4Path);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}


	@Override
	protected void onStop() {
		super.onStop();
		videoView.stopPlayback();
	}
}
