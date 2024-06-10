package com.example.yesaude;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class Info {
    private static String username;
    private static int idEscolhido;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String user) {
        username = user;
    }

    public  static int getIdEscolhido() {return idEscolhido;}
    public  static void setIdEscolhido(int id){idEscolhido = id;}

    public static String grauPressao(String pressao){
        String texto;
        int min = 0;
        int max = 0;
        min = Integer.parseInt(pressao.substring(0, pressao.indexOf('x')));
        max = Integer.parseInt(pressao.substring(pressao.indexOf('x')+1));
        if(min <= 60 || max <= 100){
            texto = "Pressão baixa";
        }
        else if(min >= 95 || max >= 130){
            texto = "Pressão alta";
        }
        else{
            texto = "Pressão normal";
        }
        return texto;
    }

    public static String grauGlicose(String glicose){
        String texto;
        int glicemia = 0;
        glicemia = Integer.parseInt(glicose);
        if(glicemia < 70){
            texto = "Sua glicemia está baixa!";
        }
        else if(glicemia > 100){
            texto = "Sua glicemia está alta!";
        }
        else{
            texto = "Sua glicemia está boa";
        }
        return texto;
    }

    public static String grauIMC(double grau){
        String texto;
        if (grau < 18.5){
            texto = "Abaixo do peso";
        }
        else if (grau <= 24.9){
            texto = "Peso ideal";
        }
        else if (grau <= 34.9){
            texto = "Obesidade I";
        }
        else if (grau <= 39.9){
            texto = "Obesidade II";
        }
        else{
            texto = "Obesidade III";
        }
        return texto;
    }

    private static void mensagem(View v, String mensagem, String cor){
        Snackbar snackbar = Snackbar
                .make(v, mensagem, Snackbar.LENGTH_LONG);

        snackbar.setBackgroundTint(Color.parseColor(cor));
        TextView textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16); // Defina o tamanho da fonte em SP

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackbar.getView().getLayoutParams();
        params.setMargins(params.leftMargin, 100, params.rightMargin, 250); // Defina a margem superior e inferior aqui
        snackbar.getView().setLayoutParams(params);

        snackbar.show();
    }

    public static void toastCerto(Context context, String mensagem){
        showCustomToast(context, mensagem, R.drawable.snackbar_background_certo);
    }

    public static void toastErro(Context context, String mensagem){
        showCustomToast(context, mensagem, R.drawable.snackbar_background_erro);
    }

    private static void showCustomToast(Context context, String message, int drawableResId) {
        View layout = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);

        TextView textToast = layout.findViewById(R.id.text_toast);
        textToast.setText(message);
        textToast.setBackgroundResource(drawableResId);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

        toast.show();
    }



    public static void mensagemErro(View v, String mensagem){
        mensagem(v, mensagem, "#FF5E5E");
    }

    public static void mensagemCerto(View v, String mensagem){
        mensagem(v, mensagem, "#0CFF00");
    }
}