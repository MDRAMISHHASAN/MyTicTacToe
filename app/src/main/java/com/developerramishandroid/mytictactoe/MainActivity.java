package com.developerramishandroid.mytictactoe;


import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    //  private AppBarConfiguration mAppBarConfiguration;
  private long backPressedTime;
    Toast toast;
    View view;
    TextView textViewToast;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_bar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);

         switch (item.getItemId()) {
             case R.id.menu_id_Share:
                 Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                 return true;

             case R.id.menu_id_tutorial:
                 Toast.makeText(this, "Tutorials", Toast.LENGTH_SHORT).show();
                 return true;

             default:
                 return false;
         }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toast = new Toast(getApplicationContext());
        view = LayoutInflater.from(this).inflate(R.layout.toast_layout,null);
        textViewToast = view.findViewById(R.id.textViewToast);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM,0,260);

    }

    @Override
    public void onBackPressed() {
        if (backPressedTime+1000>System.currentTimeMillis())
        {
            super.onBackPressed();
            //return;
        }
        else
        {
            textViewToast.setText("Press back button twice continuously");
            toast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    /*
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();


    }

     */


    public void homeScreen(View view)
    {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void tic3Screen(View view)
    {
        Intent intent = new Intent(this, Tic3Activity.class);
        startActivity(intent);
    }

    public void tic4Screen(View view)
    {
        Intent intent = new Intent(this, Tic4Activity.class);
        startActivity(intent);
    }

    public void tic5Screen(View view)
    {
        Intent intent = new Intent(this, Tic5Activity.class);
        startActivity(intent);
    }

    public void settingsScreen(View view)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


    public void aboutusScreen(View view)
    {
        Intent intent = new Intent(this, AboutusActivity.class);
        startActivity(intent);
    }


}
