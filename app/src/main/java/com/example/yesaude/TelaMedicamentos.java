package com.example.yesaude;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
                    if (medStr.isEmpty() || horaStr.isEmpty()){
                        Info.toastErro(v.getContext(), "Valores inválidos");
                    }
                    else{
                        BancoMedicar bd = new BancoMedicar(v.getContext());
                        bd.inserir(Info.getUsername(), medStr, horaStr);
                        Info.toastCerto(v.getContext(), "Remédio adicionado");
                        atividade.recreate();
                    }
                }
                catch (NumberFormatException e){
                    Info.toastErro(v.getContext(), "Valores inválidos");
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

        EditText inputHora = dialog.findViewById(R.id.inputHora);
        inputHora.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não é necessário implementar neste caso
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Não é necessário implementar neste caso
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();

                // Verifica e corrige formato da hora
                if (text.length() == 3 && text.charAt(2) != ':') {
                    // Insere ":" se ainda não está presente
                    text = text.substring(0, 2) + ":" + text.charAt(2);
                    inputHora.setText(text);
                    inputHora.setSelection(text.length()); // Move o cursor para o final
                } else if (text.length() > 5) {
                    // Limita o tamanho máximo do texto para "hh:mm"
                    inputHora.setText(text.substring(0, 5));
                    inputHora.setSelection(5); // Move o cursor para o final
                }

                // Validação de valores de hora
                if (text.length() == 5 && text.charAt(2) == ':') {
                    String[] parts = text.split(":");
                    try {
                        int horas = Integer.parseInt(parts[0]);
                        int minutos = Integer.parseInt(parts[1]);
                        if (horas > 23 || minutos > 59) {
                            Info.toastErro(getApplicationContext(), "Valores de hora inválidos");
                            inputHora.setText(""); // Limpa o campo se inválido
                        }
                    } catch (NumberFormatException e) {
                        // Trata exceção se não for possível converter para int
                        Info.toastErro(getApplicationContext(), "Formato de hora inválido");
                        inputHora.setText(""); // Limpa o campo em caso de erro
                    }
                }
            }
        });


    }



    //bd.inserir(Info.getUsername(), "medicamento", "hora");

}

