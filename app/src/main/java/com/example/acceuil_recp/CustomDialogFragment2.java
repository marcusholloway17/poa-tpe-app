package com.example.acceuil_recp;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class CustomDialogFragment2 extends DialogFragment {

    private EditText editUsername, editPassword, editEmail, editAdresse, editNum;
    private RadioGroup radioGroupSexe;
    private RadioButton radioButtonHomme, radioButtonFemme;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        // Initialiser les vues
        editUsername = view.findViewById(R.id.edit_username);
        editPassword = view.findViewById(R.id.edit_password);
        editEmail = view.findViewById(R.id.edit_email);
        editAdresse = view.findViewById(R.id.edit_adresse);
        editNum = view.findViewById(R.id.edit_num);
        radioGroupSexe = view.findViewById(R.id.radioGroupSexe);
        radioButtonHomme = view.findViewById(R.id.radioButtonHomme);
        radioButtonFemme = view.findViewById(R.id.radioButtonFemme);

        builder.setView(view)
                .setPositiveButton("OK", (dialog, which) -> {
                    String username = editUsername.getText().toString();
                    String password = editPassword.getText().toString();
                    String email = editEmail.getText().toString();
                    String adresse = editAdresse.getText().toString();
                    String num = editNum.getText().toString();
                    String sexe = radioButtonHomme.isChecked() ? "male" : "female"; // Mapper les valeurs

                    RequestBody requestBody = new FormBody.Builder()
                            .add("firstname", username)
                            .add("lastname", password)
                            .add("email", email)
                            .add("address", adresse)
                            .add("phone", num)
                            .add("sex", sexe)
                            .build();

                    String apiUrl =ApiConfig.API_BASE_URL+"/third-party/accompanyings";
                    Request request = new Request.Builder()
                            .url(apiUrl)
                            .addHeader("x-api-key", "9af3c45c-10a0-43de-b254-ab312036eb95")
                            .post(requestBody)
                            .build();

                    OkHttpClient client = new OkHttpClient();
                    client.newCall(request).enqueue(new okhttp3.Callback() {
                        @Override
                        public void onFailure(okhttp3.Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                            String responseBody = response.body().string();
                            // Afficher la réponse dans les logs
                            Log.d("CustomDialog", "Réponse de la requête POST : " + responseBody);

                            try {
                                JSONObject jsonResponse = new JSONObject(responseBody);
                                String resourceId = jsonResponse.getString("id");

                                // Utiliser resourceId comme nécessaire dans votre application
                                // Par exemple, vous pouvez le stocker dans une variable globale ou le transmettre à d'autres parties du code.
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                })
                .setNegativeButton("Annuler", (dialog, which) -> {
                    dialog.dismiss();
                });
        return builder.create();
    }
}
