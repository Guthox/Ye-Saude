package com.example.yesaude;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BancoConsultas extends SQLiteOpenHelper {

    public static final String databaseName = "ConsultasDB.db";

    public BancoConsultas(@Nullable Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table consultas (id INTEGER primary key AUTOINCREMENT, user TEXT not null, especialidade TEXT not null," +
                " data TEXT not null, hora TEXT not null, resumo TEXT, retorno TEXT, exame BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists consultas");
    }

    public boolean inserir(String user, String especialidade, String data, String hora, String resumo, String retorno, byte[] exame){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user", user);
        contentValues.put("especialidade", especialidade);
        contentValues.put("data", data);
        contentValues.put("hora", hora);
        contentValues.put("resumo", resumo);
        contentValues.put("retorno", retorno);
        contentValues.put("exame", exame);
        long result = db.insert("consultas", null, contentValues);
        return result != -1;
    }

    // Devolve um csv com os dados da tabela.
    public String pegarDados(String user){
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder dados = new StringBuilder();

        Cursor cursor = db.rawQuery("SELECT id, especialidade, data, hora, resumo, retorno FROM consultas WHERE user = ?", new String[]{user});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int especialidadeIndex = cursor.getColumnIndex("especialidade");
                int dataIndex = cursor.getColumnIndex("data");
                int horaIndex = cursor.getColumnIndex("hora");
                int resumoIndex = cursor.getColumnIndex("resumo");
                int retornoIndex = cursor.getColumnIndex("retorno");

                if (idIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String especialidade = cursor.getString(especialidadeIndex);
                    String data = cursor.getString(dataIndex);
                    String hora = cursor.getString(horaIndex);
                    String resumo = cursor.getString(resumoIndex);
                    String retorno = cursor.getString(retornoIndex);

                    dados.append(id)
                            .append(",").append(especialidade)
                            .append(",").append(data)
                            .append(",").append(hora)
                            .append(",").append(resumo)
                            .append(",").append(retorno)
                            .append("\n");
                }
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return dados.toString();
    }

    public String pegarDados(String user, int idU){
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder dados = new StringBuilder();

        Cursor cursor = db.rawQuery("SELECT id, especialidade, data, hora, resumo, retorno FROM consultas WHERE user = ? AND id = ?", new String[]{user, ""+idU});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int especialidadeIndex = cursor.getColumnIndex("especialidade");
                int dataIndex = cursor.getColumnIndex("data");
                int horaIndex = cursor.getColumnIndex("hora");
                int resumoIndex = cursor.getColumnIndex("resumo");
                int retornoIndex = cursor.getColumnIndex("retorno");

                if (idIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String especialidade = cursor.getString(especialidadeIndex);
                    String data = cursor.getString(dataIndex);
                    String hora = cursor.getString(horaIndex);
                    String resumo = cursor.getString(resumoIndex);
                    String retorno = cursor.getString(retornoIndex);

                    dados.append(id)
                            .append(",").append(especialidade)
                            .append(",").append(data)
                            .append(",").append(hora)
                            .append(",").append(resumo)
                            .append(",").append(retorno)
                            .append("\n");
                }
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return dados.toString();
    }


    public String pegarTipoData(String user){
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder dados = new StringBuilder();

        Cursor cursor = db.rawQuery("SELECT id, especialidade, data FROM consultas WHERE user = ?", new String[]{user});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int especialidadeIndex = cursor.getColumnIndex("especialidade");
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
        Cursor cursor = db.rawQuery("SELECT * FROM consultas WHERE user = ?",new String[]{user});
        int x = cursor.getCount();
        cursor.close();
        return x;
    }

    // Deleta dado do banco
    public void remover(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("consultas", "id=?", new String[]{""+id});
    }


    public String pegarNomesExames(String user){
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder dados = new StringBuilder();

        Cursor cursor = db.rawQuery("SELECT id, especialidade FROM consultas WHERE user = ?", new String[]{user});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int especialidadeIndex = cursor.getColumnIndex("especialidade");
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

    public boolean alterarConsulta(int id, String user, String especialidade, String data, String hora, String resumo, String retorno, byte[] exame) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user", user);
        contentValues.put("especialidade", especialidade);
        contentValues.put("data", data);
        contentValues.put("hora", hora);
        contentValues.put("resumo", resumo);
        contentValues.put("retorno", retorno);
        contentValues.put("exame", exame);

        int rowsAffected = db.update("consultas", contentValues, "id = ?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }

    public boolean verificarIdExistente(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM consultas WHERE id = ?", new String[]{""+id});
        boolean idExiste = cursor.moveToFirst();
        cursor.close();
        return idExiste;
    }

    public byte[] pegarExame(String user, int id){
        SQLiteDatabase db = this.getReadableDatabase();
        byte[] dados = null;

        Cursor cursor = db.rawQuery("SELECT exame FROM consultas WHERE user = ? AND id = ?", new String[]{user, ""+id});

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
