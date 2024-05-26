package com.example.yesaude;

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
            texto = "Sua pressão está baixa!";
        }
        else if(min >= 95 || max >= 130){
            texto = "Sua pressão está alta";
        }
        else{
            texto = "Sua pressão está boa!";
        }
        return texto;
    }

    public static String grauGlicose(String glicose){
        return "TODO";
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
}
