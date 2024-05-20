package com.example.yesaude;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BancoIMC extends SQLiteOpenHelper {

//    BancoIMC imc = new BancoIMC(this);
//    imc.inserir(1.83, 94.0, 27.3, "hoje");
    public static final String databaseName = "IMC.db";

    public BancoIMC(@Nullable Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IMC (id INTEGER primary key AUTOINCREMENT, altura REAL not null, peso REAL not null, indiceIMC REAL not null, data TEXT not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists IMC");
    }

    public boolean inserir(double altura, double peso, double indiceIMC, String data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("altura", altura);
        contentValues.put("peso", peso);
        contentValues.put("indiceIMC", indiceIMC);
        contentValues.put("data", data);
        long result = db.insert("IMC", null, contentValues);
        return result != -1;
    }

    public String pegarDados(){
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder dados = new StringBuilder();

        String query = "SELECT altura, peso, indiceIMC, data FROM IMC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
//                int idIndex = cursor.getColumnIndex("id");
                int alturaIndex = cursor.getColumnIndex("altura");
                int pesoIndex = cursor.getColumnIndex("peso");
                int indiceIMCIndex = cursor.getColumnIndex("indiceIMC");
                int dataIndex = cursor.getColumnIndex("data");

                if (alturaIndex != -1 && pesoIndex != -1 && indiceIMCIndex != -1 && dataIndex != -1) {
//                    int id = cursor.getInt(idIndex);
                    float altura = cursor.getFloat(alturaIndex);
                    float peso = cursor.getFloat(pesoIndex);
                    float indiceIMC = cursor.getFloat(indiceIMCIndex);
                    String data = cursor.getString(dataIndex);

                    dados.append(altura)
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
    public int contarTuplas(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM IMC", null);
        int x = cursor.getCount();
        cursor.close();
        return x;
    }

    public String ultimaMedida(){
        if (contarTuplas() == 0) return "---,---,---";
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder dados = new StringBuilder();

        String query = "SELECT altura, peso, indiceIMC FROM IMC ORDER BY ID DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);

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

}
