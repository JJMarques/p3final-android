package pt.ismt.projfinalp3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class AtividadesActivity extends AppCompatActivity {

    ListView _lv;
    private ApiConnection _api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividades);

        _lv = findViewById(R.id.listAtividades);

        String dados = "";

        _api = new ApiConnection();
        _api._activity = AtividadesActivity.this;
        _api._listaAtividades = new ArrayList();
        _api.execute("http://10.0.2.2:3001/api/v1/atividades/", "0.4", dados);

    }

    public void updateUI() {
        //lista onde irá ficar armazenado a string para mostrar na lista (listView)
        final ArrayList<String> dadosLista = new ArrayList<>();

        //ciclo que percorre todos os alunos retornados pela API
        for (int i=0; i<_api._listaAtividades.size(); i++) {
            //variável que guarda os dados do aluno (no formato key-value-pair)
            HashMap<String, String> atividade = _api._listaAtividades.get(i);

            //string com os dados a mostrar na lista no formato - nome(id) e por baixo o email
            String dadosAtividade = atividade.get("nome_atividade") + "\n"+atividade.get("sala")+"\n"+atividade.get("data_atividade") +"\n";
            dadosLista.add(dadosAtividade);
        }
        if (dadosLista.isEmpty()) {
            _lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, Collections.singletonList("Nenhuma atividade encontrada.")));
        } else {
            _lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, dadosLista));
        }

    }

}
