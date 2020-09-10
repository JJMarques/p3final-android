package pt.ismt.projfinalp3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

public class AtividadesAdminActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    String date;
    ListView _lv;
    Button escolheData;
    private ApiConnection _api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividades_admin);

        _lv = findViewById(R.id.listAtividades);

        String dados = "";

        _api = new ApiConnection();
        _api._activity = AtividadesAdminActivity.this;
        _api._listaAtividades = new ArrayList();
        _api.execute("http://10.0.2.2:3001/api/v1/atividades/", "0.4", dados);

        escolheData = findViewById(R.id.show_dialog);
        escolheData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDatePickerDailog();
            }
        });

    }

    private void ShowDatePickerDailog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        date = year + "-" + (month + 1) + "-" + dayOfMonth;
        escolheData.setText(dayOfMonth + "-" + month + "-" + year);
    }

    public  void insertAtividade(View v) {

        EditText nome_atividade, sala_atividade, data_atividade;

        nome_atividade = findViewById(R.id.etNomeAtividade);
        sala_atividade = findViewById(R.id.etSala);

        String dados = "sala=" + sala_atividade.getText() + "&nome_atividade=" +
                nome_atividade.getText() + "&data_atividade=" +
                date;

        _api = new ApiConnection();
        _api._activity = AtividadesAdminActivity.this;
        _api._listaAlunos = new ArrayList();
        _api.execute("http://10.0.2.2:3001/api/v1/atividade", "1", dados);
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

    public void sucessMessagePost() {
        Toast.makeText(this, "Dados guardados com sucesso!", Toast.LENGTH_LONG).show();
    }


}
