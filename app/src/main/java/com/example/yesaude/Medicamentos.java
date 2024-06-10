package com.example.yesaude;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Medicamentos extends SQLiteOpenHelper {


    public static final String databaseName = "medicamentos.db";

    public Medicamentos(@Nullable Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table medicamentos (id INTEGER primary key AUTOINCREMENT, farmaco TEXT, detentor TEXT, medicamento TEXT, registro TEXT, concentracao TEXT, forma TEXT, data TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists medicamentos");
    }

    public boolean inserir(String farmaco, String detentor, String medicamento, String registro, String concentracao, String forma, String data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("farmaco", farmaco);
        contentValues.put("detentor", detentor);
        contentValues.put("medicamento", medicamento);
        contentValues.put("registro", registro);
        contentValues.put("concentracao", concentracao);
        contentValues.put("forma", forma);
        contentValues.put("data", data);
        long result = db.insert("medicamentos", null, contentValues);
        return result != -1;
    }

//    public boolean checkLogin(String usuario, String senha){
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario = ? AND senha = ?", new String[]{usuario, senha});
//        return cursor.getCount() > 0;
//    }
}
