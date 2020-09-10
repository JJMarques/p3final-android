package pt.ismt.projfinalp3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SaudeActivity extends AppCompatActivity {

    private ApiConnection _api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saude);

        Intent acederSaude = getIntent();
        String cc_aluno = acederSaude.getStringExtra("cc_aluno");
        Log.d("cc aluno", cc_aluno);

        String dados = "";

        _api = new ApiConnection();
        _api._activity = SaudeActivity.this;
        _api.execute("http://10.0.2.2:3001/api/v1/saudetoday/" + cc_aluno, "0.3", dados);
    }

    public void successMessage() {

        if (_api._sesta == null){
            ((TextView)findViewById(R.id.saude)).setText("Sem informações");
            ((TextView)findViewById(R.id.saude)).setTextColor(Color.parseColor("#D55757"));
        } else {
            ((TextView)findViewById(R.id.saude)).setText(_api._sesta);
        }

        if (_api._miccao == null){
            ((TextView)findViewById(R.id.miccao)).setText("Sem informações");
            ((TextView)findViewById(R.id.miccao)).setTextColor(Color.parseColor("#D55757"));
        } else {
            ((TextView)findViewById(R.id.miccao)).setText(_api._miccao);
        }

        if (_api._defeccao == null){
            ((TextView)findViewById(R.id.defecacao)).setText("Sem informações");
            ((TextView)findViewById(R.id.defecacao)).setTextColor(Color.parseColor("#D55757"));
        } else {
            ((TextView)findViewById(R.id.defecacao)).setText(_api._defeccao);
        }

        if (_api._doenca == null){
            ((TextView)findViewById(R.id.doenca)).setText("Sem informações");
            ((TextView)findViewById(R.id.doenca)).setTextColor(Color.parseColor("#D55757"));
        } else if (_api._doenca.equals("Sim") || _api._doenca.equals("SIM") || _api._doenca.equals("sim")) {

            if (_api._sintomas != null) {
                ((TextView) findViewById(R.id.doenca)).setText(_api._sintomas);
                ((TextView) findViewById(R.id.doenca)).setTextColor(Color.parseColor("#D55757"));
            }

            if (_api._medicamentos != null && _api._dose != null) {
                ((TextView) findViewById(R.id.medicacao)).setText("Medicado com " + _api._medicamentos + " " + _api._dose + "g");
                ((TextView) findViewById(R.id.medicacao)).setTextColor(Color.parseColor("#D55757"));
            }

        } else {
            ((TextView)findViewById(R.id.doenca)).setText(_api._doenca); //Não
        }

    }
}
