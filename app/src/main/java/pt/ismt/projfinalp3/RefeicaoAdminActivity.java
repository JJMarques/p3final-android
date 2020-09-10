package pt.ismt.projfinalp3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;

public class RefeicaoAdminActivity extends AppCompatActivity {

    ListView _lv;
    String cc_educador;
    private ApiConnection _api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refeicao_admin);

        Intent goAdminRefeicoes = getIntent();
        cc_educador =goAdminRefeicoes.getStringExtra("cc_educador");

        _lv = findViewById(R.id.listAlunos);

        String dados = "";

        _api = new ApiConnection();
        _api._activity = RefeicaoAdminActivity.this;
        _api._listaAlunos = new ArrayList();
        _api.execute("http://10.0.2.2:3001/api/v1/alunos/educ/" + cc_educador, "0.5", dados);

    }

    public void updateUI() {

        final ArrayList<String> dadosLista = new ArrayList<>();

        for (int i=0; i<_api._listaAlunos.size(); i++) {

            HashMap<String, String> aluno = _api._listaAlunos.get(i);

            String dadosAlunos = aluno.get("nome") + "\n"+aluno.get("cc_aluno")+"\n";
            dadosLista.add(dadosAlunos);
        }
        if (dadosLista.isEmpty()) {
            _lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, Collections.singletonList("Nenhum aluno encontrado.")));
        } else {
            _lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, dadosLista));
        }

        _lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {

                Intent inserirRefeicao = new Intent(RefeicaoAdminActivity.this, RefeicaoInsertActivity.class);
                inserirRefeicao.putExtra("cc_aluno", dadosLista.get(position));
                startActivity(inserirRefeicao);

            }
        });
    }

}
