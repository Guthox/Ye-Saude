package com.example.yesaude;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class TelaConsultasRealizadas extends AppCompatActivity {

    Uri imagem = null;
    Activity atividade;
    private ActivityResultLauncher<String> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_consultas_realizadas);

        atividade = this;

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.verdebom));


        ImageView salvarFotoBtn = findViewById(R.id.btnSalvarFoto);

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

        TextView btnSalvar = findViewById(R.id.Salvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txEspecialidade = findViewById(R.id.txEspecialidade);
                EditText txData = findViewById(R.id.txData);
                EditText txHora = findViewById(R.id.txHora);
                EditText txResumo = findViewById(R.id.txResumo);
                EditText txRetorno = findViewById(R.id.txRetorno);
                String imgStr;
                if (imagem == null){
                    imgStr = "";
                }
                else{
                    imgStr = imagem.toString();
                }
                BancoConsultas bd = new BancoConsultas(v.getContext());
                bd.inserir(Info.getUsername(), txEspecialidade.getText().toString(), txData.getText().toString(), txHora.getText().toString(), txResumo.getText().toString(), txRetorno.getText().toString(), imagem.toString());
                Toast toast = Toast.makeText(v.getContext(), "Consulta adicionada", Toast.LENGTH_SHORT);
                toast.show();
                atividade.recreate();
                txResumo.setText(bd.pegarDados(Info.getUsername()));

//              Pegar a imagem pelo Uri
//                try {
//                    // Carregar a imagem correspondente ao Uri
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                    // Exibir a imagem ou fazer o que desejar com ela
//                    imageView.setImageBitmap(bitmap);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });

        salvarFotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryLauncher.launch("image/*");
            }
        });
    }
}