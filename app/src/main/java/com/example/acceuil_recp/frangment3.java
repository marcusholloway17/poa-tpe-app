package com.example.acceuil_recp;

import android.app.DatePickerDialog;

import android.app.TimePickerDialog;
import android.os.Bundle;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.annotation.Nullable;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


public class frangment3 extends Fragment implements AdapterView.OnItemSelectedListener {


    EditText editText , editObservation;
    EditText timePickerEditText;
    final Calendar myCalendar = Calendar.getInstance();
    private List<Item> motifItemList;
    private List<Item> patientItemList;

    private List<Item> medeciItemList;

    private List<Item> prioriteiItemList;

    private ArrayAdapter<String> motifSpinnerAdapter;
    private ArrayAdapter<String> patientSpinnerAdapter;

    private ArrayAdapter<String> medecinSpinnerAdapter;

    private ArrayAdapter<String> prioriteSpinnerAdapter;

    private OkHttpClient client;
    Spinner spinnerMotif, spinnerPatient, spinnerMedecin, spinnerPriorite;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frangment3, container, false);
        editText = view.findViewById(R.id.Birthday);
        timePickerEditText = view.findViewById(R.id.Heure);
         editObservation = view.findViewById(R.id.editObservation);

        client = new OkHttpClient();

        spinnerMotif = view.findViewById(R.id.spinnerMotif);
        spinnerPatient = view.findViewById(R.id.spinnerPatient);
        spinnerMedecin = view.findViewById(R.id.spinnerMedecin);
        spinnerPriorite = view.findViewById(R.id.spinnerPriorite);

        motifItemList = new ArrayList<>();
        patientItemList = new ArrayList<>();
        medeciItemList = new ArrayList<>();
        prioriteiItemList = new ArrayList<>();

        ArrayAdapter<MotifEnum> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                MotifEnum.values()
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMotif .setAdapter(adapter);


        patientSpinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
        patientSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPatient.setAdapter(patientSpinnerAdapter);

        medecinSpinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
        medecinSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedecin.setAdapter(medecinSpinnerAdapter);

        prioriteSpinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
        prioriteSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriorite.setAdapter(prioriteSpinnerAdapter);



        spinnerPatient.setAdapter(patientSpinnerAdapter);
        spinnerMedecin.setAdapter(medecinSpinnerAdapter);
        spinnerPriorite.setAdapter(prioriteSpinnerAdapter);

        spinnerMotif.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                MotifEnum selectedMotif = (MotifEnum) adapterView.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerPatient.setOnItemSelectedListener(this);
        spinnerMedecin.setOnItemSelectedListener(this);
        spinnerPriorite.setOnItemSelectedListener(this);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        timePickerEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtenir l'heure actuelle
                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);

                // Créer et afficher un TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        requireContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                                // Mettre à jour le calendrier avec l'heure sélectionnée
                                myCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                                myCalendar.set(Calendar.MINUTE, selectedMinute);

                                // Mettre à jour le champ de texte avec l'heure sélectionnée
                                updateTimeLabel();
                            }
                        },
                        hour,
                        minute,
                        false
                );
                timePickerDialog.show();
            }
        });


        fetchDataFromApi();

        Button submitButton = view.findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFormData();
            }
        });

        return view;
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(dateFormat.format(myCalendar.getTime()));
    }


    private void updateTimeLabel() {
        String myFormat = "hh:mm a"; // Format de l'heure avec AM/PM
        SimpleDateFormat timeFormat = new SimpleDateFormat(myFormat, Locale.US);
        timePickerEditText.setText(timeFormat.format(myCalendar.getTime()));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // Gérer la sélection des éléments dans les spinners si nécessaire
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Gérer le cas où rien n'est sélectionné si nécessaire
    }

    private void fetchDataFromApi() {



        String patientApiUrl = ApiConfig.API_BASE_URL + "/third-party/patients";

        String medecinApiUrl = ApiConfig.API_BASE_URL + "/third-party/medical-people";

        String prioriteApiUrl = ApiConfig.API_BASE_URL + "/third-party/priorities";




        Request patientRequest = new Request.Builder()
                .url(patientApiUrl)
                .addHeader("x-api-key", "9af3c45c-10a0-43de-b254-ab312036eb95")
                .build();

        client.newCall(patientRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("API", "API call failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final ResponseBody myResponse = response.body();
                    try {
                        JSONArray jsonArray = new JSONArray(myResponse.string());
                        patientItemList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String firstName = jsonObject.getString("firstname");
                            String lastName = jsonObject.getString("lastname");
                            String fullName = firstName + " " + lastName;
                            Item item = new Item(id, fullName);
                            patientItemList.add(item);
                        }

                        requireActivity().runOnUiThread(() -> updateSpinnerData(patientSpinnerAdapter, patientItemList));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("API", "API call failed: " + response.message());
                }
            }
        });


        Request medecinRequest = new Request.Builder()
                .url(medecinApiUrl)
                .addHeader("x-api-key", "9af3c45c-10a0-43de-b254-ab312036eb95")
                .build();

        client.newCall(medecinRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("API", "API call failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final ResponseBody myResponse = response.body();
                    try {
                        JSONArray jsonArray = new JSONArray(myResponse.string());
                        medeciItemList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String firstName = jsonObject.getString("firstname");
                            String lastName = jsonObject.getString("lastname");
                            String fullName = firstName + " " + lastName;
                            Item item = new Item(id, fullName);
                            medeciItemList.add(item);
                        }

                        requireActivity().runOnUiThread(() -> updateSpinnerData(medecinSpinnerAdapter, medeciItemList));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("API", "API call failed: " + response.message());
                }
            }
        });


        Request prioriteRequest = new Request.Builder()
                .url(prioriteApiUrl)
                .addHeader("x-api-key", "9af3c45c-10a0-43de-b254-ab312036eb95")
                .build();

        client.newCall(prioriteRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("API", "API call failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final ResponseBody myResponse = response.body();
                    try {
                        JSONArray jsonArray = new JSONArray(myResponse.string());
                        prioriteiItemList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String Name = jsonObject.getString("name");

                            Item item = new Item(id, Name);
                            prioriteiItemList.add(item);
                        }

                        requireActivity().runOnUiThread(() -> updateSpinnerData(prioriteSpinnerAdapter, prioriteiItemList));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("API", "API call failed: " + response.message());
                }
            }
        });


    }

    private void updateSpinnerData(ArrayAdapter<String> adapter, List<Item> itemList) {
        List<String> itemNames = new ArrayList<>();
        for (Item item : itemList) {
            itemNames.add(item.getName());
        }
        adapter.clear();
        adapter.addAll(itemNames);
        adapter.notifyDataSetChanged();
    }

    private static class Item {
        private Object id;
        private String name;

        public Item(Object id, String name) {
            this.id = id;
            this.name = name;
        }

        public Object getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }


    private void sendFormData() {
        String selectedMotif = spinnerMotif.getSelectedItem().toString();
        String selectedPatientName = spinnerPatient.getSelectedItem().toString();
        String selectedPatientId = getSelectedItemId(selectedPatientName, patientItemList);


        String selectedMedecinName = spinnerMedecin.getSelectedItem().toString();
        String selectedMedecinId = getSelectedItemId(selectedMedecinName, medeciItemList);


        String selectedPrioriteName = spinnerPriorite.getSelectedItem().toString();
        String selectedPrioriteId = getSelectedItemId(selectedPrioriteName, prioriteiItemList);
        String selectedDate = editText.getText().toString();

        // Récupérer l'heure mise à jour
        String selectedTime = timePickerEditText.getText().toString();
        // Combine la date et l'heure pour former une chaîne de format "yyyy-MM-dd hh:mm a"
        String dateTimeString = selectedDate + " " + selectedTime;

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date parsedDate = inputFormat.parse(dateTimeString);
            selectedTime = isoFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        Log.d("FormData", "Appointment Type: " + selectedMotif);
        Log.d("FormData", "Patient ID: " + selectedPatientId);
        Log.d("FormData", "Medical Person ID: " + selectedMedecinId);
        Log.d("FormData", "Priority ID: " + selectedPrioriteId);
        Log.d("FormData", "Date: " + selectedDate);
        Log.d("FormData", "Time: " + selectedTime);


        JSONObject postData = new JSONObject();
        try {
            postData.put("appointment_type", selectedMotif);
            postData.put("patient_id", selectedPatientId);
            postData.put("medical_person_id", selectedMedecinId);
            postData.put("priority_id", selectedPrioriteId);
            postData.put("date_start", selectedDate + " " + selectedTime );
            String observationValue = editObservation.getText().toString();
            postData.put("note", observationValue);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String apiUrl =ApiConfig.API_BASE_URL+"/third-party/appointments";
        Request postRequest = new Request.Builder()
                .url(apiUrl)
                .addHeader("x-api-key", "9af3c45c-10a0-43de-b254-ab312036eb95")
                .post(RequestBody.create(MediaType.parse("application/json"), postData.toString()))
                .build();

        client.newCall(postRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("API", "API call failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final ResponseBody responseBody = response.body();

                try {

                    if (response.isSuccessful()) {
                        Log.d("API", "POST successful: " + response.body().string());
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Rendez-vous enregistré!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Log.e("API","reponse data" + responseBody);

                        JSONObject jsonobject = new JSONObject(responseBody.string());
                        JSONArray errors = jsonobject.getJSONArray("errors");
                        for(int i = 0; i < errors.length(); i++)
                        {
                            JSONObject object = errors.getJSONObject(i);
                            if (object.has("msg")){
                                String msg = object.getString("msg");
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(),  msg, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }                    }
                }catch (Exception e ){
                    throw new Error(e);
                }
            }
        });
    }

    private String getSelectedItemId(String selectedName, List<Item> itemList) {
        for (Item item : itemList) {
            if (item.getName().equals(selectedName)) {
                return item.getId().toString();
            }
        }
        return ""; // Retourner une valeur par défaut si l'ID n'est pas trouvé
    }







}