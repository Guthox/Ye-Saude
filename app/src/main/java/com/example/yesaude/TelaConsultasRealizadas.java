package com.example.yesaude;

import android.app.Activity;
import android.app.DirectAction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.Scanner;

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

        if (Info.getEditarConsulta()){
            Info.setEditarConsulta(false);
            int id = Info.getIdEscolhido();
            EditText txEspecialidade = findViewById(R.id.txEspecialidade);
            EditText txData = findViewById(R.id.txData);
            EditText txHora = findViewById(R.id.txHora);
            EditText txResumo = findViewById(R.id.txResumo);
            EditText txRetorno = findViewById(R.id.txRetorno);
            BancoConsultas bd = new BancoConsultas(this);
            String dados = bd.pegarDados(Info.getUsername(), id);
            Scanner sc = new Scanner(dados);
            sc.useDelimiter(",");
            sc.next(); // Pula o primeiro pois é id
            txEspecialidade.setText(sc.next());
            txData.setText(sc.next());
            txHora.setText(sc.next());
            txResumo.setText(sc.next());
            txRetorno.setText(sc.next());
            sc.close();
        }


        ImageView salvarFotoBtn = findViewById(R.id.btnSalvarFoto);

        // Inicialize o launcher da galeria
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            imagem = result;
                            Info.toastCerto(atividade, "Imagem selecionada com sucesso");
                        }
                        else{
                            Info.toastErro(atividade, "Nenhuma imagem selecionada");
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
                    if (bd.verificarIdExistente(Info.getIdEscolhido())){
                        bd.alterarConsulta(Info.getIdEscolhido(), Info.getUsername(), txEspecialidade.getText().toString(), txData.getText().toString(), txHora.getText().toString(), txResumo.getText().toString(), txRetorno.getText().toString(), imgStr);
                        Info.mensagemCerto(v, "Consulta atualizada com sucesso");
                        Info.setIdEscolhido(-1);
                    }
                    else{
                        bd.inserir(Info.getUsername(), txEspecialidade.getText().toString(), txData.getText().toString(), txHora.getText().toString(), txResumo.getText().toString(), txRetorno.getText().toString(), imgStr);
                        Info.mensagemCerto(v, "Consulta adicionada com sucesso");
                        Info.setIdEscolhido(-1);
                    }
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

        EditText txData = findViewById(R.id.txData);
        txData.addTextChangedListener(new TextWatcher() {
            private boolean isDeleting; // Variável para rastrear se o usuário está deletando

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                isDeleting = count > after; // Verifica se está deletando
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Não é necessário implementar neste caso
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (!isDeleting) {
                    // Verifica e corrige formato da data durante a inserção
                    if (text.length() == 2 || text.length() == 5) {
                        if (text.charAt(text.length() - 1) != '/') {
                            text += "/";
                            txData.setText(text);
                            txData.setSelection(text.length()); // Move o cursor para o final
                        }
                    } else if (text.length() > 10) {
                        // Limita o tamanho máximo do texto para "dd/mm/yyyy"
                        text = text.substring(0, 10);
                        txData.setText(text);
                        txData.setSelection(10); // Move o cursor para o final
                    }
                }

                // Validação de valores de data
                if (text.length() == 10) {
                    String[] parts = text.split("/");
                    try {
                        int dia = Integer.parseInt(parts[0]);
                        int mes = Integer.parseInt(parts[1]);
                        int ano = Integer.parseInt(parts[2]);
                        if (dia > 31 || mes > 12 || ano < 1900 || ano > 2100) {
                            Info.toastErro(getApplicationContext(), "Valores de data inválidos");
                            txData.setText(""); // Limpa o campo se inválido
                        }
                    } catch (NumberFormatException e) {
                        // Trata exceção se não for possível converter para int
                        Info.toastErro(getApplicationContext(), "Formato de data inválido");
                        txData.setText(""); // Limpa o campo em caso de erro
                    }
                }
            }
        });

        EditText txHora = findViewById(R.id.txHora);
        txHora.addTextChangedListener(new TextWatcher() {
            private boolean isDeleting; // Variável para rastrear se o usuário está deletando

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                isDeleting = count > after; // Verifica se está deletando
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Não é necessário implementar neste caso
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (!isDeleting) {
                    // Verifica e corrige formato da hora durante a inserção
                    if (text.length() == 2) {
                        if (text.charAt(text.length() - 1) != ':') {
                            text += ":";
                            txHora.setText(text);
                            txHora.setSelection(text.length()); // Move o cursor para o final
                        }
                    } else if (text.length() > 5) {
                        // Limita o tamanho máximo do texto para "hh:mm"
                        text = text.substring(0, 5);
                        txHora.setText(text);
                        txHora.setSelection(5); // Move o cursor para o final
                    }
                }

                // Validação de valores de hora
                if (text.length() == 5 && text.charAt(2) == ':') {
                    String[] parts = text.split(":");
                    try {
                        int horas = Integer.parseInt(parts[0]);
                        int minutos = Integer.parseInt(parts[1]);
                        if (horas > 23 || minutos > 59) {
                            Info.toastErro(getApplicationContext(), "Valores de hora inválidos");
                            txHora.setText(""); // Limpa o campo se inválido
                        }
                    } catch (NumberFormatException e) {
                        // Trata exceção se não for possível converter para int
                        Info.toastErro(getApplicationContext(), "Formato de hora inválido");
                        txHora.setText(""); // Limpa o campo em caso de erro
                    }
                }
            }
        });



    }
}