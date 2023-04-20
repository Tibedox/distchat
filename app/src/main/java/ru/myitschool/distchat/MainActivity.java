package ru.myitschool.distchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText editText1;
    EditText editText2;
    TextView textView;
    Button button;

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
    }
}