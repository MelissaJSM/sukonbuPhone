package com.melissa.sukonbugpt;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TutorialFragment extends Fragment {

    MainActivity mainActivity; //(액티비티에서 이동하기) 주 가되는 메인액티비티 선언
    SharedPreferences sharedPreferences;

    private DataTransmissionListener dataTransmissionSetListener; // 값 전송용 변수 선언

    ImageView tutorialImg;
    ImageButton tutorialExit;
    ImageButton tutorialLeft;
    ImageButton tutorialRight;

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
    Boolean permissionResult = false;
    Boolean attensionPleaseResult = false;
    Boolean screenDarkmode = false;

    Boolean bubbleResult = false;

    Boolean tutorialResult = false;

    String callText = "";
    String conceptText = "";
    String selectGptValue = "";
    int brightValue = 0;
    //이번 액티비티에서 담당하게 되는 값
    int selectGpt = 0;


    int demoCount = 0;


    int creativityValue = 7;

    int tutorialValue = 0;


    String apiKeyResult = "";

    String json = "";

    String demoKey = "";


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
            FragmentManager manager = requireActivity().getSupportFragmentManager();
            manager.beginTransaction().remove(TutorialFragment.this).commit();
            manager.popBackStack();
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflaterTutorial = inflater.inflate(R.layout.activity_tutorial_fragment, container, false); // inflater 때문에 선언을 추가해야함



        tutorialImg = inflaterTutorial.findViewById(R.id.tutorial_img);
        tutorialExit = inflaterTutorial.findViewById(R.id.button_tutorial_exit);
        tutorialLeft = inflaterTutorial.findViewById(R.id.button_tutorial_left);
        tutorialRight = inflaterTutorial.findViewById(R.id.button_tutorial_right);

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


        ArrayList<Integer> tutorialImages = new ArrayList<>();
        tutorialImages.add(R.drawable.tutorial_chat);
        tutorialImages.add(R.drawable.tutorial_vision);
        tutorialImages.add(R.drawable.tutorial_expansion);
        tutorialImages.add(R.drawable.tutorial_touch);

        loadShared();


        tutorialExit.setOnClickListener(v ->{
            tutorialResult = true;
            saveShared();
            dataTransmissionSetListener.settingTransmissionSet();
            inflaterTutorial.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out));
            FragmentManager manager = requireActivity().getSupportFragmentManager();
            manager.beginTransaction().remove(TutorialFragment.this).commit();
            manager.popBackStack();
        });
        tutorialLeft.setOnClickListener(v ->{
            tutorialValue--;
            if(tutorialValue==0){
                tutorialLeft.setVisibility(View.INVISIBLE);
            }
            else{
                tutorialLeft.setVisibility(View.VISIBLE);
                tutorialRight.setVisibility(View.VISIBLE);
            }
            tutorialImg.setImageResource(tutorialImages.get(tutorialValue));
        });

        tutorialRight.setOnClickListener(v ->{
            tutorialValue++;
            if(tutorialValue==3){
                tutorialRight.setVisibility(View.INVISIBLE);
            }
            else{
                tutorialLeft.setVisibility(View.VISIBLE);
                tutorialRight.setVisibility(View.VISIBLE);
            }
            tutorialImg.setImageResource(tutorialImages.get(tutorialValue));
        });




        return inflaterTutorial;
    }


    private void saveShared() {


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
        editor.putInt("creativityValue", creativityValue);
        //OPEN AI 세팅에서 저장했던 데이터
        editor.putBoolean("introOpenAiResult", introOpenAiResult);
        editor.putString("gptSpinnerResult", json);
        editor.putString("apiKeyResult", apiKeyResult);
        editor.putBoolean("demoEnable", demoEnable);
        editor.putBoolean("gptApiResult", gptApiResult);
        editor.putString("selectGptValue", selectGptValue);
        editor.putInt("selectGpt", selectGpt);
        //최종 확인란에서 저장했던 데이터
        editor.putBoolean("attensionPleaseResult", attensionPleaseResult);
        //튜토리얼 진행 확인란
        editor.putBoolean("tutorialResult", tutorialResult);

        //데모 카운트 저장
        editor.putInt("demoCount", demoCount);

        Log.d("save", "attensionPleaseResult : 한번더 확인 : " + attensionPleaseResult);
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
        creativityValue = preferences.getInt("creativityValue", creativityValue);
        //OpenAi Api 에서 저장했던 데이터
        introOpenAiResult = preferences.getBoolean("introOpenAiResult", introOpenAiResult);
        json = preferences.getString("gptSpinnerResult", json);
        apiKeyResult = preferences.getString("apiKeyResult", apiKeyResult);
        demoEnable = preferences.getBoolean("demoEnable", demoEnable);
        gptApiResult = preferences.getBoolean("gptApiResult", gptApiResult);
        selectGptValue = preferences.getString("selectGptValue", selectGptValue);
        selectGpt = preferences.getInt("selectGpt", selectGpt);
        demoCount = preferences.getInt("demoCount", demoCount);

        //최종 확인란에서 저장했던 데이터
        attensionPleaseResult = preferences.getBoolean("attensionPleaseResult", attensionPleaseResult);

        demoKey = sharedPreferences.getString("demokey", "");

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
        Log.d("Tag", "introOpenAiResult : " + introOpenAiResult);

        Log.i("Tag", "어텐션 플리즈");
        Log.d("Tag", "attensionPleaseResult : " + attensionPleaseResult);
        Log.d("Tag", "demokey : " + demoKey);
        Log.d("Tag", "demoCount : " + demoCount);

        Log.i("Tag", "////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");


    }

}