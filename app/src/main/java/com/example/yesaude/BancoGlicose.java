package com.example.yesaude;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BancoGlicose extends SQLiteOpenHelper {

    public static final String databaseName = "Glicoses.db";

    public BancoGlicose(@Nullable Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Glicose (id INTEGER primary key AUTOINCREMENT, user TEXT not null, medida INTEGER not null, data TEXT not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Glicose");
    }

    public boolean inserir(String user, int medida, String data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user", user);
        contentValues.put("medida", medida);
        contentValues.put("data", data);
        long result = db.insert("Glicose", null, contentValues);
        return result != -1;
    }

    // Devolve um csv com os dados da tabela.
    public String pegarDados(String user){
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder dados = new StringBuilder();

        Cursor cursor = db.rawQuery("SELECT * FROM Glicose WHERE user = ?", new String[]{user});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int medidaIndex = cursor.getColumnIndex("medida");
                int dataIndex = cursor.getColumnIndex("data");

                if (idIndex != -1 && medidaIndex != -1 && dataIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    int medida = cursor.getInt(medidaIndex);
                    String data = cursor.getString(dataIndex);

                    dados.append(id)
                            .append(",").append(medida)
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
        Cursor cursor = db.rawQuery("SELECT * FROM Glicose WHERE user = ?",new String[]{user});
        int x = cursor.getCount();
        cursor.close();
        return x;
    }


    // Devolve um csv de uma linha contendo os valores da ultima medida para colocar no menu principal
    // Devolve --- se não tiver nenhuma medida no banco de dados.
    public String ultimaMedida(String user){
        if (contarTuplas(user) == 0) return "---";
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder dados = new StringBuilder();

        Cursor cursor = db.rawQuery("SELECT user, medida FROM Glicose WHERE user = ? ORDER BY ID DESC LIMIT 1", new String[]{user});

        if (cursor != null && cursor.moveToFirst()) {
            int medidaIndex = cursor.getColumnIndex("medida");

            if (medidaIndex != -1) {
                int medida = cursor.getInt(medidaIndex);
                dados.append(medida);
            }
            cursor.close();
        }
        return dados.toString();
    }

    // Deleta dado do banco
    public void remover(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("Glicose", "id=?", new String[]{""+id});
    }

}
