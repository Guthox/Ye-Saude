package com.example.yesaude;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TelaMedicamentos extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_medicamentos);

        RecyclerView recyclerView = findViewById(R.id.lista);

        List<ListaDeMedicamentos> itens = new ArrayList<ListaDeMedicamentos>();
        itens.add(new ListaDeMedicamentos("Bromazepam", "7:00"));
        itens.add(new ListaDeMedicamentos("Ibuprofeno", "13:00"));
        itens.add(new ListaDeMedicamentos("Rivotril", "21:00"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AdapterListaMed(getApplicationContext(), itens));
    }
}
