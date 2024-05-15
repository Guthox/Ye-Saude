package com.example.yesaude;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {

    private BancoDeDados bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bd = new BancoDeDados(this);

        // BOTAO LOGIN
        Button login = findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit = findViewById(R.id.usuario);
                String usuario = edit.getText().toString();
                edit = findViewById(R.id.senha);
                String senha = edit.getText().toString();
                //if (bd.checkLogin(usuario, senha)){
                if (true){
                    TextView erro = findViewById(R.id.txtInvalido);
                    erro.setText("");
                    Intent intent = new Intent(MainActivity.this, Menu.class);
                    startActivity(intent);
                }
                else{
                    TextView invalidoTxt = findViewById(R.id.txtInvalido);
                    invalidoTxt.setText("Usuario ou senha inválidos");
                }

            }
        });

        // BOTAO CADASTRAR
        TextView cadastrar = findViewById(R.id.cadastrar);
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Cadastrar.class);
                startActivity(intent);
            }
        });
    }


}