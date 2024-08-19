package com.melissa.sukonbugpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class IntroAttentionPleaseActivity extends AppCompatActivity {

    String fileUrl="";
    String directDownloadLink = "https://drive.google.com/uc?export=download&id=";

    int fileDownloadCount = 0;



    OkHttpClient client = new OkHttpClient();

    ArrayList<String> downloadId = new ArrayList<>();
    ArrayList<String> downloadLink = new ArrayList<>();

    ArrayList<String> downloadLink2 = new ArrayList<>();
    ArrayList<String> downloadLink3 = new ArrayList<>();

    ArrayList<String> downloadPath = new ArrayList<>();

    private ProgressBar progressBar;
    private TextView textView;

    ScrollView attentionLayout;

    private CountDownTimer restartTimer;

    Button btnDownload;

    Button restartButton;

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
    Resources resources;
    String callText = "";
    String conceptText = "";
    String selectGptValue = "";
    int brightValue = 0;
    //이번 액티비티에서 담당하게 되는 값
    int selectGpt = 0;
    int downloadValue = 0;
    int creativityValue = 7;

    int restartCount = 6;
    String apiKeyResult = "";

    String json = "";

    Boolean attensionPleaseResult = false;

    Boolean permissionResult = false;
    Boolean screenDarkmode = false;

    SharedPreferences preferences;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_attension_please);

        attentionLayout = findViewById(R.id.layout_attention);
        restartButton = findViewById(R.id.btn_restart);

        resources = getResources();
        callText = resources.getString(R.string.defaultCallType);
        conceptText = resources.getString(R.string.defaultConceptText);

        client = new OkHttpClient().newBuilder().connectTimeout(120, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build();


        preferences = getSharedPreferences("settingResult", MODE_PRIVATE);

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


        btnDownload = findViewById(R.id.btnDownload);

        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textView);

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


        btnDownload.setOnClickListener(v -> {
            attentionLayout.setVisibility(View.VISIBLE);
            btnDownload.setVisibility(View.GONE);
            downloadPath.clear();
            fileDownloadCount = 0;
            progressBar.setProgress(fileDownloadCount);
            textView.setText((int) ((float) fileDownloadCount / 28 * 100) +" %");
            if(downloadValue == 0) {
                fileUrl = directDownloadLink + downloadLink.get(fileDownloadCount);
            }
            else if(downloadValue == 1) {
                fileUrl = directDownloadLink + downloadLink2.get(fileDownloadCount);
            }
            else if(downloadValue == 2){
                fileUrl = directDownloadLink + downloadLink3.get(fileDownloadCount);
            }
            String downloadDirectory = getDefaultDownloadDirectory();
            requestFileDownload(fileUrl, downloadDirectory);

        });
        restartButton.setOnClickListener(v -> {
            restartButton.setVisibility(View.GONE);
            downloadPath.clear();
            fileDownloadCount = 0;
            progressBar.setProgress(fileDownloadCount);
            textView.setText((int) ((float) fileDownloadCount / 28 * 100) +" %");
            if(downloadValue == 0) {
                fileUrl = directDownloadLink + downloadLink.get(fileDownloadCount);
            }
            else if(downloadValue == 1) {
                fileUrl = directDownloadLink + downloadLink2.get(fileDownloadCount);
            }
            else if(downloadValue == 2){
                fileUrl = directDownloadLink + downloadLink3.get(fileDownloadCount);
            }
            String downloadDirectory = getDefaultDownloadDirectory();
            requestFileDownload(fileUrl, downloadDirectory);

        });

    }

    private String getDefaultDownloadDirectory() {
        return getExternalFilesDir(null) + "/download/";
    }



    private void requestFileDownload(String fileUrl, String downloadDirectory) {

        Request request = new Request.Builder()
                .url(fileUrl)
                .build();
        CallbackToDownloadFile callback = new CallbackToDownloadFile(downloadDirectory, getFileNameFrom(URI.create(fileUrl)));
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
            if(downloadValue ==3){
                textView.setText("다운로드 서버에 이상이 발생하였습니다. 개발자에게 문의를 시도해 주세요.");
            }
            else{
                downloadValue++;
            }
            restartButton.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) {
            if (directory.mkdirs()) {
                Log.d("onresponse", "Directory created successfully");
            } else {
                Log.e("onresponse", "Failed to create directory");
            }

            if (fileToBeDownloaded.exists()) {
                if (fileToBeDownloaded.delete()) {
                    Log.d("onresponse", "File deleted successfully");
                } else {
                    Log.e("onresponse", "ailed to delete the file");
                }
            } else {
                Log.e("onresponse", "File does not exist");
            }

            try {
                if (fileToBeDownloaded.exists()) {
                    System.out.println("File already exists");
                } else {
                    if (fileToBeDownloaded.createNewFile()) {
                        System.out.println("File created successfully");
                    } else {
                        System.err.println("Failed to create the file");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                textView.setText("쓰기 권한에 문제가 발생하였습니다. 어플을 재설치해주세요.");
                return;
            }

            String newFileName = downloadId.get(fileDownloadCount)+".mp4"; // 원하는 새 파일 이름으로 변경

            File renamedFile = new File(directory.getAbsolutePath() + "/" + newFileName);

            try {
                assert response.body() != null;
                try (InputStream is = response.body().byteStream();
                         OutputStream os = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) ?
                             Files.newOutputStream(renamedFile.toPath()) : new FileOutputStream(renamedFile)) {

                    final int BUFFER_SIZE = 2048;
                    byte[] data = new byte[BUFFER_SIZE];
                    int count;
                    long total = 0;

                    while ((count = is.read(data)) != -1) {
                        total += count;
                        os.write(data, 0, count);
                    }

                    //openToastOnUiThread("다운로드가 완료되었습니다. 파일 이름: " + newFileName);

                    // 다운로드한 파일의 경로를 로그로 출력합니다.
                    String filePath = renamedFile.getAbsolutePath();
                    Log.d("DownloadedFilePath", "다운로드한 파일 경로: " + filePath);
                    downloadPath.add(filePath+".mp4");

                    fileDownloadCount++;

                    if(fileDownloadCount< downloadLink.size()){

                        runOnUiThread(() -> {
                            progressBar.setProgress(fileDownloadCount);
                            textView.setText((int) ((float) fileDownloadCount / 28 * 100) +" %");
                        });


                        if(downloadValue == 0) {
                            fileUrl = directDownloadLink + downloadLink.get(fileDownloadCount);
                        }
                        else if(downloadValue == 1) {
                            fileUrl = directDownloadLink + downloadLink2.get(fileDownloadCount);
                        }
                        else if(downloadValue == 2){
                            fileUrl = directDownloadLink + downloadLink3.get(fileDownloadCount);
                        }
                        String downloadDirectory = getDefaultDownloadDirectory();
                        requestFileDownload(fileUrl, downloadDirectory);
                    }
                    else{
                        runOnUiThread(() -> {
                            progressBar.setProgress(fileDownloadCount);
                            textView.setText((int) ((float) fileDownloadCount / 28 * 100) +" %");


                        });

                        //openToastOnUiThread("모든 다운로드가 완료되었습니다.");
                        runOnUiThread(() -> {
                            restartTimer = new CountDownTimer(5000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                    restartCount--;
                                    // 1초마다 실행되는 코드 (여기서는 필요 없을 수 있음)
                                    textView.setText("다운로드가 완료되었습니다. " + restartCount + "초 뒤에 재시작 합니다." );
                                }

                                public void onFinish() {
                                    restartTimer = null; // 실행이 끝났으므로 변수 초기화
                                    alrightRestart();
                                }
                            }.start();
                        });

                    }




                }
            } catch (IOException e) {
                e.printStackTrace();
                textView.setText("다운로드 중 오류가 발생하였습니다. 다시 시도해 주세요.");
                if(downloadValue ==3){
                    textView.setText("다운로드 서버에 이상이 발생하였습니다. 개발자에게 문의를 시도해 주세요.");
                }
                else{
                    downloadValue++;
                }
                restartButton.setVisibility(View.VISIBLE);
                //openToastOnUiThread("파일 다운로드 중 오류 발생: " + e.getMessage());
            }
        }
    }
    private void alrightRestart(){
        attensionPleaseResult = true;

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
        editor.putString("selectGptValue", selectGptValue);
        editor.putInt("selectGpt",selectGpt);
        //최종 확인란에서 저장했던 데이터
        editor.putBoolean("attensionPleaseResult", attensionPleaseResult);

        Log.d("saveData", "attensionPleaseResult : 한번더 확인 : " + attensionPleaseResult);
        editor.apply();

        Intent intent = new Intent(IntroAttentionPleaseActivity.this, IntroActivity.class);

        // 현재 액티비티 종료
        finish();

        // IntroActivity를 새로 시작
        startActivity(intent);
    }

}