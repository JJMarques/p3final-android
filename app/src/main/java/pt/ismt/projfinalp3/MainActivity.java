package pt.ismt.projfinalp3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ApiConnection _api;
    private EditText etUsername, etPassword;
    private Object HomeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.insertUsername);
        etPassword = findViewById(R.id.insertPassword);
    }

    public void doLogin(View v) {
        if (etUsername.getText().toString().length() == 0) {
            etUsername.setError("É necessário preencher o username.");
            etUsername.requestFocus();
        } else if (etPassword.getText().toString().length() == 0) {
            etPassword.setError("É necessário preencher a password.");
            etPassword.requestFocus();
        } else {
            String dadosUser = "username=" + etUsername.getText();

            //executa o pedido à API

            _api = new ApiConnection();
            _api._activity = MainActivity.this;
            _api._listaUser = new ArrayList();
            _api.execute("http://10.0.2.2:3001/api/v1/user/" + etUsername.getText(), "0", dadosUser);
        }

    }

    public void successMessage() {
        if (String.valueOf(_api._tipo).equals("1") ) { //Login como encarregado de educação

            Toast.makeText(this, "Utilizador iniciou a sessão com sucesso!", Toast.LENGTH_LONG).show();
            etUsername.setText(etUsername.getText());
            etPassword.setText("");

            Intent entrarConta = new Intent(this, HomeActivity.class);
            entrarConta.putExtra("username", _api._username);
            entrarConta.putExtra("cc_enc_educacao", _api._cc_enc_educacao);
            startActivity(entrarConta);

        } else if (String.valueOf(_api._tipo).equals("2") ) { //Login como Admin

            Toast.makeText(this, "Admin iniciou a sessão com sucesso!", Toast.LENGTH_LONG).show();
            etUsername.setText(etUsername.getText());
            etPassword.setText("");

            Intent entrarConta = new Intent(this, HomeAdminActivity.class);
            entrarConta.putExtra("username", _api._username);
            entrarConta.putExtra("cc_educador", _api._cc_educador);
            startActivity(entrarConta);

        }

        else {
            Toast.makeText(this, "Dados incorretos!", Toast.LENGTH_LONG).show();
        }


    }

}
