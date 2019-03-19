package com.aad.contacts.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.aad.contacts.R;
import com.aad.contacts.db.ORMLightHelper;
import com.aad.contacts.db.model.Contact;
import com.aad.contacts.dialog.AboutDialog;
import com.aad.contacts.preferences.Preferences;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ORMLightHelper databaseHelper;
    private SharedPreferences prefs;

    public static String ACTOR_KEY = "ACTOR_KEY";
    public static String NOTIF_TOAST = "notif_toast";
    public static String NOTIF_STATUS = "notif_statis";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        final ListView listView = (ListView) findViewById(R.id.list_contacts);

        try{
            List<Contact> list = getDatabaseHelper().getContactDao().queryForAll();
            ListAdapter adapter = new ArrayAdapter<>(ListActivity.this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Contact c = (Contact) listView.getItemAtPosition(position);

                    Intent intent = new Intent(ListActivity.this, DetailActivity.class);
                    intent.putExtra(ACTOR_KEY, c.getmId());
                    startActivity(intent);
                }
            });


        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    private void showStatusMesage(String message){
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_louncher);
        mBuilder.setContentTitle("Pripremni test");
        mBuilder.setContentText(message);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_add);

        mBuilder.setLargeIcon(bm);
        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.add_new_kontakt:
                //DIALOG ZA UNOS PODATAKA
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.add_kontakt_layout);

                Button add = (Button) dialog.findViewById(R.id.btn_save);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText name = (EditText) dialog.findViewById(R.id.edit_ime);
                        EditText lastName = (EditText) dialog.findViewById(R.id.edit_prezime);
                        EditText addres = (EditText) dialog.findViewById(R.id.edit_adresa);

                        String stringName = name.getText().toString();
                        String stringLastName = lastName.getText().toString();
                        String stringAddres = addres.getText().toString();

                        if (stringName.isEmpty() || containsDigit(stringName)){
                            Toast.makeText(ListActivity.this, "Field Name must contains only letters and cant be empty! ", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(containsDigit(stringLastName) || stringLastName.isEmpty()){
                            Toast.makeText(ListActivity.this, "Field Last Name must contains only letters and cant be empty! ", Toast.LENGTH_LONG).show();
                            return;
                        }

                        //Provera za polje bio
                        if (stringAddres.isEmpty()){
                            Toast.makeText(ListActivity.this, "Field Addres can not be empty!", Toast.LENGTH_LONG).show();
                            return;
                        }

                        Contact k = new Contact();
                        k.setmName(name.getText().toString());
                        k.setmLastName(lastName.getText().toString());
                        k.setmAddres(addres.getText().toString());


                        try {
                            getDatabaseHelper().getContactDao().create(k);

                            //provera podesenja
                            boolean toast = prefs.getBoolean(NOTIF_TOAST, false);
                            boolean status = prefs.getBoolean(NOTIF_STATUS, false);

                            if (toast){
                                Toast.makeText(ListActivity.this, "Added new contact!", Toast.LENGTH_SHORT).show();
                            }

                            if (status){
                                showStatusMesage("Added new Contact!");
                            }

                            //REFRESH
                            refresh();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();

                    }
                });

                dialog.show();

                break;
            case R.id.priprema_about:

                AlertDialog alertDialog = new AboutDialog(this).prepareDialog();
                alertDialog.show();
                break;
            case R.id.priprema_preferences:
                startActivity(new Intent(ListActivity.this, Preferences.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //Proverava da li string sadrzi brojeve
    public boolean containsDigit(String string) {
        boolean containsDigit = false;

        if (string != null && !string.isEmpty()) {
            for (char c : string.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    return true;

                }
            }
        }

        return false;
    }



    @Override
    protected void onResume() {
        super.onResume();

        refresh();
    }

    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.list_contacts);

        if (listview != null){
            ArrayAdapter<Contact> adapter = (ArrayAdapter<Contact>) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Contact> list = getDatabaseHelper().getContactDao().queryForAll();

                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    //Metoda koja komunicira sa bazom podataka
    public ORMLightHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, ORMLightHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // nakon rada sa bazo podataka potrebno je obavezno
        //osloboditi resurse!
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
