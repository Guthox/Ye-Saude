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
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class IMC_EL extends AppCompatActivity {
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> childColecao;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    BancoIMC imc = new BancoIMC(this);
    Activity atividade;

    //Dialogbox calculadora variaveis

    Dialog dialog;
    Button btnCalcular, btnCalcCancelar;
    FloatingActionButton btnAbrirDialBoxCalc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc_el);
        createGroupList();
        createCollection();

        atividade = this;

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.verdebom));

        expandableListView = findViewById(R.id.listaDatas);
        expandableListAdapter = new MyExpandableListAdapter(this, groupList, childColecao, expandableListView, this);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition = -1;
            @Override
            public void onGroupExpand(int i) {
                if(lastExpandedPosition != -1 && i != lastExpandedPosition){
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = i;
            }
        });

//        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
//                String selected = expandableListAdapter.getChild(i,i1).toString();
//                Toast.makeText(getApplicationContext(), "Selected: " + selected, Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
        //CODIGO CAIXA DE DIALOGO CALCULADORA
        dialog = new Dialog(IMC_EL.this);
        dialog.setContentView(R.layout.caixa_diag_calculadora);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.caixa_diag_calculadora_bg));
        dialog.setCancelable(false);

        btnCalcular = dialog.findViewById(R.id.btnCalcular);
        btnCalcCancelar = dialog.findViewById(R.id.btnCalcCancelar);

        //botao flutuante pra abrir a caixa de dial
        ImageView btnCalc = findViewById(R.id.btnCalcI);
//        btnAbrirDialBoxCalc = dialog.findViewById(R.id.btnAbrirDialBoxCalc);

        btnCalcCancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //faz a caixa de diag sumir
            }
        });

        btnCalcular.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText peso = dialog.findViewById(R.id.inputPeso);
                EditText altura = dialog.findViewById(R.id.inputAltura);
                double pesoD;
                double alturaD;
                try {
                    pesoD = Double.parseDouble(peso.getText().toString());
                    alturaD = Double.parseDouble(altura.getText().toString());
                    double imc = pesoD / (alturaD * alturaD);
                    imc = Math.round(imc * 10.0) / 10.0; // 1 casa decimal
                    BancoIMC bdImc = new BancoIMC(v.getContext());
                    Format fo = new SimpleDateFormat("dd-MM-yyyy");
                    Date data = Calendar.getInstance().getTime();
                    bdImc.inserir(Info.getUsername(), alturaD, pesoD, imc, fo.format(data));
                    Toast toast = Toast.makeText(v.getContext(), "IMC adicionado", Toast.LENGTH_SHORT);
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

        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });



    }


    private void createCollection() {
        LinkedList<String[]> lista = new LinkedList<>();
        String medidas = imc.pegarDados(Info.getUsername());
        Scanner sc = new Scanner(medidas);
        int indice = 0;
        double valorImc;
        String info;
        while (sc.hasNextLine()){
            String linha = sc.nextLine();
            Scanner scItem = new Scanner(linha);
            scItem.useDelimiter(",");
            String[] item = new String[5];
            item[0] = "ID: " + scItem.next();
            item[1] = "Altura: " + scItem.next();
            item[2] = "Peso: " + scItem.next();
            valorImc = Double.parseDouble(scItem.next());
            item[3] = "IMC: " + valorImc + " - " + Info.grauIMC(valorImc);
            item[4] = "Dia: " + scItem.next();
            lista.add(item);
        }
        sc.close();
        childColecao = new HashMap<String, List<String>>();
        int n = 0;
        for(String group : groupList){
            if (group.equals("Medida " + (n + 1))){
                loadChild(lista.get(n));
            }
            n++;
            childColecao.put(group, childList);
        }
    }


    private void loadChild(String[] mobileModels) {
        childList = new ArrayList<>();
        for(String model : mobileModels){
            childList.add(model);
        }
    }

    private void createGroupList() {
        groupList = new ArrayList<>();
        int n = imc.contarTuplas(Info.getUsername());
        for (int i = 0; i < n; i++){
            groupList.add("Medida " + (i + 1));
        }
    }



}