package com.melissa.sukonbugpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class IntroOpenAiSettingActivity extends AppCompatActivity {

    RelativeLayout introAiLayout;

    Configuration configuration;
    boolean isDarkMode;

    SharedPreferences sharedPreferences;


    Button verificationButton;
    TextView resultApiText;
    TextView selectApiText;
    EditText apiKeyEdit;
    Button demoButton;

    Button introNextButton;
    Button introPrevButton;

    Spinner gptSpinner;


    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient gptclient = new OkHttpClient();

    OkHttpClient client = new OkHttpClient();

    private ArrayList<String> gptSpinnerResult = new ArrayList<>();


    Boolean orientationResult = false;
    Boolean brightAutoResult = false;
    Boolean expansionResult = false;


    Boolean emotionResult = false;

    Boolean debugResult = false;

    Boolean introSukonbuResult = false;

    Boolean introScreenResult = false;

    Boolean bubbleResult = false;


    Resources resources;
    String callText = "";
    String conceptText = "";

    String selectGptValue;

    int brightValue = 0;


    int gptListValue = 0;

    int selectGpt = 0;

    int creativityValue = 7;

    //이번 액티비티에서 담당하게 되는 값
    ArrayList<String> gptList = new ArrayList<>();
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
        setContentView(R.layout.activity_intro_open_ai_setting);

        runOnUiThread(() -> {


            client = new OkHttpClient().newBuilder().connectTimeout(120, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build();

            // MasterKey 생성
            String masterKeyAlias = null;
            try {
                masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            } catch (Exception e) {
                e.printStackTrace();
            }


            assert masterKeyAlias != null;
            Log.d("MasterKeyAlias", masterKeyAlias);

            sharedPreferences = null;
            try {
                sharedPreferences = EncryptedSharedPreferences.create("secret_shared_prefs", masterKeyAlias, getApplicationContext(), EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            } catch (Exception e) {
                e.printStackTrace();
            }


            resources = getResources();
            callText = resources.getString(R.string.defaultCallType);
            conceptText = resources.getString(R.string.defaultConceptText);

            introAiLayout = findViewById(R.id.intro_ai_layout);


            verificationButton = findViewById(R.id.intro_btn_verification);
            apiKeyEdit = findViewById(R.id.intro_edit_api_key);
            resultApiText = findViewById(R.id.intro_text_api_result);
            demoButton = findViewById(R.id.intro_btn_demo);
            introNextButton = findViewById(R.id.intro_btn_next_api_button);
            introPrevButton = findViewById(R.id.intro_btn_prev_api_button);
            gptSpinner = findViewById(R.id.intro_spinner_gpt);
            selectApiText = findViewById(R.id.intro_text_selectapi);


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
            ////최종 확인란에서 저장했던 데이터
            attensionPleaseResult = preferences.getBoolean("attensionPleaseResult", attensionPleaseResult);

            configuration = getResources().getConfiguration();

            isDarkMode = ThemeUtils.isDarkModeEnabled(configuration);


            if (demoEnable) {
                Gson gson = new Gson();
                Type type = new com.google.gson.reflect.TypeToken<ArrayList<String>>() {
                }.getType();
                gptSpinnerResult = gson.fromJson(json, type);
                dynamic_spinner();

                resultApiText.setText("체험판이 활성화 되었습니다.");
                apiKeyEdit.setText("Demo version activated.");
                introNextButton.setEnabled(true);
                introPrevButton.setEnabled(true);
                gptSpinner.setEnabled(true);
            }
            else if (gptApiResult) {
                resultApiText.setText("사용 가능한 GPT 리스트 조회가 완료되었습니다.");
                apiKeyEdit.setText(apiKeyResult);
                introNextButton.setEnabled(true);
                introPrevButton.setEnabled(true);
                gptSpinner.setEnabled(true);
                Gson gson = new Gson();
                Type type = new com.google.gson.reflect.TypeToken<ArrayList<String>>() {
                }.getType();
                gptSpinnerResult = gson.fromJson(json, type);
                dynamic_spinner();

            }
            else {
                introNextButton.setEnabled(false);
                introPrevButton.setEnabled(false);
                gptSpinner.setEnabled(false);
            }


            //검증을 시작하는 버튼
            verificationButton.setOnClickListener(v -> {
                hideKeyboard();
                apiKeyEdit.clearFocus();
                demoEnable = false;
                gptSpinnerResult.clear();
                resultApiText.setText("api key 값을 검증하고 있습니다.");
                apiKeyResult = apiKeyEdit.getText().toString();
                serverVerification(apiKeyResult);
            });


            introNextButton.setOnClickListener(v -> {

                saveShared();
                startActivity(new Intent(IntroOpenAiSettingActivity.this, IntroAttentionPleaseActivity.class));
                finish();
            });

            introPrevButton.setOnClickListener(v -> {

                saveShared();
                startActivity(new Intent(IntroOpenAiSettingActivity.this, IntroSukonbuSettingActivity.class));
                finish();

            });

            //gpt 버전 선택하는 스피너
            gptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TextView textView = (TextView) parent.getChildAt(0);
                    if (textView != null) {
                        if (isDarkMode) {
                            textView.setTextColor(Color.WHITE);
                        }
                        else {
                            textView.setTextColor(Color.BLACK);
                        }
                    }
                    else {
                        Log.e("Error", "TextView is null");
                    }
                    Log.d("gpt", "선택된 아이템 : " + gptSpinner.getItemAtPosition(position));
                    selectGptValue = (String) gptSpinner.getItemAtPosition(position);
                    selectGpt = position;
                    selectApiText.setText("선택 :\n" + selectGptValue);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Log.d("gpt", "아무것도 선택 안됨");

                }
            });

            //체험판 버튼을 눌렀을때 경고처리.
            demoButton.setOnClickListener(v -> showAlertDialog());


            introAiLayout.setOnTouchListener((v, event) -> {

                hideKeyboard();
                apiKeyEdit.clearFocus();
                return false;
            });
        });

    }

    private void saveShared() {

        introOpenAiResult = true;
        //gptSpinnerResult
        //apiKeyResult
        //demoEnable

        Gson gson = new Gson();
        json = gson.toJson(gptSpinnerResult);


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
        //최종 확인란에서 저장했던 데이터
        editor.putBoolean("attensionPleaseResult", attensionPleaseResult);

        editor.putInt("selectGpt", selectGpt);

        editor.apply();

    }

    @SuppressLint("SetTextI18n")
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("주의!");
        builder.setMessage("체험판을 사용하시겠습니까?\n체험판은 총 50회의 메세지를 주고받을 수 있으며 그 이후로는 key를 따로 발급받으셔야 합니다.");

        builder.setPositiveButton("예", (dialog, which) -> {
            // "예" 버튼을 눌렀을 때의 처리
            handleYesButtonClick(true);
            introNextButton.setEnabled(true);
            introPrevButton.setEnabled(true);

            resultApiText.setText("체험판이 활성화 되었습니다. 검증까지 기다려주세요.");
            apiKeyEdit.setText("Demo version activated.");

            serverSend();
        });

        builder.setNegativeButton("아니오", (dialog, which) -> {
            // "아니오" 버튼을 눌렀을 때의 처리
            handleYesButtonClick(false);
            introNextButton.setEnabled(false);
            introPrevButton.setEnabled(false);
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void handleYesButtonClick(boolean result) {
        Log.d("gpt", "결과값 : " + result);
        demoEnable = result;
        // 여기에서 result 값을 사용하여 처리
        // result가 true면 "예"를 선택한 경우, false면 "아니오"를 선택한 경우
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
            gptclient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d("gpt", "Failed to load response due to onFailure " + e.getMessage());
                    resultApiText.setText("조회에 실패하였습니다. 다시 시도해주세요.\n하단 리스트가 갱신되었으면 그대로 진행 해 주세요.");
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
                                runOnUiThread(() -> {
                                    // UI 업데이트 코드를 여기에 작성
                                    // 예: 텍스트뷰 업데이트
                                    Log.d("gpt", "Count End");
                                    resultApiText.setText("사용 가능한 GPT 리스트 조회가 완료되었습니다.");
                                    introNextButton.setEnabled(true);
                                    introPrevButton.setEnabled(true);
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

                            runOnUiThread(() -> {
                                // UI 업데이트 코드를 여기에 작성
                                // 예: 텍스트뷰 업데이트
                                if (errorCode.contains("invalid_api_key")) {
                                    resultApiText.setText("Error Code: " + errorCode);
                                    introNextButton.setEnabled(false);
                                    introPrevButton.setEnabled(false);
                                    gptSpinner.setEnabled(false);
                                    demoButton.setEnabled(true);
                                }
                                else {
                                    resultApiText.setText("Error Code: " + errorCode);
                                    gptListValue = gptListValue + 1;
                                    if (gptListValue == 2) {
                                        Log.d("gpt", "Count End");
                                        resultApiText.setText("사용 가능한 GPT 리스트 조회가 완료되었습니다.");
                                        introNextButton.setEnabled(true);
                                        introPrevButton.setEnabled(true);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, gptSpinnerResult);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gptSpinner.setAdapter(adapter);
        adapter.notifyDataSetChanged(); // 어댑터의 데이터 변경 후에 호출
    }

    void hideKeyboard() {
        View currentFocus = getCurrentFocus();

        if (currentFocus != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
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

    private void serverVerification(String apiKeyResult) {

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

                    keyTest(apiKeyResult);


                } catch (Exception e) {
                    e.printStackTrace();
                    //서버 수신 에러
                }
            }
        });
    }

}