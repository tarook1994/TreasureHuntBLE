package example.treasurehunt;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.Utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainSpareActivity extends AppCompatActivity implements BeaconManager.RangingListener {
    ArrayList<String> databaseArray,EMC,C3101,databaseArray2;
    TextView hello,question;
    RadioButton ans1,ans2,ans3,ans4;
    Button submit;
    int clickCounter,finishedQuestions;
    String rightAnswer;
    int arrayIndexNow;
    int numberOfQuestions;
    int EMCquestions,C3questions,EMCfinished,C3finished;
    String currentQuestion;
    public static String q ="";
    public static String a1 ="";
    public static String a2="";
    public static String a3 ="";
    public static String a4 ="";
    boolean answeredRight;
    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);
    private BeaconManager beaconManager;
    private Region region;
    int searchedTimes;
    ArrayList<String> nearByBeacons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nearByBeacons = new ArrayList<String>();
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(this);
        searchedTimes=0;
        clickCounter=0;
        answeredRight=false;
        finishedQuestions=0;
        arrayIndexNow=0;
        numberOfQuestions=0;
        EMC = new ArrayList<String>();
        C3101 = new ArrayList<String>();
        EMCquestions=0;
        C3questions=0;
        EMCfinished=0;
        C3finished=0;
        currentQuestion ="";
        databaseArray2 = new ArrayList<String>();
        hello = (TextView) findViewById(R.id.name);
        question = (TextView) findViewById(R.id.Question);
        ans1 = (RadioButton) findViewById(R.id.answer1);
        ans2 = (RadioButton) findViewById(R.id.answer2);
        ans3 = (RadioButton) findViewById(R.id.answer3);
        ans4 = (RadioButton) findViewById(R.id.answer4);
        submit = (Button) findViewById(R.id.subanswer);
        if(getIntent().getStringExtra("name").equals("not found")){
            Toast.makeText(getApplicationContext(),"not found",Toast.LENGTH_SHORT).show();
        } else {
            hello.setText("Hello "+getIntent().getStringExtra("name"));
        }
        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1000);

        final Animation out = new AlphaAnimation(1.0f, 0.0f);
        out.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                question.startAnimation(in);
