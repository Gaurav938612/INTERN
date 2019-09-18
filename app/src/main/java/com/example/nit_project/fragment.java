package com.example.nit_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.nit_project.activities.MapsActivity;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class fragment extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String username,parent;
    TextView textView;
    SharedpreferenceConfig shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent=getIntent();
        username=intent.getStringExtra("userkey");
        parent=intent.getStringExtra("parent");
        shared=new SharedpreferenceConfig(getApplicationContext());
        if(parent.equals("backgroundworker"))
        {
            shared.writeloginstatus(true);
            shared.writeusername(username);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        ImageView imageView=findViewById(R.id.locationId);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(fragment.this, MapsActivity.class));
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(fragment.this);
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage("Do you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("exit",true);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_fragment_drawer, menu);
        textView=findViewById(R.id.picId);//profile name near picture
        //email=findViewById(R.id.emailId);
        textView.setText(username);                      //set profile name at picture
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_item1) {
           //Intent intent=new Intent(fragment.this,fragment.class);
           //startActivity(intent);
        } else if (id == R.id.nav_item2) {
            String type="displayProfile";
            ProfileDisplayer profileDisplayer=new ProfileDisplayer(this);
            profileDisplayer.execute(type,username);
           // profileDisplayer.display();
           // Intent inte/nt=new Intent(fragment.this,frag_2.class);
            //startActivity(intent);

        } else if (id == R.id.nav_item3) {
            Intent intent=new Intent(fragment.this,frag_3.class);
            startActivity(intent);
        } else if (id == R.id.nav_item4) {
            Intent intent=new Intent(fragment.this,frag_4.class);
            startActivity(intent);

        } else if (id == R.id.nav_item5) {
            Intent intent=new Intent(fragment.this,frag_5.class);
            startActivity(intent);
        }else if(id==R.id.nav_item6){
            shared.writeloginstatus(false);//log_out
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("exit",false);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
