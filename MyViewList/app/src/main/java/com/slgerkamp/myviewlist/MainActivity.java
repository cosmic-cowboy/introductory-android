package com.slgerkamp.myviewlist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // データ準備
        List<User> users = new ArrayList<>();
        String [] names = {"sasaki", "taguchi", "suzuki"};
        String [] locations = {"yokohama", "kobe", "newyork"};
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        for(int i = 0; i < names.length; i++){
            users.add(new User(bitmap, names[i], locations[i]));
        }

        // UserAdapter
        UserAdapter userAdapter = new UserAdapter(this, 0, users);
        final ListView myListView = (ListView) findViewById(R.id.myListView);
        myListView.setAdapter(userAdapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv1 = (TextView) view.findViewById(R.id.name);
                String itemText = tv1.getText().toString();
                Toast.makeText(MainActivity.this, itemText, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private static class User {
        final Bitmap image;
        final String name;
        final String location;

        public User(Bitmap image, String name, String location) {
            this.image = image;
            this.name = name;
            this.location = location;
        }
    }

    private static class UserAdapter extends ArrayAdapter<User> {

        private LayoutInflater layoutInflater;

        public UserAdapter(Context context, int viewResourceId, List<User> users) {
            super(context, viewResourceId, users);
            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            // 再利用できるviewがなければlayoutInflaterを使ってrow.xmlをViewにする
            if (convertView == null){
                convertView = layoutInflater.inflate(R.layout.row, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            User user = (User) getItem(position);

            viewHolder.image.setImageBitmap(user.image);
            viewHolder.name.setText(user.name);
            viewHolder.location.setText(user.location);

            return convertView;
        }
    }

    private static class ViewHolder {
        final ImageView image;
        final TextView name;
        final TextView location;

        public ViewHolder(View view) {
            this.name = (TextView) view.findViewById(R.id.name);
            this.image = (ImageView) view.findViewById(R.id.image);;
            this.location = (TextView) view.findViewById(R.id.location);;
        }
    }
}
