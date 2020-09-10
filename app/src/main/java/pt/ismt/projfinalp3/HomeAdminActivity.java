package pt.ismt.projfinalp3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HomeAdminActivity extends AppCompatActivity {

    String username, cc_educador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_home);

        Intent entraConta = getIntent();
        username =entraConta.getStringExtra("username");
        cc_educador =entraConta.getStringExtra("cc_educador");

        ((TextView)findViewById(R.id.greetings)).setText("Bem-vindo(a), " + username);
    }

    public void goAdminRefeicoes (View v) {
        Intent goAdminRefeicoes = new Intent(this, RefeicaoAdminActivity.class);
        goAdminRefeicoes.putExtra("cc_educador", cc_educador);
        startActivity(goAdminRefeicoes);
    }

    public void goAdminSaude (View v) {
        Intent goAdminSaude = new Intent(this, SaudeAdminActivity.class);
        goAdminSaude.putExtra("cc_educador", cc_educador);
        startActivity(goAdminSaude);
    }

    public void goAdminAtividades (View v) {
        Intent goAdminAtividades = new Intent(this, AtividadesAdminActivity.class);
        startActivity(goAdminAtividades);
    }

    public void logOut(View v) {
        Intent logOut = new Intent(this, MainActivity.class);
        startActivity(logOut);
    }
}
