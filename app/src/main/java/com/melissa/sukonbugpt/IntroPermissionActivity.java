package com.melissa.sukonbugpt;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class IntroPermissionActivity extends AppCompatActivity {

    Button permissionButton;
    private static final int REQUEST_WRITE_SETTINGS = 100;
    private static final int REQUEST_OTHER_PERMISSIONS = 101;

    Boolean orientationResult= false;
    Boolean brightAutoResult = false;
    Boolean expansionResult = false;
    Boolean emotionResult = false;
    Boolean debugResult = false;
    Boolean introSukonbuResult = false;
    Boolean introScreenResult = false;
    Boolean gptApiResult = false;
    Boolean introOpenAiResult = false;
    Boolean demoEnable = false;

    Boolean bubbleResult = false;
    String callText = "";
    String conceptText = "";
    String selectGptValue = "";
    int brightValue = 0;
    //이번 액티비티에서 담당하게 되는 값
    int selectGpt = 0;
    int creativityValue = 7;
    String apiKeyResult="";

    String json ="";

    Boolean attensionPleaseResult = false;

    Boolean permissionResult = false;
    Boolean screenDarkmode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        permissionButton = findViewById(R.id.btn_permission);

        SharedPreferences preferences = getSharedPreferences("settingResult", MODE_PRIVATE);

        //권한 설정에서 저장했던 데이터
        permissionResult = preferences.getBoolean("permissionResult", permissionResult);
        //화면 세팅에서 저장했던 데이터
        screenDarkmode = preferences.getBoolean("screenDarkmode", screenDarkmode);
        orientationResult = preferences.getBoolean("orientationResult", orientationResult);
        brightAutoResult = preferences.getBoolean("brightAutoResult", brightAutoResult);
        brightValue = preferences.getInt("brightValue", brightValue);
        expansionResult= preferences.getBoolean("expansionResult", expansionResult);
        introScreenResult = preferences.getBoolean("introScreenResult", introScreenResult);
        bubbleResult = preferences.getBoolean("BubbleResult",bubbleResult);
        //스콘부 세팅에서 저장했던 데이터
        callText = preferences.getString("callText", callText);
        conceptText = preferences.getString("conceptText", conceptText);
        debugResult = preferences.getBoolean("debugResult", debugResult);
        emotionResult = preferences.getBoolean("emotionResult", emotionResult);
        introSukonbuResult = preferences.getBoolean("introSukonbuResult", introSukonbuResult);
        creativityValue = preferences.getInt("creativityValue",creativityValue);
        //OpenAi Api 에서 저장했던 데이터
        introOpenAiResult = preferences.getBoolean("introOpenAiResult", introOpenAiResult);
        json = preferences.getString("gptSpinnerResult", json);
        apiKeyResult = preferences.getString("apiKeyResult", apiKeyResult);
        demoEnable = preferences.getBoolean("demoEnable", demoEnable);
        gptApiResult = preferences.getBoolean("gptApiResult", gptApiResult);
        selectGptValue = preferences.getString("selectGptValue", selectGptValue);
        selectGpt = preferences.getInt("selectGpt",selectGpt);
        ////최종 확인란에서 저장했던 데이터
        attensionPleaseResult = preferences.getBoolean("attensionPleaseResult", attensionPleaseResult);





        permissionButton.setOnClickListener(v -> requestWriteSettingsPermission());


    }

    private void requestWriteSettingsPermission() {
        if (Settings.System.canWrite(this)) {
            // 이미 WRITE_SETTINGS 권한을 가지고 있음
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requestOtherPermissions();
            }
        } else {
            // WRITE_SETTINGS 권한이 없는 경우, 사용자에게 권한 요청 다이얼로그 표시
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_WRITE_SETTINGS);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void requestOtherPermissions() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_PHONE_NUMBERS,
                Manifest.permission.RECORD_AUDIO
        };

        // 권한이 허용되어 있는지 확인
        boolean allPermissionsGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        // 모든 권한이 허용되어 있지 않다면 권한 요청
        if (!allPermissionsGranted) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_OTHER_PERMISSIONS);
        }
        // 여기에서 필요한 권한을 사용할 수 있음
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_OTHER_PERMISSIONS) {
            // 권한 요청 결과 처리
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                permissionResult = true;
                // 권한이 부여됨
                SharedPreferences preferences = getSharedPreferences("settingResult", MODE_PRIVATE);

                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();

                //권한 설정에서 저장했던 데이터
                editor.putBoolean("permissionResult", permissionResult);
                //화면 세팅에서 저장했던 데이터
                editor.putBoolean("screenDarkmode", screenDarkmode);
                editor.putBoolean("orientationResult", orientationResult);
                editor.putBoolean("brightAutoResult", brightAutoResult);
                editor.putInt("brightValue", brightValue);
                editor.putBoolean("expansionResult", expansionResult);
                editor.putBoolean("introScreenResult", introScreenResult);
                editor.putBoolean("BubbleResult",bubbleResult);
                //스콘부 세팅에서 저장했던 데이터
                editor.putString("callText", callText);
                editor.putString("conceptText", conceptText);
                editor.putBoolean("debugResult", debugResult);
                editor.putBoolean("emotionResult", emotionResult);
                editor.putBoolean("introSukonbuResult", introSukonbuResult);
                editor.putInt("creativityValue", creativityValue);
                //OPEN AI 세팅에서 저장했던 데이터
                editor.putBoolean("introOpenAiResult", introOpenAiResult);
                editor.putString("gptSpinnerResult", json);
                editor.putString("apiKeyResult", apiKeyResult);
                editor.putBoolean("demoEnable", demoEnable);
                editor.putBoolean("gptApiResult", gptApiResult);
                editor.putString("selectGptValue",selectGptValue);
                editor.putInt("selectGpt",selectGpt);
                //최종 확인란에서 저장했던 데이터
                editor.putBoolean("attensionPleaseResult", attensionPleaseResult);

                editor.apply();

               Log.d("permission", "권한이 부여됨.");
                    Intent intent = new Intent(getApplicationContext(), IntroScreenSettingActivity.class);
                    startActivity(intent);
                    finish();
                // 권한이 필요한 작업 수행
            }  // 사용자가 권한을 거부함
            // 필요한 작업에 대한 처리 (예: 앱 종료 또는 다른 방법으로 권한 요청)
            //권한 요청하는 화면으로 다시 이동.

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_WRITE_SETTINGS) {
            // WRITE_SETTINGS 권한에 대한 사용자의 응답 처리
            if (Settings.System.canWrite(this)) {
                // 권한이 허용된 경우, 다른 권한을 요청
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    requestOtherPermissions();
                }
            }  // 권한이 거부된 경우, 사용자에게 메시지 표시 또는 다시 권한 요청 가능

        }
    }
}