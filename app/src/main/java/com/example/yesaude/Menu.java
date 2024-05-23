package com.example.yesaude;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

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

        // Coloca os ultimos valores no menu principal.
        informarIMC();

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

        // Ao clicar nos quadrados de imc, peso e altura, muda a tela para a tela de IMC.
        Button btnImc = findViewById(R.id.btnImc);
        btnImc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, IMC_EL.class);
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