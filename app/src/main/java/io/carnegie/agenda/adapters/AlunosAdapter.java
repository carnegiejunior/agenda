package io.carnegie.agenda.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import io.carnegie.agenda.R;
import io.carnegie.agenda.activities.ListaAlunosActivity;
import io.carnegie.agenda.models.Aluno;

public class AlunosAdapter extends BaseAdapter {

    private final List<Aluno> alunos;
    private final Context context;

    public AlunosAdapter(Context context, List<Aluno> alunos) {
        this.alunos = alunos;
        this.context = context;
    }

    // Quantos itens a lista tem e pode-se pedir? #1
    // Quantos dados?
    @Override
    public int getCount() {
        return this.alunos.size();
    }

    @Override
    public Object getItem(int position) {
        return alunos.get(position);
    }


    // Este é o terceiro parâmetro do onItemClick do listener da listaAlunos
    @Override
    public long getItemId(int position) {
        return alunos.get(position).getId();
    }

    @Override
    public View getView(int position, View converterView, ViewGroup parent) {

        Aluno aluno = alunos.get(position);

        LayoutInflater inflater = LayoutInflater.from(this.context);
        // usa o parent mas não coloca ainda dentro da parent(false)
        View view = inflater.inflate(R.layout.form_list_item, parent,false);

        TextView campoNome =  view.findViewById(R.id.form_item_nome);
        campoNome.setText(aluno.getNome());

        TextView campoTelefone =  view.findViewById(R.id.form_item_phone);
        campoTelefone.setText(aluno.getTelefone());

        ImageView campoFoto =  view.findViewById(R.id.fotoItem);
        String pathFoto = aluno.getFoto();
        if (pathFoto!= null) {
            Bitmap bitmap = BitmapFactory.decodeFile(pathFoto);
            Bitmap reductedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            campoFoto.setImageBitmap(reductedBitmap);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        return view;
    }
}
