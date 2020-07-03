package me.noteon;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Activity Main
    EditText edTextURL;
    Button btForm, btAtualizaForm;

    // Activity Second
    Button btFormVoltar, btFormEnviar;
    EditText edTextFormField1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadWidgetsMainActivity();
        setListenersMainActivity();
    }

    private void loadWidgetsMainActivity() {
        edTextURL = findViewById(R.id.edtTextURL);
        btForm = findViewById(R.id.btnForm);
        btAtualizaForm = findViewById(R.id.btnAtualizaForm);
    }

    private void loadWidgetsSecondActivity() {
        btFormVoltar = findViewById(R.id.btnFormVoltar);
        btFormEnviar = findViewById(R.id.btnFormEnviar);
        edTextFormField1 = findViewById(R.id.edtTextFormField1);
    }

    private void setListenersMainActivity() {
        btForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Indo para o formulário!", Toast.LENGTH_SHORT).show();
                setContentView(R.layout.activity_second);
                loadWidgetsSecondActivity();
                setListenersSecondActivity();
            }
        });
    }

    private void setListenersSecondActivity() {
        btFormVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,
                        "Voltando para a Home", Toast.LENGTH_SHORT).show();
                setContentView(R.layout.activity_main);
                loadWidgetsMainActivity();
                setListenersMainActivity();
            }
        });

        btFormEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}