//                ans1.startAnimation(in);
//                ans2.startAnimation(in);
//                ans3.startAnimation(in);
//                ans4.startAnimation(in);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        submit.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(MainSpareActivity.this,TestActivity.class);
                startActivity(i);
                return false;
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ans1.getText().toString().equals(rightAnswer)){
                    if(ans1.isChecked()){
                        Toast.makeText(getApplicationContext(),"You answered the right answer",Toast.LENGTH_LONG).show();
                        finishedQuestions++;
                        answeredRight=true;
                    } else {
                        Toast.makeText(getApplicationContext(),"Wrong answer try again",Toast.LENGTH_LONG).show();

                    }

                } else {
                    if(ans2.getText().toString().equals(rightAnswer)){
                        if(ans2.isChecked()){
                            Toast.makeText(getApplicationContext(),"You answered the right answer",Toast.LENGTH_LONG).show();
                            finishedQuestions++;
                            answeredRight=true;
                        } else {
                            Toast.makeText(getApplicationContext(),"Wrong answer try again",Toast.LENGTH_LONG).show();

                        }

                    } else {
                        if(ans3.getText().toString().equals(rightAnswer)){
                            if(ans3.isChecked()){
                                Toast.makeText(getApplicationContext(),"You answered the right answer",Toast.LENGTH_LONG).show();
                                finishedQuestions++;
                                answeredRight=true;
                            } else {
                                Toast.makeText(getApplicationContext(),"Wrong answer try again",Toast.LENGTH_LONG).show();

                            }
                        } else {
                            if(ans4.isChecked()){
                                Toast.makeText(getApplicationContext(),"You answered the right answer",Toast.LENGTH_LONG).show();
                                finishedQuestions++;
                                answeredRight=true;
                            } else {
                                Toast.makeText(getApplicationContext(),"Wrong answer try again",Toast.LENGTH_LONG).show();

                            }

                        }
                    }
                }
                if(answeredRight){
                    answeredRight=false;
                    int index=0;
                    if(EMCfinished==1){
                        EMCfinished++;
                        out.setDuration(1000);
                        question.startAnimation(in);
                        ans1.startAnimation(in);
                        ans2.startAnimation(in);
                        ans3.startAnimation(in);
                        ans4.startAnimation(in);
                        question.setText("Question : " + EMC.get(2));
                        ans1.setText(EMC.get(3).split(",")[0]);
                        ans2.setText(EMC.get(3).split(",")[1]);
                        ans3.setText(EMC.get(3).split(",")[2]);
                        ans4.setText(EMC.get(3).split(",")[3]);
                        rightAnswer = EMC.get(3).split(",")[4];

                    } else {
                        if(currentQuestion.equals("EMC")){
                            if(EMCfinished<EMCquestions){
                                index= EMCfinished+1;
                                out.setDuration(1000);
                                question.startAnimation(in);
                                ans1.startAnimation(in);
                                ans2.startAnimation(in);
                                ans3.startAnimation(in);
                                ans4.startAnimation(in);
                                question.setText("Question : " + EMC.get(index));
                                index++;
                                ans1.setText(EMC.get(index).split(",")[0]);
                                ans2.setText(EMC.get(index).split(",")[1]);
                                ans3.setText(EMC.get(index).split(",")[2]);
                                ans4.setText(EMC.get(index).split(",")[3]);
                                rightAnswer= EMC.get(index).split(",")[4];
                                EMCfinished++;

                            } else {
                                currentQuestion="C3";
                                if(C3finished==0){
                                    out.setDuration(1000);
                                    question.startAnimation(in);
                                    ans1.startAnimation(in);
                                    ans2.startAnimation(in);
                                    ans3.startAnimation(in);
                                    ans4.startAnimation(in);
                                    question.setText("Question : " + C3101.get(0));
                                    ans1.setText(C3101.get(1).split(",")[0]);
                                    ans2.setText(C3101.get(1).split(",")[1]);
                                    ans3.setText(C3101.get(1).split(",")[2]);
                                    ans4.setText(C3101.get(1).split(",")[3]);
                                    rightAnswer= C3101.get(1).split(",")[4];
                                    C3finished++;
                                } else {
                                    out.setDuration(1000);
                                    question.startAnimation(in);
                                    ans1.startAnimation(in);
                                    ans2.startAnimation(in);
                                    ans3.startAnimation(in);
                                    ans4.startAnimation(in);
                                    index = C3finished+1;
                                    question.setText("Question : " + C3101.get(index));
                                    index++;
                                    ans1.setText(C3101.get(index).split(",")[0]);
                                    ans2.setText(C3101.get(index).split(",")[1]);
                                    ans3.setText(C3101.get(index).split(",")[2]);
                                    ans4.setText(C3101.get(index).split(",")[3]);
                                    rightAnswer= C3101.get(index).split(",")[4];
                                    C3finished++;
                                }
                            }
                        } else {
                            if(currentQuestion.equals("C3")){
                                if(C3finished<C3questions){
                                    out.setDuration(1000);
                                    question.startAnimation(in);
                                    ans1.startAnimation(in);
                                    ans2.startAnimation(in);
                                    ans3.startAnimation(in);
                                    ans4.startAnimation(in);
                                    Log.d("C3finished",C3finished+"");
                                    Log.d("C3Questions",C3questions+"");
                                    index = C3finished+1;
                                    question.setText("Question : " + C3101.get(index));
                                    index++;
                                    ans1.setText(C3101.get(index).split(",")[0]);
                                    ans2.setText(C3101.get(index).split(",")[1]);
                                    ans3.setText(C3101.get(index).split(",")[2]);
                                    ans4.setText(C3101.get(index).split(",")[3]);
                                    rightAnswer= C3101.get(index).split(",")[4];
                                    C3finished++;

                                } else {
                                    Toast.makeText(getApplicationContext(),"Questions Are finished",Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    }
                }
            }

        });
        ans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ans1.isChecked()){
                    ans1.setChecked(true);
                    ans2.setChecked(false);
                    ans3.setChecked(false);
                    ans4.setChecked(false);
                    Log.d("not checked","not checked");
                } else {
                    ans1.setChecked(true);
                    ans2.setChecked(false);
                    ans3.setChecked(false);
                    ans4.setChecked(false);
                    Log.d("checked","checked");

                }
            }
        });
        ans2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ans2.isChecked()){
                    ans1.setChecked(false);
                    ans2.setChecked(true);
                    ans3.setChecked(false);
                    ans4.setChecked(false);
                } else {
                    ans1.setChecked(false);
                    ans2.setChecked(true);
                    ans3.setChecked(false);
                    ans4.setChecked(false);
                }
            }
        });
        ans3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ans3.isChecked()){
                    ans1.setChecked(false);
                    ans2.setChecked(false);
                    ans3.setChecked(true);
                    ans4.setChecked(false);
                } else {
                    ans1.setChecked(false);
                    ans2.setChecked(false);
                    ans3.setChecked(true);
                    ans4.setChecked(false);
                }
            }
        });
        ans4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ans4.isChecked()){
                    ans1.setChecked(false);
                    ans2.setChecked(false);
                    ans3.setChecked(false);
                    ans4.setChecked(true);
                } else {
                    ans1.setChecked(false);
                    ans2.setChecked(false);
                    ans3.setChecked(false);
                    ans4.setChecked(true);
                }
            }
        });


        final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        DatabaseReference myRef2 = database2.getReference();
        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                    int counter=1;

                    databaseArray2.add(friendSnapshot.getKey().toString());

                    for(int i =0 ;i<friendSnapshot.getChildrenCount()/2;i++){

                        databaseArray2.add(friendSnapshot.child("Question"+counter).getValue()+"");
                        databaseArray2.add(friendSnapshot.child("Answer"+counter).getValue()+"");
                        counter++;
                    }

                }
                for(int j=0;j<databaseArray2.size();j++){

                    Log.d("arrayitemtest",databaseArray2.get(j));
                }
                int emcflag=0;
                int c3flag=0;
                for(int k =0;k<databaseArray2.size();k++){
                    if(databaseArray2.get(k).equals("C3-101")){
                        c3flag=1;
                    } else {
                        if(databaseArray2.get(k).equals("EMC")){
                            emcflag=1;
                        }
                    }
                    if(emcflag==1){
                        emcflag=0;
                        for(int m=k+1;m<databaseArray2.size();m++){
                            if(databaseArray2.get(m).contains(",") || databaseArray2.get(m).contains("?")){
                                EMC.add(databaseArray2.get(m));
                            } else {
                                break;
                            }
                        }
                        EMCquestions = EMC.size()/2;

                    } else {
                        if(c3flag==1){
                            c3flag=0;
                            for(int n=k+1;n<databaseArray2.size();n++){
                                if(databaseArray2.get(n).contains(",") || databaseArray2.get(n).contains("?")){
                                    C3101.add(databaseArray2.get(n));
                                } else {
                                    break;
                                }
                            }
                            C3questions=C3101.size()/2;
                            Log.d("C3",C3101.size()/2+"");
                        }
                    }
                    for(int c=0;c<EMC.size();c++){
                        Log.d("EMC",EMC.get(c));
                    }
                    for(int c=0;c<C3101.size();c++){
                        Log.d("C3",C3101.get(c));
                    }
                }
                if(nearByBeacons == null || nearByBeacons.size()==0){

                    Toast.makeText(getApplicationContext(),"mafeeesh beacons",Toast.LENGTH_SHORT).show();

                } else {

                    if(nearByBeacons.get(0).equals("20:91:48:4C:5B:16")){
                        question.setText("Question 1 : "+EMC.get(0));
                        ans1.setText(EMC.get(1).split(",")[0]);
                        ans2.setText(EMC.get(1).split(",")[1]);
                        ans3.setText(EMC.get(1).split(",")[2]);
                        ans4.setText(EMC.get(1).split(",")[3]);
                        rightAnswer=EMC.get(1).split(",")[4];
                        currentQuestion = "EMC";
                        EMCfinished++;
                    } else {
                        if(nearByBeacons.get(0).equals("20:91:48:42:86:D5")){
                            question.setText("Question 1 : "+C3101.get(0));
                            ans1.setText(C3101.get(1).split(",")[0]);
                            ans2.setText(C3101.get(1).split(",")[1]);
                            ans3.setText(C3101.get(1).split(",")[2]);
                            ans4.setText(C3101.get(1).split(",")[3]);
                            rightAnswer=C3101.get(1).split(",")[4];
                            currentQuestion = "C3";
                            C3finished++;
                        }
                    }

                }


                final Animation in = new AlphaAnimation(0.0f, 1.0f);
                in.setDuration(1000);
                question.startAnimation(in);
                ans1.startAnimation(in);
                ans2.startAnimation(in);
                ans3.startAnimation(in);
                ans4.startAnimation(in);
                submit.startAnimation(in);
                question.setVisibility(View.VISIBLE);
                ans1.setVisibility(View.VISIBLE);
                ans2.setVisibility(View.VISIBLE);
                ans3.setVisibility(View.VISIBLE);
                ans4.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
