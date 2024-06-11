package com.example.yesaude;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TelaConsulta extends AppCompatActivity {
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> childColecao;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    BancoConsultas bd = new BancoConsultas(this);
    Activity atividade;
    String especialidades;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_consulta);
        createGroupList();
        createCollection();

        atividade = this;

        // Deixa o menu no topo em verde escuro.
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.verdebom));

        ImageView buttonConsulta = findViewById(R.id.btnAddConsulta);
        buttonConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Info.setIdEscolhido(-1);
                Intent intent = new Intent(TelaConsulta.this, TelaConsultasRealizadas.class);
                startActivity(intent);
            }
        });

    }

    private void iniciarTela(){
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
    }

    private void createCollection() {
        LinkedList<String[]> lista = new LinkedList<>();
        String medidas = bd.pegarTipoData(Info.getUsername());
        Scanner sc = new Scanner(medidas);
        int indice = 0;
        while (sc.hasNextLine()){
            String linha = sc.nextLine();
            Scanner scItem = new Scanner(linha);
            scItem.useDelimiter(",");
            String[] item = new String[3];
            item[0] = "ID: " + scItem.next();
            item[1] = "Especialidade: " + scItem.next();
            item[2] = "Data: " + scItem.next();
            lista.add(item);
        }
        sc.close();
        childColecao = new HashMap<String, List<String>>();
        int n = 0;
        for(String group : groupList){
            loadChild(lista.get(n));
            childColecao.put(group, childList);
            n++;
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
        especialidades = bd.pegarNomesExames(Info.getUsername());
        Scanner sc = new Scanner(especialidades);
        int i = 1;
        while (sc.hasNextLine()){
            groupList.add("" + i++ + ") " + sc.nextLine());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        groupList.clear();
        createGroupList();
        createCollection();
        iniciarTela();
    }

}