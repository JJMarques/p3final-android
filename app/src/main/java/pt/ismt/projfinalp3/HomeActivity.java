package pt.ismt.projfinalp3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.OnLifecycleEvent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    private ApiConnection _api;
    String username;
    String cc_enc_educacao;
    String cc_aluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent entraConta = getIntent();
        username =entraConta.getStringExtra("username");
        cc_enc_educacao =entraConta.getStringExtra("cc_enc_educacao");

        ((TextView)findViewById(R.id.greetings)).setText("Bem-vindo(a), " + username);

        String dados = "";

        _api = new ApiConnection();
        _api._activity = HomeActivity.this;
        _api.execute("http://10.0.2.2:3001/api/v1/dado_clinico/ee/" + cc_enc_educacao, "0.1", dados);

    }


    public void successMessage() {

        cc_aluno = _api._cc_aluno;

    }

    public void goRefeicoes(View v) {
        Intent acederRefeicoes = new Intent(this, RefeicaoActivity.class);
        acederRefeicoes.putExtra("cc_aluno", cc_aluno);
        startActivity(acederRefeicoes);
    }

    public void goSaude(View v) {
        Intent acederSaude = new Intent(this, SaudeActivity.class);
        acederSaude.putExtra("cc_aluno", cc_aluno);
        startActivity(acederSaude);
    }

    public void goAtividades(View v) {
        Intent acederAtividades = new Intent(this, AtividadesActivity.class);
        startActivity(acederAtividades);
    }

    public void logOut(View v) {
        Intent logOut = new Intent(this, MainActivity.class);
        startActivity(logOut);
    }

}
