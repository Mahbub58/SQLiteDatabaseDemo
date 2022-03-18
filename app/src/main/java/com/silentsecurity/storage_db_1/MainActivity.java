package com.silentsecurity.storage_db_1;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DBManager dbManager;
    EditText etuser;
    EditText etpass;
    String RecordID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager=new DBManager(this);

         etuser=findViewById(R.id.etuser);
         etpass=findViewById(R.id.etpass);
    }



    public void buSave(View view) {

        ContentValues values=new ContentValues();
        values.put(DBManager.ColUserName,etuser.getText().toString());
        values.put(DBManager.ColPassword,etpass.getText().toString());
        long id=dbManager.Insert(values);
        if(id>0){
            Toast.makeText(getApplicationContext(),"User Id: "+id,Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getApplicationContext(), "Can not insert", Toast.LENGTH_LONG).show();
              }
        }
    //adapter class
    ArrayList<AdapterItems>    listnewsData = new ArrayList<AdapterItems>();
    MyCustomAdapter myadapter;



    public void buload(View view) {
       LoadElement();

    }
        void LoadElement(){



  //sarch
    String[]SelectionArgs={"%"+etuser.getText().toString()+"%"};
  //clear after load
       listnewsData.clear();

    //String[] projection=("UserName","password");            //sarch
        Cursor cursor=dbManager.query(null,"UserName LIke ? ",SelectionArgs,DBManager.ColUserName);

        if(cursor.moveToFirst()){
            String tableData="";
            do{
                /*tableData+=cursor.getString(cursor.getColumnIndex(DBManager.ColUserName))+","+
                       cursor.getString(cursor.getColumnIndex(DBManager.ColPassword))+"::";
            */
                //adaptor
                listnewsData.add(new AdapterItems(cursor.getString(cursor.getColumnIndex(DBManager.ColID))
                        ,cursor.getString(cursor.getColumnIndex(DBManager.ColUserName))
                        ,cursor.getString(cursor.getColumnIndex(DBManager.ColPassword))));
            }while ((cursor.moveToNext()));
            Toast.makeText(getApplicationContext(),tableData,Toast.LENGTH_LONG).show();
        }
        //add data and view it

        myadapter=new MyCustomAdapter(listnewsData);


        ListView lsNews=(ListView)findViewById(R.id.LVNews);
        lsNews.setAdapter(myadapter);//intisal with data

    }

    public void buUpdateNow(View view) {
        ContentValues values=new ContentValues();
        values.put(DBManager.ColUserName,etuser.getText().toString());
        values.put(DBManager.ColPassword,etpass.getText().toString());
        values.put(DBManager.ColID,RecordID);
        String[] SelectionArgs={String.valueOf(RecordID)};
        dbManager.Update(values,"id=?",SelectionArgs);
        LoadElement();
    }

    //display news list
    private class MyCustomAdapter extends BaseAdapter {
        public ArrayList<AdapterItems> listnewsDataAdpater;

        public MyCustomAdapter(ArrayList<AdapterItems> listnewsDataAdpater) {
            this.listnewsDataAdpater = listnewsDataAdpater;
        }


        @Override
        public int getCount() {
            return listnewsDataAdpater.size();
        }

        @Override
        public String getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater mInflater = getLayoutInflater();
            View myView = mInflater.inflate(R.layout.layout_ticket, null);

            final AdapterItems s = listnewsDataAdpater.get(position);

            TextView tvid = (TextView) myView.findViewById(R.id.tvid);
            tvid.setText(s.ID);

            TextView tvuser=(TextView)myView.findViewById(R.id.tvuser);
                    tvuser.setText(s.UserName);

            TextView tvpass=(TextView)myView.findViewById(R.id.tvpass);
            tvpass.setText(s.Password);
//Delet
            Button budelet=myView.findViewById(R.id.budelet);
            budelet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] SelectionArgs={s.ID};
                   int count= dbManager.Delet("ID=?",SelectionArgs);
 //refresh Element
                   if(count>0){
                       LoadElement();
                   }
                }
            });
//update
            Button buUpdate=myView.findViewById(R.id.buUpdate);
            buUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etuser.setText(s.UserName);
                    etpass.setText(s.Password);
                    RecordID=s.ID;
                }
            });
            return myView;

        }
    }

}
