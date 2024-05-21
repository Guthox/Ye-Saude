package com.example.yesaude;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BancoIMC extends SQLiteOpenHelper {
// Exemplo de uso
//    BancoIMC imc = new BancoIMC(this);
//    imc.inserir(1.83, 94.0, 27.3, "hoje");
    public static final String databaseName = "IMC.db";

    public BancoIMC(@Nullable Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IMC (id INTEGER primary key AUTOINCREMENT, user TEXT not null, altura REAL not null, peso REAL not null, indiceIMC REAL not null, data TEXT not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists IMC");
    }

    public boolean inserir(String user, double altura, double peso, double indiceIMC, String data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user", user);
        contentValues.put("altura", altura);
        contentValues.put("peso", peso);
        contentValues.put("indiceIMC", indiceIMC);
        contentValues.put("data", data);
        long result = db.insert("IMC", null, contentValues);
        return result != -1;
    }

    // Devolve um csv com os dados da tabela.
    public String pegarDados(String user){
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder dados = new StringBuilder();

        Cursor cursor = db.rawQuery("SELECT id, user, altura, peso, indiceIMC, data FROM IMC WHERE user = ?", new String[]{user});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int alturaIndex = cursor.getColumnIndex("altura");
                int pesoIndex = cursor.getColumnIndex("peso");
                int indiceIMCIndex = cursor.getColumnIndex("indiceIMC");
                int dataIndex = cursor.getColumnIndex("data");

                if (idIndex != -1 && alturaIndex != -1 && pesoIndex != -1 && indiceIMCIndex != -1 && dataIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    float altura = cursor.getFloat(alturaIndex);
                    float peso = cursor.getFloat(pesoIndex);
                    float indiceIMC = cursor.getFloat(indiceIMCIndex);
                    String data = cursor.getString(dataIndex);

                    dados.append(id)
                            .append(",").append(altura)
                            .append(",").append(peso)
                            .append(",").append(indiceIMC)
                            .append(",").append(data)
                            .append("\n");
                }
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return dados.toString();
    }

    // Conta o número de tuplas da tabela
    public int contarTuplas(String user){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM IMC WHERE user = ?",new String[]{user});
        int x = cursor.getCount();
        cursor.close();
        return x;
    }


    // Devolve um csv de uma linha contendo os valores da ultima medida para colocar no menu principal
    // Devolve --- se não tiver nenhuma medida no banco de dados.
    public String ultimaMedida(String user){
        if (contarTuplas(user) == 0) return "---,---,---";
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder dados = new StringBuilder();

        Cursor cursor = db.rawQuery("SELECT user, altura, peso, indiceIMC FROM IMC WHERE user = ? ORDER BY ID DESC LIMIT 1", new String[]{user});

        if (cursor != null && cursor.moveToFirst()) {
            int alturaIndex = cursor.getColumnIndex("altura");
            int pesoIndex = cursor.getColumnIndex("peso");
            int indiceIMCIndex = cursor.getColumnIndex("indiceIMC");

            if (alturaIndex != -1 && pesoIndex != -1 && indiceIMCIndex != -1) {
                float altura = cursor.getFloat(alturaIndex);
                float peso = cursor.getFloat(pesoIndex);
                float indiceIMC = cursor.getFloat(indiceIMCIndex);

                dados.append(altura)
                        .append(",").append(peso)
                        .append(",").append(indiceIMC);
            }
            cursor.close();
        }
        return dados.toString();
    }

    // Deleta dado do banco
    public void remover(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("IMC", "id=?", new String[]{""+id});
    }

}