//                currentQuestion="EMC";
//                EMCfinished++;


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public void animation(String a,  String b,  String c,  String d,  String e){
        q =a;
        a1= b;
        a2=c;
        a3=d;
        a4=e;
        Log.d("ana gy eh ", q);

        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1000);

        final Animation out = new AlphaAnimation(1.0f, 0.0f);
        out.setAnimationListener(new Animation.AnimationListener() {


            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                question.setText(q);
                ans1.setText(a1);
                ans2.setText(a2);
                ans3.setText(a3);
                ans4.setText(a4);
                question.startAnimation(in);
                ans1.startAnimation(in);
                ans2.startAnimation(in);
                ans3.startAnimation(in);
                ans4.startAnimation(in);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onBeaconsDiscovered(Region region, List<Beacon> list) {
        searchedTimes++;
        for (int i = 0; i < list.size(); i++) {
            Log.i("beaconsMAC", "" + list.get(i).getMacAddress().toStandardString());
            Log.i("beaconsRSSI", "" + list.get(i).getRssi());
            nearByBeacons.add(list.get(i).getMacAddress().toStandardString());
            if(nearByBeacons.size()==0 || nearByBeacons==null){
                if(searchedTimes==3){
                    beaconManager.disconnect();
                    break;
                }
            } else {
                beaconManager.disconnect();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                if (region != null) {
                    beaconManager.startRanging(region);
                } else {

                    Intent enableBtIntent = new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    int REQUEST_ENABLE_BT = 0;
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
            }
        });
    }

    private void startScanning() {
        Toast.makeText(this, "Scanning..", Toast.LENGTH_LONG);
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SystemRequirementsChecker.checkWithDefaultDialogs(this)) {
            startScanning();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        beaconManager.disconnect();
    }
}

