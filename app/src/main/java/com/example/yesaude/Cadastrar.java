package com.example.yesaude;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;

import android.widget.TextView;

import androidx.core.view.WindowInsetsCompat;

public class Cadastrar extends AppCompatActivity {

    BancoDeDados bd = new BancoDeDados(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastrar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView edit = findViewById(R.id.login);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cadastrar.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button cadastrar = findViewById(R.id.btnCadastrar);
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCadastrado()){
                    TextView edit = findViewById(R.id.erro);
                    edit.setText("Usuário já cadastrado");
                    return;
                }
                if (!validarCredenciais()){
                    TextView edit = findViewById(R.id.erro);
                    edit.setText("Usuario e senha deve ter entre 4 e 20 caracteres");
                    return;
                }
                EditText edit = findViewById(R.id.usuario);
                String usuario = edit.getText().toString();
                edit = findViewById(R.id.senha);
                String senha = edit.getText().toString();
                edit = findViewById(R.id.nome);
                String nome = edit.getText().toString();
                bd.inserir(usuario, nome, senha);
                Intent intent = new Intent(Cadastrar.this, MainActivity.class);
                startActivity(intent);
            }
        });



    }

    private boolean checkCadastrado(){
        EditText edit = findViewById(R.id.usuario);
        if (bd.checkUsuario(edit.getText().toString())) { // Usuario já cadastrado
            return true;
        }
        return false;
    }

    private boolean validarCredenciais(){
        EditText edit = findViewById(R.id.usuario);
        String usuario = edit.getText().toString();
        edit = findViewById(R.id.senha);
        String senha = edit.getText().toString();
        if (usuario.length() > 4 && usuario.length() <= 20){
            if (senha.length() > 4 && senha.length() <= 20){
                return true;
            }
        }
        return false;
    }
}