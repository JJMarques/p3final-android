package pt.ismt.projfinalp3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class RefeicaoActivity extends AppCompatActivity {

    private ApiConnection _api;
    String como_comeu_manha, como_comeu_almoco, como_comeu_tarde;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refeicao);

        Intent acederRefeicoes = getIntent();
        String cc_aluno = acederRefeicoes.getStringExtra("cc_aluno");
        Log.d("cc aluno", cc_aluno);

        String dados = "";

        _api = new ApiConnection();
        _api._activity = RefeicaoActivity.this;
        _api.execute("http://10.0.2.2:3001/api/v1/refeicaotoday/" + cc_aluno, "0.2", dados);
    }

    public void successMessage() {

        if (_api._como_comeu_manha == null){
            ((TextView)findViewById(R.id.como_comeu_manha)).setText("Sem informações");
            ((TextView)findViewById(R.id.como_comeu_manha)).setTextColor(Color.parseColor("#D55757"));
        } else {
            ((TextView)findViewById(R.id.como_comeu_manha)).setText("Comeu " + _api._como_comeu_manha);
        }

        if (_api._como_comeu_almoco == null){
            ((TextView)findViewById(R.id.como_comeu_almoco)).setText("Sem informações");
            ((TextView)findViewById(R.id.como_comeu_almoco)).setTextColor(Color.parseColor("#D55757"));
        } else {
            ((TextView)findViewById(R.id.como_comeu_almoco)).setText("Comeu " + _api._como_comeu_almoco);
        }

        if (_api._como_comeu_tarde == null){
            ((TextView)findViewById(R.id.como_comeu_tarde)).setText("Sem informações");
            ((TextView)findViewById(R.id.como_comeu_tarde)).setTextColor(Color.parseColor("#D55757"));
        } else {
            ((TextView)findViewById(R.id.como_comeu_tarde)).setText("Comeu " + _api._como_comeu_tarde);
        }


    }
}
