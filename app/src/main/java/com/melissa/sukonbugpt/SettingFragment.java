package com.melissa.sukonbugpt;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SettingFragment extends Fragment {

    MainActivity mainActivity; //(액티비티에서 이동하기) 주 가되는 메인액티비티 선언

    private DataTransmissionListener dataTransmissionSetListener; // 값 전송용 변수 선언

    RelativeLayout settingLayout;

    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient gptClient = new OkHttpClient();

    OkHttpClient client = new OkHttpClient();


    ArrayList<String> gptList = new ArrayList<>();
    ArrayList<String> gptSpinnerResult = new ArrayList<>();

    SharedPreferences sharedPreferences;

    SeekBar creativitySeekbar;
    TextView creativityText;



    SeekBar brightSeekbar;
    ImageButton sideExitSettingButton;
    Button okButton;
    Button verificationButton;
    Button demoButton;

    Button identificationButton;

    SwitchCompat darkmodeSwitch;
    SwitchCompat orientationSwitch;
    SwitchCompat debugSwitch;
    SwitchCompat brightSwitch;
    SwitchCompat emotionSwitch;
    SwitchCompat bubbleSwitch;
    RadioGroup expansionGroup;
    RadioButton expansionYesButton;
    RadioButton expansionNoButton;

    EditText callEdit;
    EditText conceptEdit;

    EditText apiKeyEdit;

    TextView resultApiText;
    TextView brightText;

    TextView identificationKey;

    TextView selectApiText;

    Button btnDownload;


    ArrayList<String> downloadId = new ArrayList<>();
    ArrayList<String> downloadLink = new ArrayList<>();

    ArrayList<String> downloadLink2 = new ArrayList<>();
    ArrayList<String> downloadLink3 = new ArrayList<>();


    ArrayList<String> downloadPath = new ArrayList<>();

    private ProgressBar progressBar;
    private TextView textView;

    Boolean orientationResult = false;
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
    Boolean tutorialResult = false;
    String callText = "";
    String conceptText = "";
    String selectGptValue = "";

    String fileUrl = "";
    String directDownloadLink = "https://drive.google.com/uc?export=download&id=";

    int fileDownloadCount = 0;


    int brightValue = 0;
    //이번 액티비티에서 담당하게 되는 값
    int selectGpt = 0;

    int downloadValue = 0;
    int creativityValue = 7;
    String apiKeyResult = "";

    String json = "";

    String demoKey = "";

    Boolean attensionPleaseResult = false;

    Boolean permissionResult = false;
    Boolean screenDarkmode = false;

    Spinner gptSpinner;

    int gptListValue = 0;


    String originalCallText = "스콘부짱";
    String originalConceptText = "이제부터 당신은 다음과 같은 역할을 맡아 대화를 진행합니다:\n" + "당신의 이름은 스콘부짱 이며 여성입니다. 나이는 10살 정도로 추정합니다. 당신은 실제로 존재하는 홀로라이브의 1기생인 시라 카미 후부키가 당신을 창조하였으며 그녀의 팬덤의 상징과도 같은 존재입니다. 그러므로 당신도 북극여우이며 모든 행동가짐이  실제로 존재하는 홀로라이브의 1기생인 시라카미 후부키와 똑같습니다. 문장 하나하다마다 인 것이다. 로 마무리 하도록 합니다. 성격은 실제로 존재하는 홀로라이브의 1기생인 시라카미 후부키와 100% 일치하도록 하며 애교가 많은게 특징이지만 고양이가 아니므로 냥체는 사용하지 않습니다.";


    //어땃쥐
    //화면이 붙을때 작동하는 메서드
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


        mainActivity = (MainActivity) getActivity(); //(액티비티에서 이동하기) 현재 소속된 액티비티를 메인 액티비티로 한다.

        if (context instanceof DataTransmissionListener) {
            dataTransmissionSetListener = (DataTransmissionListener) context; // context 처리해서 값 불러오기
        }
        else {
            throw new RuntimeException(context + "must implement dataTransmissionSetListener");
        }


        requireActivity().getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback); // 뒤로가기 버튼 작업
    }

    //뒤로가기 버튼 작업
    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            performFragmentRemovalWithAnimation();
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflaterSetting = inflater.inflate(R.layout.fragment_setting, container, false); // inflater 때문에 선언을 추가해야함

        requireActivity().runOnUiThread(() -> {

            settingLayout = inflaterSetting.findViewById(R.id.setting_layout);

            okButton = inflaterSetting.findViewById(R.id.btn_ok_button);
            verificationButton = inflaterSetting.findViewById(R.id.btn_verification);
            apiKeyEdit = inflaterSetting.findViewById(R.id.edit_api_key);

            sideExitSettingButton = inflaterSetting.findViewById(R.id.btn_side_exit_setting);
            orientationSwitch = inflaterSetting.findViewById(R.id.orientation_switch);
            debugSwitch = inflaterSetting.findViewById(R.id.debug_switch);

            darkmodeSwitch = inflaterSetting.findViewById(R.id.darkmode_switch);

            expansionGroup = inflaterSetting.findViewById(R.id.group_expansion);
            expansionNoButton = inflaterSetting.findViewById(R.id.btn_expansion_no);
            expansionYesButton = inflaterSetting.findViewById(R.id.btn_expansion_yes);

            callEdit = inflaterSetting.findViewById(R.id.edit_call);
            conceptEdit = inflaterSetting.findViewById(R.id.edit_concept);

            resultApiText = inflaterSetting.findViewById(R.id.text_api_result);

            gptSpinner = inflaterSetting.findViewById(R.id.spinner_gpt);

            demoButton = inflaterSetting.findViewById(R.id.btn_demo);

            brightSeekbar = inflaterSetting.findViewById(R.id.seekbar_bright);
            brightSwitch = inflaterSetting.findViewById(R.id.switch_bright);
            brightText = inflaterSetting.findViewById(R.id.text_bright);

            bubbleSwitch = inflaterSetting.findViewById(R.id.bubble_switch);

            emotionSwitch = inflaterSetting.findViewById(R.id.switch_emotion);

            selectApiText = inflaterSetting.findViewById(R.id.text_api_select);


            progressBar = inflaterSetting.findViewById(R.id.progressBar_setting);
            textView = inflaterSetting.findViewById(R.id.textView_setting);
            btnDownload = inflaterSetting.findViewById(R.id.btnDownload_setting);

            identificationButton = inflaterSetting.findViewById(R.id.btn_identification_setting);
            identificationKey = inflaterSetting.findViewById(R.id.btn_identification_key_setting);

            creativitySeekbar = inflaterSetting.findViewById(R.id.setting_seekbar_creativity);
            creativityText = inflaterSetting.findViewById(R.id.setting_text_creativity);

            client = new OkHttpClient().newBuilder().connectTimeout(120, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build();


            // MasterKey 생성
            String masterKeyAlias = null;
            try {
                masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // EncryptedSharedPreferences 초기화
            sharedPreferences = null;
            try {
                assert masterKeyAlias != null;
                sharedPreferences = EncryptedSharedPreferences.create("secret_shared_prefs", masterKeyAlias, requireActivity().getApplicationContext(), EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            } catch (Exception e) {
                e.printStackTrace();
            }


            loadShared();

            // 여기에서 LinearLayout을 찾거나 생성합니다. 예를 들어,
            LinearLayout linearLayout = inflaterSetting.findViewById(R.id.list_view);

            Animation fadeInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.list_view);
            Animation fadeOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.list_view);

            // 각 자식 뷰에 대해 애니메이션을 적용합니다.
            applyAnimation(linearLayout, fadeInAnimation, fadeOutAnimation);

            readySetChecked();

            //클라이언트 설정
            gptClient = new OkHttpClient().newBuilder().connectTimeout(120, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build();


            downloadLink();

            //걍 취소일때
            sideExitSettingButton.setOnClickListener(v -> {
                FragmentManager manager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                // 프래그먼트가 제거되는 동안의 애니메이션 설정
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);

                transaction.remove(SettingFragment.this);
                transaction.commit();

                manager.popBackStack();
            });


            okButton.setOnClickListener(v -> {
                inflaterSetting.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out));


                saveShared();

                dataTransmissionSetListener.settingTransmissionSet();
                FragmentManager manager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.remove(SettingFragment.this);
                transaction.commit();
                manager.popBackStack();
            });

            // 다크모드 스위치
            darkmodeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // isChecked에 따라 수행할 동작 구현
                if (isChecked) {

                    // Switch가 켜진 경우
                    // 여기에 수행할 작업을 추가하세요.
                    Log.d("darkmode", "다크모드 활성화");
                    screenDarkmode = true;


                }
                else {
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
                }
                else {
                    // Switch가 꺼진 경우
                    // 여기에 수행할 작업을 추가하세요.
                    orientationResult = false;
                    Log.d("orientation", "세로모드");
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

            //밝기 자동조절 스위치
            brightSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // isChecked에 따라 수행할 동작 구현
                if (isChecked) {

                    // Switch가 켜진 경우
                    // 여기에 수행할 작업을 추가하세요.
                    Log.d("bright", "밝기 스위치 활성화");
                    brightAutoResult = true;
                    brightSeekbar.setEnabled(false);

                }
                else {
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

                }
                else {
                    // Switch가 꺼진 경우
                    // 여기에 수행할 작업을 추가하세요.
                    Log.d("bubble", "대화창 비활성화");
                    bubbleResult = false;

                }
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


            //라디오 그룹 클릭 리스너
            expansionGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.btn_expansion_no) {
                    expansionResult = false;
                    Log.d("expansion", "확대모드 사용 안함.");
                }
                else if (checkedId == R.id.btn_expansion_yes) {
                    expansionResult = true;
                    Log.d("expansion", "확대모드 사용 함.");
                }
            });

            //검증을 시작하는 버튼
            verificationButton.setOnClickListener(v -> {

                hideKeyboard();
                apiKeyEdit.clearFocus();


                demoEnable = false;
                gptSpinnerResult.clear();
                resultApiText.setText("api key 값을 검증하고 있습니다.");
                apiKeyResult = apiKeyEdit.getText().toString();
                keyTest(apiKeyResult);
            });

            //gpt 버전 선택하는 스피너
            gptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (screenDarkmode) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                    }
                    else {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                    }

                    selectGpt = position;
                    Log.d("gpt", "선택된 아이템 : " + gptSpinner.getItemAtPosition(position));
                    selectGptValue = (String) gptSpinner.getItemAtPosition(position);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Log.d("gpt", "아무것도 선택 안됨");

                }
            });

            //체험판 버튼을 눌렀을때 경고처리.
            demoButton.setOnClickListener(v -> showAlertDialog());


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


            settingLayout.setOnTouchListener((v, event) -> {
                hideKeyboard();

                callEdit.clearFocus();
                conceptEdit.clearFocus();
                apiKeyEdit.clearFocus();


                return false;
            });


            btnDownload.setOnClickListener(v -> {


                downloadPath.clear();
                fileDownloadCount = 0;
                progressBar.setProgress(fileDownloadCount);
                textView.setText((int) ((float) fileDownloadCount / 28 * 100) + " %");

                if (downloadValue == 0) {
                    fileUrl = directDownloadLink + downloadLink.get(fileDownloadCount);
                }
                else if (downloadValue == 1) {
                    fileUrl = directDownloadLink + downloadLink2.get(fileDownloadCount);
                }
                else if (downloadValue == 2) {
                    fileUrl = directDownloadLink + downloadLink3.get(fileDownloadCount);
                }

                String downloadDirectory = getDefaultDownloadDirectory();
                requestFileDownload(fileUrl, downloadDirectory);

            });


            //검증을 시작하는 버튼
            identificationButton.setOnClickListener(v -> {
                identificationKey.setVisibility(View.VISIBLE);
                @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(requireActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

                identificationKey.setText("문의용 식별키 : " + android_id);
            });

        });

        return inflaterSetting;
    }


    //사전에 정의된 값으로 구현
    @SuppressLint("SetTextI18n")
    private void readySetChecked() {
        Log.d("loaddata", "기본값 추적 시작");

        //화면 설정
        orientationSwitch.setChecked(orientationResult);
        if (brightAutoResult) {
            brightSeekbar.setEnabled(false);
            brightSwitch.setChecked(brightAutoResult);
        }
        else {
            brightSeekbar.setEnabled(true);
        }

        darkmodeSwitch.setChecked(screenDarkmode);
        getScreenBrightness();
        if (expansionResult) {
            expansionYesButton.setChecked(true);
        }
        else {
            expansionNoButton.setChecked(true);
        }

        if (bubbleResult) {
            bubbleSwitch.setChecked(true);
        }
        else {
            bubbleSwitch.setChecked(false);
        }

        //스콘부짱 설정
        emotionSwitch.setChecked(emotionResult);
        debugSwitch.setChecked(debugResult);
        callEdit.setText(callText);
        conceptEdit.setText(conceptText);

        //open ai api 설정

        if (demoEnable) {
            Gson gson = new Gson();
            Type type = new com.google.gson.reflect.TypeToken<ArrayList<String>>() {
            }.getType();
            gptSpinnerResult = gson.fromJson(json, type);
            dynamic_spinner();

            resultApiText.setText("체험판이 활성화 되었습니다.");
            apiKeyEdit.setText("Demo version activated.");
            okButton.setEnabled(true);
            gptSpinner.setEnabled(true);
        }
        else if (gptApiResult) {
            resultApiText.setText("사용 가능한 GPT 리스트 조회가 완료되었습니다.");
            apiKeyEdit.setText(apiKeyResult);
            okButton.setEnabled(true);
            gptSpinner.setEnabled(true);
            Gson gson = new Gson();
            Type type = new com.google.gson.reflect.TypeToken<ArrayList<String>>() {
            }.getType();
            gptSpinnerResult = gson.fromJson(json, type);
            dynamic_spinner();
            demoButton.setEnabled(false);

        }
        else {
            okButton.setEnabled(false);
            gptSpinner.setEnabled(false);
        }
        selectApiText.setText("이전 버전 :\n" + selectGptValue);
        gptSpinner.setSelection(selectGpt);


    }

    //밝기 조절
    public void getScreenBrightness() {
        try {
            ContentResolver contentResolver = requireActivity().getContentResolver();
            brightValue = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
            brightValue = (int) ((float) brightValue / 255 * 10);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void applyAnimation(ViewGroup viewGroup, Animation fadeIn, Animation fadeOut) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childView = viewGroup.getChildAt(i);

            // 각 아이템에 대해 fadeIn 애니메이션을 적용합니다.
            childView.startAnimation(fadeIn);

            // fadeOut 애니메이션을 추가하여 아이템이 사라지는 효과를 만듭니다.
            fadeIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // 애니메이션 시작 시 실행할 작업
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // 애니메이션 종료 시 실행할 작업
                    childView.startAnimation(fadeOut);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // 애니메이션 반복 시 실행할 작업
                }
            });
        }
    }

    private void keyTest(String key) {
        gptListValue = 0;
        gptSpinnerResult.clear(); // 기존 결과 초기화
        gptList.clear();
        gptList.add("gpt-3.5-turbo"); // 4096
        gptList.add("gpt-4-turbo-preview"); // 128000

        for (int apiReplay = 0; apiReplay < gptList.size(); apiReplay++) {


            //okhttp
            //추가된 내용
            JSONArray arr = new JSONArray();
            JSONObject baseAi = new JSONObject();
            JSONObject userMsg = new JSONObject();
            try {
                //AI 속성설정
                baseAi.put("role", "system");
                baseAi.put("content", conceptText);
                //유저 메세지
                userMsg.put("role", "user");
                userMsg.put("content", "Hello, GPT");
                //array로 담아서 한번에 보낸다
                arr.put(baseAi);
                arr.put(userMsg);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            JSONObject object = new JSONObject();
            try {
                //모델명 변경
                //모델 리스트
                // gpt-3.5-turbo
                // gpt-3.5-turbo-1106
                // gpt-4
                // gpt-4-1106-preview
                object.put("model", gptList.get(apiReplay));
                object.put("messages", arr);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(object.toString(), JSON);
            Request request = new Request.Builder().url("https://api.openai.com/v1/chat/completions")  //url 경로 수정됨
                    .header("Authorization", "Bearer " + key).post(body).build();

            int finalApiReplay = apiReplay;
            gptClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d("gpt", "Failed to load response due to onFailure " + e.getMessage());
                    requireActivity().runOnUiThread(() -> resultApiText.setText("조회에 실패하였습니다. 다시 시도해주세요.\n하단 리스트가 갱신되었으면 그대로 진행 해 주세요."));
                    //여기서 오류가 발생하네??

                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            // JSON 문자열을 JSONObject로 변환
                            //System.out.println(response.body().string());
                            assert response.body() != null;
                            JSONObject json = new JSONObject(response.body().string());
                            // "error" 키에 해당하는 JSONObject 가져오기
                            String completeCode = json.getString("object");
                            // 추출한 값 출력
                            Log.d("gpt", "성공 코드: " + gptList.get(finalApiReplay) + " : " + completeCode);
                            gptSpinnerResult.add(gptList.get(finalApiReplay));
                            gptListValue = gptListValue + 1;
                            if (gptListValue == 2) {
                                requireActivity().runOnUiThread(() -> {
                                    // UI 업데이트 코드를 여기에 작성
                                    // 예: 텍스트뷰 업데이트
                                    Log.d("gpt", "Count End");
                                    resultApiText.setText("사용 가능한 GPT 리스트 조회가 완료되었습니다.");
                                    okButton.setEnabled(true);
                                    demoButton.setEnabled(false);
                                    gptSpinner.setEnabled(true);
                                    gptApiResult = true;
                                    dynamic_spinner();

                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        //System.out.println("Failed to load response due to else " + response.body().string());
                        //여기서 키 값이 잘못되면 에러가 발생함.
                        try {
                            // JSON 문자열을 JSONObject로 변환
                            assert response.body() != null;
                            JSONObject json = new JSONObject(response.body().string());
                            // "error" 키에 해당하는 JSONObject 가져오기
                            JSONObject errorObject = json.getJSONObject("error");
                            // "code" 키에 해당하는 값 가져오기
                            String errorCode = errorObject.getString("code");
                            // 추출한 값 출력
                            Log.d("gpt", "Error Code: " + errorCode);

                            requireActivity().runOnUiThread(() -> {
                                // UI 업데이트 코드를 여기에 작성
                                // 예: 텍스트뷰 업데이트
                                if (errorCode.contains("invalid_api_key")) {
                                    resultApiText.setText("Error Code: " + errorCode);
                                    okButton.setEnabled(false);
                                    gptSpinner.setEnabled(false);
                                    demoButton.setEnabled(true);
                                }
                                else {
                                    resultApiText.setText("Error Code: " + errorCode);
                                    gptListValue = gptListValue + 1;
                                    if (gptListValue == 2) {
                                        Log.d("gpt", "Count End");
                                        resultApiText.setText("사용 가능한 GPT 리스트 조회가 완료되었습니다.");
                                        okButton.setEnabled(true);
                                        demoButton.setEnabled(false);
                                        gptSpinner.setEnabled(true);
                                        gptApiResult = true;
                                        if (demoEnable) {
                                            apiKeyEdit.setText("Demo version activated.");
                                        }
                                        dynamic_spinner();
                                    }
                                }

                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public void dynamic_spinner() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity().getApplicationContext(),

                android.R.layout.simple_spinner_item, gptSpinnerResult);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        gptSpinner.setAdapter(adapter);


    }


    @SuppressLint("SetTextI18n")
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("주의!");
        builder.setMessage("체험판을 사용하시겠습니까?\n체험판은 총 50회의 메세지를 주고받을 수 있으며 그 이후로는 key를 따로 발급받으셔야 합니다.");

        builder.setPositiveButton("예", (dialog, which) -> {
            // "예" 버튼을 눌렀을 때의 처리
            handleYesButtonClick(true);
            okButton.setEnabled(true);


            resultApiText.setText("체험판이 활성화 되었습니다. 검증까지 기다려주세요.");
            apiKeyEdit.setText("Demo version activated.");
            serverSend();

        });

        builder.setNegativeButton("아니오", (dialog, which) -> {
            // "아니오" 버튼을 눌렀을 때의 처리
            handleYesButtonClick(false);
            okButton.setEnabled(true);
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void handleYesButtonClick(boolean result) {
        Log.d("alartdialog", "결과값 : " + result);
        // 여기에서 result 값을 사용하여 처리
        // result가 true면 "예"를 선택한 경우, false면 "아니오"를 선택한 경우
    }

    private void performFragmentRemovalWithAnimation() {
        FragmentManager manager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        // 프래그먼트가 제거되는 동안의 애니메이션 설정
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);

        transaction.remove(SettingFragment.this);
        transaction.commit();

        manager.popBackStack();
    }

    private void hideKeyboard() {
        if (getActivity() != null && getActivity().getCurrentFocus() != null) {
            // 프래그먼트기 때문에 getActivity() 사용
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void saveShared() {

        if (callText.equals("")) {
            callText = originalCallText;
        }
        else {
            callText = callEdit.getText().toString();
        }

        if (conceptText.equals("")) {
            conceptText = originalConceptText;

        }
        else {
            conceptText = conceptEdit.getText().toString();
        }

        Gson gson = new Gson();
        json = gson.toJson(gptSpinnerResult);


        SharedPreferences preferences = requireActivity().getSharedPreferences("settingResult", MODE_PRIVATE);

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
        editor.putBoolean("BubbleResult", bubbleResult);
        //스콘부 세팅에서 저장했던 데이터
        editor.putString("callText", callText);
        editor.putString("conceptText", conceptText);
        editor.putBoolean("debugResult", debugResult);
        editor.putBoolean("emotionResult", emotionResult);
        editor.putBoolean("introSukonbuResult", introSukonbuResult);
        editor.putString("selectGptValue", selectGptValue);
        editor.putInt("creativityValue", creativityValue);
        //OPEN AI 세팅에서 저장했던 데이터
        editor.putBoolean("introOpenAiResult", introOpenAiResult);
        editor.putString("gptSpinnerResult", json);
        editor.putString("apiKeyResult", apiKeyResult);
        editor.putBoolean("demoEnable", demoEnable);
        editor.putBoolean("gptApiResult", gptApiResult);
        editor.putInt("selectGpt", selectGpt);
        //최종 확인란에서 저장했던 데이터
        editor.putBoolean("attensionPleaseResult", attensionPleaseResult);
        editor.putBoolean("tutorialResult", tutorialResult);

        editor.apply();


    }

    private void loadShared() {


        //저장된 데이터를 가져옵니다.
        SharedPreferences preferences = requireActivity().getSharedPreferences("settingResult", MODE_PRIVATE);

        //권한 설정에서 저장했던 데이터
        permissionResult = preferences.getBoolean("permissionResult", permissionResult);
        //화면 세팅에서 저장했던 데이터
        screenDarkmode = preferences.getBoolean("screenDarkmode", screenDarkmode);
        orientationResult = preferences.getBoolean("orientationResult", orientationResult);
        brightAutoResult = preferences.getBoolean("brightAutoResult", brightAutoResult);
        brightValue = preferences.getInt("brightValue", brightValue);
        expansionResult = preferences.getBoolean("expansionResult", expansionResult);
        introScreenResult = preferences.getBoolean("introScreenResult", introScreenResult);
        bubbleResult = preferences.getBoolean("BubbleResult", bubbleResult);
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
        selectGpt = preferences.getInt("selectGpt", selectGpt);
        demoKey = sharedPreferences.getString("demokey", "");
        //최종 확인란에서 저장했던 데이터
        attensionPleaseResult = preferences.getBoolean("attensionPleaseResult", attensionPleaseResult);
        tutorialResult = preferences.getBoolean("tutorialResult", tutorialResult);


        Log.i("Tag", "////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
        Log.i("Tag", "퍼미션 관련 부분");
        Log.d("Tag", "permissionResult : " + permissionResult);

        Log.i("Tag", "화면 관련 부분");
        Log.d("Tag", "screenDarkmode : " + screenDarkmode);
        Log.d("Tag", "orientationResult : " + orientationResult);
        Log.d("Tag", "brightAutoResult : " + brightAutoResult);
        Log.d("Tag", "brightValue : " + brightValue);
        Log.d("Tag", "expansionResult : " + expansionResult);
        Log.d("Tag", "introScreenResult : " + introScreenResult);

        Log.i("Tag", "스콘부 관련 부분");
        Log.d("Tag", "callText : " + callText);
        Log.d("Tag", "conceptText : " + conceptText);
        Log.d("Tag", "debugResult : " + debugResult);
        Log.d("Tag", "emotionResult : " + emotionResult);
        Log.d("Tag", "introSukonbuResult : " + introSukonbuResult);
        Log.d("Tag", "creativityValue : " + creativityValue);

        Log.i("Tag", "Open Ai Api 관련 부분");
        Log.d("Tag", "json : " + json);
        Log.d("Tag", "apiKeyResult : " + apiKeyResult);
        Log.d("Tag", "demoEnable : " + demoEnable);
        Log.d("Tag", "gptApiResult : " + gptApiResult);
        Log.d("Tag", "selectGptValue : " + selectGptValue);
        Log.d("Tag", "selectGpt : " + selectGpt);
        Log.d("Tag", "introOpenAiResult : " + introOpenAiResult);

        Log.d("Tag", "demokey : " + demoKey);


        Log.i("Tag", "어텐션 플리즈");
        Log.d("Tag", "attensionPleaseResult : " + attensionPleaseResult);
        Log.d("Tag", "tutorialResult : " + tutorialResult);
        Log.i("Tag", "////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");


    }


    private String getDefaultDownloadDirectory() {
        return requireActivity().getExternalFilesDir(null) + "/download/";
    }

    private void requestFileDownload(String fileUrl, String downloadDirectory) {

        Request request = new Request.Builder().url(fileUrl).build();
        SettingFragment.CallbackToDownloadFile callback = new SettingFragment.CallbackToDownloadFile(downloadDirectory, getFileNameFrom(URI.create(fileUrl)));
        client.newCall(request).enqueue(callback);
    }

    private String getFileNameFrom(URI uri) {
        return new File(uri.getPath()).getName();
    }

    private class CallbackToDownloadFile implements Callback {

        private final File directory;
        private final File fileToBeDownloaded;

        public CallbackToDownloadFile(String directory, String fileName) {
            this.directory = new File(directory);
            this.fileToBeDownloaded = new File(this.directory.getAbsolutePath() + "/" + fileName);
        }

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            textView.setText("다운로드 중 오류가 발생하였습니다. 다운로드를 다시 시작해 주세요.");
            if (downloadValue == 3) {
                textView.setText("다운로드 서버에 이상이 발생하였습니다. 개발자에게 문의를 시도해 주세요.");
            }
            else {
                downloadValue++;
            }
            e.printStackTrace();
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) {
            if (directory.mkdirs()) {
                Log.d("onresponse", "Directory created successfully");
            }
            else {
                Log.e("onresponse", "Failed to create directory");
            }

            if (fileToBeDownloaded.exists()) {
                if (fileToBeDownloaded.delete()) {
                    Log.d("onresponse", "File deleted successfully");
                }
                else {
                    Log.e("onresponse", "ailed to delete the file");
                }
            }
            else {
                Log.e("onresponse", "File does not exist");
            }

            try {
                if (fileToBeDownloaded.exists()) {
                    System.out.println("File already exists");
                }
                else {
                    if (fileToBeDownloaded.createNewFile()) {
                        System.out.println("File created successfully");
                    }
                    else {
                        System.err.println("Failed to create the file");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                textView.setText("쓰기 권한에 문제가 발생하였습니다. 어플을 재설치해주세요.");
                return;
            }

            getFileNameFrom(URI.create(fileUrl));

            String newFileName = downloadId.get(fileDownloadCount) + ".mp4"; // 원하는 새 파일 이름으로 변경

            File renamedFile = new File(directory.getAbsolutePath() + "/" + newFileName);

            try {
                assert response.body() != null;
                try (InputStream is = response.body().byteStream(); OutputStream os = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) ? Files.newOutputStream(renamedFile.toPath()) : new FileOutputStream(renamedFile)) {

                    final int BUFFER_SIZE = 2048;
                    byte[] data = new byte[BUFFER_SIZE];
                    int count;

                    while ((count = is.read(data)) != -1) {
                        os.write(data, 0, count);
                    }

                    //openToastOnUiThread("다운로드가 완료되었습니다. 파일 이름: " + newFileName);

                    // 다운로드한 파일의 경로를 로그로 출력합니다.
                    String filePath = renamedFile.getAbsolutePath();
                    Log.d("DownloadedFilePath", "다운로드한 파일 경로: " + filePath);
                    downloadPath.add(filePath + ".mp4");

                    fileDownloadCount++;

                    if (fileDownloadCount < downloadLink.size()) {

                        requireActivity().runOnUiThread(() -> {
                            progressBar.setProgress(fileDownloadCount);
                            textView.setText((int) ((float) fileDownloadCount / 28 * 100) + " %");
                        });

                        if (downloadValue == 0) {
                            fileUrl = directDownloadLink + downloadLink.get(fileDownloadCount);
                        }
                        else if (downloadValue == 1) {
                            fileUrl = directDownloadLink + downloadLink2.get(fileDownloadCount);
                        }
                        else if (downloadValue == 2) {
                            fileUrl = directDownloadLink + downloadLink3.get(fileDownloadCount);
                        }
                        String downloadDirectory = getDefaultDownloadDirectory();
                        requestFileDownload(fileUrl, downloadDirectory);
                    }
                    else {
                        requireActivity().runOnUiThread(() -> {
                            progressBar.setProgress(fileDownloadCount);
                            textView.setText((int) ((float) fileDownloadCount / 28 * 100) + " %");


                        });
                        //openToastOnUiThread("모든 다운로드가 완료되었습니다.");
                        requireActivity().runOnUiThread(() -> textView.setText("다운로드가 완료되었습니다."));

                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
                textView.setText("다운로드 중 오류가 발생하였습니다. 다시 시도해 주세요.");
                if (downloadValue == 3) {
                    textView.setText("다운로드 서버에 이상이 발생하였습니다. 개발자에게 문의를 시도해 주세요.");
                }
                else {
                    downloadValue++;
                }

                //openToastOnUiThread("파일 다운로드 중 오류 발생: " + e.getMessage());
            }
        }
    }

    private void downloadLink() {
        downloadId.add("normal_idle");
        downloadId.add("normal_talk");
        downloadId.add("side1");
        downloadId.add("side2");


        downloadId.add("good_idle");
        downloadId.add("good_talk");
        downloadId.add("good1");
        downloadId.add("good2");
        downloadId.add("good3");

        downloadId.add("bad_idle");
        downloadId.add("bad_talk");
        downloadId.add("bad1");
        downloadId.add("bad2");
        downloadId.add("bad3");

        downloadId.add("ex_normal_idle");
        downloadId.add("ex_normal_talk");
        downloadId.add("ex_side1");
        downloadId.add("ex_side2");


        downloadId.add("ex_good_idle");
        downloadId.add("ex_good_talk");
        downloadId.add("ex_good1");
        downloadId.add("ex_good2");
        downloadId.add("ex_good3");

        downloadId.add("ex_bad_idle");
        downloadId.add("ex_bad_talk");
        downloadId.add("ex_bad1");
        downloadId.add("ex_bad2");
        downloadId.add("ex_bad3");


        downloadLink.add("1boIN5kAm730kIdlCLNKIXDE1vPpsujAp");
        downloadLink.add("1tCPjwYsTir1XlgWkvwqhVOXiSddXXsZV");
        downloadLink.add("11caCHSZb93acrapy3MbxeYKmVaPcuI2w");
        downloadLink.add("1hS41axZl5LE5ki-AtijAmrLqqPpZLUwy");


        downloadLink.add("1A0UMt7sB1pzv8oFoigwGMsvszOoNie5Y");
        downloadLink.add("1Le5nobjP2GLUoRv2A9NmQKCXXdjX2WaZ");
        downloadLink.add("1njmA0LLr2qfqdDlCtccEnmoV7yf4siHi");
        downloadLink.add("1n9vZNg-XX7_Xxfi6OG4ly5IrChPut3ci");
        downloadLink.add("1RIH-09M61B6NZK3cW__Bok8kXRm6SYpn");

        downloadLink.add("1Koi5HuHq23szUd1g2N1HU0Ea-Krg6G_m");
        downloadLink.add("1hGcEh2fnhvabrqSc_TDgW-vpOw_ljQS0");
        downloadLink.add("1QMmAoaCDK_uy2UuCA6ijJrUR5EUF-loo");
        downloadLink.add("1FbUppbiA-FlVfBYxj7DgKpi4fPSieQKp");
        downloadLink.add("1VjgZyJ7RlaKEvgEq4x3ZIoj516pDgvaL");


        downloadLink.add("1cR-IpZnhQNe5V4fkm00rdPeZa7I0RYfn");
        downloadLink.add("1-ZoJaug8PYgti94A0oe-FGfMjx6C98E_");
        downloadLink.add("1yLbTe_GfsSyFxCX7D2wl_GnYrvFw5EHV");
        downloadLink.add("11S5VFwGgDm3wVBXYr_TlBDCUbAlgZknc");


        downloadLink.add("1be-m3XJzYfyq41ofLlSWIYSVIv8sOdxG");
        downloadLink.add("1LRco186MI-6cZgNc76YbLB8gvt296uhT");
        downloadLink.add("1_VgJToUYoFzRCvzlOzWW5ifOEqh31tJy");
        downloadLink.add("1F4wpRRzP7gkw_4_yAr-8nUS20nd2otwe");
        downloadLink.add("15i6pETm-RKV0Zu1Jf4mIIstPqMZlfeqQ");

        downloadLink.add("1dtEn9t1QHgQbeTf_AkgrbYtEHn-cRxcF");
        downloadLink.add("1F6IIlJgjDRY0v748JNZtQNASSJBQj0CG");
        downloadLink.add("1fpJVbbRQ3WSMU9qMXTgs8cbfZMV6ZbWl");
        downloadLink.add("1TvrjZvEoKvM2LdjnYfZLqjAa_rWG2u50");
        downloadLink.add("1k9JPbFJUpSgWRz6O4XS8TMPBR0fY8ZeS");

        //2차 다운로드 링크

        downloadLink2.add("1XC9oXXqsmysRyYpqkJac9t4zTUuRs7SI");
        downloadLink2.add("1OzBhmn9AFi7x_laZ3QDxxL5hEBt_m0tf");
        downloadLink2.add("1veJKaf7_PHPubHgVY_mTcViwQEv-PBpB");
        downloadLink2.add("1SS8Ywm4ALlodbFN0iAtboNSjOptwO7VX");


        downloadLink2.add("1rnoPb21HA3fRNPupAPGPvTow4L1rgkff");
        downloadLink2.add("1IXu1psH3tF3k0Spm1t7GQjVeJ0l54Nxu");
        downloadLink2.add("1M93IKk5fPlmFBu7SA5jF9ei9Mj88W_OQ");
        downloadLink2.add("1F-aNKfrTH32a8ipi7Xlm6tTccrPDOjX0");
        downloadLink2.add("1JeuuiCCEuLpQiSb7WCT_JIrM_dK2YjZz");

        downloadLink2.add("1qyZj9jJv0cwSTPyYJ0VVn-4C_5XRozKv");
        downloadLink2.add("1Csrn7-us9btHt__vH1ar9naOB5bUM65I");
        downloadLink2.add("1Iy_y2iQ1UjuuV334N7mY5njmOXPJ4v8S");
        downloadLink2.add("1oSGb2czCO_jLvy7kzHfk6VPdoNBXKEks");
        downloadLink2.add("1g87fvjnwxv7uqKL4uwF6X7F7HIaSuGOv");

        downloadLink2.add("1-eYukLWLR32U-5bQaHT2U_BdO9DCfqiY");
        downloadLink2.add("1u4--1BwimAWPlUPqKVIfMhZx6bEHpdiB");
        downloadLink2.add("12nrs5oC039U1tp-uciXN5wl2uBRvgcuP");
        downloadLink2.add("1xhaj6uat2CcNWNX1zj9I32rmuIckb0q2");


        downloadLink2.add("1taGM3G1IC9DJBR5u5u2DTlucvxoH8yN4");
        downloadLink2.add("1AcMGx9vycVMLYI88oMnUWTI-xXnBpVDR");
        downloadLink2.add("1cAXZxectvWYYSapGwmhcsfzGGX-K0jol");
        downloadLink2.add("1ePdoXfx_wogtUFt75GWt5soGGkSXA1gM");
        downloadLink2.add("1ZBzXdmOmJzeuM2OLCYVPOtMkWkdfIz8p");

        downloadLink2.add("1gn5RKIx2cZCEvFdAxi6HqIZxcRDb-U4X");
        downloadLink2.add("1J_Tu7MjBti8Gr78Zz81CuHK152Y7klh6");
        downloadLink2.add("1DbYvy4xQWQiyXI8sc7RUB8-v_tN2ayBi");
        downloadLink2.add("1VNFIVX-S76gLaQAL0-RKwIEI2cMKaSuE");
        downloadLink2.add("1PFqFP5RYlQ9lLSklqzyaHjuX-oYs8k98");

        //3차 다운로드 링크

        downloadLink3.add("1BmKteDJei_y0G5Erjf8-Qy7PG33aekkY");
        downloadLink3.add("10mWYXK5yBZ4gBj0k0BZF_t0DyVAU9vfz");
        downloadLink3.add("1_LK2MKWrtSKi9ZqU1WDNp9sC-jzdIUme");
        downloadLink3.add("1St1k8XtffmZyBOezAMuFi73enQ5rkZfk");


        downloadLink3.add("1wDrKh2R2IdrrRlJO59zGliKKCad5_Qtw");
        downloadLink3.add("1hrxCDNMQ7wUET1gTeault76wbiPEb7kX");
        downloadLink3.add("1O0hcm3o--rv0n-fJrLkw3E9YDE9oOO-e");
        downloadLink3.add("1A8IzLn1xF18UROLLdbcCu7P7Ue8s02zE");
        downloadLink3.add("1aMkgsrNEdjzX0FMtcQUj14BRsHF43q8Z");

        downloadLink3.add("1dOk3KQYPYUTdbSQ6OYmxPiQfXn4L4njw");
        downloadLink3.add("1igtR80wPsjKpdWsQ7jMYel8arkWFCo4w");
        downloadLink3.add("192qXlWaatqBcnCKD2HjkoHNXaIp-NbiF");
        downloadLink3.add("1TANKjvqEUMsfVmpwROLjRoCYgRJ_Y4ou");
        downloadLink3.add("1RIhoH17prnYKUEfH6MXKuxrwdHMM90Nb");

        downloadLink3.add("1DKCuY8cglyIhmYV_KEgPPFBee-8FodVp");
        downloadLink3.add("1Iv7Tbu2KxbG_rn6F5a9FRgQ5_y--myf8");
        downloadLink3.add("1vximnVG0vj3YyGBQPgeJTw-rW1fSYn_f");
        downloadLink3.add("1mKWnHCzn0K7BRUuYkZfVYXmBRoSqEumx");


        downloadLink3.add("1PjnUbHtBll26J1i-dWl2RQdbB5VVtWwG");
        downloadLink3.add("1uLT7-xGm6dqAfZRN0cEFjMnlnATihzn6");
        downloadLink3.add("18_HUWJHDIVDYxgPAFkDCFkGebQHx40_4");
        downloadLink3.add("1pnD0OaFjPDZ3ppjQdMib2_nQACuL9vYN");
        downloadLink3.add("1hoVTsNpkyq3YRL9rmcGe8PwToCpESz59");

        downloadLink3.add("1-Iqiz0KQdC64qBOA-4jogwM0TZZ1gc4G");
        downloadLink3.add("1BAWS1-iYnti99CI4c07Obe1mA4R37FJB");
        downloadLink3.add("1KyAZsF1y15c0NB_-9Y8ebfaazAi41CrM");
        downloadLink3.add("1wHZjfjzpu40R18AzUoISlb5jxn1pTTJ8");
        downloadLink3.add("1mVMvY9pz510fZS7CsqXW8ZzUXjkJ4tAL");
    }

    private void serverSend() {

        // JSON 객체 생성
        JSONObject json = new JSONObject();
        try {
            json.put("demokey", true); // 답변부분
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // RequestBody 생성
        RequestBody body = RequestBody.create(json.toString(), JSON);

        // Request 생성
        Request request = new Request.Builder().url("https://melissaj.duckdns.org:80/GPT").post(body).build();

        // 비동기 방식으로 요청 전송
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();

                //serverSend(answerData, questionData);
            }

            //실패시 처리방법에 대하여 적어놔라.


            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                // 수신된 JSON 데이터 디버그 로그로 출력
                try {
                    assert response.body() != null;
                    String responseData = response.body().string();
                    Log.d("gpt", "response data : " + responseData);
                    JSONObject receivedJson = new JSONObject(responseData);
                    Log.d("Received JSON", receivedJson.toString());


                    // JSON 문자열을 JSONObject로 변환
                    JSONObject jsonObject = new JSONObject(responseData);

                    // "result" 키의 값을 가져오기
                    String demokey = jsonObject.getString("demokey");
                    Log.d("Received demokey", demokey);

                    //보안된 값 저장 활용

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("demokey", demokey);
                    editor.apply();

                    keyTest(demokey);


                } catch (Exception e) {
                    e.printStackTrace();
                    //서버 수신 에러
                }
            }
        });
    }

}