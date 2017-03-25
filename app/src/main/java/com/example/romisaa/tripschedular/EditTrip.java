package com.example.romisaa.tripschedular;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditTrip extends AppCompatActivity {


    EditText name;
    EditText date;
    EditText time;
    TextView status;

    CheckBox done;
    String strsource, strdestination;
    FloatingActionButton addNoteBtn;
    LinearLayout mLayout;
    ArrayList<EditText> notesEditTexts = new ArrayList<>();
    ArrayList<EditText> newTripNotes=new ArrayList<>();


    Trip newTrip;


    Calendar calendar;
    int year, month, day, hours, minutes;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_edit_trip);
        setContentView(R.layout.content_edit_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        newTrip=(Trip) intent.getParcelableExtra("trip");
        System.out.println("l gy mn l edit"+newTrip.getStatus());


        //Filling Data
        calendar = Calendar.getInstance();
        String am_pm = ((calendar.get(Calendar.AM_PM)) == Calendar.AM) ? "am" : "pm";
        String myFormat = "MMM dd, yyyy";
        SimpleDateFormat sdformat = new SimpleDateFormat(myFormat);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hours = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);

        //Referencing The Layout

        name = (EditText) findViewById(R.id.nameEdit);
        name.setText(newTrip.getName());

        status=(TextView) findViewById(R.id.statusEdit);
        status.setText(newTrip.getStatus());
        PlaceAutocompleteFragment placeAutocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.source);
        PlaceAutocompleteFragment placeAutocompleteFragment2 = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.destination);
        placeAutocompleteFragment.setHint("Source");
        placeAutocompleteFragment2.setHint("destenation");
        placeAutocompleteFragment.setText(tripNameFromLngLat(newTrip.getSource()));
        strsource=newTrip.getSource();
        strdestination=newTrip.getDestination();
        placeAutocompleteFragment2.setText(tripNameFromLngLat(newTrip.getDestination()));
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                EditTrip.this.strsource = place.getName().toString();

            }

            @Override
            public void onError(Status status) {

            }
        });
        placeAutocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                EditTrip.this.strdestination = place.getName().toString();
            }

            @Override
            public void onError(Status status) {

            }
        });
        date=(EditText) findViewById(R.id.date);
        android.icu.text.SimpleDateFormat formatter = new android.icu.text.SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date(newTrip.getDate()));
        date.setText(dateString);


        time=(EditText) findViewById(R.id.time) ;
        calendar=Calendar.getInstance();
        calendar.setTimeInMillis(newTrip.getDate());
        calendar.get(Calendar.HOUR_OF_DAY);
        time.setText(calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE)+ " "+am_pm);


        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DatePickerDialog dialog = new DatePickerDialog(EditTrip.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String myFormat = "MMM dd, yyyy";
                            SimpleDateFormat sdformat = new SimpleDateFormat(myFormat);
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, monthOfYear);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            date.setText(sdformat.format(calendar.getTime()));
                        }
                    }, year, month, day);
                    dialog.show();
                    date.clearFocus();
                }
            }
        });

        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    TimePickerDialog dialog = new TimePickerDialog(EditTrip.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            calendar.set(Calendar.HOUR, hourOfDay);
                            calendar.set(Calendar.MINUTE, minute);
                            time.setText(hourOfDay + ":" + minute);
                            Toast.makeText(EditTrip.this, calendar.getTime().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }, hours, minutes, false);

                    dialog.show();
                    time.clearFocus();
                }
            }
        });


        mLayout = (LinearLayout) findViewById(R.id.mLayout);


            for (int i = 0; i < newTrip.getNotes().size(); i++)
            {
                final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                EditText editText = new EditText(getApplicationContext());
                editText.setLayoutParams(lparams);
                editText.setSingleLine(false);  //TODO Check
                editText.setTextColor(0xff000000);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setTextSize(20);

                editText.setText(newTrip.getNotes().get(i).getContent());

                //Create Horizontal View
                notesEditTexts.add(editText);
                LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                mLayout.addView(editText);
            }
        addNoteBtn= (FloatingActionButton) findViewById(R.id.addNoteBtn);

        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating Edit Text
                final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                EditText editText = new EditText(getApplicationContext());
                editText.setLayoutParams(lparams);
                editText.setSingleLine(false);  //TODO Check
                editText.setTextColor(0xff000000);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setHintTextColor(0xff000000);
                editText.setHint("New Note Here ");
            //    notesEditTexts.add(editText);
                newTripNotes.add(editText);
                //Creating Button
                final LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Button remove=new Button(getApplicationContext());
                //remove.setBackground();
                remove.setLayoutParams(lparams);
                Drawable drawable = getResources().getDrawable(R.drawable.remove);
                remove.setBackground(drawable);

                remove.setBackgroundColor(0xff000000);
                remove.setGravity(Gravity.LEFT);



                //Create Horizontal View
                LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                mLayout.addView(editText);
              //  mLayout.addView(remove);
            }
        });



        done=(CheckBox) findViewById(R.id.markasdone);
        if (newTrip.getStatus()==Trip.STATUS_DONE)
        {
            done.setChecked(true);
            status.setText(Trip.STATUS_DONE);
        }
        else
        {
            done.setChecked(false);
        }

        done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (done.isChecked())
                {
                    status.setText(Trip.STATUS_DONE);
                    TaskManager.getInstance(getApplicationContext()).deleteTask(newTrip.getId());
                }
                else
                {
                    status.setText(newTrip.getStatus());
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateTrip())
                {
                    DataBaseHandler handler=new DataBaseHandler(getApplicationContext());
                    Trip updatedTrip=new Trip();
                    updatedTrip.setId(newTrip.getId());
                    updatedTrip.setName(String.valueOf(name.getText()));
                    updatedTrip.setDuration((long) 0);
                    updatedTrip.setStatus(String.valueOf(status.getText()));
                    updatedTrip.setSource(strsource);
                    updatedTrip.setDestination(strdestination);
                    updatedTrip.setDate(calendar.getTimeInMillis());
                    handler.updateTrip(updatedTrip);
                    System.out.println("size adem"+notesEditTexts.size());
                    System.out.println("size gded"+newTripNotes.size());
                    for(int i=0;i<notesEditTexts.size();i++)
                    {
                        if(!notesEditTexts.get(i).getText().toString().isEmpty())
                        {
                            Notes updatedNote=new Notes();
                            updatedNote.setTripId(newTrip.getId());
                            updatedNote.setNoteId(newTrip.notes.get(i).getNoteId());
                            updatedNote.setContent(notesEditTexts.get(i).getText().toString());
                            handler.updateNote(updatedNote);
                        }
                    }
                    for (int i=0;i<newTripNotes.size();i++)
                    {
                        if(!newTripNotes.get(i).getText().toString().isEmpty())
                        {
                            Notes updatedNote=new Notes();
                            updatedNote.setTripId(newTrip.getId());
                            updatedNote.setContent(newTripNotes.get(i).getText().toString());
                                handler.addNote(updatedNote);
                        }
                    }


                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                }
            }
        });

    }


    private boolean validateTrip() {

        if(name.getText().toString().trim().equals("")){
            Toast.makeText(this, "Your trip must have a name", Toast.LENGTH_SHORT).show();
            return false;
        }



        if(strsource==null || strsource.trim().equals("")){
            Toast.makeText(this, "Your trip must have a source", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(strdestination==null || strdestination.trim().equals("")){
            Toast.makeText(this, "Your trip must have a destination", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            Toast.makeText(EditTrip.this, "Time specified already passed", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public String tripNameFromLngLat(String fullName){
        return fullName.substring(fullName.indexOf("#"),fullName.length());
    }


}
