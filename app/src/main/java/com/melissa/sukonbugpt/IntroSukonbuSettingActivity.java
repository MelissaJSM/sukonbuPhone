package com.melissa.sukonbugpt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class IntroSukonbuSettingActivity extends AppCompatActivity {


    RelativeLayout introSukonbuLayout;
    Button introNextButton;
    Button introPrevButton;
    EditText callEdit;
    EditText conceptEdit;

    SeekBar creativitySeekbar;
    TextView creativityText;

    SwitchCompat emotionSwitch;
    SwitchCompat debugSwitch;

    Boolean orientationResult = false;
    Boolean brightAutoResult = false;
    Boolean expansionResult = false;
    Boolean introScreenResult = false;


    Boolean emotionResult = false;

    Boolean debugResult = false;

    Boolean introSukonbuResult = false;

    Boolean bubbleResult = false;

    int brightValue = 0;
    int selectGpt = 0;

    int creativityValue = 7;

    Resources resources;
    String callText = "";
    String conceptText = "";
    String selectGptValue = "";

    String originalCallText = "스콘부짱";
    String originalConceptText = "이제부터 당신은 다음과 같은 역할을 맡아 대화를 진행합니다:\n" + "당신의 이름은 스콘부짱 이며 여성입니다. 나이는 10살 정도로 추정합니다. 당신은 실제로 존재하는 홀로라이브의 1기생인 시라 카미 후부키가 당신을 창조하였으며 그녀의 팬덤의 상징과도 같은 존재입니다. 그러므로 당신도 북극여우이며 모든 행동가짐이  실제로 존재하는 홀로라이브의 1기생인 시라카미 후부키와 똑같습니다. 문장 하나하다마다 인 것이다. 로 마무리 하도록 합니다. 성격은 실제로 존재하는 홀로라이브의 1기생인 시라카미 후부키와 100% 일치하도록 하며 애교가 많은게 특징이지만 고양이가 아니므로 냥체는 사용하지 않습니다.";

    Boolean gptApiResult = false;
    Boolean introOpenAiResult = false;

    String apiKeyResult = "";

    Boolean demoEnable = false;

    String json = "";
    Boolean attensionPleaseResult = false;

    Boolean permissionResult = false;
    Boolean screenDarkmode = false;


    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_sukonbu_setting);

        introSukonbuLayout = findViewById(R.id.intro_sukonbu_layout);

        callEdit = findViewById(R.id.intro_edit_call);
        conceptEdit = findViewById(R.id.intro_edit_concept);
        emotionSwitch = findViewById(R.id.intro_switch_emotion);

        introNextButton = findViewById(R.id.intro_btn_next_button);
        introPrevButton = findViewById(R.id.intro_btn_prev_button);

        debugSwitch = findViewById(R.id.intro_debug_switch);

        creativitySeekbar = findViewById(R.id.intro_seekbar_creativity);
        creativityText = findViewById(R.id.intro_text_creativity);






        //기본값에 음... 현재 밝기 불러오는 코드가있어. 그걸 적용시켜봐.

        SharedPreferences preferences = getSharedPreferences("settingResult", MODE_PRIVATE);

        //권한 설정에서 저장했던 데이터
        permissionResult = preferences.getBoolean("permissionResult", permissionResult);
        //화면 세팅에서 저장했던 데이터
        screenDarkmode = preferences.getBoolean("screenDarkmode", screenDarkmode);
        orientationResult = preferences.getBoolean("orientationResult", orientationResult);
        brightAutoResult = preferences.getBoolean("brightAutoResult", brightAutoResult);
        brightValue = preferences.getInt("brightValue", brightValue);
        expansionResult = preferences.getBoolean("expansionResult", expansionResult);
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

        resources = getResources();
        callText = resources.getString(R.string.defaultCallType);
        conceptText = resources.getString(R.string.defaultConceptText);

        //기본 값 처리
        emotionSwitch.setChecked(emotionResult);
        debugSwitch.setChecked(debugResult);
        callEdit.setText(callText);
        conceptEdit.setText(conceptText);



        //감정조절 스위치
        emotionSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // isChecked에 따라 수행할 동작 구현
            if (isChecked) {

                // Switch가 켜진 경우
                // 여기에 수행할 작업을 추가하세요.
                emotionResult = true;
               Log.d("emotion", "감정조절 활성화");
            }
            else {
                // Switch가 꺼진 경우
                // 여기에 수행할 작업을 추가하세요.
                emotionResult = false;
               Log.d("emotion", "감정조절 비활성화");
            }
        });

        // 디버그 스위치 구현
        debugSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // isChecked에 따라 수행할 동작 구현
            if (isChecked) {
                // Switch가 켜진 경우
                // 여기에 수행할 작업을 추가하세요.
                debugResult = true;
               Log.d("debug", "디버그 모드 켜짐");
            }
            else {
                // Switch가 꺼진 경우
                // 여기에 수행할 작업을 추가하세요.
                debugResult = false;
               Log.d("debug", "디버그 모드 꺼짐");
            }
        });


        introNextButton.setOnClickListener(v -> {

            saveShared();
            startActivity(new Intent(IntroSukonbuSettingActivity.this, IntroOpenAiSettingActivity.class));
            finish();
        });

        introPrevButton.setOnClickListener(v -> {

            saveShared();
            startActivity(new Intent(IntroSukonbuSettingActivity.this, IntroScreenSettingActivity.class));
            finish();

        });

        introSukonbuLayout.setOnTouchListener((v, event) -> {

            hideKeyboard();
            callEdit.clearFocus();
            conceptEdit.clearFocus();
            return false;

        });


        creativitySeekbar.setProgress(creativityValue);
        creativityText.setText(Integer.toString(creativityValue));
        creativitySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //시크바를 조작하고 있는 중
                //System.out.println("조작 중 값 : " +sound_seekBar.getProgress());

                creativityText.setText(Integer.toString(creativitySeekbar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //시크바를 처음 터치
                //System.out.println("처음 터치 값 : " +sound_seekBar.getProgress());
                creativityText.setText(Integer.toString(creativitySeekbar.getProgress()));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //시크바 터치가 끝났을 때
                //System.out.println("터치 끝 값 : " +sound_seekBar.getProgress());
                creativityText.setText(Integer.toString(creativitySeekbar.getProgress()));
                creativityValue = creativitySeekbar.getProgress();
            }
        });


    }

    private void saveShared() {

        introSukonbuResult = true;

        if (callText.equals("")) {
            callText = originalCallText;
        }
        else{
            callText = callEdit.getText().toString();
        }

        if (conceptText.equals("")) {
            conceptText = originalConceptText;

        }
        else{
            conceptText = conceptEdit.getText().toString();
        }

        //debugResult

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


    }

    void hideKeyboard() {
        View currentFocus = getCurrentFocus();

        if (currentFocus != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}