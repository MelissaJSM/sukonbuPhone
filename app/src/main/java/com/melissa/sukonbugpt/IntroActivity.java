package com.melissa.sukonbugpt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {



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
    Resources resources;
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


        resources = getResources();
        callText = resources.getString(R.string.defaultCallType);
        conceptText = resources.getString(R.string.defaultConceptText);


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
        //최종 확인란에서 저장했던 데이터
        attensionPleaseResult = preferences.getBoolean("attensionPleaseResult", attensionPleaseResult);

       Log.d("loadData", "introScreenResult 값 : " + introScreenResult);
       Log.d("loadData", "introSukonbuResult 값 : " + introSukonbuResult);
       Log.d("loadData", "introOpenAiResult 값 : " + introOpenAiResult);
       Log.d("loadData", "attensionPleaseResult 값 : " + attensionPleaseResult);



        // 권한 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(!permissionResult) {
                // 권한이 부여되지 않았으면 IntroPermissionActivity로 이동
                startActivity(new Intent(this, IntroPermissionActivity.class));
                finish();
            }
            else if(!introScreenResult){
                startActivity(new Intent(this, IntroScreenSettingActivity.class));
                finish();

            }
            else if(!introSukonbuResult){
                startActivity(new Intent(this, IntroSukonbuSettingActivity.class));
                finish();
            }
            else if(!introOpenAiResult){
                startActivity(new Intent(this, IntroOpenAiSettingActivity.class));
                finish();
            }
            else if(!attensionPleaseResult){
                startActivity(new Intent(this, IntroAttentionPleaseActivity.class));
                finish();
            }
            else{
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }
    }
}