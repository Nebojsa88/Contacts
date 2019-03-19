package com.aad.contacts.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.aad.contacts.R;
import com.aad.contacts.db.ORMLightHelper;
import com.aad.contacts.db.model.Contact;
import com.aad.contacts.db.model.Telefon;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private ORMLightHelper databaseHelper;
    private SharedPreferences prefs;
    private Contact contact;

    private EditText name = null;
    private EditText lastName = null;
    private EditText addres= null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        if(toolbar != null){
            setSupportActionBar(toolbar);
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int key = getIntent().getExtras().getInt(ListActivity.ACTOR_KEY);

        try {
            contact = getDatabaseHelper().getContactDao().queryForId(key);

            name = (EditText) findViewById(R.id.detail_name);
            lastName = (EditText) findViewById(R.id.detail_last_name);
            addres = (EditText) findViewById(R.id.detail_adresa);


            name.setText(contact.getmName());
            lastName.setText(contact.getmLastName());
            addres.setText(contact.getmAddres());

        }catch (SQLException e){
            e.printStackTrace();
        }

        final ListView listView = (ListView) findViewById(R.id.list_phones);
        try {
            final List<Telefon> list = getDatabaseHelper().getTelefonDao().queryBuilder().where().eq(Telefon.FIELD_TELEFON_CONTACT, contact.getmId()).query();
            ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Telefon t = (Telefon) listView.getItemAtPosition(position);

                }
            });

        }catch (SQLException e){
            e.printStackTrace();
        }





    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_phone:
                //OTVORI SE DIALOG UNESETE INFORMACIJE
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.add_phone_layout);

                Button add = (Button) dialog.findViewById(R.id.btn_phone_save);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText phone = (EditText) dialog.findViewById(R.id.edit_phone);

                        Spinner tipPhones = (Spinner) findViewById(R.id.spinner);
                        String[] phones= {"kucni", "Mobilni", "Poslovni"};
                        ArrayAdapter<String> adapter = new ArrayAdapter(DetailActivity.this,  android.R.layout.simple_spinner_item, phones);
                        tipPhones.setAdapter(adapter);



                        Telefon t = new Telefon();
                        t.setBrTelefona(phone.getText().toString());

                        try {
                            getDatabaseHelper().getTelefonDao().create(t);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        //URADITI REFRESH
                        refresh();

                        showMessage("New movie added to actor");

                        dialog.dismiss();
                    }
                });

                dialog.show();

                break;
            case R.id.kontakt_edit:
                //POKUPITE INFORMACIJE SA EDIT POLJA
                contact.setmName(name.getText().toString());
                contact.setmLastName(lastName.getText().toString());
                contact.setmAddres(addres.getText().toString());
                try {
                    getDatabaseHelper().getContactDao().update(contact);

                    showMessage("COntact detail updated");

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.kontakt_remove:
                try {
                    getDatabaseHelper().getContactDao().delete(contact);

                    showMessage("Contact deleted");

                    finish(); //moramo pozvati da bi se vratili na prethodnu aktivnost
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.list_phones);

        if (listview != null){
            ArrayAdapter<Telefon> adapter = (ArrayAdapter<Telefon>) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Telefon> list = getDatabaseHelper().getTelefonDao().queryBuilder()
                            .where()
                            .eq(Telefon.TABLE_NAME_USERS, contact.getmId())
                            .query();

                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void showMessage(String message){
        //provera podesenja
        boolean toast = prefs.getBoolean("notif_toast", false);
        boolean status = prefs.getBoolean("notif_statis", false);

        if (toast){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        if (status){
            showStatusMesage(message);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
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
