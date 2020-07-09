package me.noteon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import me.noteon.models.UserList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // Activity Main
    EditText edTextURL;
    Button btForm, btAtualizaForm, btSincronizar;

    // Activity Second
    Button btFormVoltar, btFormSalvar;
//    EditText edTextFormField1;
    EditText edusername,edfname,edlname,edemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadWidgetsMainActivity();
        setListenersMainActivity();

        boolean isFilePresent = isFilePresent(getApplicationContext(), "storage.json");
        if(!isFilePresent) {
            create(getApplicationContext(), "storage.json", "{\"entries\": []}");
        }
    }

    private void loadWidgetsMainActivity() {
        edTextURL = findViewById(R.id.edtTextURL);
        btForm = findViewById(R.id.btnForm);
        btAtualizaForm = findViewById(R.id.btnAtualizaForm);
        btSincronizar = findViewById(R.id.btnSincronizar);
    }

    private void loadWidgetsSecondActivity() {
        btFormVoltar = findViewById(R.id.btnFormVoltar);
        btFormSalvar = findViewById(R.id.btnFormSalvar);
//        edTextFormField1 = findViewById(R.id.edtTextFormField1);
        edusername = findViewById(R.id.username);
        edfname = findViewById(R.id.fname);
        edlname = findViewById(R.id.lname);
        edemail = findViewById(R.id.email);

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

        btAtualizaForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edTextURL.getText().toString().length() > 0) {
                    if (!edTextURL.getText().toString()
                            .matches("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,4}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)")) {
                        Toast.makeText(MainActivity.this, "Insira uma URL válida!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        ApiClient.updateBaseURL(edTextURL.getText().toString());
                        Toast.makeText(MainActivity.this, "URL de Formulário Atualizada!",
                                Toast.LENGTH_SHORT).show();
                        edTextURL.setText("");
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Insira uma URL para atualizar!",
                            Toast.LENGTH_SHORT).show();
                    edTextURL.requestFocus();
                }
            }
        });

        btSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jsonString = read(getApplicationContext(), "storage.json");
                UserList userList = new Gson().fromJson(jsonString, UserList.class);

                int entriesToSend = userList.entries.size();

                if (entriesToSend == 0) {
                    Toast.makeText(MainActivity.this,
                            "Não há registros para sincronizar" ,Toast.LENGTH_LONG).show();
                } else {
                    int entriesSended = 0;
                    ArrayList<UserRequest> entriesNotSended = new ArrayList<>();

                    for (UserRequest entry : userList.entries) {
                        if (sendUser(entry)) {
                            entriesSended++;
                        } else {
                            entriesNotSended.add(entry);
                        }
                    }

                    Toast.makeText(MainActivity.this,"Registros enviados: "
                            + entriesSended + "/" + entriesToSend ,Toast.LENGTH_LONG).show();

                    userList.entries.clear();
                    userList.entries.addAll(entriesNotSended);
                    Gson gson = new Gson();
                    create(getApplicationContext(), "storage.json", gson.toJson(userList));
                }
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



        btFormSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(buildUser());
                edusername.setText("");
                edfname.setText("");
                edlname.setText("");
                edemail.setText("");
            }
        });
    }

    private void saveData(UserRequest userRequest) {
        String jsonString = read(getApplicationContext(), "storage.json");
        UserList userList = new Gson().fromJson(jsonString, UserList.class);

        userList.entries.add(userRequest);
        Gson gson = new Gson();
        create(getApplicationContext(), "storage.json", gson.toJson(userList));

        Toast.makeText(MainActivity.this,"Registro salvo com sucesso",
                Toast.LENGTH_SHORT).show();
    }

    public UserRequest buildUser(){
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(edusername.getText().toString());
        userRequest.setEmail(edemail.getText().toString());
        userRequest.setLast_name(edlname.getText().toString());
        userRequest.setFirst_name(edfname.getText().toString());

        return userRequest;
    }

    public boolean sendUser(final UserRequest userRequest) {
        final AtomicBoolean atomicBoolean = new AtomicBoolean(true);


        Call<UserResponse> userResponseCall = ApiClient.getUserService().saveUser(userRequest);
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                if(response.isSuccess()) {
                    atomicBoolean.set(true);
                } else {
                    Toast.makeText(MainActivity.this,"Não foi possível salvar. (Erro "  +  response.code() + ")" ,Toast.LENGTH_LONG).show();
                    atomicBoolean.set(false);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Request failed "+ t.getCause(),Toast.LENGTH_LONG).show();
                atomicBoolean.set(false);
            }
        });

        return atomicBoolean.get();
    }

    public String read(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (FileNotFoundException fileNotFound) {
            return null;
        } catch (IOException ioException) {
            return null;
        }
    }

    public boolean create(Context context, String fileName, String jsonString){
        String FILENAME = "storage.json";
        try {
            FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            if (jsonString != null) {
                fos.write(jsonString.getBytes());
            }
            fos.close();
            return true;
        } catch (FileNotFoundException fileNotFound) {
            return false;
        } catch (IOException ioException) {
            return false;
        }

    }

    public boolean isFilePresent(Context context, String fileName) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }
}