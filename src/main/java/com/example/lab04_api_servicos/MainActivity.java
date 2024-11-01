package com.example.lab04_api_servicos;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private EditText addBike;
    private Button btnAdd, btnShow;
    private ListView showBikes;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> bikeList;
    private String link_api = "http://192.168.239.250:8080/bicicleta";
    //private String link_api = "192.168.239.250:8080/bicicleta";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBike = findViewById(R.id.addBike);
        btnAdd = findViewById(R.id.btnAdd);
        btnShow = findViewById(R.id.btnShow);
        showBikes = findViewById(R.id.showBikes);

        bikeList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bikeList);
        showBikes.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bikeName = addBike.getText().toString();
                if (!bikeName.isEmpty()) {
                    addBike(bikeName);
                    addBike.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Digite um nome para a bicicleta", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBikes();
            }
        });
    }

    private void addBike(String bikeName) {
        executorService.execute(() -> {
            String result;
            try {
                URL url = new URL(link_api);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("nome", bikeName);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                writer.write(jsonParam.toString());
                writer.flush();
                writer.close();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    result = "Bicicleta adicionada com sucesso!";
                } else {
                    result = "Falha ao adicionar bicicleta!";
                }
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                result = "Erro de conexão!";
            }
            String finalResult = result;
            mainHandler.post(() -> Toast.makeText(MainActivity.this, finalResult, Toast.LENGTH_SHORT).show());
        });
    }

    private void getBikes() {
        executorService.execute(() -> {
            ArrayList<String> bikes = new ArrayList<>();
            try {
                URL url = new URL(link_api);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder jsonBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                reader.close();

                JSONArray jsonArray = new JSONArray(jsonBuilder.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject bike = jsonArray.getJSONObject(i);
                    bikes.add(bike.getString("nome"));
                }

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mainHandler.post(() -> {
                if (bikes.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Nenhuma bicicleta encontrada!", Toast.LENGTH_SHORT).show();
                } else {
                    bikeList.clear();
                    bikeList.addAll(bikes);
                    adapter.notifyDataSetChanged();
                }
            });
        });
    }
}

