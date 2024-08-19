package com.melissa.sukonbugpt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
/*
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

 */
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class IntroScreenSettingActivity extends AppCompatActivity {

    SwitchCompat orientationSwitch;
    SwitchCompat brightSwitch;
    SwitchCompat darkModeSwitch;
    SwitchCompat bubbleSwitch;

    SeekBar brightSeekbar;
    TextView brightText;

    LinearLayout introScreenLayout;

    RadioGroup expansionGroup;
    RadioButton expansionYesButton;
    RadioButton expansionNoButton;

    Button introScreenNextButton;

    Boolean orientationResult= false;
    Boolean brightAutoResult = false;
    Boolean expansionResult = false;

    Boolean introScreenResult = false;

    Boolean emotionResult = false;

    Boolean bubbleResult = false;

    Boolean debugResult = false;

    Boolean introSukonbuResult = false;
    Resources resources;
    String callText = "";
    String conceptText = "";
    String selectGptValue = "";

    Boolean gptApiResult = false;
    Boolean introOpenAiResult = false;

    String apiKeyResult="";

    Boolean demoEnable = false;

    String json ="";



    int brightValue = 0;
    int selectGpt = 0;
    int creativityValue = 7;


    Boolean attensionPleaseResult=false;

    Boolean permissionResult = false;

    Boolean screenDarkmode = false;






    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen_setting);

        resources = getResources();
        callText = resources.getString(R.string.defaultCallType);
        conceptText = resources.getString(R.string.defaultConceptText);

        orientationSwitch = findViewById(R.id.intro_orientation_switch);
        brightSwitch = findViewById(R.id.intro_switch_bright);
        darkModeSwitch = findViewById(R.id.intro_darkmode_switch);
        brightSeekbar = findViewById(R.id.intro_seekbar_bright);
        brightText = findViewById(R.id.intro_text_bright);

        expansionGroup = findViewById(R.id.intro_group_expansion);
        expansionNoButton = findViewById(R.id.intro_btn_expansion_no);
        expansionYesButton = findViewById(R.id.intro_btn_expansion_yes);

        introScreenNextButton = findViewById(R.id.intro_btn_next_button);

        introScreenLayout = findViewById(R.id.intro_screen_layout);

        bubbleSwitch = findViewById(R.id.intro_bubble_switch);

        //애니메이션 추가용 소스
        //animateLinearLayoutWithResource(introScreenLayout);

        SharedPreferences preferences = getSharedPreferences("settingResult", MODE_PRIVATE);

        //권한 설정에서 저장했던 데이터
        permissionResult = preferences.getBoolean("permissionResult", permissionResult);
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
        ////최종 확인란에서 저장했던 데이터
        attensionPleaseResult = preferences.getBoolean("attensionPleaseResult", attensionPleaseResult);

        //미리 저장되어있는 값으로 사전 정의 실시

        orientationSwitch.setChecked(orientationResult);
        brightSwitch.setChecked(brightAutoResult);
        darkModeSwitch.setChecked(screenDarkmode);
        getScreenBrightness();
        if(expansionResult){
            expansionYesButton.setChecked(true);
        }
        else{
            expansionNoButton.setChecked(true);
        }

        if(bubbleResult){
            bubbleSwitch.setChecked(true);
        }
        else{
            bubbleSwitch.setChecked(false);
        }


        // 다크모드 스위치
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // isChecked에 따라 수행할 동작 구현
            if (isChecked) {

                // Switch가 켜진 경우
                // 여기에 수행할 작업을 추가하세요.
               Log.d("darkmode", "다크모드 활성화");
                screenDarkmode = true;


            } else {
                // Switch가 꺼진 경우
                // 여기에 수행할 작업을 추가하세요.
               Log.d("darkmode", "다크모드 비활성하");
                screenDarkmode = false;

            }
        });


        // 가로모드 스위치 구현
        orientationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // isChecked에 따라 수행할 동작 구현
            if (isChecked) {

                // Switch가 켜진 경우
                // 여기에 수행할 작업을 추가하세요.
                orientationResult = true;
               Log.d("orientation", "가로모드");
            } else {
                // Switch가 꺼진 경우
                // 여기에 수행할 작업을 추가하세요.
                orientationResult = false;
               Log.d("orientation", "세로모드");
            }
        });

        //밝기 자동조절 스위치
        brightSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // isChecked에 따라 수행할 동작 구현
            if (isChecked) {

                // Switch가 켜진 경우
                // 여기에 수행할 작업을 추가하세요.
               Log.d("bright", "밝기 스위치 활성화");
                brightAutoResult = true;
                brightSeekbar.setEnabled(false);

            } else {
                // Switch가 꺼진 경우
                // 여기에 수행할 작업을 추가하세요.
               Log.d("bright", "밝기 스위치 비활성화");
                brightAutoResult = false;
                brightSeekbar.setEnabled(true);

            }
        });


        //대화창 생성 스위치
        bubbleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // isChecked에 따라 수행할 동작 구현
            if (isChecked) {

                // Switch가 켜진 경우
                // 여기에 수행할 작업을 추가하세요.
                Log.d("bubble", "대화창 활성화");
                bubbleResult = true;

            } else {
                // Switch가 꺼진 경우
                // 여기에 수행할 작업을 추가하세요.
                Log.d("bubble", "대화창 비활성화");
                bubbleResult = false;

            }
        });

        brightSeekbar.setProgress(brightValue);
        brightText.setText(Integer.toString(brightValue));
        brightSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //시크바를 조작하고 있는 중
                //System.out.println("조작 중 값 : " +sound_seekBar.getProgress());

                brightText.setText(Integer.toString(brightSeekbar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //시크바를 처음 터치
                //System.out.println("처음 터치 값 : " +sound_seekBar.getProgress());
                brightText.setText(Integer.toString(brightSeekbar.getProgress()));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //시크바 터치가 끝났을 때
                //System.out.println("터치 끝 값 : " +sound_seekBar.getProgress());
                brightText.setText(Integer.toString(brightSeekbar.getProgress()));
                brightValue = brightSeekbar.getProgress();
            }
        });





        introScreenNextButton.setOnClickListener(v -> saveShared());

        //라디오 그룹 클릭 리스너

        expansionGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.intro_btn_expansion_no) {
                expansionResult = false;
               Log.d("expansion", "확대모드 사용 안함.");
            } else if (checkedId == R.id.intro_btn_expansion_yes) {
                expansionResult = true;
               Log.d("expansion", "확대모드 사용 함.");
            }
        });


    }


    private void saveShared() {


        introScreenResult = true;

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

        startActivity(new Intent(this, IntroSukonbuSettingActivity.class));
        finish();



    }

    public void getScreenBrightness() {
        try {
            ContentResolver contentResolver = getContentResolver();
            brightValue = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
            brightValue = (int) ((float)brightValue/255*10);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
    private void animateLinearLayoutWithResource(LinearLayout linearLayout) {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);

            // R.anim.list_view은 여러분이 정의한 애니메이션 리소스 파일의 이름입니다.
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.list_view);

            // 각각의 아이템이 나타나는 시작 지연 시간을 조절할 수 있습니다.
            animation.setStartOffset(i * 200);

            // 애니메이션 지속 시간을 조절할 수 있습니다.
            animation.setDuration(500);

            // Interpolator 설정
            animation.setInterpolator(new AccelerateInterpolator());

            // 애니메이션을 적용할 대상 뷰에 적용합니다.
            child.startAnimation(animation);
        }
    }

     */

}