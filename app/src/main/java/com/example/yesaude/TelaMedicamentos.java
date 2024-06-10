package com.example.yesaude;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TelaMedicamentos extends AppCompatActivity {

    Activity atividade;
    Dialog dialog;
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

        //CODIGO CAIXA DE DIALOGO CALCULADORA
        dialog = new Dialog(TelaMedicamentos.this);
        dialog.setContentView(R.layout.caixa_diag_med);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.caixa_diag_calculadora_bg));
        dialog.setCancelable(false);

        Button btnCalcCancelar = dialog.findViewById(R.id.btnMedCancelar);
        btnCalcCancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //faz a caixa de diag sumir
            }
        });

        atividade = this;

        Button btnCalcAceitar = dialog.findViewById(R.id.btnAddMed);
        btnCalcAceitar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                EditText med = dialog.findViewById(R.id.inputMed);
                EditText hora = dialog.findViewById(R.id.inputHora);
                String medStr;
                String horaStr;
                try {
                    medStr = med.getText().toString();
                    horaStr = hora.getText().toString();
                    BancoMedicar bd = new BancoMedicar(v.getContext());
                    bd.inserir(Info.getUsername(), medStr, horaStr);
                    Toast toast = Toast.makeText(v.getContext(), "Remédio adicionado", Toast.LENGTH_SHORT);
                    toast.show();
                    atividade.recreate();
                }
                catch (NumberFormatException e){
                    Toast toast = Toast.makeText(v.getContext(), "Valores inválidos", Toast.LENGTH_SHORT);
                    toast.show();
                }
                dialog.dismiss();
            }
        });

        ImageView btnMed = findViewById(R.id.btnMed);
        btnMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

    }



    //bd.inserir(Info.getUsername(), "medicamento", "hora");

}

