package io.carnegie.agenda.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import io.carnegie.agenda.R;
import io.carnegie.agenda.dao.AlunoDAO;
import io.carnegie.agenda.helpers.FomularioHelper;
import io.carnegie.agenda.models.Aluno;

public class FormularioActivity extends AppCompatActivity {

    public static final int CAMERA_CODE = 567;
    private FomularioHelper helper;
    private ImageView foto;
    private String pathFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_formulario);

        final EditText edtName = findViewById(R.id.form_name);

        foto = (ImageView) findViewById(R.id.form_foto);
        final Button btn_img = (Button) findViewById(R.id.form_btn_foto);
        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                pathFoto = getExternalFilesDir(null)+"/"+System.currentTimeMillis()+".jpg";
                File fileFoto = new File(pathFoto);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileFoto));
                startActivityForResult(intentCamera, CAMERA_CODE);
            }
        });

        helper = new FomularioHelper(this);
        Intent intent = getIntent();
        Aluno aluno = (Aluno) intent.getSerializableExtra("aluno");
        if ( aluno != null){
            helper.preencheFormulario(aluno);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CAMERA_CODE:
                    helper.carregaImagem(pathFoto);
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_form,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_form_ok:

                Aluno aluno = helper.pegaAluno();

                AlunoDAO dao = new AlunoDAO(getContext());
                dao.salva(aluno);
                dao.close();

                Toast.makeText(getContext(),"Aluno "+aluno.getNome()+" salvo!",Toast.LENGTH_LONG).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    public Context getContext() {
        return this;
    }
}
