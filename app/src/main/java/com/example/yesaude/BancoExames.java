package com.example.yesaude;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BancoExames extends SQLiteOpenHelper {

    public static final String databaseName = "ExamesDB.db";

    public BancoExames(@Nullable Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table exames (id INTEGER primary key AUTOINCREMENT, user TEXT not null, tipo TEXT not null," +
                " data TEXT not null,  exame BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists exames");
    }

    public boolean inserir(String user, String tipo, String data, byte[] exame){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user", user);
        contentValues.put("tipo", tipo);
        contentValues.put("data", data);
        contentValues.put("exame", exame);
        long result = db.insert("exames", null, contentValues);
        return result != -1;
    }

    // Devolve um csv com os dados da tabela.
    public String pegarDados(String user){
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder dados = new StringBuilder();

        Cursor cursor = db.rawQuery("SELECT id, tipo, data FROM exames WHERE user = ?", new String[]{user});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int especialidadeIndex = cursor.getColumnIndex("tipo");
                int dataIndex = cursor.getColumnIndex("data");


                if (idIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String especialidade = cursor.getString(especialidadeIndex);
                    String data = cursor.getString(dataIndex);

                    dados.append(id)
                            .append(",").append(especialidade)
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
        Cursor cursor = db.rawQuery("SELECT * FROM exames WHERE user = ?",new String[]{user});
        int x = cursor.getCount();
        cursor.close();
        return x;
    }

    // Deleta dado do banco
    public void remover(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("exames", "id=?", new String[]{""+id});
    }

    public String pegarNomesExames(String user){
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder dados = new StringBuilder();

        Cursor cursor = db.rawQuery("SELECT id, tipo FROM exames WHERE user = ?", new String[]{user});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int especialidadeIndex = cursor.getColumnIndex("tipo");
                if (especialidadeIndex != -1) {
                    String especialidade = cursor.getString(especialidadeIndex);
                    dados.append(especialidade).append("\n");
                }
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return dados.toString();
    }

    public byte[] pegarExame(String user, int id){
        SQLiteDatabase db = this.getReadableDatabase();
        byte[] dados = null;

        Cursor cursor = db.rawQuery("SELECT exame FROM exames WHERE user = ? AND id = ?", new String[]{user, ""+id});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int exameIndex = cursor.getColumnIndex("exame");
                if (exameIndex != -1) {
                    dados = cursor.getBlob(exameIndex);

                }
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return dados;
    }

}
