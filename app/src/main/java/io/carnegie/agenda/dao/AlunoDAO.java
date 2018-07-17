package io.carnegie.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.carnegie.agenda.models.Aluno;

public class AlunoDAO extends SQLiteOpenHelper {

    public static final int DATA_BASE_VERSION = 2;

    public AlunoDAO(Context context) {
        //Context, Database, Customização do banco de dados, versão do banco
        super(context, "Agenda", null, DATA_BASE_VERSION);
    }

    // Sempre que precisar criar o banco de dados
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE alunos(" +
                "id INTEGER PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "endereco TEXT, " +
                "telefone TEXT, " +
                "site TEXT, " +
                "nota REAL," +
                "foto TEXT" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersin, int newVersion) {
        final String sql;
        switch (oldVersin) {
            case 1:
                sql = "DROP TABLE IF EXISTS Alunos";
                db.execSQL(sql);
                onCreate(db);
                break;
            case 2:
                sql = "ALTER TABLE Alunos ADD COLUMN foto";
                db.execSQL(sql);
            default: break;
        }
    }

    @NonNull
    private ContentValues getPegaDadosDoAluno(Aluno aluno) {
        ContentValues dados = new ContentValues(); // Cria um conjunto chave-valor como o Map do Java
        dados.put("nome", aluno.getNome());
        dados.put("endereco", aluno.getEndereco());
        dados.put("telefone", aluno.getTelefone());
        dados.put("site", aluno.getSite());
        dados.put("nota", aluno.getNota());
        dados.put("foto",aluno.getFoto());
        return dados;
    }

    public List<Aluno> buscaAlunos() {

        String sql = "SELECT * FROM Alunos;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        List<Aluno> alunos = new ArrayList<Aluno>();
        while (cursor.moveToNext()) {
            Aluno aluno = new Aluno();
            aluno.setId(cursor.getLong(cursor.getColumnIndex("id")));
            aluno.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            aluno.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            aluno.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
            aluno.setSite(cursor.getString(cursor.getColumnIndex("site")));
            aluno.setNota(cursor.getDouble(cursor.getColumnIndex("nota")));
            aluno.setFoto(cursor.getString(cursor.getColumnIndex("foto")));
            alunos.add(aluno);
        }
        cursor.close();
        return alunos;
    }

    public void deleta(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase(); // Pega uma instância do banco da classe SQLiteOpenHelper
        ContentValues dados = new ContentValues(); // Cria um conjunto chave-valor como o Map do Java
        String[] params = {aluno.getId().toString()};
        db.delete("Alunos", "id = ?", params);
    }

    public void insere(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase(); // Pega uma instância do banco da classe SQLiteOpenHelper
        ContentValues dados = getPegaDadosDoAluno(aluno);
        db.insert("Alunos", null, dados);
    }

    public void altera(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase(); // Pega uma instância do banco da classe SQLiteOpenHelper
        ContentValues dados = getPegaDadosDoAluno(aluno);
        String[] params = {aluno.getId().toString()};
        db.update("Alunos", dados, "id=?", params);
    }

    public void salva(Aluno aluno) {
        if (aluno.getId() != null) {
            this.altera(aluno);
        } else {
            this.insere(aluno);
        }

    }
}
