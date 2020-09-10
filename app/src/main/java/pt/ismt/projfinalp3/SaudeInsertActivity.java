package pt.ismt.projfinalp3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class SaudeInsertActivity extends AppCompatActivity {

    String _id_saude, _nome_aluno, _cc_aluno, _cc_educador;
    EditText _sesta, _miccao, _defecacao, _doenca, _sintomas, _medicacao, _dose;
    private ApiConnection _api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saude_insert);

        Intent inserirSaude = getIntent();
        String cc_aluno = inserirSaude.getStringExtra("cc_aluno");
        _cc_educador = inserirSaude.getStringExtra("cc_educador");

        StringTokenizer tokens = new StringTokenizer(cc_aluno);
        _nome_aluno = tokens.nextToken();
        _cc_aluno = tokens.nextToken();

        String dados = "";

        _api = new ApiConnection();
        _api._activity = SaudeInsertActivity.this;
        _api.execute("http://10.0.2.2:3001/api/v1/saudetoday/" + _cc_aluno, "0.3", dados);
    }

    public void updateUI() {

        _id_saude = _api._id_saude;

        _sesta = findViewById(R.id.etSesta);
        _miccao = findViewById(R.id.etMiccao);
        _defecacao = findViewById(R.id.etDefecacao);
        _doenca = findViewById(R.id.etDoenca);

        _sintomas = findViewById(R.id.etSintomas);
        _medicacao = findViewById(R.id.etMedicacao);
        _dose = findViewById(R.id.etDose);

        if (_api._sesta == null && _api._miccao == null && _api._defeccao == null && _api._doenca == null) {
            ((TextView) findViewById(R.id.tvNomeAluno2)).setText("Insira os dados de saúde do aluno(a) " + _nome_aluno + " no dia de hoje.");

            findViewById(R.id.btnEditSaude).setVisibility(View.GONE);
            findViewById(R.id.btnRemoveSaude).setVisibility(View.GONE);

        } else {
            ((TextView) findViewById(R.id.tvNomeAluno2)).setText("Dados já submetidos! Abaixo pode editar os dados da saúde do aluno(a) " + _nome_aluno + " no dia de hoje.");

            findViewById(R.id.btnInsertSaude).setVisibility(View.GONE);

            _sesta.setText(_api._sesta);
            _miccao.setText(_api._miccao);
            _defecacao.setText(_api._defeccao);
            _doenca.setText(_api._doenca);

            if (_api._sintomas.equals("null")) {
                _sintomas.setText("");
            } else {_sintomas.setText(_api._sintomas);}

            if (_api._medicamentos.equals("null")) {
                _medicacao.setText("");
            } else {_medicacao.setText(_api._medicamentos);}

            if (_api._dose.equals("null")) {
                _dose.setText("");
            } else {_dose.setText(_api._dose);}

        }

    }

    public void insertSaude(View v) {

        String dados = "cc_educador=" + _cc_educador +
                "&cc_aluno=" + _cc_aluno +
                "&sesta=" + _sesta.getText() +
                "&miccao=" + _miccao.getText() +
                "&defeccao=" + _defecacao.getText() +
                "&doenca=" + _doenca.getText() +
                "&sintomas=" + _sintomas.getText() +
                "&medicamentos=" + _medicacao.getText() +
                "&dose=" + _dose.getText() +
                "&data=" + _doenca.getText();

        _api = new ApiConnection();
        _api._activity = SaudeInsertActivity.this;
        _api.execute("http://10.0.2.2:3001/api/v1/saudetoday/", "1", dados);

    }

    public void editSaude(View v) {

    }

    public void removeSaude(View v) {
        String dados = "";

        _api = new ApiConnection();
        _api._activity = SaudeInsertActivity.this;
        _api.execute("http://10.0.2.2:3001/api/v1/saude/"+ _id_saude, "3", dados);
    }

    public void sucessMessagePost() {
        Toast.makeText(this, "Dados inseridos com sucesso!", Toast.LENGTH_LONG).show();
        finish();
    }

    public void sucessMessageDelete() {
        Toast.makeText(this, "Dados removidos com sucesso!", Toast.LENGTH_LONG).show();
        finish();
    }
}