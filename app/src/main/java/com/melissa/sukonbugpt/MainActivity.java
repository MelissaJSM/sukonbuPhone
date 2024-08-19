package com.melissa.sukonbugpt;

import static android.os.SystemClock.sleep;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import android.content.ContentResolver;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import okhttp3.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

public class MainActivity extends AppCompatActivity implements DataTransmissionListener, RecognitionListener {

    private static final int REQUEST_RECORD_PERMISSION = 100;
    RelativeLayout fullBackground;
    RelativeLayout bubbleLayout;
    //tts 서버와 gpt 서버 선언
    private OkHttpClient client = new OkHttpClient();

    private OkHttpClient gptclient = new OkHttpClient();

    private ImageSelector imageSelector;


    //파싱할 JSON 선언
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    DBManager dbManager;


    //모든 작업 끝난 후 TTS 재생
    MediaPlayer mediaPlayer;
    MediaPlayer callMediaPlayer;


    //엑소플레이어 처리
    private ExoPlayer exoPlayer;
    private PlayerView playerView;

    private ProgressBar progressBar;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;

    private CountDownTimer thirdStandbyTimer;
    CountDownTimer tenStanbyTimer;

    Toast currentToast = null;


    private final ArrayList<String> callList = new ArrayList<>();

    // 첫 번째 재생목록을 추가합니다.
    List<String> playlistNormal = new ArrayList<>();
    // 두 번째 재생목록을 추가합니다.
    List<String> playlistGood = new ArrayList<>();
    // 두 번째 재생목록을 추가합니다.
    List<String> playlistBad = new ArrayList<>();

    List<String> oldDataValue = new ArrayList<>();

    //업로드용
    List<Bitmap> resizedImages;

    //사전 정의된 데이터들
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

    Boolean repeat = false;
    Boolean callResult = false;

    Boolean callNext = false;

    Boolean isFabEnabled = false;

    Boolean bubbleResult = false;

    Boolean tutorialResult = false;

    Resources resources;
    String callText = "";
    String conceptText = "";
    String selectGptValue = "";
    int brightValue = 0;
    //이번 액티비티에서 담당하게 되는 값
    int selectGpt = 0;

    int brightCount = 0;

    int demoCount = 0;

    int emotionCount = 0;

    int currentIndex = 0;
    int creativityValue = 7;

    int invalidCount = 0;


    String apiKeyResult = "";

    String json = "";

    String overText = "";

    String bubbleData = "";


    //버튼(추후 이미지버튼으로 교체)
    SwipableButton ttsButton;

    int swipeValue = 0;

    // 이미지 리소스를 저장할 ArrayList 선언
    private ArrayList<Integer> fabImages;
    // 현재 이미지 인덱스를 추적하는 변수
    Boolean speechLock = false;


    Boolean uploadLock = false;


    TextView demoCountText;
    TextView bubbleText;

    ImageButton uploadButton;

    ImageView uploadSample;
    LinearLayout uploadLayout;
    ImageButton uploadDelete;
    Button uploadAdd;


    private FloatingActionButton fab_main;
    private FloatingActionButton fab_sub1; // 로그 기록 확인
    private FloatingActionButton fab_sub2; // 토큰 초기화
    private FloatingActionButton fab_sub3; // 세팅 메뉴 진입
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;

    //에딧텍스트
    EditText chatText;

    //텍스트뷰


    ImageButton headTouch;
    ImageButton chestTouch;
    ImageButton bottomTouch;

    ImageButton expansionHeadTouch;
    ImageButton expansionChestTouch;


    LinearLayout normalEmotion;
    LinearLayout expantionEmotion;

    //추후 서버에서 받아오도록 시킬예정
    String apiKey = "";

    String demoKey = "";

    SharedPreferences sharedPreferences;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Handler bubbleHandler = new Handler();

    private final Runnable hideSystemUiRunnable = this::hideSystemUi;






