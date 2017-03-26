package example.treasurehunt;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GpsActivity extends AppCompatActivity {
    TextView hello,question;
    RadioButton ans1,ans2,ans3,ans4;
    Button submit,destination;
    GPSTracker gps;
    int questionsSize=2;
    ArrayList<String> databaseArray,laRoma,platform,orange,uD,uC,sa3d,pronto,clues;
    int laRomaFinished,platformFinished,orangeFinihsed,uDFinished,uCFinished,sa3dFinished,prontoFinished;
    String currentQuestion="";
    String rightAnswer="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        laRomaFinished = 0;
        platformFinished=0;
        orangeFinihsed=0;
        uDFinished=0;
        uCFinished=0;
        sa3dFinished=0;
        prontoFinished=0;
        hello = (TextView) findViewById(R.id.namee);
        question = (TextView) findViewById(R.id.Question);
        ans1 = (RadioButton) findViewById(R.id.answer1);
        ans2 = (RadioButton) findViewById(R.id.answer2);
        ans3 = (RadioButton) findViewById(R.id.answer3);
        ans4 = (RadioButton) findViewById(R.id.answer4);
        submit = (Button) findViewById(R.id.subanswer);
        destination= (Button) findViewById(R.id.destination);
        databaseArray = new ArrayList<String>();
        laRoma = new ArrayList<String>();
        platform = new ArrayList<String>();
        orange = new ArrayList<String>();
        uD = new ArrayList<String>();
        uC = new ArrayList<String>();
        sa3d = new ArrayList<String>();
        pronto = new ArrayList<String>();
        clues = new ArrayList<String>();
        clues.add("Go to Platform");
        clues.add("Go to 3am Sa3d");
        clues.add("Go to Pronto");
        clues.add("Go to Laroma");
        clues.add("Go to Orange");
        clues.add("Go to UC");
        clues.add("Go to UD");
        gps = new GPSTracker(getApplicationContext());
        if(!gps.canGetLocation()){

            Toast.makeText(getApplicationContext(),"Please Enable Your GPS",Toast.LENGTH_SHORT).show();

        }


        final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        DatabaseReference myRef2 = database2.getReference();
        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                    int counter=1;

                    databaseArray.add(friendSnapshot.getKey().toString());

                    for(int i =0 ;i<2;i++){

                        databaseArray.add(friendSnapshot.child("Question"+counter).getValue()+"");
                        databaseArray.add(friendSnapshot.child("Answer"+counter).getValue()+"");
                        Log.d("null wla la2",friendSnapshot.child("Lad").getValue()+"");

                        counter++;
                    }
                    if(friendSnapshot.child("Lad").getValue()!=null){
                        if(!friendSnapshot.child("Lad").getValue().equals("null")){
                            databaseArray.add(friendSnapshot.child("Lad").getValue()+"");
                            databaseArray.add(friendSnapshot.child("Lng").getValue()+"");
                        }

                    }

                }
                for(int k =0;k<databaseArray.size();k++){
                    Log.d("database",databaseArray.get(k)+"");
                }
                int amSa3dFlag=0;
                int prontoFlag=0;
                int platformFlag=0;
                int laromeFlag=0;
                int orangeFlag=0;
                int uCFlag=0;
                int uDFlag=0;
                for(int i =0;i<databaseArray.size();i++){

                    if(databaseArray.get(i).equals("U D")){
                        uDFlag=1;
                    } else {
                        if(databaseArray.get(i).equals("3am Sa3d")){
                            amSa3dFlag=1;
                        } else {
                            if(databaseArray.get(i).equals("Laroma")){
                                laromeFlag=1;
                            } else {
                                if(databaseArray.get(i).equals("Orange")){
                                    orangeFlag=1;
                                } else {
                                    if(databaseArray.get(i).equals("U C")) {
                                        uCFlag=1;
                                    } else {
                                        if(databaseArray.get(i).equals("Pronto")){
                                            prontoFlag=1;
                                        } else {
                                            if(databaseArray.get(i).equals("Platform")){
                                                platformFlag=1;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if(uCFlag==1){
                        uCFlag=0;
                        for(int m=i+1;m<databaseArray.size();m++){
                            if(databaseArray.get(m).contains(",") || databaseArray.get(m).contains("?")||databaseArray.get(m).contains("!")){
                                if(databaseArray.get(m).contains("!")){

                                    uC.add(databaseArray.get(m).replace("!","0"));

                                } else {
                                    uC.add(databaseArray.get(m));
                                }
                            } else {

                                break;
                            }
                        }

                    } else {
                        if(uDFlag==1){
                            uDFlag=0;
                            for(int m=i+1;m<databaseArray.size();m++){
                                if(databaseArray.get(m).contains(",") || databaseArray.get(m).contains("?")||databaseArray.get(m).contains("!")){
                                    if(databaseArray.get(m).contains("!")){
                                        uD.add(databaseArray.get(m).replace("!","0"));

                                    } else {
                                        uD.add(databaseArray.get(m));
                                    }
                                } else {

                                    break;
                                }
                            }

                        } else {
                            if(amSa3dFlag==1){
                                amSa3dFlag=0;
                                for(int m=i+1;m<databaseArray.size();m++){
                                    if(databaseArray.get(m).contains(",") || databaseArray.get(m).contains("?")||databaseArray.get(m).contains("!")){
                                        if(databaseArray.get(m).contains("!")){
                                            sa3d.add(databaseArray.get(m).replace("!","0"));
                                        } else {
                                            sa3d.add(databaseArray.get(m));
                                        }
                                    } else {

                                        break;
                                    }
                                }

                            } else {
                                if(platformFlag==1){
                                    platformFlag=0;
                                    for(int m=i+1;m<databaseArray.size();m++){
                                        if(databaseArray.get(m).contains(",") || databaseArray.get(m).contains("?")||databaseArray.get(m).contains("!")){
                                            if(databaseArray.get(m).contains("!")){
                                                platform.add(databaseArray.get(m).replace("!","0"));

                                            } else {
                                                platform.add(databaseArray.get(m));

                                            }
                                        } else {

                                            break;
                                        }
                                    }

                                } else {
                                    if(prontoFlag==1){
                                        prontoFlag=0;
                                        for(int m=i+1;m<databaseArray.size();m++){
                                            if(databaseArray.get(m).contains(",") || databaseArray.get(m).contains("?")||databaseArray.get(m).contains("!")){
                                                if(databaseArray.get(m).contains("!")){
                                                    pronto.add(databaseArray.get(m).replace("!","0"));

                                                } else {

                                                    pronto.add(databaseArray.get(m));

                                                }
                                            } else {

                                                break;
                                            }
                                        }

                                    } else {
                                        if(laromeFlag==1){
                                            laromeFlag=0;
                                            for(int m=i+1;m<databaseArray.size();m++){
                                                if(databaseArray.get(m).contains(",") || databaseArray.get(m).contains("?")||databaseArray.get(m).contains("!")){
                                                    if(databaseArray.get(m).contains("!")){

                                                        laRoma.add(databaseArray.get(m).replace("!","0"));

                                                    } else {
                                                        laRoma.add(databaseArray.get(m));

                                                    }
                                                } else {

                                                    break;
                                                }
                                            }

                                        } else {
                                            if(orangeFlag==1){
                                                orangeFlag=0;
                                                for(int m=i+1;m<databaseArray.size();m++){
                                                    if(databaseArray.get(m).contains(",") || databaseArray.get(m).contains("?")||databaseArray.get(m).contains("!")){
                                                        if(databaseArray.get(m).contains("!")){
                                                            orange.add(databaseArray.get(m).replace("!","0"));

                                                        } else {
                                                            orange.add(databaseArray.get(m));

                                                        }
                                                    } else {

                                                        break;
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                for(int i =0;i<uD.size();i++){
                    Log.d("UD",uD.get(i)+"");
                }
                for(int i =0;i<uC.size();i++){
                    Log.d("Uc",uC.get(i)+"");
                }
                for(int i =0;i<platform.size();i++){
                    Log.d("platform",platform.get(i)+"");
                }
                for(int i =0;i<orange.size();i++){
                    Log.d("orange",orange.get(i)+"");
                }
                for(int i =0;i<sa3d.size();i++){
                    Log.d("sa3d",sa3d.get(i)+"");
                }
                for(int i =0;i<laRoma.size();i++){
                    Log.d("Laroma",laRoma.get(i)+"");
                }
                Log.d("get accuracy",gps.getAccuarcy()+"");
                Log.d("get lad",gps.getLatitude()+"");
                Log.d("get lng",gps.getLongitude()+"");
                double currentLad = gps.getLatitude();
                double currentLng = gps.getLongitude();
                double destLad =Double.parseDouble(platform.get(platform.size()-2));
                double destLng = Double.parseDouble(platform.get(platform.size()-1));
                float [] x = new float[10];
                android.location.Location.distanceBetween(currentLad,currentLad,destLad,destLng,x);
                for(int h=0;h<10;h++){
                    Log.d("Result",x[h]+"");
                }

                if(calculateDistance(currentLad,currentLng,Double.parseDouble(platform.get(platform.size()-2))
                        ,Double.parseDouble(platform.get(platform.size()-1)))<=0.15){

                    currentQuestion="platform";
                    question.setText("Question 1 : "+platform.get(0));
                    ans1.setText(platform.get(1).split(",")[0]);
                    ans2.setText(platform.get(1).split(",")[1]);
                    ans3.setText(platform.get(1).split(",")[2]);
                    ans4.setText(platform.get(1).split(",")[3]);
                    rightAnswer=platform.get(1).split(",")[4];
                    for(int i =0;i<clues.size();i++){
                        if(clues.get(i).contains("Platform")){
                            clues.remove(i);
                        }
                    }
                    platformFinished++;
                } else {
                    if(calculateDistance(currentLad,currentLng,Double.parseDouble(uC.get(uC.size()-2))
                            ,Double.parseDouble(uC.get(uC.size()-1)))<=0.15){

                        currentQuestion="UC";
                        question.setText("Question 1 : "+uC.get(0));
                        ans1.setText(uC.get(1).split(",")[0]);
                        ans2.setText(uC.get(1).split(",")[1]);
                        ans3.setText(uC.get(1).split(",")[2]);
                        ans4.setText(uC.get(1).split(",")[3]);
                        rightAnswer=uC.get(1).split(",")[4];
                        for(int i =0;i<clues.size();i++){
                            if(clues.get(i).contains("UC")){
                                clues.remove(i);
                            }
                        }
                        platformFinished++;
                    } else {
                        if(calculateDistance(currentLad,currentLng,Double.parseDouble(uD.get(uD.size()-2))
                                ,Double.parseDouble(uD.get(uD.size()-1)))<=0.15){

                            currentQuestion="UD";
                            question.setText("Question 1 : "+uD.get(0));
                            ans1.setText(uD.get(1).split(",")[0]);
                            ans2.setText(uD.get(1).split(",")[1]);
                            ans3.setText(uD.get(1).split(",")[2]);
                            ans4.setText(uD.get(1).split(",")[3]);
                            rightAnswer=uD.get(1).split(",")[4];
                            for(int i =0;i<clues.size();i++){
                                if(clues.get(i).contains("UD")){
                                    clues.remove(i);
                                }
                            }
                            uDFinished++;
                        } else {
                            if(calculateDistance(currentLad,currentLng,Double.parseDouble(sa3d.get(sa3d.size()-2))
                                    ,Double.parseDouble(sa3d.get(sa3d.size()-1)))<=0.15){

                                currentQuestion="3am";
                                question.setText("Question 1 : "+sa3d.get(0));
                                ans1.setText(sa3d.get(1).split(",")[0]);
                                ans2.setText(sa3d.get(1).split(",")[1]);
                                ans3.setText(sa3d.get(1).split(",")[2]);
                                ans4.setText(sa3d.get(1).split(",")[3]);
                                rightAnswer=sa3d.get(1).split(",")[4];
                                for(int i =0;i<clues.size();i++){
                                    if(clues.get(i).contains("3am")){
                                        clues.remove(i);
                                    }
                                }
                                sa3dFinished++;
                            } else {
                                if(calculateDistance(currentLad,currentLng,Double.parseDouble(pronto.get(pronto.size()-2))
                                        ,Double.parseDouble(pronto.get(pronto.size()-1)))<=0.15){

                                    currentQuestion="Pronto";
                                    question.setText("Question 1 : "+pronto.get(0));
                                    ans1.setText(pronto.get(1).split(",")[0]);
                                    ans2.setText(pronto.get(1).split(",")[1]);
                                    ans3.setText(pronto.get(1).split(",")[2]);
                                    ans4.setText(pronto.get(1).split(",")[3]);
                                    rightAnswer=pronto.get(1).split(",")[4];
                                    for(int i =0;i<clues.size();i++){
                                        if(clues.get(i).contains("Pronto")){
                                            clues.remove(i);
                                        }
                                    }
                                    prontoFinished++;
                                } else {
                                    if(calculateDistance(currentLad,currentLng,Double.parseDouble(orange.get(orange.size()-2))
                                            ,Double.parseDouble(orange.get(orange.size()-1)))<=0.15){

                                        currentQuestion="Orange";
                                        question.setText("Question 1 : "+orange.get(0));
                                        ans1.setText(orange.get(1).split(",")[0]);
                                        ans2.setText(orange.get(1).split(",")[1]);
                                        ans3.setText(orange.get(1).split(",")[2]);
                                        ans4.setText(orange.get(1).split(",")[3]);
                                        rightAnswer=orange.get(1).split(",")[4];
                                        for(int i =0;i<clues.size();i++){
                                            if(clues.get(i).contains("Orange")){
                                                clues.remove(i);
                                            }
                                        }
                                        orangeFinihsed++;
                                    } else {
                                        if(calculateDistance(currentLad,currentLng,Double.parseDouble(laRoma.get(laRoma.size()-2))
                                                ,Double.parseDouble(laRoma.get(laRoma.size()-1)))<=0.15){

                                            currentQuestion="Laroma";
                                            question.setText("Question 1 : "+laRoma.get(0));
                                            ans1.setText(laRoma.get(1).split(",")[0]);
                                            ans2.setText(laRoma.get(1).split(",")[1]);
                                            ans3.setText(laRoma.get(1).split(",")[2]);
                                            ans4.setText(laRoma.get(1).split(",")[3]);
                                            rightAnswer=laRoma.get(1).split(",")[4];
                                            for(int i =0;i<clues.size();i++){
                                                if(clues.get(i).contains("Larome")){
                                                    clues.remove(i);
                                                }
                                            }
                                            orangeFinihsed++;
                                        } else {
                                            Toast.makeText(getApplicationContext(),"msh f wala 7eta  "+ currentLad + " " +currentLng,Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public double calculateDistance(double userLat, double userLng,
                                    double venueLat, double venueLng) {
        double AVERAGE_RADIUS_OF_EARTH = 6371;

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (double) (AVERAGE_RADIUS_OF_EARTH * c);
    }

}
