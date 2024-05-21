package com.example.yesaude;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListaDeMedicamentos extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_medicamentos);

        List<TelaMedicamentos> meds = todosOsMeds();

        ListView listaDeMedicamentos = (ListView) findViewById(R.id.lista);

        ArrayAdapter<TelaMedicamentos> adapter = new ArrayAdapter<TelaMedicamentos>(this, android.R.layout.simple_list_item_1, meds);

        listaDeMedicamentos.setAdapter(adapter);

    }
    private List<TelaMedicamentos> todosOsMeds() {
        return new ArrayList<>(Arrays.asList(
                new TelaMedicamentos("Ibuprofeno", "13:00"),
                new TelaMedicamentos("Bromazepam", "7:00")));
    }

}

