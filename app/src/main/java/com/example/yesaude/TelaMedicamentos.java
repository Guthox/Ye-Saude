package com.example.yesaude;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.Scanner;

public class TelaMedicamentos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_medicamentos);

        // Deixa o menu no topo em verde escuro.
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.verdebom));

        RecyclerView recyclerView = findViewById(R.id.lista);

        List<ListaDeMedicamentos> itens = new ArrayList<ListaDeMedicamentos>();
        BancoMedicar bd = new BancoMedicar(this);
        bd.inserir(Info.getUsername(), "medicamento", "hora");
        bd.inserir(Info.getUsername(), "medicamento2", "hora2");
        String dados = bd.pegarDados(Info.getUsername());
        Scanner sc = new Scanner(dados);
        while (sc.hasNextLine()){
            String linha = sc.nextLine();
            Scanner scItem = new Scanner(linha);
            scItem.useDelimiter(",");
            itens.add(new ListaDeMedicamentos(scItem.next(), scItem.next()));
        }
        sc.close();

        for (ListaDeMedicamentos item : itens) {
            String nomeMedicamento = item.getMeds();
            if (nomeMedicamento.length() > 11) {
                // Ajustando a string se exceder 11 caracteres
                nomeMedicamento = nomeMedicamento.substring(0, 11) + "...";
                item.setMeds(nomeMedicamento);
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new AdapterListaMed(getApplicationContext(), itens));
        }
    }
}

