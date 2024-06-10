package com.example.yesaude;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
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

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TelaExames extends AppCompatActivity {
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> childColecao;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    Activity atividade;
    Uri imagem = null;
    Dialog dialog;
    BancoExames bd = new BancoExames(this);
    private ActivityResultLauncher<String> galleryLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_exames);
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

        dialog = new Dialog(TelaExames.this);
        dialog.setContentView(R.layout.caixa_diag_exame);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.caixa_diag_calculadora_bg));
        dialog.setCancelable(false);

        Button btnCancel = dialog.findViewById(R.id.btnCalcCancelar);
        Button btnDAdicionar = dialog.findViewById(R.id.btnAdicinar);

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog.dismiss();
            }
        });

        // Inicialize o launcher da galeria
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            imagem = result;
                            Toast toast = Toast.makeText(atividade, "Imagem selecionada", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else{
                            Toast toast = Toast.makeText(atividade, "Nenhuma imagem selecionada", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
        btnDAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText data = dialog.findViewById(R.id.inputData);
                EditText nomeExame = dialog.findViewById(R.id.inputNomeExame);
                String dataStr = data.getText().toString();
                String exameStr = nomeExame.getText().toString();
                String imgStr;
                if (imagem == null){
                    imgStr = "";
                }
                else{
                    imgStr = imagem.toString();
                }
                if (exameStr.isEmpty() || dataStr.isEmpty() || imgStr.isEmpty()){
                    Toast toast = Toast.makeText(v.getContext(), "Campos inválidos", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    BancoExames bd = new BancoExames(v.getContext());
                    bd.inserir(Info.getUsername(), exameStr, dataStr, imgStr);
                    Toast toast = Toast.makeText(v.getContext(), "Exame adicionado com sucesso", Toast.LENGTH_SHORT);
                    toast.show();
                    atividade.recreate();
                }
                dialog.dismiss();
            }
        });

        ImageView btnAdicionar = findViewById(R.id.btnAddExame);
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        Button btnAnexar = dialog.findViewById(R.id.btnAnexar);
        btnAnexar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryLauncher.launch("image/*");
            }
        });

    }

    private void createCollection() {
        LinkedList<String[]> lista = new LinkedList<>();
        String medidas = bd.pegarDados(Info.getUsername());
        Scanner sc = new Scanner(medidas);
        int indice = 0;
        double valorImc;
        String info;
        while (sc.hasNextLine()){
            String linha = sc.nextLine();
            Scanner scItem = new Scanner(linha);
            scItem.useDelimiter(",");
            String[] item = new String[4];
            item[0] = "ID: " + scItem.next();
            item[1] = "Tipo: " + scItem.next();
            item[2] = "Data: " + scItem.next();
            item[3] = "Exame: " + scItem.next();
            lista.add(item);
        }
        sc.close();
        childColecao = new HashMap<String, List<String>>();
        int n = 0;
        for(String group : groupList){
            loadChild(lista.get(n));
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
        String especialidades = bd.pegarNomesExames(Info.getUsername());
        Scanner sc = new Scanner(especialidades);
        int i = 1;
        while (sc.hasNextLine()){
            groupList.add("" + i++ + ") " + sc.nextLine());
        }
    }
}