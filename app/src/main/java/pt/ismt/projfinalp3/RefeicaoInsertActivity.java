package pt.ismt.projfinalp3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class RefeicaoInsertActivity extends AppCompatActivity {

    String _id_refeicao, _nome_aluno, _cc_aluno;
    EditText como_comeu_manha, como_comeu_almoco, como_comeu_tarde ;
    private ApiConnection _api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refeicao_insert);

        Intent inserirRefeicao = getIntent();
        String cc_aluno =inserirRefeicao.getStringExtra("cc_aluno");

        //Separar a string do nome do aluno e do nº do CC
        StringTokenizer tokens = new StringTokenizer(cc_aluno);
        _nome_aluno = tokens.nextToken();
        _cc_aluno = tokens.nextToken();

        String dados = "";

        _api = new ApiConnection();
        _api._activity = RefeicaoInsertActivity.this;
        _api.execute("http://10.0.2.2:3001/api/v1/refeicaotoday/" + _cc_aluno, "0.2", dados);


    }

    public void updateUI() {

        _id_refeicao = _api._id_refeicao;

        //Inserir dados
        if (_api._como_comeu_manha == null && _api._como_comeu_almoco == null && _api._como_comeu_tarde == null) {
            ((TextView)findViewById(R.id.tvNomeAluno)).setText("Insira os dados das refeições do aluno(a) " + _nome_aluno + " no dia de hoje.");

            como_comeu_manha = findViewById(R.id.refeicaomanha);
            como_comeu_almoco = findViewById(R.id.refeicaoalmoco);
            como_comeu_tarde = findViewById(R.id.refeicaotarde);

            findViewById(R.id.btnEditRefeicao).setVisibility(View.GONE);
            findViewById(R.id.btnRemoveRefeicao).setVisibility(View.GONE);

        } else { //Editar os dados

            ((TextView)findViewById(R.id.tvNomeAluno)).setText("Dados já submetidos! Abaixo pode editar os dados das refeições do aluno(a) " + _nome_aluno + " no dia de hoje.");

            findViewById(R.id.btnInsertRefeicao).setVisibility(View.GONE);

            como_comeu_manha = findViewById(R.id.refeicaomanha);
            como_comeu_almoco = findViewById(R.id.refeicaoalmoco);
            como_comeu_tarde = findViewById(R.id.refeicaotarde);

            como_comeu_manha.setText(_api._como_comeu_manha);
            como_comeu_almoco.setText(_api._como_comeu_almoco);
            como_comeu_tarde.setText(_api._como_comeu_tarde);

        }

    }

    public void insertRefeicao(View v) {
        String dados = "cc_aluno=" + _cc_aluno + "&como_comeu_manha=" +
                como_comeu_manha.getText() + "&como_comeu_almoco=" +
                como_comeu_almoco.getText() + "&como_comeu_tarde=" +
                como_comeu_tarde.getText();

        _api = new ApiConnection();
        _api._activity = RefeicaoInsertActivity.this;
        _api.execute("http://10.0.2.2:3001/api/v1/refeicaotoday/", "1", dados);
    }

    public void editRefeicao(View v) {
        String dados = "id_refeicao=" + _api._id_refeicao + "&cc_aluno=" +
                _cc_aluno + "&como_comeu_manha=" +
                como_comeu_manha.getText() + "&como_comeu_almoco=" +
                como_comeu_almoco.getText() + "&como_comeu_tarde=" +
                como_comeu_tarde.getText();

        _api = new ApiConnection();
        _api._activity = RefeicaoInsertActivity.this;
        _api._listaAlunos = new ArrayList();
        _api.execute("http://10.0.2.2:3001/api/v1/refeicaotoday/"+ _id_refeicao, "2", dados);
    }

    public void removeRefeicao(View v) {
        String dados = "";

        _api = new ApiConnection();
        _api._activity = RefeicaoInsertActivity.this;
        _api.execute("http://10.0.2.2:3001/api/v1/refeicao/"+ _id_refeicao, "3", dados);
    }

    public void sucessMessagePost() {
        Toast.makeText(this, "Dados guardados com sucesso!", Toast.LENGTH_LONG).show();
        finish();
    }

    public void sucessMessagePut() {
        Toast.makeText(this, "Dados editados com sucesso!", Toast.LENGTH_LONG).show();
        finish();
    }

    public void sucessMessageDelete() {
        Toast.makeText(this, "Dados removidos com sucesso!", Toast.LENGTH_LONG).show();
        finish();
    }



}

