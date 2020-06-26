package com.quintus.labs.datingapp.Main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.PulsatorLayout;
import com.quintus.labs.datingapp.Utils.TopNavigationViewHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * DatingApp
 * https://github.com/quintuslabs/DatingApp
 * Created on 25-sept-2018.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final int ACTIVITY_NUM = 1;
    final private int MY_PERMISSIONS_REQUEST_LOCATION = 123;
    ListView listView;
    List<Cards> rowItems;
    FrameLayout cardFrame, moreFrame;
    private Context mContext = MainActivity.this;
    private NotificationHelper mNotificationHelper;
    private Cards cards_data[];
    private PhotoAdapter arrayAdapter;
    private MediaPlayer mPlayer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPlayer = new MediaPlayer();

        cardFrame = findViewById(R.id.card_frame);
        moreFrame = findViewById(R.id.more_frame);
        // start pulsator
        PulsatorLayout mPulsator = findViewById(R.id.pulsator);
        mPulsator.start();
        mNotificationHelper = new NotificationHelper(this);


        setupTopNavigationView();


        rowItems = new ArrayList<Cards>();
        Cards cards = new Cards("1", "捲菸", "美秀集團", "data:image/jpeg;base64,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", "Simple and beautiful Girl", "Acting", 200,R.raw.test1);
        rowItems.add(cards);
        cards = new Cards("2", "小老婆", "美秀", "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxISEhUSEBAVFRUVFRYVFxUVFRUVFRUVFRUWFhUVFRUYHSggGBolHRUVITEhJSkrLi4uFx8zODMtNygtLisBCgoKDg0OGxAQGysfICUtLS0tLSstLS0tLS0tLy0tLSsrLS0tLS0tLS0tLS0tKysrLS0tKy0tKystLS0rKystLf/AABEIAOEA4QMBIgACEQEDEQH/xAAbAAACAwEBAQAAAAAAAAAAAAAAAQIFBgMEB//EAEYQAAIBAgEHBwcJBgYDAAAAAAABAgMRIQQFBhIxQVETYXGBkaGxIjJCwdHh8AcjM1JicoKSsjRUc4OT0hQVFiRDwhfi8f/EABkBAQADAQEAAAAAAAAAAAAAAAABAgMEBf/EACERAQACAgICAwEBAAAAAAAAAAABAgMREjIhMRNBUSIE/9oADAMBAAIRAxEAPwD6QwACyAACAYhiAbABAAxDAAQIAAABANiY2hMAGIYCY0IYCGIYCSAYmAhoQ0AAgBAFwCwAACGACAAGAAACGxMBoAQIARJPAijnlNdQV3d4bEm32IDtrCuZrKdKI4wVOUd2s7Oy3tJPb1lJl2fqkfo8om+DaXfhiRtOn0HWFrGAo6X5So4qEueUbPuaR2p6XVsNZRtzKz77g03KkPWZVZvz1SqRu5pPhsfZ8eyzuShLWYORFje0A1mO5FABLWYXYmABrMSkxiQDcmK4AAAIAAGMTAAQIAKPPedqlKpqw1baqeKvi2+fmK9aQ1vs/l95LSd/Pfgj4yKqCO3HSs1jwwtadrX/AD+v9n8vvB5/rfY/L7ytaIlvjr+I5W/Vt/n9a3o7fq8EufnF/n9b7P5feVz2L42t+pIgx8dfw5W/Vn/qCt9n8vvPHnbPVSXkz1XbBKzSdsLvE40tq6cejeUecHKU3cyyxWseIXx7mfZ5Tlbk3d482zq5jyu5cU8xyjT5SrdOTSjDf0y9hW1nZ4HI30nCzTvuXacItvHcidNXsns3nTKvsrmA5U6soPWTafFGgzfpXWUVBuOCsm44u2y+Jn66wut550i9ZiJVmNtxS0jrtrzNv1d2/eL/AFHX+x+X3mcyHLLYT4NX6VbE9p2VrS3mIYWm0LeOkNd7NX8vvJVc/ZRHzopX4wa8SlpO0k3eyaeG3A9Wc8sVWSai1aKj6O5JbkuBPx136V5z+rKnnzKZebFS6IN+BY5gzlUrOanbyUrWVtrZTZmzqqCd6es5c6ta2xq3Fdnf7dEvOqdEfFmeSkRWfC9bTuPLSiQ2I5GxjECABgAEUDAGAACADK6TfTfgj4yKxFrpIvnvwR8ZFbSjdpcWd2Of4hz29nqnSlk8pebFvoTZKEbySW92XWzXZPRjBKMVZL4uzPNm4LUpyZDKaTi7STTslZq2xWPPJGyzjQVSEk1jZtPg9pkpLAnDl5wXpxcqW98E+/D1nszNkUHN1qkbqFrK1/Ld3d9CXeSyDN86qeqrLBOT2WxbtxeCNAsk5Gi408Xtu1tb6DP/AEZI1x+2mGk728GdtWrBKDxTuk8L4WsYbLKLjPVkrY7z6NkvlxXKQSk0rrg+ZmO0poOFR9Ka6Le45Ky6Lwp3Sd12d5zk2pNBGu8V1ivdl2absznJWIzTJJ3JQJSVj2Zuyj0Xu2dHA8So3PTTpqNmaY7cbbVtG4WUWBGLJPnPQchl/oh51T7sfFmduaHQ9+VU6I+LM83SV6dmnAYjz3SAAAGArAAAwABIYkAGY0j+m/BHxkVtJ49F32JssNJpfPfgj4yKym8H1Lrvf1M7sfSHPbtKSm1imajIs6QmsZKMt6btjzPgZS5KLK5MUXjymt5q02cc5QUXGElKTTWGKV99yhoOOtHX826v0HJysdshhGVSKm/J39hWuOMdZW3NrQ1aqKy1dmCSO8RKlHBpLZg+Yeqea707LgZ7SzJFKDlvXgXsii0pqfNW4lo9q2YSrFKyJTkluLLJczzqJytsV1z8C1yDR2P/ADJtvcnbVW673v2Gm2XGWZVmrs5TtuPRnvIuQqSpptrBp77NYX8CtUr4FoVenlkkeerXbOc0FGOtJLi+7eWjyL6ls+MUTZJebbh4N7O23ayJ6MenJKLNDoY/Kq/dj4sz7Rf6Gryqv3Y+LKZukrU7NUCAR57oMBIaALgFgAAAAEgHEEBk9J388vuR8ZFYpWj0vwXvZY6U/Tr7kfGRVz2Lo8W/VY78fSHPb3IuSiyCGXV26ORY5jgpVNVxv5L57c/q6yrL3RetCLmpNKUrWvwV7pdxln6S1xd4aKhBRVlsOpx5WPFCdaPE8t3OsjwZwyONWOrLpT4HeWUI5TrYYAc4ZNKnBKCXO3hbDCyDVUVeTxtZt828jUlNLz30PYYnSPPFScnC9oLCyVr9O8tEbRM6h5c95aqtaclisEuhFY5LcRZBGrCTsWGbaGOtwwR5sjydzfMtr9XSX0JWVko2X2Y+w6MOPc7lnkt9FBbeh9yv4pEDtru0tm5YRitrvtS4J9pwOuGAuaDQ3zqvRHxZnTRaG+dV6I+LM83SU07Q1AACOB0hDQkNAADABAAAEd4BuEBkNK38/wDy4+MiqlK/cuxWLTSz6f8Alx8ZFPE78fWHNf3LqmO5BDRdVMnSe18E+/D1kLkl5vS/BY+KIlaHellk4vzm+Z4mhySCklLirmVNNo7PWpW+rJrwfrOT/TSNcodGC071L3xoo7RpkkjpY4nS8OV4JnzbObcpyb4n0rKkY7PmapX1oRw329hekq2jwzUY3wSuywoZsVrz28NyPVkOTaqu1i/ix6bHdjxRrcuS1/qHPklGyWyyeHOrgkdqivJpcbLoWwi/J2bePDoN2aNThw729vs6jlYm0IlVBo0Gh3nVeiPiyhsX+h/nVOiPiymbpK1O0NOgQAcDpCGhIYDAQAAAIBsQABjtLV8//Lj4yKeKwLjS36f+XHxkU8Ed+PrDmt7lKJNEYkky6EidTB24Yde/vw6iNLDyuHju9vUIhKRbaPZZqy5N7J7OaXv9hUDpzaaa2pprpWwpkryrMLUtxnbcpnZSPLSmpJSW9J9qud6abwR5evp3oSiNZEnjLs9rPZTpKPO+PsCRetdMrX/GOz1m9U53TtGWKw2PejwUtXWW12x2JbMduPA2mX5IqkHCW9YPg9zMVybi5J4OKafTdRfizuxX5RpzXrqUHPcsFzb+lkGSsRkjZRGxxyh4Hc45T5vWJ9IeCjVakjZaIryqnRHxZiqfnI22iXnVOiPizK/SVq9oaQAA43QYAhAMBgAhMaEwAABAY3S5fP8A8uPjIqKew9um9ZrKUk/+KPjIoo5TLj4Hbjt/MOe0eVnFk4xvsKyOUy49xZ0J+Tb0tj5ubp49nEvyVTk9y2Lve9iCw0iUiwJDLPMObuWqYryI4y5+Eev2kWmIjckRtf5louVKF8PJXut1FvFJKyGlbBEZHnfe3VsmRYNiuShzqPEzGkVC0tZena/TG9/GJpJlTn+F6V/qu/a9VmuKdWVtG4ZmwmDkuIm1xR2MCOGV+b2HfWXFdp58sktV470JQrox818W+xWt6+w22iXnVOiPizFTdnFcEu9uX/Y2miLvKpj6MfFmN+kr17Q0whgzkbkMAAAAABACABANAB8907/aV/Cj+qZQR4Gh06g3lKexclDF7Ns+18yxM/yqWEeuT2vmXBfHMdNOsMLe3aL1dnncfq9H2ufd07PRkE8bcfUeBHajOzutxeJ0hckjz67dsOc6co+BpyhGnVI3uZ8i5GlGPpPypfefs2dRkdHKHKV43jhHyn1bO+xu7nNnvvw1xx9kyDJSZBs52iLZFinUS2s81bL4pfG7/wCltDpMrs9UnOjUgtrg7dNrrvOiy+LduJCtlkdty0IfK61eT2vZ1BHKZWsnvDK42nJcJNdjZzjs614mseWb2yqONnNWur9W5nKWXR3JvqOeU1brBbEltbvjtPLk0W5rmd3fhHF9yJ3KNPblWcFrStF4NrqWC8DV/JvXcp1rw1Vqwtje93IwVSDvh8dZuPkxm3OumtkKfjIrf0tWPLfiGDMGhDAQAAwAEA7/ABZBrAFhXEgQHzz5QJf7pfwo/qmZiNda1rq1tvPwLT5T5NZav4MP1VDM0Ztuyi2+Cx7jorPhlaPK4VRXsT5SxT8rzHeLVrvWXPq4dty21eLS5LO8ejb0fHgNVYv0l2rxKGnXj9d9aZYSoQ5HlLu+LteNrKWrsvrb+BE2hMVlu9BKacKlVX8qSim96hta63bqNRcqNHMn5LJqULWeom+mXlPvZZ6xz28y1jxCU5FblmXpYIedcp1VbiUNSpcRBMvTVymUjhVn3Ye3vuQUrK+97Pb8eo5ORdDpB49r60rrwOVRNEoPb1Lta9jDKHgWhEsJnHCrNfbl4hyqJZ4wr1Lcb9qTPJ5X1l2ImJVl6XVXwh0qq8p8IvdxtH/seN63FflR0UJqDxWMkti2JSb3cdUtyRpN1VxNj8ms0517P0YfqmYJuXFdi9htvkuvyle9vMp7ElvmVvPhNY8voYhiMGoQDQAK4CACQhgAgAEB8x+UfJlPLFi/oYbPvTM9klHk5Ka2rinbZY+kaQ6KyymuqyqxilGEXFwbvqybeKa23PFluguu1qVYQS17rk276zTjv3LA1i0KTEvn7yVX2vs9x6p404090W2sHfHjuNXP5O6l01lcejk5f3HT/QE/3mP9N/3E8qo1ZiqeTJb32e4uMhnKpyeT+jKSj6eyU03he27gXkdAKl7/AOKjbhyb/uPfmnRCVGrCrKspKLb1VBq7s0sbsbqalqk7HmyzLlBYbRZblGpHpKGpJyx73s7Sml3SvlDk7tnPZi+pcfcRulzvu7N5CUr7SUCcr4kHIJMg2SOjeHS/Bf8AsQlJsKjwXRftb9Vjm2Shk8+fTz6v0o8lzZPQ2WU3rKvGOt6Lg21q+Tt1lwJf+Pp/vMf6b/uHKDjLFxNDTX+2X8Cv4wLJfJ9P95j/AE3/AHHZ6C1dVR/xasm/Qla0rXVtbZgVtMSREwwLNp8l/n1/u0/GZJ/J1P8Aeo/03/cXmimjMsjlUlKqp66isIuNtVt8XxJtaJhERO2jAAMmgGAMCIDABgAmAAAAAAADAACSK/O+VOCiltd+PqLAoM+VL1LcEu/EmvtE+nmyjLZTte2HMvWeeUm9r7SNwLqmRYxMBM5slI5tkiVSV+xLsSRzbBsg2SjbXaP/AEEemX6mWJX5g+gh+L9TLAyn2vAGhAQlIQ9oggCGIABggAQAADAAAAAAAAGAIAQBIMpl1TWnJ87NNlM9WMnwi33GRuWqrIBsQFkFKVlceTQc9mrtaSbs3ZX4EZrBnncOa5EpiHqyiCiruS9HDH0lJrdb0Xc81yNeKbbSsm20uCbwRytbYTEkw7MgwUriZZRscwfs8PxfqkWB4Mwfs9P8X6pHvMZ9tYDAGDAaHfiJAEHYSiwEA3ETAAFYAABoEAAAkAANgAAERoACXlzn9FP7rMqAFqqyEJgBZCLOcgAiVoQkcpgAAhMALwpLZ6P/ALPD8X65HvADGfbSDkAAAIYAEBggABCQAAwAAP/Z", "cool Minded Girl", "Dancing", 800,R.raw.test3);
        rowItems.add(cards);
        cards = new Cards("3", "浪流連", "茄子蛋", "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQvTxYUT6bZPqjEBuq8y88YFVIAHaIAdl6-6hk-eP-9RIlOypt8&usqp=CAU", "Simple and beautiful Girl", "Singing", 400,R.raw.test2);
        rowItems.add(cards);
        cards = new Cards("4", "Preety Deshmukh", "19", "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSP-VNM3hrB4MDl_-7FI9f6gu0K4S9uhd-UpV8SRW0I8vmZ1JGY&usqp=CAU", "dashing girl", "swiming", 1308,R.raw.test);
        rowItems.add(cards);
        cards = new Cards("5", "Srutimayee Sen", "20", "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQvTxYUT6bZPqjEBuq8y88YFVIAHaIAdl6-6hk-eP-9RIlOypt8&usqp=CAU", "chulbuli nautankibaj ", "Drawing", 1200,R.raw.test3);
        rowItems.add(cards);
        cards = new Cards("6", "Dikshya Agarawal", "21", "https://3.bp.blogspot.com/-1yDGdtBgxoU/WxUvXwQOW3I/AAAAAAAACis/GYiqwfAMonwPVnE1UTvAiJN4OBv91Qn7QCLcBGAs/s1600/29744578_1913348338737226_6551562096976275766_o.jpg", "Simple and beautiful Girl", "Sleeping", 700,R.raw.test2);
        rowItems.add(cards);
        cards = new Cards("7", "Sudeshna Roy", "19", "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQB6H5JMp5FP6LE9At6HutJEVZ9C2XMgMPx69jAsjQe_8PMfcD-&usqp=CAU", "Papa's Pari", "Art", 5000,R.raw.test);
        rowItems.add(cards);

        arrayAdapter = new PhotoAdapter(this, R.layout.item, rowItems,this);

        checkRowItem();
        updateSwipeCard();
    }

    @Override
    protected void onStop() {
        super.onStop();
        StopMusic();
    }

    public void playMusic(Uri path) {

        if(path!=null) {
            mPlayer.reset();
            try {
                Toast.makeText(mContext, "played", Toast.LENGTH_LONG).show();
                mPlayer.setDataSource(mContext, path);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                mPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.start();
        }
        else{

        }
    }
    private void  StopMusic(){
        mPlayer.stop();
    }
    private void checkRowItem() {
        if (rowItems.isEmpty()) {
            moreFrame.setVisibility(View.VISIBLE);
            cardFrame.setVisibility(View.GONE);
        }
    }

    private void updateLocation() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        updateLocation();
                    } else {
                        Toast.makeText(MainActivity.this, "Location Permission Denied. You have to give permission inorder to know the user range ", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void updateSwipeCard() {
        final SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        playMusic(rowItems.get(0).getMusic());
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
                StopMusic();
                if(!rowItems.isEmpty()) {
                    playMusic(rowItems.get(0).getMusic());
                }
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Cards obj = (Cards) dataObject;
                checkRowItem();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Cards obj = (Cards) dataObject;

                //check matches
                checkRowItem();

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here


            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);

            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();



            }
        });
    }


    public void sendNotification() {
        NotificationCompat.Builder nb = mNotificationHelper.getChannel1Notification(mContext.getString(R.string.app_name), mContext.getString(R.string.match_notification));

        mNotificationHelper.getManager().notify(1, nb.build());
    }


    public void DislikeBtn(View v) {
        StopMusic();
        if (rowItems.size() != 0) {

            Cards card_item = rowItems.get(0);

            String userId = card_item.getUserId();

            rowItems.remove(0);
            arrayAdapter.notifyDataSetChanged();

            Intent btnClick = new Intent(mContext, BtnDislikeActivity.class);
            btnClick.putExtra("url", card_item.getProfileImageUrl());
            startActivity(btnClick);
        }
    }

    public void LikeBtn(View v) {
        StopMusic();
        if (rowItems.size() != 0) {

            Cards card_item = rowItems.get(0);

            String userId = card_item.getUserId();

            //check matches

            rowItems.remove(0);
            arrayAdapter.notifyDataSetChanged();
            Intent btnClick = new Intent(mContext, BtnLikeActivity.class);
            btnClick.putExtra("url", card_item.getProfileImageUrl());
            startActivity(btnClick);
        }
    }


    /**
     * setup top tool bar
     */
    private void setupTopNavigationView() {

        Log.d(TAG, "setupTopNavigationView: setting up TopNavigationView");
        BottomNavigationViewEx tvEx = findViewById(R.id.topNavViewBar);
        TopNavigationViewHelper.setupTopNavigationView(tvEx);
        TopNavigationViewHelper.enableNavigation(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


    @Override
    public void onBackPressed() {

    }


}
