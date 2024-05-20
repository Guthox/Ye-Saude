package com.example.yesaude;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc_el);
        createGroupList();
        createCollection();

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.verdebom));

        expandableListView = findViewById(R.id.listaDatas);
        expandableListAdapter = new MyExpandableListAdapter(this, groupList, childColecao);
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
    }

    private void createCollection() {
        LinkedList<String[]> lista = new LinkedList<>();
        String medidas = imc.pegarDados();
        Scanner sc = new Scanner(medidas);
        int indice = 0;
        while (sc.hasNextLine()){
            String linha = sc.nextLine();
            Scanner scItem = new Scanner(linha);
            scItem.useDelimiter(",");
            String[] item = new String[4];
            item[0] = "Altura: " + scItem.next();
            item[1] = "Peso: " + scItem.next();
            item[2] = "IMC: " + scItem.next();
            item[3] = "Dia: " + scItem.next();
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
        int n = imc.contarTuplas();
        for (int i = 0; i < n; i++){
            groupList.add("Medida " + (i + 1));
        }
    }

}