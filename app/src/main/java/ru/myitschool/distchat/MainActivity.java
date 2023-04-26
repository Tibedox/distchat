package ru.myitschool.distchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText editText1;
    EditText editText2;
    TextView textView;
    Button button;
    MyData myData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText1 = findViewById(R.id.edit1);
        editText2 = findViewById(R.id.edit2);
        textView = findViewById(R.id.text1);
        button = findViewById(R.id.button);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://distchat.sch120.ru")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = Integer.parseInt(editText1.getText().toString());
                int y = Integer.parseInt(editText2.getText().toString());
                myApi.getData(x, y).enqueue(new Callback<MyData>() {
                    @Override
                    public void onResponse(Call<MyData> call, Response<MyData> response) {
                        myData = response.body();
                        textView.setText("x0+y="+myData.z);
                    }

                    @Override
                    public void onFailure(Call<MyData> call, Throwable t) {
                        textView.setText("не вышло");
                    }
                });
            }
        });
    }
}