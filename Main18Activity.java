package test.sinh.test.testcontentprovider;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import test.sinh.test.R;

public class Main18Activity extends AppCompatActivity {
    Button show;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main18);
        show = findViewById(R.id.show);
        listView = findViewById(R.id.listView);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showAllContacts();
                showAllContacts2();
            }
        });
    }

    private void showAllContacts() {
        Uri uri = Uri.parse("content://contacts/people");
        ArrayList<String> list = new ArrayList();
        CursorLoader loader = new CursorLoader(this, uri, null, null, null, null);
        Cursor c1 = loader.loadInBackground();
        c1.moveToFirst();
        while (c1.isAfterLast() == false) {
            String s = "";
            String idColumnName = ContactsContract.Contacts._ID;
            int idIndex = c1.getColumnIndex(idColumnName);
            s = c1.getString(idIndex) + " - ";
            String nameColumnName = ContactsContract.Contacts.DISPLAY_NAME;
            int nameIndex = c1.getColumnIndex(nameColumnName);
            s += c1.getString(nameIndex);
            Log.e("tusinh", "name: " + s);
//            String phoneColumnName = ContactsContract.Contacts.HAS_PHONE_NUMBER;
//            int phoneIndex = c1.getColumnIndex(phoneColumnName);
//            s += c1.getString(phoneIndex)+" - ";
            c1.moveToNext();
            list.add(s);
        }
        c1.close();
        ArrayAdapter<String> adapter = new ArrayAdapter(Main18Activity.this, android.R.layout.simple_expandable_list_item_1, list);
        listView.setAdapter(adapter);

    }

    public void showAllContacts2() {
//        Uri uri = Uri.parse("content://contacts/people");
        ArrayList<String> list = new ArrayList<String>();
        Cursor c1 = getContentResolver()
                .query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        Log.e("tusinh", "cursor: " + c1);
        c1.moveToFirst();
        while (c1.isAfterLast() == false) {
            String s = "";

            String idColumnName = ContactsContract.Contacts._ID;
            int idIndex = c1.getColumnIndex(idColumnName);
            s = c1.getString(idIndex) + " - "; // lay theo id nay trong query cursor phones.

            String id = c1.getString(
                    c1.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID));
            s += id + " - ";

            String nameColumnName = ContactsContract.Contacts.DISPLAY_NAME;
            int nameIndex = c1.getColumnIndex(nameColumnName);
            s += c1.getString(nameIndex)+" - ";


            Cursor phones = getContentResolver()
                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + c1.getString(idIndex),
                            null,
                            null);
            if (phones != null) {
                while (phones.moveToNext()) {
                    String phoneNumber = phones.getString(
                            phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    s += phoneNumber;
                    Log.e("tusinh", "doMagicContacts: " + c1.getString(nameIndex) + " " + phoneNumber);
                }
                phones.close();
            }
            c1.moveToNext();
            list.add(s);
        }
        c1.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Main18Activity.this,
                android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }
}