    @SuppressLint({"ClickableViewAccessibility", "InvalidWakeLockTag", "HandlerLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //내장 데이터베이스 불러오는 코드
        dbManager = new DBManager(this);


        //statusbar 와 네비게이션바 를 투명하게 만드는 코드
        Window window = getWindow();

        // 투명한 상태 바
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


        // 투명한 네비게이션 바
        window.setNavigationBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        enableImmersiveMode();


        //호출과 텍스트 기본값 초기값
        resources = getResources();
        callText = resources.getString(R.string.defaultCallType);
        conceptText = resources.getString(R.string.defaultConceptText);

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
            sharedPreferences = EncryptedSharedPreferences.create("secret_shared_prefs", masterKeyAlias, getApplicationContext(), EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //저장된 값 불러오기
        //loadShared();


        fullBackground = findViewById(R.id.full_background);
        bubbleLayout = findViewById(R.id.sukonbu_text_layout);


        //애니메이션 초기설정
        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        //레이아웃과 연결
        ttsButton = findViewById(R.id.tts_button);

        chatText = findViewById(R.id.textGPT);


        playerView = findViewById(R.id.playerView);

        //floatingActionButon 설정
        fab_main = findViewById(R.id.fab_main);
        fab_sub1 = findViewById(R.id.fab_sub1);
        fab_sub2 = findViewById(R.id.fab_sub2);
        fab_sub3 = findViewById(R.id.fab_sub3);

        headTouch = findViewById(R.id.touch_head);
        chestTouch = findViewById(R.id.touch_chest);
        bottomTouch = findViewById(R.id.touch_bottom);

        expansionHeadTouch = findViewById(R.id.touch_head_expansion);
        expansionChestTouch = findViewById(R.id.touch_chest_expansion);

        progressBar = findViewById(R.id.progressBar1);

        demoCountText = findViewById(R.id.text_demo_count);

        normalEmotion = findViewById(R.id.normal_emotion);
        expantionEmotion = findViewById(R.id.expansion_emotion);

        bubbleText = findViewById(R.id.bubble_text);

        uploadButton = findViewById(R.id.btn_upload);

        uploadSample = findViewById(R.id.upload_sample);
        uploadLayout = findViewById(R.id.upload_layout);
        uploadDelete = findViewById(R.id.btn_upload_delete);
        uploadAdd = findViewById(R.id.btn_upload_add);


        imageSelector = new ImageSelector();

        resizedImages = new ArrayList<>();


        //클라이언트 초기설정
        gptclient = new OkHttpClient().newBuilder().connectTimeout(120, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build();
        client = new OkHttpClient().newBuilder().connectTimeout(120, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build();

        //미디어플레이어 초기설정
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());

        callMediaPlayer = new MediaPlayer();
        callMediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());


        // ExoPlayer를 초기화합니다.
        exoPlayer = new ExoPlayer.Builder(this).build();

        fabImages = new ArrayList<>();
        fabImages.add(R.drawable.button_send);
        fabImages.add(R.drawable.img_fabone);
        fabImages.add(R.drawable.img_faball);


        //호출음을 초기화한다.


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        settingTransmissionSet();




        ttsButton.setOnSwipeListener(new SwipableButton.OnSwipeListener() {
            @Override
            public void onSwipeUp() {
                if (!speechLock && !uploadLock) {
                    swipeCheck(true);


                    // 애니메이션을 설정합니다.
                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_translate_up_1);

                    // 애니메이션 리스너를 설정하여 애니메이션이 끝나는 시점을 감지합니다.
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            // 애니메이션이 끝나면 오른쪽으로 스와이프하는 애니메이션을 실행합니다.
                            ttsButton.setImageResource(fabImages.get(swipeValue));
                            ttsButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                            Animation animationRight = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_translate_up_2);
                            ttsButton.startAnimation(animationRight);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    // 애니메이션을 시작합니다.
                    ttsButton.startAnimation(animation);
                }

            }

            @Override
            public void onSwipeDown() {
                if (!speechLock && !uploadLock) {
                    swipeCheck(false);

                    // 애니메이션을 설정합니다.
                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_translate_down_1);

                    // 애니메이션 리스너를 설정하여 애니메이션이 끝나는 시점을 감지합니다.
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            // 애니메이션이 끝나면 오른쪽으로 스와이프하는 애니메이션을 실행합니다.
                            ttsButton.setImageResource(fabImages.get(swipeValue));
                            ttsButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                            Animation animationRight = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_translate_down_2);
                            ttsButton.startAnimation(animationRight);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    // 애니메이션을 시작합니다.
                    ttsButton.startAnimation(animation);
                }


            }

            @Override
            public void onSingleTap() {
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.button_press_animation);
                ttsButton.startAnimation(animation);
                //tts 생성용 소스코드
                //serverSend("다시 안들어오네... 슬립모드로 전환하는니에.","오류 지정");


                if (swipeValue == 0) {
                    //일반 전송


                    String textData = String.valueOf(chatText.getText());
                    Log.d("textdata", "textData : " + textData);


                    if (textData.isEmpty()) {
                        Log.d("textdata", "null 값 진입으로 인한 오류발생");
                    }
                    else {

                        hideKeyboard();
                        chatText.clearFocus();


                        callMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.tts_send);

                        callMediaPlayer.setOnPreparedListener(mp -> {

                            callNext = true;
                            if (emotionCount >= 3) {
                                sukonbuPlay(String.valueOf(playlistGood.get(1)));
                            }
                            else if (emotionCount <= -3) {
                                sukonbuPlay(String.valueOf(playlistBad.get(1)));
                            }
                            else {
                                sukonbuPlay(String.valueOf(playlistNormal.get(1)));
                            }

                            callMediaPlayer.start();

                            if (bubbleResult) {
                                runOnUiThread(() -> bubbleLayout.setVisibility(View.VISIBLE));
                                runOnUiThread(() -> bubbleText.setText("잘 알아들었어요. 잠시만 기다려 주시겠어요?"));
                            }


                            callMediaPlayer.setOnCompletionListener(mp1 -> {
                                // TODO Auto-generated method stub

                                if (emotionCount >= 3) {
                                    sukonbuPlay(String.valueOf(playlistGood.get(0)));
                                }
                                else if (emotionCount <= -3) {
                                    sukonbuPlay(String.valueOf(playlistBad.get(0)));
                                }
                                else {
                                    sukonbuPlay(String.valueOf(playlistNormal.get(0)));
                                }

                                callMediaPlayer.pause();
                                callMediaPlayer.reset();
                                thirdStandbyGone();
                            });
                        });


                        if (resizedImages.isEmpty()) {
                            gptSend(textData);
                        }
                        else {
                            uploadImagesToServer(textData);
                        }

                        chatText.setText(null);
                        chatText.setHint(null);

                        if (debugResult) {
                            runOnUiThread(() -> chatText.setHint("데이터를 전송 요청 하였습니다."));
                        }

                    }

                }
                else if (swipeValue == 1) {
                    //1회 전송
                    repeat = false;
                    start();
                    progressBar.setIndeterminate(true);
                    progressBar.setVisibility(View.VISIBLE);

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_PERMISSION);

                }
                else if (swipeValue == 2) {

                    if (!speechLock) {
                        showAlertDialog();
                    }
                    else {
                        speechLock = false;
                        repeat = false;
                        callNext = false;
                        //다중 전송
                        //원래는 여기에 경고알림 넣는거 알지?
                        turnOf();
                        progressBar.setVisibility(View.INVISIBLE);
                        ttsButton.setImageResource(R.drawable.img_faball);
                    }


                }



            }
        });

        uploadDelete.setOnClickListener(V -> {
            resizedImages.clear();
            uploadLayout.setVisibility(View.GONE);
            resizedImages.clear();

            uploadLock = false;
        });

        //업로드 이미지 버튼 (까지)
        uploadButton.setOnClickListener(v -> imageSelector.selectImages(MainActivity.this));

        //floatingActionButton 동작
        fab_main.setOnClickListener(v -> toggleFab());

        fab_sub1.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction;
            LogFragment LogFragment = new LogFragment();

            transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_in);
            transaction.replace(R.id.main_background, LogFragment).commit();
            toggleFab();

        });
        fab_sub2.setOnClickListener(v -> {
            showTokenAlertDialog();
            toggleFab();
        });
        fab_sub3.setOnClickListener(v -> {
            //액티비티에서 값 전송하기
            saveShared();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction;
            SettingFragment SettingFragment = new SettingFragment();

            transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_in);
            transaction.replace(R.id.main_background, SettingFragment).commit();
            toggleFab();
        });


        fullBackground.setOnTouchListener((v, event) -> {

            hideKeyboard();
            chatText.clearFocus();

            return false;
        });

        //머리를  터치했을때 ++
        headTouch.setOnClickListener(v -> {


            Random random = new Random();

            // 2 또는 3 중 하나를 랜덤하게 선택
            int randomNumber = random.nextInt(2) + 2; // 0 또는 1을 더해 2 또는 3을 얻음


            if (emotionCount <= 2) {
                //2 3 4 일때
                //그러나 카운트는 0 1 2 3 으로올라가지.
                sukonbuPlay(String.valueOf(playlistGood.get(randomNumber)));
                emotionCount++;
                overText = "";
            }
            else if (emotionCount == 3) {
                sukonbuPlay(String.valueOf(playlistGood.get(4)));
                overText = " 그리고 뒷부분의 내용을 바탕으로 말투를 반드시 변경합니다. : 대화하는 분이 당신을 매우 기쁘게 해주었기때문에 사랑에 빠진 소녀가 되었습니다. 말투에서 사랑이 듬뿍 느껴지도록 합니다.";
            }


        });

        //가슴을  터치했을때 -
        chestTouch.setOnClickListener(v -> {


            Random random = new Random();

            // 2 또는 3 중 하나를 랜덤하게 선택
            int randomNumber = random.nextInt(2) + 2; // 0 또는 1을 더해 2 또는 3을 얻음


            if (emotionCount >= -2) {
                //2 3 4 일때
                //그러나 카운트는 0 1 2 3 으로올라가지.
                sukonbuPlay(String.valueOf(playlistNormal.get(randomNumber)));
                emotionCount--;
                overText = "";
            }
            else if (emotionCount == -3) {
                sukonbuPlay(String.valueOf(playlistBad.get(4)));
                overText = " 그리고 뒷부분의 내용을 바탕으로 말투를 반드시 변경합니다. :  대화하는 분이 당신을 매우 괴롭혔기 때문에 매우 화난 소녀가 되었습니다. 말투도 화가 난 상태로 합니다.";
            }


        });

        //그부분을  터치했을때 -
        bottomTouch.setOnClickListener(v -> {


            Random random = new Random();

            // 2 또는 3 중 하나를 랜덤하게 선택
            int randomNumber = random.nextInt(2) + 2; // 0 또는 1을 더해 2 또는 3을 얻음


            if (emotionCount >= -2) {
                //2 3 4 일때
                //그러나 카운트는 0 1 2 3 으로올라가지.
                sukonbuPlay(String.valueOf(playlistBad.get(randomNumber)));
                emotionCount--;
            }
            else if (emotionCount == -3) {
                sukonbuPlay(String.valueOf(playlistBad.get(4)));
            }


        });


        //머리를  터치했을때 ++
        expansionHeadTouch.setOnClickListener(v -> {


            Random random = new Random();

            // 2 또는 3 중 하나를 랜덤하게 선택
            int randomNumber = random.nextInt(2) + 2; // 0 또는 1을 더해 2 또는 3을 얻음


            if (emotionCount <= 2) {
                //2 3 4 일때
                //그러나 카운트는 0 1 2 3 으로올라가지.
                sukonbuPlay(String.valueOf(playlistGood.get(randomNumber)));
                emotionCount++;
                overText = "";
            }
            else if (emotionCount == 3) {
                sukonbuPlay(String.valueOf(playlistGood.get(4)));
                overText = " 그리고 뒷부분의 내용을 바탕으로 말투를 반드시 변경합니다. : 대화하는 분이 당신을 매우 기쁘게 해주었기때문에 사랑에 빠진 소녀가 되었습니다. 말투에서 사랑이 듬뿍 느껴지도록 합니다.";
            }


        });

        //가슴을  터치했을때 -
        expansionChestTouch.setOnClickListener(v -> {


            Random random = new Random();

            // 2 또는 3 중 하나를 랜덤하게 선택
            int randomNumber = random.nextInt(2) + 2; // 0 또는 1을 더해 2 또는 3을 얻음


            if (emotionCount >= -2) {
                //2 3 4 일때
                //그러나 카운트는 0 1 2 3 으로올라가지.
                sukonbuPlay(String.valueOf(playlistNormal.get(randomNumber)));
                emotionCount--;
                overText = "";
            }
            else if (emotionCount == -3) {
                sukonbuPlay(String.valueOf(playlistBad.get(4)));
                overText = " 그리고 뒷부분의 내용을 바탕으로 말투를 반드시 변경합니다. :  대화하는 분이 당신을 매우 괴롭혔기 때문에 매우 화난 소녀가 되었습니다. 말투도 화가 난 상태로 합니다.";
            }


        });


    } // oncreate end


    @Override
    public void onReadyForSpeech(Bundle bundle) {
        Log.i("speechrecognize", "onReadyForSpeech");

    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i("speechrecognize", "onBeginningOfSpeech");
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        //Log.i("speechrecognize", "onRmsChanged: " + rmsdB);
        progressBar.setProgress((int) rmsdB);

    }

    @Override
    public void onBufferReceived(byte[] bytes) {
        Log.i("speechrecognize", "onBufferReceived: " + Arrays.toString(bytes));

    }

    @Override
    public void onEndOfSpeech() {
        Log.i("speechrecognize", "onEndOfSpeech");
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d("speechrecognize", "FAILED " + errorMessage);
        chatText.setHint(errorMessage);
        speech.startListening(recognizerIntent);

    }

    @Override
    public void onResults(Bundle results) {
        Log.i("speechrecognize", "onResults");
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        if (matches != null && !matches.isEmpty()) {
            String mostAccurateResult = matches.get(0);
            Log.d("speechRecognize", mostAccurateResult);

            //음성인식 제대로된건지 테스트용
            if (repeat) {
                for (int i = 0; i < callList.size(); i++) {
                    if (mostAccurateResult.contains(callList.get(i))) {
                        callResult = true;
                        Log.d("speechRecognize", "onResults: 호출음 인식 성공");
                    }
                }
            }


            //반복 모드인데 음성인식 성공
            if (repeat && callResult && !callNext) {


                callMediaPlayer = MediaPlayer.create(this, R.raw.call);

                callMediaPlayer.setOnPreparedListener(mp -> {
                    callNext = true;
                    if (emotionCount >= 3) {
                        sukonbuPlay(String.valueOf(playlistGood.get(1)));
                    }
                    else if (emotionCount <= -3) {
                        sukonbuPlay(String.valueOf(playlistBad.get(1)));
                    }
                    else {
                        sukonbuPlay(String.valueOf(playlistNormal.get(1)));
                    }

                    callMediaPlayer.start();
                    turnOf();
                    if (bubbleResult) {
                        runOnUiThread(() -> bubbleLayout.setVisibility(View.VISIBLE));
                        runOnUiThread(() -> bubbleText.setText("네? 부르셨나요?"));
                    }

                    callMediaPlayer.setOnCompletionListener(mp1 -> {
                        // TODO Auto-generated method stub

                        if (emotionCount >= 3) {
                            sukonbuPlay(String.valueOf(playlistGood.get(0)));
                        }
                        else if (emotionCount <= -3) {
                            sukonbuPlay(String.valueOf(playlistBad.get(0)));
                        }
                        else {
                            sukonbuPlay(String.valueOf(playlistNormal.get(0)));
                        }

                        callMediaPlayer.pause();
                        callMediaPlayer.reset();

                        thirdStandbyGone();
                        tenStandbyGone();

                        start();
                        speech.startListening(recognizerIntent);
                        progressBar.setIndeterminate(true);
                        progressBar.setVisibility(View.VISIBLE);
                        callResult = false;
                    });
                });

            }
            //반복 모드도 아니고 음성인식 호출도 안쓰는 경우
            else if (!repeat && !callResult && !callNext) {
                chatText.setHint(mostAccurateResult);
                turnOf();

                //여기에도!
                callMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.tts_send);

                callMediaPlayer.setOnPreparedListener(mp -> {

                    callNext = true;
                    if (emotionCount >= 3) {
                        sukonbuPlay(String.valueOf(playlistGood.get(1)));
                    }
                    else if (emotionCount <= -3) {
                        sukonbuPlay(String.valueOf(playlistBad.get(1)));
                    }
                    else {
                        sukonbuPlay(String.valueOf(playlistNormal.get(1)));
                    }

                    callMediaPlayer.start();

                    if (bubbleResult) {
                        runOnUiThread(() -> bubbleLayout.setVisibility(View.VISIBLE));
                        runOnUiThread(() -> bubbleText.setText("잘 알아들었어요. 잠시만 기다려 주시겠어요?"));
                    }


                    callMediaPlayer.setOnCompletionListener(mp1 -> {
                        // TODO Auto-generated method stub

                        if (emotionCount >= 3) {
                            sukonbuPlay(String.valueOf(playlistGood.get(0)));
                        }
                        else if (emotionCount <= -3) {
                            sukonbuPlay(String.valueOf(playlistBad.get(0)));
                        }
                        else {
                            sukonbuPlay(String.valueOf(playlistNormal.get(0)));
                        }

                        callMediaPlayer.pause();
                        callMediaPlayer.reset();
                        gptSend(mostAccurateResult);
                        thirdStandbyGone();
                    });
                });

            }
            //자동 인식에서 호출된 이후의 명령어
            else if (repeat && callNext) {
                if (tenStanbyTimer != null) {
                    tenStanbyTimer.cancel();
                }
                callNext = false;
                chatText.setHint(mostAccurateResult);
                turnOf();

                //여기에 잠시만 기다리라는 그 명령어 넣으면됨.


                callMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.tts_send);

                callMediaPlayer.setOnPreparedListener(mp -> {

                    callNext = true;
                    if (emotionCount >= 3) {
                        sukonbuPlay(String.valueOf(playlistGood.get(1)));
                    }
                    else if (emotionCount <= -3) {
                        sukonbuPlay(String.valueOf(playlistBad.get(1)));
                    }
                    else {
                        sukonbuPlay(String.valueOf(playlistNormal.get(1)));
                    }

                    callMediaPlayer.start();

                    if (bubbleResult) {
                        runOnUiThread(() -> bubbleLayout.setVisibility(View.VISIBLE));
                        runOnUiThread(() -> bubbleText.setText("잘 알아들었어요. 잠시만 기다려 주시겠어요?"));
                    }


                    callMediaPlayer.setOnCompletionListener(mp1 -> {
                        // TODO Auto-generated method stub

                        if (emotionCount >= 3) {
                            sukonbuPlay(String.valueOf(playlistGood.get(0)));
                        }
                        else if (emotionCount <= -3) {
                            sukonbuPlay(String.valueOf(playlistBad.get(0)));
                        }
                        else {
                            sukonbuPlay(String.valueOf(playlistNormal.get(0)));
                        }

                        callMediaPlayer.pause();
                        callMediaPlayer.reset();
                        gptSend(mostAccurateResult);
                        callResult = false;
                        callNext = false;
                        thirdStandbyGone();
                    });
                });
            }
            else {
                speech.startListening(recognizerIntent);
            }


        }


    }

    @Override
    public void onPartialResults(Bundle results) {
        Log.i("speechrecognize", "onPartialResults");
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        StringBuilder text = new StringBuilder();
        assert matches != null;
        for (String result : matches)
            text.append(result).append("\n");
        Log.i("speechrecognize", "onPartialResults=" + text);

    }

    @Override
    public void onEvent(int i, Bundle bundle) {
        Log.i("speechrecognize", "onEvent");

    }

    public String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                turnOf();
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (exoPlayer != null) {
            exoPlayer.release();
        }
        if (callMediaPlayer != null) {
            callMediaPlayer.release();
            callMediaPlayer = null;
        }
        if (bubbleHandler != null) {
            bubbleHandler.removeCallbacksAndMessages(null);
            currentIndex = 0;
        }


    }


    @SuppressLint("SetTextI18n")
    private void gptSend(String textData) {

        if (demoEnable && demoCount > 50) {

            demoErrorVoice();
            demoCountText.setVisibility(View.VISIBLE);
            demoCountText.setText("데모 사용 횟수를 초과하였습니다.");
            chatText.setHint("데모 사용 횟수를 초과하였습니다.");
            return;


        }
        else if (demoEnable) {
            demoCountText.setVisibility(View.VISIBLE);
        }
        else {
            demoCountText.setVisibility(View.INVISIBLE);
        }


        ////////////////////////////////////////////////////////////////////////////////////////////

        //okhttp
        //추가된 내용
        JSONArray arr = new JSONArray();
        JSONObject baseAi = new JSONObject();
        //JSONObject userMsg = new JSONObject();
        try {

            if (debugResult) {
                runOnUiThread(() -> chatText.setHint("GPT 에 보낼 메세지 준비"));
            }

            //AI 속성설정
            baseAi.put("role", "system");
            baseAi.put("content", "이제부터 당신은 다음과 같은 역할을 맡아 대화를 진행합니다:\n" + conceptText + overText);


            //테스트 해보자구
            dbManager.open();

            // dbManager.deleteAllData(); 전체 삭제
            // 데이터 삽입

            //머리가 안돌고있어서 주석으로 남겨놓는다. textData 는 바로 작업이 가능해. 대신 assistant 작업은 내려가서 오픈하고 닫아줘.
            dbManager.insert("{\"role\":\"user\",\"content\":\"" + textData + "\"}");

            //dbManager.insert("Jane,Doe"); // 띄어쓰기를 쉼표로 대체

            oldDataValue.clear();
            // 데이터 조회
            oldDataValue = dbManager.getAllData();

            // 데이터베이스 닫기
            dbManager.close();

            //유저 메세지
            //userMsg.put("role", "user");
            //userMsg.put("content",finalSentence +", "+ textData);


            //Log.d("지금 보내는 메세지 입력사항", "gptSend: " + userMsg);


            //array로 담아서 한번에 보낸다
            arr.put(baseAi);
            for (int i = 0; i < oldDataValue.size(); i++) {
                JSONObject finalSentenceObject = new JSONObject(oldDataValue.get(i));
                arr.put(finalSentenceObject);
            }

        } catch (JSONException e) {
            gptErrorVoice("else");
            if (debugResult) {
                runOnUiThread(() -> chatText.setHint("GPT 에 보낼 JSON 작업에 실패하였습니다."));
            }


            throw new RuntimeException(e);


        }

        JSONObject object = new JSONObject();
        try {
            if (debugResult) {
                runOnUiThread(() -> chatText.setHint("GPT 서버에 모델과 메세지 전송 준비"));
            }

            //모델명 변경
            //모델 리스트
            // gpt-3.5-turbo
            // gpt-3.5-turbo-1106
            // gpt-4
            // gpt-4-1106-preview
            object.put("model", selectGptValue);
            object.put("messages", arr);

            DecimalFormat df = new DecimalFormat("#.#");
            double temperature = Double.parseDouble(df.format((double) creativityValue / 10));

            object.put("temperature", temperature);
            //            아래 put 내용은 삭제하면 된다
            //            object.put("model", "text-davinci-003");
            //            object.put("prompt", question);
            //            object.put("max_tokens", 4000);
            //

            Log.d("gptsend", "object: " + object);

        } catch (JSONException e) {
            gptErrorVoice("else");
            if (debugResult) {
                runOnUiThread(() -> chatText.setHint("GPT서버에 전송할 JSON 작업에 실패하였습니다."));
            }
            e.printStackTrace();
        }

        Log.d("sendData", "RequestBody : " + object);


        ////////////////////////////////////////////////////////////////////////////////////////////

        RequestBody body = RequestBody.create(object.toString(), JSON);
        Request request = new Request.Builder().url("https://api.openai.com/v1/chat/completions")  //url 경로 수정됨
                .header("Authorization", "Bearer " + apiKey).post(body).build();


        if (debugResult) {
            runOnUiThread(() -> chatText.setHint("GPT서버에 데이터 전송중..."));
        }


        gptclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("gpt1", "Failed to load response due to " + e.getMessage());

                if (debugResult) {
                    gptErrorVoice("else");
                    runOnUiThread(() -> chatText.setHint("GPT서버와 통신에 실패하였습니다."));
                }

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONObject jsonObject;


                    try {
                        if (debugResult) {
                            runOnUiThread(() -> chatText.setHint("TTS 서버로 송신하였습니다."));
                        }
                        assert response.body() != null;
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        //아래 result 받아오는 경로가 좀 수정되었다.
                        String result = jsonArray.getJSONObject(0).getJSONObject("message").getString("content");
                        bubbleData = result.trim();
                        serverSend(result.trim(), textData);
                    } catch (Exception e) {
                        gptErrorVoice("else");
                        if (debugResult) {
                            runOnUiThread(() -> chatText.setHint("GPT서버와 응답에 실패하였습니다."));
                        }
                        e.printStackTrace();

                    }


                }
                else {
                    assert response.body() != null;
                    String errorData = response.body().string();
                    Log.d("gpt2", "Failed to load response due to " + errorData);

                    try {
                        // 주어진 JSON 문자열
                        // JSON 파싱
                        JSONObject jsonObject = new JSONObject(errorData);

                        // "error" 키에 해당하는 JSONObject 추출
                        JSONObject errorObject = jsonObject.getJSONObject("error");

                        // "type" 키에 해당하는 값 추출
                        String typeValue = errorObject.getString("message");

                        // 추출된 값 출력
                        Log.d("gpt2", "에러 타입: " + typeValue);

                        if (typeValue.contains("large")) {
                            //토큰이 너무 많아서 생기는 오유라고 보내주면됨.
                            gptTokenErrorVoice();
                            if (debugResult) {
                                runOnUiThread(() -> chatText.setHint("새로 대화하기를 해 주세요."));

                            }
                        }

                        else if (typeValue.contains("safety")) {

                            gptErrorVoice("safety");
                            if (debugResult) {
                                runOnUiThread(() -> chatText.setHint("이미지가 선정적이므로 차단되었습니다."));
                            }
                            Log.d("gpterror", "onResponse: safety");
                        }
                        else if (typeValue.contains("Invalid")) {

                            gptErrorVoice("invalid");
                            if (debugResult) {
                                runOnUiThread(() -> chatText.setHint("GPT 에서 이미지 인식에 실패하였습니다."));
                            }
                            Log.d("gpterror", "onResponse: Invalid");

                        }

                        else if (typeValue.contains("The server had an error processing your request.")) {

                            gptErrorVoice("request");
                            if (debugResult) {
                                runOnUiThread(() -> chatText.setHint("서버에서 해당 요청을 수신 받지 못 했습니다."));
                            }
                            Log.d("gpterror", "onResponse: The server had an error processing your request.");
                        }

                        else if (typeValue.contains("Internal server error")) {
                            gptErrorVoice("internal");
                            if (debugResult) {
                                runOnUiThread(() -> chatText.setHint("Internal server error"));
                            }
                        }
                        else {
                            gptErrorVoice("else");
                            if (debugResult) {
                                runOnUiThread(() -> chatText.setHint("GPT서버와 응답로드에 실패하였습니다."));
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });


    }

    private void gptSend(ArrayList<String> imageUrl, String textData) {
        //okhttp
        //추가된 내용
        JSONArray arr = new JSONArray();
        JSONObject baseAi = new JSONObject();
        JSONObject userMsg = new JSONObject();
        try {


            if (debugResult) {
                runOnUiThread(() -> chatText.setHint("GPT 에 보낼 메세지 준비"));
            }

            //AI 속성설정
            baseAi.put("role", "system");
            baseAi.put("content", "이제부터 당신은 다음과 같은 역할을 맡아 대화를 진행하며 이미지를 분석하는 능력을 지녔습니다.:\n" + conceptText + overText);
            // baseAi.put("content", "이제부터 당신은 다음과 같은 역할을 맡아 대화를 진행합니다:\n" + conceptText + overText);

            userMsg.put("role", "user");

            JSONArray contentArray = new JSONArray();

            JSONObject textContent = new JSONObject();
            textContent.put("type", "text");
            textContent.put("text", textData);
            contentArray.put(textContent);


            for (int urlCount = 0; urlCount < imageUrl.size(); urlCount++) {
                JSONObject imageUrlObject = new JSONObject();
                imageUrlObject.put("type", "image_url");
                JSONObject imageUrlObject1 = new JSONObject();
                imageUrlObject1.put("url", imageUrl.get(urlCount));
                imageUrlObject.put("image_url", imageUrlObject1);
                contentArray.put(imageUrlObject);
            }

            userMsg.put("content", contentArray);

            arr.put(baseAi);
            arr.put(userMsg);

        } catch (JSONException e) {

            gptErrorVoice("else");
            if (debugResult) {
                runOnUiThread(() -> chatText.setHint("GPT 에 보낼 JSON 작업에 실패하였습니다."));
            }
            runOnUiThread(() -> {
                resizedImages.clear();
                uploadLayout.setVisibility(View.GONE);
                uploadLock = false;


            });


            throw new RuntimeException(e);


        }

        JSONObject object = new JSONObject();
        try {

            if (debugResult) {
                runOnUiThread(() -> chatText.setHint("GPT 서버에 모델과 메세지 전송 준비"));
            }


            //모델명 변경
            //모델 리스트
            // gpt-3.5-turbo
            // gpt-3.5-turbo-1106
            // gpt-4
            // gpt-4-1106-preview
            object.put("model", "gpt-4-vision-preview");
            object.put("messages", arr);
            //            아래 put 내용은 삭제하면 된다
            //            object.put("model", "text-davinci-003");
            //            object.put("prompt", question);
            object.put("temperature", (float) creativityValue / 10);
            object.put("max_tokens", 4000);
            //            object.put("temperature", 0);
            //온도 이거 매우 중요하다!!!

            Log.d("gptsend", "object: " + object);

        } catch (JSONException e) {

            gptErrorVoice("else");
            if (debugResult) {
                runOnUiThread(() -> chatText.setHint("GPT서버에 전송할 JSON 작업에 실패하였습니다."));
            }
            runOnUiThread(() -> {
                resizedImages.clear();
                uploadLayout.setVisibility(View.GONE);
                uploadLock = false;


            });


            e.printStackTrace();
        }

        Log.d("sendData", "RequestBody : " + object);


        ////////////////////////////////////////////////////////////////////////////////////////////

        RequestBody body = RequestBody.create(object.toString(), JSON);
        Request request = new Request.Builder().url("https://api.openai.com/v1/chat/completions")  //url 경로 수정됨
                .header("Authorization", "Bearer " + apiKey).post(body).build();


        if (debugResult) {
            runOnUiThread(() -> chatText.setHint("GPT서버에 데이터 전송중..."));
        }


        gptclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("gpt1", "Failed to load response due to " + e.getMessage());


                if (debugResult) {
                    gptErrorVoice("else");
                    runOnUiThread(() -> chatText.setHint("GPT서버와 통신에 실패하였습니다."));
                }
                runOnUiThread(() -> {
                    resizedImages.clear();
                    uploadLayout.setVisibility(View.GONE);
                    uploadLock = false;


                });


            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONObject jsonObject;


                    try {

                        if (debugResult) {
                            runOnUiThread(() -> chatText.setHint("TTS 서버로 송신하였습니다."));
                        }


                        assert response.body() != null;
                        jsonObject = new JSONObject(response.body().string());
                        Log.d("순수 오브젝트", "onResponse: " + jsonObject);
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        Log.d("이미지 분석결과", "onResponse: " + jsonArray);
                        //아래 result 받아오는 경로가 좀 수정되었다.
                        String result = jsonArray.getJSONObject(0).getJSONObject("message").getString("content");
                        //bubbleData = result.trim();
                        //serverSend(result.trim(), textData);
                        Log.d("대답", "onResponse : " + result.trim());

                        //여기에 대답처리하는거 넣어야한다.


                        bubbleData = result.trim();
                        serverSend(result.trim(), textData);

                    } catch (Exception e) {

                        gptErrorVoice("else");
                        if (debugResult) {
                            runOnUiThread(() -> chatText.setHint("GPT서버와 응답에 실패하였습니다."));
                        }
                        e.printStackTrace();
                        runOnUiThread(() -> {
                            resizedImages.clear();
                            uploadLayout.setVisibility(View.GONE);
                            uploadLock = false;


                        });

                    }


                }
                else {
                    assert response.body() != null;
                    String errorData = response.body().string();
                    Log.d("gpt2", "Failed to load response due to " + errorData);

                    try {
                        // 주어진 JSON 문자열
                        // JSON 파싱
                        JSONObject jsonObject = new JSONObject(errorData);

                        // "error" 키에 해당하는 JSONObject 추출
                        JSONObject errorObject = jsonObject.getJSONObject("error");

                        // "type" 키에 해당하는 값 추출
                        String typeValue = errorObject.getString("message");

                        // 추출된 값 출력
                        Log.d("gpt2", "에러 타입: " + typeValue);

                        if (typeValue.contains("large")) {

                            //토큰이 너무 많아서 생기는 오유라고 보내주면됨.
                            gptTokenErrorVoice();
                            if (debugResult) {
                                runOnUiThread(() -> chatText.setHint("새로 대화하기를 해 주세요."));

                            }
                            runOnUiThread(() -> {
                                resizedImages.clear();
                                uploadLayout.setVisibility(View.GONE);
                                uploadLock = false;


                            });


                        }
                        else if (typeValue.contains("safety")) {

                            gptErrorVoice("safety");
                            if (debugResult) {
                                runOnUiThread(() -> chatText.setHint("이미지가 선정적이므로 차단되었습니다."));
                            }
                            Log.d("gpterror", "onResponse: safety");
                            runOnUiThread(() -> {
                                resizedImages.clear();
                                uploadLayout.setVisibility(View.GONE);
                                uploadLock = false;


                            });


                        }
                        else if (typeValue.contains("Invalid")) {

                            gptErrorVoice("invalid");
                            if (debugResult) {
                                runOnUiThread(() -> chatText.setHint("GPT 에서 이미지 인식에 실패하였습니다."));
                            }
                            Log.d("gpterror", "onResponse: Invalid");
                            runOnUiThread(() -> {
                                resizedImages.clear();
                                uploadLayout.setVisibility(View.GONE);
                                uploadLock = false;


                            });


                        }

                        else if (typeValue.contains("The server had an error processing your request.")) {

                            gptErrorVoice("request");
                            if (debugResult) {
                                runOnUiThread(() -> chatText.setHint("서버에서 해당 요청을 수신 받지 못 했습니다."));
                            }
                            Log.d("gpterror", "onResponse: The server had an error processing your request.");
                            runOnUiThread(() -> {
                                resizedImages.clear();
                                uploadLayout.setVisibility(View.GONE);
                                uploadLock = false;


                            });


                        }

                        else if (typeValue.contains("Internal server error")) {
                            gptErrorVoice("internal");
                            if (debugResult) {
                                runOnUiThread(() -> chatText.setHint("Internal server error"));
                            }
                            runOnUiThread(() -> {
                                resizedImages.clear();
                                uploadLayout.setVisibility(View.GONE);
                                uploadLock = false;


                            });
                        }

                        else {

                            gptErrorVoice("else");
                            if (debugResult) {
                                runOnUiThread(() -> chatText.setHint("GPT서버에 문제가 발생했습니다."));
                            }
                            runOnUiThread(() -> {
                                resizedImages.clear();
                                uploadLayout.setVisibility(View.GONE);
                                uploadLock = false;


                            });


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void serverSend(String answerData, String questionData) {
        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);


        String finalAnswerData = answerData.replaceAll("\"", "");
        dbManager.open();
        dbManager.insert("{\"role\": \"assistant\", \"content\": \"" + finalAnswerData + "\"}"); // 띄어쓰기를 쉼표로 대체
        // 데이터베이스 닫기
        dbManager.close();

        Log.d("tts", "androidid : " + android_id);

        Date date = new Date();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat mFormat = new SimpleDateFormat("yyyy_MM_dd__hh_mm_ss");

        String getTime = mFormat.format(date);

        // JSON 객체 생성
        JSONObject json = new JSONObject();
        try {
            json.put("id", android_id);
            json.put("time", getTime);
            json.put("question", questionData); // 질문 부분
            json.put("answer", answerData); // 답변부분
            json.put("demo", demoEnable); // 답변부분

            if (debugResult) {
                runOnUiThread(() -> chatText.setHint("서버에서 TTS 가져오는 json 데이터 작업중"));
            }
        } catch (JSONException e) {
            ttsErrorVoice();
            if (debugResult) {
                runOnUiThread(() -> chatText.setHint("서버에서 TTS 가져오는 json 데이터 작업에 실패하였습니다."));
            }
            runOnUiThread(() -> {
                resizedImages.clear();
                uploadLayout.setVisibility(View.GONE);
                uploadLock = false;


            });
            e.printStackTrace();
        }

        // RequestBody 생성
        RequestBody body = RequestBody.create(json.toString(), JSON);

        // Request 생성
        Request request = new Request.Builder().url("https://melissaj.duckdns.org:80/GPT").post(body).build();

        if (debugResult) {
            runOnUiThread(() -> chatText.setHint("서버에서 TTS데이터 가져오도록 요청중"));
        }

        // 비동기 방식으로 요청 전송
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();

                //serverSend(answerData, questionData);

                runOnUiThread(() -> {
                    ttsErrorVoice();
                    if (debugResult) {
                        runOnUiThread(() -> chatText.setHint("TTS를 가져오기 위한 서버의 요청에 실패하였습니다."));
                    }
                    runOnUiThread(() -> {
                        resizedImages.clear();
                        uploadLayout.setVisibility(View.GONE);
                        uploadLock = false;


                    });
                });
            }

            //실패시 처리방법에 대하여 적어놔라.


            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                stopThirdStandby();
                if (!response.isSuccessful()) {
                    ttsErrorVoice();
                    if (debugResult) {
                        runOnUiThread(() -> chatText.setHint("TTS를 가져오기 위한 서버와의 연결에 실패하였습니다."));
                    }
                    runOnUiThread(() -> {
                        resizedImages.clear();
                        uploadLayout.setVisibility(View.GONE);
                        uploadLock = false;


                    });
                    throw new IOException("Unexpected code " + response);

                }

                // 수신된 JSON 데이터 디버그 로그로 출력
                try {
                    if (debugResult) {
                        runOnUiThread(() -> chatText.setHint("TTS 수신을 성공했습니다."));
                    }


                    assert response.body() != null;
                    String responseData = response.body().string();
                    Log.d("tts", "response data : " + responseData);
                    JSONObject receivedJson = new JSONObject(responseData);
                    Log.d("Received JSON", receivedJson.toString());


                    // JSON 문자열을 JSONObject로 변환
                    JSONObject jsonObject = new JSONObject(responseData);

                    // "result" 키의 값을 가져오기
                    String ttsResult = jsonObject.getString("result");

                    // "url" 키의 값을 가져오기
                    String ttsUrl = jsonObject.getString("ttsUrl");

                    demoCount = jsonObject.getInt("count");

                    saveShared();

                    if (demoEnable && demoCount > 50) {


                        demoErrorVoice();
                        runOnUiThread(() -> {
                            demoErrorVoice();
                            demoCountText.setText("데모 사용 횟수를 초과하였습니다.");
                            chatText.setHint("데모 사용 횟수를 초과하였습니다.");
                        });

                        return;


                    }
                    else if (demoEnable) {
                        runOnUiThread(() -> demoCountText.setText("데모 사용 중 : " + demoCount + " / 50"));
                    }


                    // 값 확인
                    Log.d("JSON_Parsing", "Result: " + ttsResult);
                    Log.d("JSON_Parsing", "URL: " + ttsUrl);

                    try {


                        //보이스 영역

                        if (debugResult) {
                            runOnUiThread(() -> chatText.setHint("TTS 데이터 재생 시도"));
                        }

                        mediaPlayer.setDataSource(ttsUrl);
                        mediaPlayer.prepareAsync(); // 비동기식으로 prepareAsync()를 사용할 수도 있음
                        mediaPlayer.setOnPreparedListener(mp -> {
                            //토크 플레이
                            runOnUiThread(() -> {
                                if (emotionCount >= 3) {
                                    sukonbuPlay(String.valueOf(playlistGood.get(1)));
                                }
                                else if (emotionCount <= -3) {
                                    sukonbuPlay(String.valueOf(playlistBad.get(1)));
                                }
                                else {
                                    sukonbuPlay(String.valueOf(playlistNormal.get(1)));
                                }

                            });
                            mediaPlayer.start();
                            if (debugResult) {
                                runOnUiThread(() -> chatText.setHint("재생 시작"));
                            }
                            if (bubbleResult) {
                                runOnUiThread(() -> bubbleLayout.setVisibility(View.VISIBLE));


                                bubbleHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (currentIndex < bubbleData.length()) {
                                                    bubbleText.setText(bubbleData.substring(0, currentIndex + 1));
                                                    currentIndex++;
                                                    bubbleHandler.postDelayed(this, 50);
                                                }
                                                else {
                                                    // Text fully displayed, remove callbacks to stop the handler
                                                    bubbleHandler.removeCallbacksAndMessages(null);
                                                    bubbleData = "";
                                                    currentIndex = 0;
                                                }
                                            }
                                        });
                                    }
                                }, 0);


                                //핸들러 필요.
                            }
                            mediaPlayer.setOnCompletionListener(mp1 -> {
                                // MediaPlayer의 재생이 완료되면 서버와의 연결을 끊습니다.
                                // 이 부분에서 서버와의 연결을 끊는 코드를 추가해야 합니다.
                                // 예를 들어, 클라이언트의 disconnect() 메서드 호출 등을 사용할 수 있습니다.
                                // 아래는 가상의 메서드 호출로 표시된 예시입니다.

                                //재생이 끝나면 노멀모드로 돌아오는 과정
                                runOnUiThread(() -> {
                                    if (emotionCount >= 3) {
                                        sukonbuPlay(String.valueOf(playlistGood.get(0)));
                                    }
                                    else if (emotionCount <= -3) {
                                        sukonbuPlay(String.valueOf(playlistBad.get(0)));
                                    }
                                    else {
                                        sukonbuPlay(String.valueOf(playlistNormal.get(0)));
                                    }
                                    thirdStandbyGone();
                                });
                                if (repeat) {
                                    start();
                                    speech.startListening(recognizerIntent);
                                    progressBar.setIndeterminate(true);
                                    progressBar.setVisibility(View.VISIBLE);
                                }
                                else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                }

                                //new Thread(() -> {
                                //client.connectionPool().evictAll();

                                callNext = false;
                                runOnUiThread(() -> chatText.setHint("스콘부짱에게 무엇을 물어볼까요?"));

                                runOnUiThread(() -> {
                                    uploadLayout.setVisibility(View.GONE);
                                    uploadLock = false;


                                });
                                invalidCount = 0;

                                resizedImages.clear();

                                mediaPlayer.reset();
                                //}).start();

                            });

                        });
                    } catch (IOException e) {
                        e.printStackTrace();

                        if (debugResult) {
                            runOnUiThread(() -> chatText.setHint("TTS 재생에 실패하였습니다."));
                        }
                        runOnUiThread(() -> {
                            resizedImages.clear();
                            uploadLayout.setVisibility(View.GONE);
                            uploadLock = false;


                        });

                        // 미디어 플레이어 재생 에러
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    //서버 수신 에러
                    if (debugResult) {
                        runOnUiThread(() -> chatText.setHint("TTS 응답데이터 정리에 실패하였습니다."));
                    }
                    runOnUiThread(() -> {
                        resizedImages.clear();
                        uploadLayout.setVisibility(View.GONE);
                        uploadLock = false;


                    });

                }
            }
        });
    }

    private void sukonbuPlay(String playItem) {
        // 기존의 리스너를 제거합니다.
        exoPlayer.removeListener(playbackStateListener);

        // File path for the "bad3.mp4" file
        String filePath = "/storage/emulated/0/Android/data/com.melissa.sukonbugpt/files/download/" + playItem + ".mp4";

        MediaItem mediaItem = MediaItem.fromUri(filePath);

        exoPlayer.setMediaItem(mediaItem);
        playerView.setPlayer(exoPlayer);

        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true); // Set ExoPlayer to play when ready


        // 비디오 재생이 완료될 때 다음 비디오를 재생합니다.
        exoPlayer.addListener(playbackStateListener);
    }

    // 이벤트 리스너를 전역변수로 선언합니다.
    private final Player.Listener playbackStateListener = new Player.Listener() {
        @Override
        public void onPlaybackStateChanged(int state) {
            if (state == Player.STATE_ENDED) {
                Log.d("play", "다음 미디어 재생");
                if (emotionCount >= 3) {
                    sukonbuPlay(String.valueOf(playlistGood.get(0)));
                }
                else if (emotionCount <= -3) {
                    sukonbuPlay(String.valueOf(playlistBad.get(0)));
                }
                else {
                    sukonbuPlay(String.valueOf(playlistNormal.get(0)));
                }
            }
        }
    };


    private void toggleFab() {

        if (isFabOpen) {

            fab_main.setImageResource(R.drawable.extend_up);
            fab_sub1.startAnimation(fab_close);
            fab_sub2.startAnimation(fab_close);
            fab_sub3.startAnimation(fab_close);
            fab_sub1.setClickable(false);
            fab_sub2.setClickable(false);
            fab_sub3.setClickable(false);
            isFabOpen = false;


        }
        else {

            fab_main.setImageResource(R.drawable.extend_down);
            fab_sub1.startAnimation(fab_open);
            fab_sub2.startAnimation(fab_open);
            fab_sub3.startAnimation(fab_open);
            fab_sub1.setClickable(true);
            fab_sub2.setClickable(true);
            fab_sub3.setClickable(true);
            isFabOpen = true;

        }

    }

    private void saveShared() {


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
        Log.d("Tag", "tutorialResult : " + tutorialResult);

        Log.i("Tag", "////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");


    }

    private void setBrightness(int brightness) {

        Handler loading_Handler = new Handler();
        loading_Handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (brightCount >= 2) {
                    ContentResolver contentResolver = getContentResolver();
                    Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);

                    WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                    layoutParams.screenBrightness = brightness / 255.0f;
                    getWindow().setAttributes(layoutParams);
                    brightCount = 0;
                    loading_Handler.removeMessages(0);
                    loading_Handler.removeCallbacksAndMessages(null);
                    Log.d("brightapply", "정상적으로 3초 지남");
                    return;

                }
                else {
                    brightCount = brightCount + 1;
                }
                loading_Handler.postDelayed(this, 1000);
            }
        }, 0);


    }

    private void setAutoBrightness() {
        try {
            ContentResolver contentResolver = getContentResolver();
            // 화면 밝기 모드 설정
            Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    //세팅값 받아오는 장소
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void settingTransmissionSet() {
        Log.d("settingapply", "프래그먼트에서 정상적으로 이 함수로 들어왔음.");
        loadShared();

        //다크모드 설정
        if (screenDarkmode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        getDelegate().applyDayNight();
        //가로모드 설정
        if (orientationResult) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로 모드
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 세로 모드
        }
        //밝기 설정 (자동과 수동)
        if (brightAutoResult) {
            setAutoBrightness();
        }
        else {
            int newValue = (int) ((float) (brightValue) / (9) * (255));
            setBrightness(newValue);
            Log.d("birghtvalue", "새로 255가 된 밸류값 : " + newValue);
        }
        //스콘부짱 확대모드 설정
        if (expansionResult) {
            playlistNormal.clear();
            playlistNormal.add("ex_normal_idle");
            playlistNormal.add("ex_normal_talk");
            playlistNormal.add("ex_side1");
            playlistNormal.add("ex_side2");


            playlistGood.clear();
            playlistGood.add("ex_good_idle");
            playlistGood.add("ex_good_talk");
            playlistGood.add("ex_good1");
            playlistGood.add("ex_good2");
            playlistGood.add("ex_good3");

            playlistBad.clear();
            playlistBad.add("ex_bad_idle");
            playlistBad.add("ex_bad_talk");
            playlistBad.add("ex_bad1");
            playlistBad.add("ex_bad2");
            playlistBad.add("ex_bad3");

            sukonbuPlay(String.valueOf(playlistNormal.get(0)));
        }
        else {
            playlistNormal.clear();
            playlistNormal.add("normal_idle");
            playlistNormal.add("normal_talk");
            playlistNormal.add("side1");
            playlistNormal.add("side2");


            playlistGood.clear();
            playlistGood.add("good_idle");
            playlistGood.add("good_talk");
            playlistGood.add("good1");
            playlistGood.add("good2");
            playlistGood.add("good3");

            playlistBad.clear();
            playlistBad.add("bad_idle");
            playlistBad.add("bad_talk");
            playlistBad.add("bad1");
            playlistBad.add("bad2");
            playlistBad.add("bad3");

            sukonbuPlay(String.valueOf(playlistNormal.get(0)));
        }


        //스콘부짱 호출 설정
        callList.clear();
        if (callText.equals("")) {
            callText = resources.getString(R.string.defaultCallType);

        }

        String withoutSpaces = callText.replaceAll("\\s+", "");
        if (withoutSpaces.contains(",")) {
            String[] wordsArray = withoutSpaces.split(",");
            callList.addAll(Arrays.asList(wordsArray));
        }
        else {
            callList.add(withoutSpaces);
        }

        // 결과 출력
        for (String word : callList) {
            Log.d("calllist", word);
        }


        // 스콘부짱 컨셉 설정
        if (conceptText.equals("")) {
            conceptText = resources.getString(R.string.defaultConceptText);
        }

        //스콘부짱 감정모드 설정
        if (emotionResult) {
            if (expansionResult) {
                expantionEmotion.setVisibility(View.VISIBLE);
            }
            else {
                normalEmotion.setVisibility(View.VISIBLE);
            }
        }
        else {
            expantionEmotion.setVisibility(View.GONE);
            normalEmotion.setVisibility(View.GONE);
        }

        //데모 활성화
        if (demoEnable) {
            demoCountText.setVisibility(View.VISIBLE);
            apiKey = demoKey;
        }
        else {
            apiKey = apiKeyResult;
            demoCountText.setVisibility(View.INVISIBLE);
        }

        if (!tutorialResult) {
            FragmentManager tutorialFragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction;
            TutorialFragment TutorialFragment = new TutorialFragment();

            transaction = tutorialFragmentManager.beginTransaction();
            transaction.replace(R.id.main_background, TutorialFragment).commit();
        }
        //gpt버전에 따른 업로드 기능 활성화
        if (selectGptValue.contains("4")) {
            uploadButton.setVisibility(View.VISIBLE);
        }
        else {
            uploadButton.setVisibility(View.GONE);
        }
    }

    void hideKeyboard() {
        View currentFocus = getCurrentFocus();

        if (currentFocus != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void enableImmersiveMode() {
        hideSystemUi();
        // 화면 클릭 등의 이벤트 발생 시 시스템 UI를 다시 숨기도록 설정
        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                // 시스템 UI가 나타나면 다시 숨김
                hideSystemUi();
            }
        });
    }

    private void hideSystemUi() {
        View decorView = getWindow().getDecorView();
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            flags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        }
        else {
            flags |= View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }

        decorView.setSystemUiVisibility(flags);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 액티비티가 활성화될 때 몰입 모드를 다시 설정
        handler.postDelayed(hideSystemUiRunnable, 500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 액티비티가 비활성화될 때 핸들러 콜백 제거
        handler.removeCallbacks(hideSystemUiRunnable);
    }

    public void start() {
        Log.d("speechrecognize", "Start 메소드 진입");
        progressBar.setVisibility(View.INVISIBLE);
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        Log.i("speechrecognize", "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(this));
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        int maxLinesInput = 10;
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, maxLinesInput);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "지금 말해주세요!", Toast.LENGTH_SHORT).show();
                speech.startListening(recognizerIntent);
            }
            else {
                Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void turnOf() {
        if (speech != null) {
            speech.cancel();
            speech.destroy();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        //        if (speech != null) {
        //            speech.destroy();
        //            Log.i("speechrecognize", "destroy");
        //        }
    }


    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("주의!");
        builder.setMessage("해당 기능은 음성인식을 상시 활성화 합니다\n태블릿이나 키오스크에서 사용을 권장합니다.");

        builder.setPositiveButton("예", (dialog, which) -> {
            // "예" 버튼을 눌렀을 때의 처리

            repeat = true;
            start();
            speechLock = true;
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.VISIBLE);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_PERMISSION);
            ttsButton.setImageResource(R.drawable.img_fabend);


        });

        builder.setNegativeButton("아니오", (dialog, which) -> {
            // "아니오" 버튼을 눌렀을 때의 처리

        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void showTokenAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("기록 초기화 안내");
        builder.setMessage("새로 대화 하기는 누적된 대화를 초기화 하고 처음부터 대화하는 기능입니다.\n서버에 등록된 로그는 유지됩니다. 계속하시겠습니까?");

        builder.setPositiveButton("예", (dialog, which) -> {
            // "예" 버튼을 눌렀을 때의 처리
            dbManager.open();
            dbManager.deleteAllData();
            dbManager.close();
            Toast.makeText(MainActivity.this, "새로운 대화가 시작됩니다.", Toast.LENGTH_SHORT).show();


        });

        builder.setNegativeButton("아니오", (dialog, which) -> {
            // "아니오" 버튼을 눌렀을 때의 처리

        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void ttsErrorVoice() {
        callMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.tts_error);

        callMediaPlayer.setOnPreparedListener(mp -> {

            callNext = true;
            if (emotionCount >= 3) {
                sukonbuPlay(String.valueOf(playlistGood.get(1)));
            }
            else if (emotionCount <= -3) {
                sukonbuPlay(String.valueOf(playlistBad.get(1)));
            }
            else {
                sukonbuPlay(String.valueOf(playlistNormal.get(1)));
            }
            callMediaPlayer.start();

            if (bubbleResult) {
                runOnUiThread(() -> bubbleLayout.setVisibility(View.VISIBLE));
                runOnUiThread(() -> bubbleText.setText("tts 서버와의 연결에 문제가 발생했나봐요. 다시 실행 해 주시겠어요?"));
            }


            callMediaPlayer.setOnCompletionListener(mp1 -> {
                // TODO Auto-generated method stub


                if (emotionCount >= 3) {
                    sukonbuPlay(String.valueOf(playlistGood.get(0)));
                }
                else if (emotionCount <= -3) {
                    sukonbuPlay(String.valueOf(playlistBad.get(0)));
                }
                else {
                    sukonbuPlay(String.valueOf(playlistNormal.get(0)));
                }

                callMediaPlayer.pause();
                callMediaPlayer.reset();
                thirdStandbyGone();
            });
        });
    }

    @SuppressLint("SetTextI18n")
    private void gptErrorVoice(String code) {
        if (code.contains("invalid")) {
            if (invalidCount > 3) {
                callMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.gpt_invalid_over);
            }
            else {
                callMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.gpt_invalid);
                invalidCount++;
            }

        }
        else if (code.contains("safety")) {
            callMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.gpt_safety);
        }
        else if (code.contains("request")) {
            callMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.gpt_request);
        }
        else if (code.contains("Internal")) {
            callMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.gpt_request);
        }
        else {
            callMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.gpt_error);
        }


        callMediaPlayer.setOnPreparedListener(mp -> {

            callNext = true;
            if (emotionCount >= 3) {
                sukonbuPlay(String.valueOf(playlistGood.get(1)));
            }
            else if (emotionCount <= -3) {
                sukonbuPlay(String.valueOf(playlistBad.get(1)));
            }
            else {
                sukonbuPlay(String.valueOf(playlistNormal.get(1)));
            }

            callMediaPlayer.start();

            if (bubbleResult) {
                runOnUiThread(() -> bubbleLayout.setVisibility(View.VISIBLE));

                if (code.contains("invalid")) {
                    if (invalidCount > 3) {
                        runOnUiThread(() -> bubbleText.setText("너무 많은 오류가 발생했어요. 지피티 서버의 문제일 가능성이 매우 높으니 잠시 후에 다시 시도 해 주세요."));
                    }
                    else {
                        runOnUiThread(() -> bubbleText.setText("GPT 서버에서 이미지 인식을 실패했어요. 다시 시도하거나 좀 더 정확한 이미지를 제시 해 주세요."));
                    }

                }
                else if (code.contains("safety")) {
                    runOnUiThread(() -> bubbleText.setText("선정적인 대화는 사용 할 수 없어요. 좀 더 순화 해 주세요."));
                }
                else if (code.contains("request")) {
                    runOnUiThread(() -> bubbleText.setText("현재 GPT 서버 상태가 좋지 않습니다. 나중에 다시 시도 해 주세요."));
                }
                else if (code.contains("Internal")) {
                    runOnUiThread(() -> bubbleText.setText("현재 GPT 서버 상태가 좋지 않습니다. 나중에 다시 시도 해 주세요."));
                }
                else {
                    runOnUiThread(() -> bubbleText.setText("Chat GPT 와의 연결에 문제가 발생했나봐요. 다시 실행 해 주시겠어요?"));
                }
            }


            callMediaPlayer.setOnCompletionListener(mp1 -> {
                // TODO Auto-generated method stub


                if (emotionCount >= 3) {
                    sukonbuPlay(String.valueOf(playlistGood.get(0)));
                }
                else if (emotionCount <= -3) {
                    sukonbuPlay(String.valueOf(playlistBad.get(0)));
                }
                else {
                    sukonbuPlay(String.valueOf(playlistNormal.get(0)));
                }

                callMediaPlayer.pause();
                callMediaPlayer.reset();
                thirdStandbyGone();
            });
        });
    }


    private void gptTokenErrorVoice() {
        callMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.gpt_token);

        callMediaPlayer.setOnPreparedListener(mp -> {

            callNext = true;
            if (emotionCount >= 3) {
                sukonbuPlay(String.valueOf(playlistGood.get(1)));
            }
            else if (emotionCount <= -3) {
                sukonbuPlay(String.valueOf(playlistBad.get(1)));
            }
            else {
                sukonbuPlay(String.valueOf(playlistNormal.get(1)));
            }

            callMediaPlayer.start();

            if (bubbleResult) {
                runOnUiThread(() -> bubbleLayout.setVisibility(View.VISIBLE));
                runOnUiThread(() -> bubbleText.setText("대화 기록이 꽉 찼어요. 새로 대화하기를 해 주세요"));
            }


            callMediaPlayer.setOnCompletionListener(mp1 -> {
                // TODO Auto-generated method stub


                if (emotionCount >= 3) {
                    sukonbuPlay(String.valueOf(playlistGood.get(0)));
                }
                else if (emotionCount <= -3) {
                    sukonbuPlay(String.valueOf(playlistBad.get(0)));
                }
                else {
                    sukonbuPlay(String.valueOf(playlistNormal.get(0)));
                }

                callMediaPlayer.pause();
                callMediaPlayer.reset();
                thirdStandbyGone();
            });
        });
    }

    private void demoErrorVoice() {
        callMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.demo_error);

        callMediaPlayer.setOnPreparedListener(mp -> {

            callNext = true;
            if (emotionCount >= 3) {
                sukonbuPlay(String.valueOf(playlistGood.get(1)));
            }
            else if (emotionCount <= -3) {
                sukonbuPlay(String.valueOf(playlistBad.get(1)));
            }
            else {
                sukonbuPlay(String.valueOf(playlistNormal.get(1)));
            }

            callMediaPlayer.start();

            if (bubbleResult) {
                runOnUiThread(() -> bubbleLayout.setVisibility(View.VISIBLE));
                runOnUiThread(() -> bubbleText.setText("데모판 횟수를 전부 소진하셨어요 API KEY 를 발급받아서 사용 해 주세요."));
            }


            callMediaPlayer.setOnCompletionListener(mp1 -> {
                // TODO Auto-generated method stub


                if (emotionCount >= 3) {
                    sukonbuPlay(String.valueOf(playlistGood.get(0)));
                }
                else if (emotionCount <= -3) {
                    sukonbuPlay(String.valueOf(playlistBad.get(0)));
                }
                else {
                    sukonbuPlay(String.valueOf(playlistNormal.get(0)));
                }

                callMediaPlayer.pause();
                callMediaPlayer.reset();
                thirdStandbyGone();
            });
        });
    }

    private void thirdStandbyGone() {
        thirdStandbyTimer = new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
                // 1초마다 실행되는 코드 (여기서는 필요 없을 수 있음)
            }

            public void onFinish() {
                bubbleLayout.setVisibility(View.GONE);
                thirdStandbyTimer = null; // 실행이 끝났으므로 변수 초기화
            }
        }.start();
    }

    private void stopThirdStandby() {
        if (thirdStandbyTimer != null) {
            thirdStandbyTimer.cancel(); // 타이머를 취소하여 실행 중인 작업 중단
            thirdStandbyTimer = null; // 변수 초기화
        }
    }

    private void swipeCheck(boolean upCheck) {
        if (upCheck) {
            swipeValue++;
        }
        else {
            swipeValue--;
        }

        if (swipeValue < 0) {
            swipeValue = 2;
        }
        else if (swipeValue > 2) {
            swipeValue = 0;
        }

        switch (swipeValue) {
            case 0:
                if (currentToast != null) {
                    currentToast.cancel();
                }

                currentToast = Toast.makeText(getApplicationContext(), "일반 채팅 모드", Toast.LENGTH_SHORT);
                currentToast.show();
                break;
            case 1:
                // swipeValue가 1일 때 실행할 코드
                if (currentToast != null) {
                    currentToast.cancel();
                }


                currentToast = Toast.makeText(getApplicationContext(), "1회 음성인식 모드", Toast.LENGTH_SHORT);
                currentToast.show();

                break;
            // 다른 case들에 대한 처리
            case 2:
                // swipeValue가 1일 때 실행할 코드

                if (currentToast != null) {
                    currentToast.cancel();
                }

                currentToast = Toast.makeText(getApplicationContext(), "다중 음성인식 모드", Toast.LENGTH_SHORT);
                currentToast.show();
                break;
            default:
                // 경 아무것도안함 축
        }
    }

    //이미지 업로드 부분

    private List<Bitmap> resizeImages(List<Bitmap> images) {
        List<Bitmap> resizedImages = new ArrayList<>();
        for (Bitmap image : images) {
            // 이미지가 512보다 작은 경우에는 리사이징하지 않고 그대로 추가
            if (image.getWidth() <= 1024 && image.getHeight() <= 1024) {
                resizedImages.add(image);
            }
            else {
                Bitmap resizedImage = resizeBitmap(image, 1024);
                resizedImages.add(resizedImage);
            }
        }
        return resizedImages;
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int maxSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float aspectRatio = (float) width / (float) height;

        int newWidth;
        int newHeight;

        if (width > height) {
            newWidth = maxSize;
            newHeight = Math.round(maxSize / aspectRatio);
        }
        else {
            newHeight = maxSize;
            newWidth = Math.round(maxSize * aspectRatio);
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        resizedImages = new ArrayList<>();
        if (resultCode == Activity.RESULT_OK) {
            List<Bitmap> selectedImages = imageSelector.handleImageSelectionResult(MainActivity.this, requestCode, resultCode, data);
            if (selectedImages != null && !selectedImages.isEmpty()) {
                // 이미지를 512 사이즈로 리사이징
                resizedImages = resizeImages(selectedImages);

                uploadLayout.setVisibility(View.VISIBLE);
                ttsButton.setImageResource(R.drawable.img_vision);
                Toast.makeText(MainActivity.this, "Gpt4 With Vision 모드 활성화", Toast.LENGTH_SHORT).show();
                uploadLock = true;
                // 선택된 이미지를 ImageView에 표시
                uploadSample.setImageBitmap(resizedImages.get(0)); // 일단 첫 번째 이미지만 표시

                int uploadCount = 0;
                // 선택된 이미지를 한꺼번에 서버에 업로드하거나 다른 처리 수행
                // 각 이미지의 데이터를 로그로 출력
                for (Bitmap image : resizedImages) {
                    uploadCount++;
                    Log.d("image", "image data : " + image);
                    Log.d("count", "count data : " + uploadCount);
                }
                if (uploadCount > 1) {
                    uploadAdd.setVisibility(View.VISIBLE);
                    uploadAdd.setText("+" + (uploadCount - 1));
                }
                else {
                    uploadAdd.setVisibility(View.GONE);
                }
            }
        }
    }


    private void uploadImagesToServer(String textData) {
        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        new Thread(new Runnable() {
            @Override
            public void run() {

                ArrayList<String> imageUrl = new ArrayList<>();
                client = new OkHttpClient();
                int successfulUploadCount = 0; // 성공적으로 업로드된 이미지 수
                int failedUploadCount = 0; // 업로드 실패한 이미지 수

                for (int i = 0; i < resizedImages.size(); i++) {
                    Bitmap image = resizedImages.get(i);

                    // 이미지를 파일로 변환 및 요청 생성
                    File file = saveBitmapToFile(image, i, "png");
                    if (file == null) {
                        failedUploadCount++;
                        continue; // 파일 변환 실패 시 다음 이미지로 이동
                    }

                    String imageName = (i + 1) + ".png";
                    RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("id", android_id).addFormDataPart("image", imageName, RequestBody.create(MediaType.parse("image/png"), file)).build();

                    Request request = new Request.Builder().url("https://melissaj.duckdns.org:80/upload").post(requestBody).build();

                    try {
                        Response response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            String responseBody = response.body().string();
                            Log.d("Upload Response", responseBody);

                            JSONObject jsonObject = new JSONObject(responseBody);

                            // "image_url" 키의 값을 추출하여 String 변수에 저장
                            imageUrl.add(jsonObject.getString("image_url"));
                            Log.d("imageUrl", "run: " + jsonObject.getString("image_url"));
                            successfulUploadCount++;
                        }
                        else {
                            Log.e("Upload Error", "Failed to upload image. Response code: " + response.code());
                            failedUploadCount++;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("Upload Error", "Failed to upload image: " + e.getMessage());
                        failedUploadCount++;
                    } catch (JSONException e) {
                        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        ttsErrorVoice();
                        runOnUiThread(() -> {
                            resizedImages.clear();
                            uploadLayout.setVisibility(View.GONE);
                            uploadLock = false;


                        });

                        throw new RuntimeException(e);

                    }
                }

                // 모든 이미지에 대한 업로드 완료 후 처리
                if (successfulUploadCount + failedUploadCount == resizedImages.size()) {
                    if (successfulUploadCount == resizedImages.size()) {
                        Log.d("Upload", "All images uploaded successfully."); // 모든 이미지 업로드 성공을 로그로 출력하거나 다른 처리 수행

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                        gptSend(imageUrl, textData);
                    }
                    else {
                        Log.e("Upload", "Some images failed to upload."); // 일부 이미지 업로드 실패를 로그로 출력하거나 다른 처리 수행
                        ttsErrorVoice();
                        runOnUiThread(() -> {
                            resizedImages.clear();
                            uploadLayout.setVisibility(View.GONE);
                            uploadLock = false;


                        });
                    }
                }
            }
        }).start();
    }

    private File saveBitmapToFile(Bitmap bitmap, int index, String extension) {
        // Bitmap을 파일로 저장하여 확장자를 추출
        File file = new File(getCacheDir(), (index + 1) + "." + extension);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(getCompressFormat(extension), 100, fos);
            fos.flush();
            fos.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap.CompressFormat getCompressFormat(String extension) {
        switch (extension.toLowerCase()) {
            case "jpg":
            case "jpeg":
                return Bitmap.CompressFormat.JPEG;
            case "png":
                return Bitmap.CompressFormat.PNG;
            case "webp":
                return Bitmap.CompressFormat.WEBP;
            default:
                return Bitmap.CompressFormat.PNG; // 기본값은 PNG로 설정
        }
    }

    private void tenStandbyGone() {
        tenStanbyTimer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                // 1초마다 실행되는 코드 (여기서는 필요 없을 수 있음)
            }

            public void onFinish() {
                callResult = false;
                callNext = false;
                //오류안내 작성

                turnOf();
                callMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.gpt_timeover);

                callMediaPlayer.setOnPreparedListener(mp -> {

                    if (emotionCount >= 3) {
                        sukonbuPlay(String.valueOf(playlistGood.get(1)));
                    }
                    else if (emotionCount <= -3) {
                        sukonbuPlay(String.valueOf(playlistBad.get(1)));
                    }
                    else {
                        sukonbuPlay(String.valueOf(playlistNormal.get(1)));
                    }

                    callMediaPlayer.start();

                    if (bubbleResult) {
                        runOnUiThread(() -> bubbleLayout.setVisibility(View.VISIBLE));
                        runOnUiThread(() -> bubbleText.setText("잘 못 부르셨나봐요 기다리고 있을게요!"));
                    }


                    callMediaPlayer.setOnCompletionListener(mp1 -> {
                        // TODO Auto-generated method stub


                        if (emotionCount >= 3) {
                            sukonbuPlay(String.valueOf(playlistGood.get(0)));
                        }
                        else if (emotionCount <= -3) {
                            sukonbuPlay(String.valueOf(playlistBad.get(0)));
                        }
                        else {
                            sukonbuPlay(String.valueOf(playlistNormal.get(0)));
                        }

                        callMediaPlayer.pause();
                        callMediaPlayer.reset();
                        thirdStandbyGone();
                        MainActivity.this.start();
                        speech.startListening(recognizerIntent);
                        progressBar.setVisibility(View.VISIBLE);
                        tenStanbyTimer.cancel();
                        tenStanbyTimer = null; // 실행이 끝났으므로 변수 초기화
                    });
                });


            }
        }.start();
    }





}
