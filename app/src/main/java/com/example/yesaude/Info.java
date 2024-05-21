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
}
