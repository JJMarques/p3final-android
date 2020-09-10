package pt.ismt.projfinalp3;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class ApiConnection extends AsyncTask<String, Void, Void>  {

    public Activity _activity;
    private ProgressDialog _pdialog;
    public ArrayList<HashMap<String, String>> _listaUser;

    String codigoPedido;

    //Variáveis para dados do utilizador
    String _username, _password, _tipo, _cc_educador, _cc_enc_educacao;

    //Variáveis para dados do Aluno
    String _cc_aluno;

    //Variáveis para dados da refeição do Aluno
    String _id_refeicao, _como_comeu_manha, _como_comeu_almoco, _como_comeu_tarde;

    //Variáveis para dados da Saúde do Aluno
    String _id_saude, _sesta, _miccao, _defeccao, _doenca, _sintomas, _medicamentos, _dose;

    //Lista de atividades da creche
    public ArrayList<HashMap<String, String>> _listaAtividades;

    //Lista de alunos da creche
    public ArrayList<HashMap<String, String>> _listaAlunos;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        _pdialog = new ProgressDialog(_activity);
        _pdialog.setMessage("A carregar...");
        _pdialog.setCancelable(false);
        _pdialog.show();
    }


    @Override
    protected Void doInBackground(String... urls) {
        HttpURLConnection _conexao;
        InputStream _is;
        String _resJson;

        _pdialog.setMessage("A receber dados...");

        try {
            URL _endpoint = new URL(urls[0]);
            Log.d("urls", "O url do pedido é: " + _endpoint);

            codigoPedido = urls[1];

            _conexao = (HttpURLConnection) _endpoint.openConnection();

                if (
                        urls[1] == "0" ||
                        urls[1] == "0.1" ||
                        urls[1] == "0.2" ||
                        urls[1] == "0.3" ||
                        urls[1] == "0.4" ||
                        urls[1] == "0.5" ||
                        urls[1] == "0.6"
                ) { //GET

                    _conexao.setRequestMethod("GET");


                } else if (urls[1] == "1") { //POST

                    String data = urls[2];
                    Log.d("http", "Os dados enviados no body do pedido foram: " + data);


                    byte[] dados = data.getBytes(StandardCharsets.UTF_8);

                    _conexao.setDoOutput(true);
                    _conexao.setRequestMethod("POST");
                    _conexao.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    _conexao.setRequestProperty("charset", "utf-8");
                    _conexao.setRequestProperty("Content-Length", Integer.toString(dados.length));

                        try (DataOutputStream wr = new DataOutputStream(_conexao.getOutputStream())) {
                            wr.write(dados);
                        }

                } else if (urls[1] == "2") { //PUT
                    _conexao.setRequestMethod("PUT");

                } else if (urls[1] == "3") { //DELETE
                    _conexao.setRequestMethod("DELETE");
                }

            _conexao.setReadTimeout(12000);
            _conexao.setConnectTimeout(12000);
            _conexao.connect();
            Log.d("http", "O código HTTP do pedido foi: " + _conexao.getResponseCode());

            int _httpStatus = _conexao.getResponseCode();
                if (_httpStatus != HttpURLConnection.HTTP_BAD_REQUEST && _httpStatus != HttpURLConnection.HTTP_INTERNAL_ERROR && _httpStatus != HttpURLConnection.HTTP_NOT_FOUND) {
                    _is = _conexao.getInputStream();
                } else {
                    _is = _conexao.getErrorStream();
                }
            _resJson = converteStreamParaString(_is);
            Log.d("http", "A resposta ao pedido HTTP foi: " + _resJson);

            if (_resJson != null) {
                try {
                    JSONObject _resposta = new JSONObject(_resJson);

                    //verifica se a API devolveu um array (JSONArray) ou um objeto (JSONObject)
                    if (_resposta.get("dados") instanceof JSONArray) {
                        JSONArray _listDadosJson = _resposta.getJSONArray("dados");

                        for (int i = 0; i < _listDadosJson.length(); i++) {
                            JSONObject _dado = _listDadosJson.getJSONObject(i);

                            Log.d("value of urls[2]", urls[2]);

                            if (urls[1].equals("0")) { //MainActivity -> GET dados do User

                                _username = _dado.getString("username");
                                _password = _dado.getString("password");
                                _tipo = _dado.getString("tipo");
                                _cc_educador = _dado.getString("cc_educador");
                                _cc_enc_educacao = _dado.getString("cc_enc_educacao");

                            } else if (urls[1].equals("0.1")) { //HomeActivity -> GET dados do Aluno correspondente ao User
                                _cc_aluno = _dado.getString("cc_aluno");

                            } else if (urls[1].equals("0.2")) { //RefeicaoActivity & RefeicaoInsertActivity -> GET dados da refeição do aluno no dia corrente
                                _id_refeicao = _dado.getString("id_refeicao");
                                _como_comeu_manha = _dado.getString("como_comeu_manha");
                                _como_comeu_almoco = _dado.getString("como_comeu_almoco");
                                _como_comeu_tarde = _dado.getString("como_comeu_tarde");

                            } else if (urls[1].equals("0.3")) { //SaudeActivity & SaudeInsertActivity -> GET dados da saúde do aluno no dia corrente
                                _id_saude = _dado.getString("id_saude");
                                _sesta = _dado.getString("sesta");
                                _miccao = _dado.getString("miccao");
                                _defeccao = _dado.getString("defeccao");
                                _doenca = _dado.getString("doenca");
                                _sintomas = _dado.getString("sintomas");
                                _medicamentos = _dado.getString("medicamentos");
                                _dose = _dado.getString("dose");
                            } else if (urls[1].equals("0.4")) { //AtividadesActivity -> GET lista de todas as atividades

                                    JSONObject _atividade = _listDadosJson.getJSONObject(i);
                                    String sala = _atividade.getString("sala");
                                    String nome_atividade = _atividade.getString("nome_atividade");
                                    String data_atividade = _atividade.getString("data_atividade");

                                    HashMap<String, String> atividade = new HashMap();
                                    atividade.put("sala", String.valueOf(sala));
                                    atividade.put("nome_atividade", String.valueOf(nome_atividade));
                                    atividade.put("data_atividade", String.valueOf(data_atividade));
                                    _listaAtividades.add(atividade);

                                    Log.d("lista de atividades", String.valueOf(_listaAtividades));

                            } else if (urls[1].equals("0.5")) { //RefeicaoAdminActivity -> GET lista de todas os alunos do educador

                                JSONObject _aluno = _listDadosJson.getJSONObject(i);
                                String nome = _aluno.getString("nome");
                                String cc_aluno = _aluno.getString("cc_aluno");

                                HashMap<String, String> aluno = new HashMap();
                                aluno.put("nome", String.valueOf(nome));
                                aluno.put("cc_aluno", String.valueOf(cc_aluno));
                                _listaAlunos.add(aluno);

                                Log.d("lista de alunos", String.valueOf(_listaAlunos));

                            }

                        }

                    } /* else if (_resposta.get("dados") instanceof JSONObject) {
                        JSONObject _refeicao = _resposta.getJSONObject("dados");
                        String id_refeicao = _refeicao.getString("id_refeicao");
                        String cc_aluno = _refeicao.getString("cc_aluno");
                        String como_comeu_manha = _refeicao.getString("como_comeu_manha");
                        String como_comeu_almoco = _refeicao.getString("como_comeu_almoco");
                        String como_comeu_tarde = _refeicao.getString("como_comeu_tarde");

                        HashMap<String, String> refeicao = new HashMap();
                        refeicao.put("id_refeicao", String.valueOf(id_refeicao));
                        refeicao.put("cc_aluno", String.valueOf(cc_aluno));
                        refeicao.put("como_comeu_manha", String.valueOf(como_comeu_manha));
                        refeicao.put("como_comeu_almoco", String.valueOf(como_comeu_almoco));
                        refeicao.put("como_comeu_tarde", String.valueOf(como_comeu_tarde));
                        _listaAlunos.add(refeicao);
                    }*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            _is.close();
            _conexao.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    //este método converte de stream para string
    private static String converteStreamParaString(InputStream is) {
        StringBuffer buffer = new StringBuffer();

        try {
            BufferedReader br;
            String linha;

            br = new BufferedReader(new InputStreamReader(is));
            while ((linha = br.readLine()) != null) {
                buffer.append(linha);
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    @Override
    protected void onPostExecute(Void _resultado) {
        super.onPostExecute(_resultado);

        if (_pdialog.isShowing()) {
            _pdialog.dismiss();
        }

        //verifica qual das activities invocou a API
        if (_activity.getClass().getSimpleName().equals("MainActivity")) {
            MainActivity ma = (MainActivity) _activity;
            ma.successMessage();
        }
        else if (_activity.getClass().getSimpleName().equals("HomeActivity")) {
            HomeActivity ha = (HomeActivity) _activity;
            ha.successMessage();
        }
        else if (_activity.getClass().getSimpleName().equals("RefeicaoActivity")) {
            RefeicaoActivity ra = (RefeicaoActivity) _activity;
            ra.successMessage();
        }
        else if (_activity.getClass().getSimpleName().equals("SaudeActivity")) {
            SaudeActivity sa = (SaudeActivity) _activity;
            sa.successMessage();
        }
        else if (_activity.getClass().getSimpleName().equals("AtividadesActivity")) {
            AtividadesActivity aa = (AtividadesActivity) _activity;
            aa.updateUI();
        }
        else if (_activity.getClass().getSimpleName().equals("RefeicaoAdminActivity")) {
            RefeicaoAdminActivity raa = (RefeicaoAdminActivity) _activity;
            raa.updateUI();
        }
        else if (_activity.getClass().getSimpleName().equals("RefeicaoInsertActivity")) {

            if (codigoPedido.equals("0.2")){
                RefeicaoInsertActivity ria = (RefeicaoInsertActivity) _activity;
                ria.updateUI();
            } else if (codigoPedido.equals("1")) {
                RefeicaoInsertActivity ia = (RefeicaoInsertActivity) _activity;
                ia.sucessMessagePost();
            } else if (codigoPedido.equals("2")) {
                RefeicaoInsertActivity ia = (RefeicaoInsertActivity) _activity;
                ia.sucessMessagePut();
            } else if (codigoPedido.equals("3")) {
                RefeicaoInsertActivity ia = (RefeicaoInsertActivity) _activity;
                ia.sucessMessageDelete();
            }

        }
        else if (_activity.getClass().getSimpleName().equals("SaudeAdminActivity")) {
            SaudeAdminActivity saa = (SaudeAdminActivity) _activity;
            saa.updateUI();
        }
        else if (_activity.getClass().getSimpleName().equals("SaudeInsertActivity")) {
            if (codigoPedido.equals("0.3")){
                SaudeInsertActivity sia = (SaudeInsertActivity) _activity;
                sia.updateUI();
            } else if (codigoPedido.equals("1")) {
                SaudeInsertActivity ia = (SaudeInsertActivity) _activity;
                ia.sucessMessagePost();
            } else if (codigoPedido.equals("3")) {
            SaudeInsertActivity ia = (SaudeInsertActivity) _activity;
            ia.sucessMessageDelete();
            }
        }
        else if (_activity.getClass().getSimpleName().equals("AtividadesAdminActivity")) {
            if (codigoPedido.equals("0.4")) {
                AtividadesAdminActivity aaa = (AtividadesAdminActivity) _activity;
                aaa.updateUI();
            } else if (codigoPedido.equals("2")) {
                AtividadesAdminActivity aaa = (AtividadesAdminActivity) _activity;
                aaa.sucessMessagePost();
            }
        }
    }

}
