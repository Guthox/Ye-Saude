package com.example.yesaude;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BancoMedicar extends SQLiteOpenHelper {

    public static final String databaseName = "medicar.db";

    public BancoMedicar(@Nullable Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table medicar (id INTEGER primary key AUTOINCREMENT, user TEXT, medicamento TEXT, hora TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists medicar");
    }

    public boolean inserir(String user, String medicamento, String hora){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user", user);
        contentValues.put("medicamento", medicamento);
        contentValues.put("hora", hora);
        long result = db.insert("medicar", null, contentValues);
        return result != -1;
    }

    public String pegarDados(String user){
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder dados = new StringBuilder();

        Cursor cursor = db.rawQuery("SELECT id, medicamento, hora FROM medicar WHERE user = ?", new String[]{user});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int medicamentoIndex = cursor.getColumnIndex("medicamento");
                int horaIndex = cursor.getColumnIndex("hora");

                if (medicamentoIndex != -1 && horaIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String medicamento = cursor.getString(medicamentoIndex);
                    String hora = cursor.getString(horaIndex);

                    dados.append(id).append(",").append(medicamento).append(",").append(hora).append("\n");
                }
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return dados.toString();
    }

    public boolean deletarMedicamento(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("medicar", "id=?", new String[]{String.valueOf(id)}) > 0;
    }
}
