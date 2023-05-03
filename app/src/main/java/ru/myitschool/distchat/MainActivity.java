package ru.myitschool.distchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    EditText editMSG;
    TextView allMSG;
    Button btnSendMSG;
    ScrollView scrollView;
    String name = "Oleg";
    ArrayList<MyMSG> msgs;
    Retrofit retrofit;
    MyApi myApi;
    Handler handler;
    int lastId;

    Runnable myTimer = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 500); // интервал обновления
            readData(); // читаем из базы
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editMSG = findViewById(R.id.editmsg);
        allMSG = findViewById(R.id.allmsg);
        btnSendMSG = findViewById(R.id.btnsend);
        scrollView = findViewById(R.id.scrollView);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://distchat.sch120.ru")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(MyApi.class);

        // тикают часы
        handler = new Handler();
        handler.postDelayed(myTimer, 500);

        btnSendMSG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editMSG.getText().toString();
                editMSG.setText("");
                myApi.send(name, s).enqueue(new Callback<ArrayList<MyMSG>>() {
                    @Override
                    public void onResponse(Call<ArrayList<MyMSG>> call, Response<ArrayList<MyMSG>> response) {
                        msgs = response.body();
                        String s = "";
                        for (int i = 0; i < msgs.size(); i++) {
                            s += msgs.get(i).name+" "+msgs.get(i).datetime+"\n"+
                                    msgs.get(i).msg+"\n\n";
                        }
                        allMSG.setText(s);
                        if(msgs.get(msgs.size()-1).id>lastId) {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            lastId = msgs.get(msgs.size()-1).id;
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<MyMSG>> call, Throwable t) {
                        allMSG.setText("не вышло");
                    }
                });
            }
        });
    }

    void readData() {
        myApi.read("1").enqueue(new Callback<ArrayList<MyMSG>>() {
            @Override
            public void onResponse(Call<ArrayList<MyMSG>> call, Response<ArrayList<MyMSG>> response) {
                msgs = response.body();
                String s = "";
                for (int i = 0; i < msgs.size(); i++) {
                    s += msgs.get(i).name+" "+msgs.get(i).datetime+"\n"+
                            msgs.get(i).msg+"\n\n";
                }
                allMSG.setText(s);
                if(msgs.get(msgs.size()-1).id>lastId) {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    lastId = msgs.get(msgs.size()-1).id;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MyMSG>> call, Throwable t) {
                allMSG.setText("не вышло");
            }
        });
    }
}

interface MyApi {
    @GET("/chat.php")
    Call<ArrayList<MyMSG>> send(@Query("name") String x, @Query("msg") String msg);

    @GET("/chat.php")
    Call<ArrayList<MyMSG>> read(@Query("read") String x);
}

class MyMSG {
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("datetime")
    String datetime;
    @SerializedName("msg")
    String msg;
}