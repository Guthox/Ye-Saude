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
                            Info.mensagemCerto(atividade.getCurrentFocus(), "Imagem selecionada com sucesso");
                        }
                        else{
                            Info.mensagemErro(atividade.getCurrentFocus(), "Nenhuma imagem selecionada");
                        }
                    }
                });

        TextView btnCancelar = findViewById(R.id.cancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaConsultasRealizadas.this, TelaConsulta.class);
                startActivity(intent);
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

                String especialidadeStr = txEspecialidade.getText().toString();
                String dataStr = txData.getText().toString();
                String horaStr = txHora.getText().toString();
                String resumoStr = txResumo.getText().toString();
                String retornoStr = txRetorno.getText().toString();
                if (imagem == null){
                    imgStr = "";
                }
                else{
                    imgStr = imagem.toString();
                }
                if (especialidadeStr.isEmpty() || dataStr.isEmpty() || horaStr.isEmpty()){
                    Info.mensagemErro(v, "Especialidade, data e hora não podem estar vázios.");
                }
                else {
                    BancoConsultas bd = new BancoConsultas(v.getContext());
                    bd.inserir(Info.getUsername(), txEspecialidade.getText().toString(), txData.getText().toString(), txHora.getText().toString(), txResumo.getText().toString(), txRetorno.getText().toString(), imgStr);
                    Info.mensagemCerto(v, "Consulta Adicionada com sucesso");
                }

                //txResumo.setText(bd.pegarDados(Info.getUsername()));

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