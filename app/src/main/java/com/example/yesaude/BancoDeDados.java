package com.example.yesaude;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BancoDeDados extends SQLiteOpenHelper {


    public static final String databaseName = "usuarios.db";

    public BancoDeDados(@Nullable Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table usuarios (usuario TEXT primary key, nome TEXT, senha TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists usuarios");
    }

    public boolean inserir(String usuario, String nome, String senha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("usuario", usuario);
        contentValues.put("nome", nome);
        contentValues.put("senha", senha);
        long result = db.insert("usuarios", null, contentValues);
        return result != -1;
    }

    public boolean checkUsuario(String usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario = ?", new String[]{usuario});
        return cursor.getCount() > 0;
    }

    public boolean checkLogin(String usuario, String senha) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario = ? AND senha = ?", new String[]{usuario, senha});
        return cursor.getCount() > 0;
    }

    public String getNome(String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT nome FROM usuarios WHERE usuario = ?", new String[]{user});
        String nome = "";
        if (cursor != null && cursor.moveToFirst()) {
            int nomeIndex = cursor.getColumnIndex("nome");
            if (nomeIndex != -1) {
                nome = cursor.getString(nomeIndex);
            }
        }
        cursor.close();
        return nome;
    }
}
