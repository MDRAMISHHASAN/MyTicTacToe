package com.developerramishandroid.mytictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.animation.Animator;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Arrays;

public class Tic4Activity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;
    private View background;
    FloatingActionButton fabMain,fabOne,fabTwo,fabThree,fabFour,fabFive;
    Boolean isMenuOpen = false;
    OvershootInterpolator interpolator = new OvershootInterpolator();
    MediaPlayer player;
    GridLayout gridLayout;
    TextView status;
    Toast toast;
    View view;
    TextView textViewToast;

    boolean gameActive = true;
    // Player representation
    // 0 - X
    // 1 - O
    int activePlayer = 0;
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
    //    State meanings:
    //    0 - X
    //    1 - O
    //    2 - Null
    int[][] winPositions = {{0,1,2,3}, {4,5,6,7}, {8,9,10,11},{12,13,14,15},
            {0,4,8,12}, {1,5,9,13}, {2,6,10,14},{3,7,11,15},
            {0,5,10,15}, {3,6,9,12}};



    public void playerTap(View view)
    {
        if(gameActive)
        {

            ImageView img = (ImageView) view;
            int tappedImage = Integer.parseInt(img.getTag().toString());

            if(gameState[tappedImage] == 2) {
                 gameState[tappedImage] = activePlayer;

                player = MediaPlayer.create(this, R.raw.button_tap_sound);
                player.start();

              //  img.setTranslationY(-1000f);
                 if (activePlayer == 0)
                 {
                    img.setImageResource(R.drawable.x);
                    activePlayer = 1;
                    status = findViewById(R.id.status);
                    status.setText("O's Turn");
                 }
                 else
                     {
                     img.setImageResource(R.drawable.o);
                     activePlayer = 0;
                     status = findViewById(R.id.status);
                     status.setText("X's Turn");
                 }
                //  img.animate().translationYBy(1000f).setDuration(300);
                img.setAlpha(1f);
            }
             // Check if any player has won
             for(int[] winPosition: winPositions){
                 if(gameState[winPosition[0]] == gameState[winPosition[1]] && gameState[winPosition[1]] == gameState[winPosition[2]] &&
                    gameState[winPosition[2]] == gameState[winPosition[3]] && gameState[winPosition[0]]!=2)
                 {
                       // Somebody has won! - Find out who!
                       String winnerStr;
                       gameActive = false;

                    if(gameState[winPosition[0]] == 0)
                    {
                       winnerStr = "X has won";
                       showAd();
                    }
                    else
                    {
                       winnerStr = "O has won";
                       showAd();
                    }
                   // Update the status bar for winner announcement
                     status = findViewById(R.id.status);
                     status.setTextColor(Color.parseColor("#FFFFFF"));
                     status.setTextSize(50);
                     status.setText(winnerStr);

                 }
            else if(gameState[0] !=2 && gameState[1]!=2 && gameState[2]!=2 && gameState[3]!=2 && gameState[4] !=2 && gameState[5]!=2 && gameState[6]!=2 && gameState[7]!=2
                 && gameState[8] !=2 && gameState[9]!=2 && gameState[10]!=2 && gameState[11]!=2 && gameState[12] !=2 && gameState[13]!=2 && gameState[14]!=2 && gameState[15]!=2)
                {
                 gameActive = false;
                    status.setTextSize(60);
                    status.setTextColor(Color.parseColor("#ffff00"));
                    status.setText("Draw");
                }
        }
        }
        else
        {
            textViewToast.setText("Please restart game.");
            toast.show();
        }
    }

    public void gameRestart(View view) {

        textViewToast.setText("Restart game \n(Anyone has won or game has been tie)");
        toast.show();

        if(!gameActive) {
            gameActive = true;
            activePlayer = 0;
            Arrays.fill(gameState, 2);

            gridLayout = findViewById(R.id.gridLayout);

            for (int i = 0; i < gridLayout.getChildCount(); i++)
                ((ImageView) gridLayout.getChildAt(i)).setImageResource(0);

           // status = findViewById(R.id.status);
            status.setTextSize(30);
            status.setTextColor(Color.parseColor("#FF0000"));
            status.setText("X's Turn\n Tap to play");

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.do_not_move,R.anim.do_not_move);
        setContentView(R.layout.activity_tic4);

        background = findViewById(R.id.background);

        if (savedInstanceState == null) {
            background.setVisibility(View.INVISIBLE);

            final ViewTreeObserver viewTreeObserver = background.getViewTreeObserver();

            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        circularRevealActivity();
                        background.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                });
            }

        }

        initFabMenu();

        toast = new Toast(getApplicationContext());
        view = LayoutInflater.from(this).inflate(R.layout.toast_layout,null);
        textViewToast = view.findViewById(R.id.textViewToast);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM,0,260);
        prepareAd();
    }



    private void circularRevealActivity() {
        int cx = background.getRight() - getDips(300) ;
        int cy = background.getBottom() - getDips(300) ;

        float finalRadius = Math.max(background.getWidth(), background.getHeight());

        Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                background,
                cx,
                cy,
                0,
                finalRadius);

        circularReveal.setDuration(700);
        background.setVisibility(View.VISIBLE);
        circularReveal.start();

    }

    private int getDips(int dps) {
        Resources resources = getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dps,
                resources.getDisplayMetrics());
    }

    @Override
    public void onBackPressed() {

        showAd();

            int cx = background.getWidth() - getDips(300);
            int cy = background.getBottom() - getDips(300);

            float finalRadius = Math.max(background.getWidth(), background.getHeight());
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(background, cx, cy, finalRadius, 0);

            circularReveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    background.setVisibility(View.INVISIBLE);
                    finish();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            circularReveal.setDuration(700);
            circularReveal.start();

    }


    private void initFabMenu()
    {
        fabMain = findViewById(R.id.fabMain);
        fabOne = findViewById(R.id.fabOne);
        fabTwo = findViewById(R.id.fabTwo);
        fabThree = findViewById(R.id.fabThree);
        fabFour = findViewById(R.id.fabFour);
        fabFive = findViewById(R.id.fabFive);

        fabOne.setAlpha(0f);
        fabTwo.setAlpha(0f);
        fabThree.setAlpha(0f);
        fabFour.setAlpha(0f);
        fabFive.setAlpha(0f);

        fabOne.setTranslationY(200f);
        fabTwo.setTranslationY(200f);
        fabThree.setTranslationY(200f);
        fabFour.setTranslationY(200f);
        fabFive.setTranslationY(200f);

        // fabOne.setTranslationY(100f);

    }

    public void openMenu()
    {
        isMenuOpen = !isMenuOpen;

        fabMain.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();
        fabOne.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabTwo.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabThree.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabFour.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(700).start();
        fabFive.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(700).start();


    }

    public void closeMenu()
    {
        isMenuOpen = !isMenuOpen;

        fabMain.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();
        fabOne.animate().translationY(200f).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabTwo.animate().translationY(200f).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabThree.animate().translationY(200f).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabFour.animate().translationY(200f).alpha(0f).setInterpolator(interpolator).setDuration(700).start();
        fabFive.animate().translationY(200f).alpha(0f).setInterpolator(interpolator).setDuration(700).start();



    }

    public void fabMainClick(View view)
    {
        if (!isMenuOpen)
        openMenu();
        else
            closeMenu();
    }

    // for playing the music
    public void fabOneClick(View view)
    {
        if (player != null && player.isPlaying())
            player.stop();

        player = MediaPlayer.create(this, R.raw.guitar);
        player.start();

        textViewToast.setText("Guitar");
        toast.show();
    }

    // for playing the music
    public void fabFourClick(View view)
    {
        if (player != null && player.isPlaying())
            player.stop();

        player = MediaPlayer.create(this, R.raw.drum_machine);
        player.start();

        textViewToast.setText("Drum Machine");
        toast.show();
    }

    // for playing the music
    public void fabFiveClick(View view)
    {
        if (player != null && player.isPlaying())
            player.stop();

        player = MediaPlayer.create(this, R.raw.drum_pad);
        player.start();
        textViewToast.setText("Drum Pad");
        toast.show();
    }

    // for stoping the music
    public void fabTwoClick(View view)
    {
        if (player != null && player.isPlaying())
        {
            player.stop();
            //player.release();

            textViewToast.setText("Music Stopped");
            toast.show();
        }
        else
        {
            textViewToast.setText("No Music was Playing");
            toast.show();
        }
    }


    public void prepareAd()
    {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public void showAd()
    {
        if (mInterstitialAd.isLoaded())
        {
            mInterstitialAd.show();
        }
        else
        {
            Log.i("ad", "Interstitial ad is not loaded");
        }
        prepareAd();
    }



}