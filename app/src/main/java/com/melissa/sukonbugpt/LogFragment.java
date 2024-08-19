package com.melissa.sukonbugpt;

import static android.content.Context.MODE_PRIVATE;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

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

public class LogFragment extends Fragment {

    MainActivity mainActivity; //(액티비티에서 이동하기) 주 가되는 메인액티비티 선언


    Button recieveButton;
    Button exitButton;

    Button logDeleteButton;

    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient logclient = new OkHttpClient();

    RecyclerView recyclerView;

    ImageButton sideExitLog;

    SimpleTextAdapter adapter;


    ArrayList<String> list;
    ArrayList<String> name;

    Boolean demoEnable = false;


    //어땃쥐
    //화면이 붙을때 작동하는 메서드
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity(); //(액티비티에서 이동하기) 현재 소속된 액티비티를 메인 액티비티로 한다.
        requireActivity().getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback); // 뒤로가기 버튼 작업
    }

    //뒤로가기 버튼 작업
    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            FragmentManager manager = requireActivity().getSupportFragmentManager();
            manager.beginTransaction().remove(LogFragment.this).commit();
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
        View inflaterLog = inflater.inflate(R.layout.activity_log_fragment, container, false); // inflater 때문에 선언을 추가해야함
        recieveButton = inflaterLog.findViewById(R.id.btn_recieve_log);
        recyclerView = inflaterLog.findViewById(R.id.recycler1);
        sideExitLog = inflaterLog.findViewById(R.id.btn_side_exit_log);
        logDeleteButton = inflaterLog.findViewById(R.id.btn_delete_log);
        exitButton = inflaterLog.findViewById(R.id.btn_recieve_exit);

        SharedPreferences preferences = requireActivity().getSharedPreferences("settingResult", MODE_PRIVATE);
        demoEnable = preferences.getBoolean("demoEnable", demoEnable);


        recieveButton.setOnClickListener(v -> {
            Toast.makeText(requireActivity().getApplicationContext(), "로그 데이터를 불러오고 있습니다.", Toast.LENGTH_SHORT).show();
            logSend();
        });

        logDeleteButton.setOnClickListener(v -> showAlertDialog());

        sideExitLog.setOnClickListener(v -> {
            inflaterLog.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out));


            FragmentManager manager = requireActivity().getSupportFragmentManager();
            manager.beginTransaction().remove(LogFragment.this).commit();
            manager.popBackStack();
        });

        exitButton.setOnClickListener(v -> {
            inflaterLog.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out));


            FragmentManager manager = requireActivity().getSupportFragmentManager();
            manager.beginTransaction().remove(LogFragment.this).commit();
            manager.popBackStack();
        });


        return inflaterLog;
    }

    private void logSend() {
        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(requireActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        JSONObject json = new JSONObject();
        try {
            json.put("id", android_id);
            json.put("log", "ok");
            //json.put("gptData2", "hello");
            //debugText.setText("LOG 가져올 JSON 데이터 작성중");
        } catch (JSONException e) {
            e.printStackTrace();
            //debugText.setText("LOG 가져올 JSON 데이터 작성에 실패하였습니다.");
        }

        // RequestBody 생성
        RequestBody body = RequestBody.create(json.toString(), JSON);

        // Request 생성
        Request request = new Request.Builder().url("https://melissaj.duckdns.org:80/GPT").post(body).build();
        //debugText.setText("서버에 LOG 데이터 요청중");

        // 비동기 방식으로 요청 전송
        logclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //debugText.setText("서버에 LOG 데이터 요청을 실패하였습니다.");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (!response.isSuccessful()) {
                    //debugText.setText("서버에 LOG 데이터 응답에 실패하였습니다.");
                    throw new IOException("Unexpected code " + response);
                }

                assert response.body() != null;
                String responseData = response.body().string();

                requireActivity().runOnUiThread(() -> {


                    // 리사이클러뷰에 표시할 데이터 리스트 생성.
                    list = new ArrayList<>();
                    name = new ArrayList<>();

                    try {
                        //debugText.setText("LOG 데이터 파싱중");
                        JSONArray jsonArray = new JSONArray(responseData);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String time = jsonObject.getString("time");
                            String question = jsonObject.getString("question");
                            String answer = jsonObject.getString("answer");

                            // 파싱된 데이터를 dataList에 추가

                            Log.d("SystemTest", "question : " + question + "\nanswer : " + answer);
                            list.add("question : " + question + "\nanswer : " + answer);
                            name.add("time : " + time);

                        }
                        //debugText.setText("LOG 데이터 파싱완료");

                        //client.dispatcher().cancelAll(); // 아직 처리되지 않은 모든 요청을 취소합니다.
                        //client.connectionPool().evictAll(); // 연결 풀의 모든 연결을 해제합니다.
                        //client.cache().close(); // 캐시를 닫습니다.
                        //client = null; // OkHttpClient를 null로 설정하여 메모리에서 해제합니다.
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(requireActivity().getApplicationContext(), "로그 데이터가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                        return;
                        //debugText.setText("LOG 데이터 파싱에 실패하였습니다.");
                    }

                    // 리사이클러뷰에 LinearLayoutManager 객체 지정.

                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.scrollToPosition(list.size() - 1);

                    // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
                    adapter = new SimpleTextAdapter(list, name);
                    recyclerView.setAdapter(adapter);
                    recyclerView.startLayoutAnimation();

                    logDeleteButton.setVisibility(View.VISIBLE);

                    //debugText.setText("로그를 리사이클러뷰에 표시 완료.");
                });

            }

        });
    }


    private void deleteSend() {
        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(requireActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        JSONObject json = new JSONObject();
        try {
            json.put("id", android_id);
            json.put("delete", "ok");
            json.put("demo", demoEnable);
            //json.put("gptData2", "hello");
            //debugText.setText("LOG 가져올 JSON 데이터 작성중");
        } catch (JSONException e) {
            e.printStackTrace();
            //debugText.setText("LOG 가져올 JSON 데이터 작성에 실패하였습니다.");
        }

        // RequestBody 생성
        RequestBody body = RequestBody.create(json.toString(), JSON);

        // Request 생성
        Request request = new Request.Builder().url("https://melissaj.duckdns.org:80/GPT").post(body).build();
        //debugText.setText("서버에 LOG 데이터 요청중");

        // 비동기 방식으로 요청 전송
        logclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(requireActivity().getApplicationContext(), "로그 데이터가 이미 없거나 오류가 발생하였습니다.\n다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (!response.isSuccessful()) {
                    //debugText.setText("서버에 LOG 데이터 응답에 실패하였습니다.");
                    throw new IOException("Unexpected code " + response);
                }

                JSONObject json;
                String deleteCode;
                try {
                    assert response.body() != null;
                    json = new JSONObject(response.body().string());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                // "error" 키에 해당하는 JSONObject 가져오기
                try {
                    deleteCode = json.getString("delete");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


                String finalDeleteCode = deleteCode;
                requireActivity().runOnUiThread(() -> {

                    if (finalDeleteCode.contains("ok")) {
                        logDeleteButton.setVisibility(View.GONE);
                        Toast.makeText(requireActivity().getApplicationContext(), "서버에서 로그 데이터를 성공적으로 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                        list.clear();
                        name.clear();
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(requireActivity().getApplicationContext(), "로그 데이터가 이미 없거나 오류가 발생하였습니다.\n다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                    }

                });


            }

        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("주의!");
        builder.setMessage("해당 기능은 기록된 데이터를 전부 삭제합니다.\n계속 진행 할까요?");

        builder.setPositiveButton("예", (dialog, which) -> {
            // "예" 버튼을 눌렀을 때의 처리
            deleteSend();


        });

        builder.setNegativeButton("아니오", (dialog, which) -> {
            // "아니오" 버튼을 눌렀을 때의 처리

        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}