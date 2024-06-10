package com.example.yesaude;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Scanner;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        // Deixa o menu no topo em verde escuro.
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.verdebom));

        TextView textView = findViewById(R.id.nomeUsuario);
        BancoDeDados bd = new BancoDeDados(this);
        textView.setText(bd.getNome(Info.getUsername()));
        if (textView.getText().toString().isEmpty()){
            textView.setText("-----");
        }
        // Coloca os ultimos valores no menu principal.
        informarIMC();
        informarPressao();
        informarGlicose();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // ######################################## BOTOES #########################################
        Button buttonMed = findViewById(R.id.buttonMed);
        buttonMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, TelaMedicamentos.class);
                startActivity(intent);
            }
        });

        Button buttonExames = findViewById(R.id.buttonExames);
        buttonExames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, TelaExames.class);
                startActivity(intent);
            }
        });

        Button buttonConsultas = findViewById(R.id.buttonConsultas);
        buttonConsultas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, TelaConsulta.class);
                startActivity(intent);
            }
        });

        Button buttonPressao = findViewById(R.id.buttonPressao);
        buttonPressao.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Menu.this, TelaPressao.class);
                startActivity(intent);
            }
        });

        // Ao clicar nos quadrados de imc, peso e altura, muda a tela para a tela de IMC.
        Button btnImc = findViewById(R.id.btnImc);
        btnImc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, IMC_EL.class);
                startActivity(intent);
            }
        });

        Button btnGlicose = findViewById(R.id.buttonGlicose);
        btnGlicose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, TelaGlicose.class);
                startActivity(intent);
            }
        });

        Button btnImc2 = findViewById(R.id.btnImc2);
        btnImc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, IMC_EL.class);
                startActivity(intent);
            }
        });

        // #########################################################################################


    }
    @Override
    protected void onResume(){
        super.onResume();
        informarIMC();
        informarPressao();
        informarGlicose();
    }


    private void informarPressao(){
        TextView pressao = findViewById(R.id.caixa1Cima);
        TextView desc = findViewById(R.id.caixa1Baixo);

        BancoPressao bd = new BancoPressao(this);
        String dados = bd.ultimaMedida(Info.getUsername());
        pressao.setText(dados + " mmHg");
        if (dados.equals("---")){
            desc.setText("---");
        }
        else{
            desc.setText(Info.grauPressao(dados));
        }
    }

    private void informarGlicose(){
        TextView glicose = findViewById(R.id.caixa2Cima);
        TextView desc = findViewById(R.id.caixa2Baixo);

        BancoGlicose bd = new BancoGlicose(this);
        String dados = bd.ultimaMedida(Info.getUsername());
        glicose.setText(dados + " mg/dl");
        if (dados.equals("---")){
            glicose.setText("---");
            desc.setText("---");
        }
        else{
            desc.setText(Info.grauGlicose(dados));
        }
    }
    // Coloca os valores de peso, altura e imc no menu princiapal
    // Coloca --- se não tiver nenhuma medida
    private void informarIMC(){
        TextView peso = findViewById(R.id.caixa3Cima);
        TextView altura = findViewById(R.id.caixa3Baixo);
        TextView imc = findViewById(R.id.caixa4Cima);
        TextView desc = findViewById(R.id.caixa4Baixo);

        BancoIMC bdImc = new BancoIMC(this);

        String dados = bdImc.ultimaMedida(Info.getUsername());
        Scanner sc = new Scanner(dados);
        sc.useDelimiter(",");
        altura.setText(sc.next() + " m");
        peso.setText(sc.next() + " kg");
        imc.setText(sc.next());
        if (imc.getText().equals("---")){
            desc.setText("---");
        }
        else{
            String texto = "";
            double grau;
            try{
                grau = Double.parseDouble(imc.getText().toString());
                texto = Info.grauIMC(grau);
            }
            catch (Exception e){
                texto = "---";
            }

            desc.setText(texto);
        }
        sc.close();
    }
}