package com.developerramishandroid.mytictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;
import android.animation.Animator;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Tic5Activity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;
    private View background;
    FloatingActionButton fabMain,fabOne,fabTwo,fabThree,fabFour,fabFive;
    boolean isMenuOpen = false, gameActive = true;
    OvershootInterpolator interpolator = new OvershootInterpolator();
    MediaPlayer player;
    GridLayout gridLayout;
    TextView status,textViewScore1,textViewScore2, textViewToast;
    Toast toast;
    View view;
    LottieAnimationView lottieAnimationView,lottieAnimationView2;

    // Player representation
    // 0 - X
    // 1 - O
    int activePlayer = 0, score1 = 0, score2 = 0, roundCount=0;
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
    //    State meanings:
    //    0 - X
    //    1 - O
    //    2 - Null

    int[][] winPositions = {{0,1,2,3,4}, {5,6,7,8,9}, {10,11,12,13,14},{15,16,17,18,19},{20,21,22,23,24},
            {0,5,10,15,20}, {1,6,11,16,21}, {2,7,12,17,22},{3,8,13,18,23},{4,9,14,19,24},
            {0,6,12,18,24}, {4,8,12,16,20}};


    public void playerTap(View view){
        if(gameActive)
        {

            ImageView img = (ImageView) view;
            int tappedImage = Integer.parseInt(img.getTag().toString());

           if(gameState[tappedImage] == 2) {
                gameState[tappedImage] = activePlayer;

               player = MediaPlayer.create(this, R.raw.button_tap_sound);
               player.start();

               roundCount++;

              if (activePlayer == 0) {
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
               img.setAlpha(1f);
           }
                  // Check if any player has won
              for(int[] winPosition: winPositions){
                     if(gameState[winPosition[0]] == gameState[winPosition[1]] && gameState[winPosition[1]] == gameState[winPosition[2]] &&
                        gameState[winPosition[2]] == gameState[winPosition[3]] && gameState[winPosition[3]] == gameState[winPosition[4]] && gameState[winPosition[0]]!=2){
                      // Somebody has won! - Find out who!

                         lottieAnimationView =findViewById(R.id.animation_view);
                         lottieAnimationView.setVisibility(View.VISIBLE);

                         player = MediaPlayer.create(this, R.raw.audience_applause);
                         String winnerStr;
                         gameActive = false;

                     if(gameState[winPosition[0]] == 0){
                         winnerStr = "X has won";
                         img.setImageResource(R.drawable.green_x);

                         player.start();

                         lottieAnimationView.setAnimation("won_animation.json");
                         lottieAnimationView.playAnimation();
                         score1 = score1 +10;
                         textViewScore1 = findViewById(R.id.textViewScore1);
                         textViewScore1.setText(String.valueOf(score1));

                         new CountDownTimer(3000,1000)
                         {
                             @Override
                             public void onTick(long millisUntilFinished) {

                             }

                             @Override
                             public void onFinish() {
                                 showAd();
                             }
                         }.start();


                     }
                     else{
                          winnerStr = "O has won";
                         img.setImageResource(R.drawable.green_o);

                         player.start();

                         lottieAnimationView.setAnimation("won_animation2.json");
                         lottieAnimationView.playAnimation();
                         score2 = score2 +10;
                         textViewScore2 = findViewById(R.id.textViewScore2);
                         textViewScore2.setText(String.valueOf(score2));
                         new CountDownTimer(3000,1000)
                         {
                             @Override
                             public void onTick(long millisUntilFinished) {

                             }

                             @Override
                             public void onFinish() {
                                 showAd();
                             }
                         }.start();

                     }
                       // Update the status bar for winner announcement
                       status = findViewById(R.id.status);
                       status.setTextColor(Color.parseColor("#FFFFFF"));
                       status.setTextSize(50);
                       status.setText(winnerStr);

                       break;
                     }
                    else if(roundCount == 25)
                     {
                        gameActive = false;
                        status.setTextSize(60);
                        status.setTextColor(Color.parseColor("#ffff00"));
                        status.setText("Draw");

                         lottieAnimationView2 =findViewById(R.id.animation_view2);
                         lottieAnimationView2.setVisibility(View.VISIBLE);
                         lottieAnimationView2.setAnimation("draw_animation.json");
                         lottieAnimationView2.playAnimation();

                         new CountDownTimer(3000,1000)
                         {
                             @Override
                             public void onTick(long millisUntilFinished) {

                             }

                             @Override
                             public void onFinish() {
                                 lottieAnimationView2.setVisibility(View.INVISIBLE);
                                 showAd();
                             }
                         }.start();

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
            roundCount = 0;
            int i;

             for (i = 0; i < gameState.length; i++)
                gameState[i] = 2;

                //   it is similar to Arrays.fill(gameState, 2);

             gridLayout = findViewById(R.id.gridLayout);

            for (i = 0; i < gridLayout.getChildCount(); i++)
                ((ImageView) gridLayout.getChildAt(i)).setImageResource(0);

            status = findViewById(R.id.status);
            status.setTextSize(30);
            status.setTextColor(Color.parseColor("#FF0000"));
            status.setText("X's Turn\n Tap to play");

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.do_not_move,R.anim.do_not_move);
        setContentView(R.layout.activity_tic5);

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
        int cx = background.getRight() - getDips(100) ;
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

        int cx = background.getWidth() - getDips(100);
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
            mInterstitialAd.show();
        else
            Log.i("ad", "Interstitial ad is not loaded ");

        prepareAd();
    }

}
