package io.carnegie.agenda.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import io.carnegie.agenda.R;
import io.carnegie.agenda.adapters.AlunosAdapter;
import io.carnegie.agenda.dao.AlunoDAO;
import io.carnegie.agenda.models.Aluno;

public class ListaAlunosActivity extends AppCompatActivity {

    private ListView listaAlunos;

    private Context getContext() {
        return this;
    }

    private void loadList() {
        AlunoDAO dao = new AlunoDAO(getContext());
        List<Aluno> alunos = dao.buscaAlunos();
        dao.close();
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,alunos);

        //ArrayAdapter<Aluno> adapter = new ArrayAdapter<>(getContext(), R.layout.form_list_item, alunos);
        AlunosAdapter adapter = new AlunosAdapter(this,alunos);


        this.listaAlunos.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_lista_alunos);
        //String[] alunos = { "Daniel","Ronaldo","Jeferson","Felipe","Daniel","Ronaldo","Jeferson","Felipe","Daniel","Ronaldo","Jeferson","Felipe","Daniel","Ronaldo","Jeferson","Felipe"};

        this.listaAlunos = this.findViewById(R.id.lista_alunos);

        this.listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int positin, long id) {
                Aluno aluno = (Aluno) lista.getItemAtPosition(positin);

                Intent intent = new Intent(getContext(), FormularioActivity.class);
                intent.putExtra("aluno", aluno);
                startActivity(intent);

                //Toast.makeText(getContext(),aluno.getNome(),Toast.LENGTH_SHORT).show();
            }
        });

/*        this.listaAlunos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> lista, View item, int positin, long id) {
                Toast.makeText(getContext(),"Clique longo",Toast.LENGTH_SHORT).show();
                return false; // se for true não passa o evento para frente e não aparece o menu de contexto
            }
        });*/


        Button novoAluno = findViewById(R.id.novo_aluno);
        novoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FormularioActivity.class);
                startActivity(intent);
            }
        });

        registerForContextMenu(this.listaAlunos);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }

    // É possível fazer com XML também.
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        //super.onCreateContextMenu(menu, v, menuInfo);

        // Qual o item da LISTA que fora clicado (vem de um adapter).
        // Não o item do menu que no caso é Deletar
        // menuInfo = qual o item do MENU clicado. Não o da lista

        // info.position = devolve a posição da LISTA que foi clicada
        // getItemAtPosition = Devolve um object que neste caso é o do Aluno.
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        final Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(info.position);

        //Call
        MenuItem itemCall = menu.add("Ligar");
        itemCall.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ListaAlunosActivity.this,new String[]{Manifest.permission.CALL_PHONE},123);
                }else{
                    Intent intentCall = new Intent(Intent.ACTION_CALL);
                    intentCall.setData(Uri.parse("tel:" + aluno.getTelefone()));
                    startActivity(intentCall);
                }
                return false;
            }
        });


        //SITE
        Intent intentVisitarSite = new Intent(Intent.ACTION_VIEW);
        String site = aluno.getSite();
        if (!site.startsWith("http://") || !site.startsWith("https://")){
            site = "http://"+aluno.getSite();
        }
        intentVisitarSite.setData(Uri.parse(site));
        menu.add("Visitar site").setIntent(intentVisitarSite);

        //SMS
        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
        intentSMS.setData(Uri.parse("sms:"+aluno.getTelefone()));
        menu.add("Enviar SMS").setIntent(intentSMS);

        //VISUALIZAR MAPA
        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentMapa.setData(Uri.parse("geo:0,0?q="+aluno.getEndereco()));
        menu.add("Visualizar mapa").setIntent(intentMapa);




        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                AlunoDAO dao = new AlunoDAO(getContext());
                dao.deleta(aluno);
                dao.close();
                loadList();
                return false;
            }
        });
    }

    //Qualquer permissão que for solicitada cairá dentro deste método
    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 123){

        }else (requestCode == 124){
            //faz o envio do SMS
        }
    }*/

    /*
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }
*/


}
