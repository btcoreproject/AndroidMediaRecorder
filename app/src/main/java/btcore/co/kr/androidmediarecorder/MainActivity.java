package btcore.co.kr.androidmediarecorder;

import android.Manifest;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MediaRecorder recorder = new MediaRecorder();
    Context mContext;
    private Button btn_start;
    private Button btn_stop;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        // 테드 퍼미션 라이브러리 상용
        TedPermission.with(mContext)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

        btn_start = findViewById(R.id.btn_start);
        btn_stop = findViewById(R.id.btn_stop);

        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);




    }

    /**
     * 퍼미션 리스너
     */
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

            Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_start:
                startRec();

                break;

            case R.id.btn_stop:
                stopRec();

                break;

        }
    }


    public void startRec(){
        try {

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("/yyyyMMdd");

            String getTime = sdf.format(date);


            File file = Environment.getExternalStorageDirectory();
            // Storage/emlated/0/ 경로로 시작
            String path = file.getAbsolutePath() + "/오디오파일" ;

            file = new File(path);
            File[] files = file.listFiles();
            int nCounter = files.length;
            String strCounter = String.valueOf( nCounter + 1 );
            String savepath = file.getAbsolutePath() + getTime +"_" +strCounter+ ".3gp" ;

            if(!file.exists()){
                file.mkdir();
            }
            // 마이크로 녹음할 환경 셋팅
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 파일 타입 설정
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            // 코텍 설정
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(savepath);
            recorder.prepare();
            recorder.start();

        }catch (IllegalStateException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void stopRec(){
        try {

            recorder.stop();

        }catch (IllegalStateException e){
            e.printStackTrace();
        }

    }
}
