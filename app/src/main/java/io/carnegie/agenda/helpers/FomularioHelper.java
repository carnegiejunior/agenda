package io.carnegie.agenda.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import io.carnegie.agenda.activities.FormularioActivity;
import io.carnegie.agenda.R;
import io.carnegie.agenda.models.Aluno;

public class FomularioHelper {


    private final EditText campoNome;
    private final EditText campoEndereco;
    private final EditText campoTelefone;
    private final EditText campoSite;
    private final RatingBar campoNota;
    private final ImageView campoFoto;

    private Aluno aluno;

    public FomularioHelper(FormularioActivity activity) {

        aluno = new Aluno();

        campoNome      = activity.findViewById(R.id.form_name);
        campoEndereco  = activity.findViewById(R.id.form_address);
        campoTelefone  = activity.findViewById(R.id.form_phone);
        campoSite      = activity.findViewById(R.id.form_site);
        campoNota      = activity.findViewById(R.id.form_nota);
        campoFoto      = activity.findViewById(R.id.form_foto);
    }

    public Aluno pegaAluno() {

        aluno.setNome(this.campoNome.getText().toString());
        aluno.setEndereco(this.campoEndereco.getText().toString());
        aluno.setTelefone(this.campoTelefone.getText().toString());
        aluno.setSite(this.campoSite.getText().toString());
        aluno.setNota(Double.valueOf(this.campoNota.getProgress()));
        aluno.setFoto((String) campoFoto.getTag());

        return aluno;

    }

    public void preencheFormulario(Aluno aluno) {

        campoNome.setText(aluno.getNome());
        campoEndereco.setText(aluno.getEndereco());
        campoTelefone.setText(aluno.getTelefone());
        campoSite.setText(aluno.getSite());
        campoNota.setProgress(aluno.getNota().intValue());
        carregaImagem(aluno.getFoto());
        this.aluno = aluno; // O ID já está gravado em auluno
    }

    public void carregaImagem(String pathFoto) {
        if (pathFoto!= null) {
            Bitmap bitmap = BitmapFactory.decodeFile(pathFoto);
            Bitmap reductedBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
            campoFoto.setImageBitmap(reductedBitmap);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
            campoFoto.setTag(pathFoto);
        }
    }
}
