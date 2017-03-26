package example.treasurehunt;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements BeaconManager.RangingListener {
    ArrayList<String> databaseArray,EMC,C3101,databaseArray2,C3332,C3331,RFLab,macAddress,clues;
    TextView hello,question;
    RadioButton ans1,ans2,ans3,ans4;
    Button submit;
    int clickCounter,finishedQuestions;
    String rightAnswer;
    int arrayIndexNow;
    int numberOfQuestions;
    int EMCquestions,C3questions,EMCfinished,C3finished,C3332Questions,C3332Finished,C3331Questions,C3331Finished,RFQuestions,RFFinished;
    String currentQuestion;
    boolean discoverFinished=false;
    public static String q ="";
    public static String a1 ="";
    public static String a2="";
    public static String a3 ="";
    public static String a4 ="";
    private final String EMC_ADDRESS="20:91:48:4C:5B:E6";
    private final String C3332_ADDRESS = "20:91:48:4C:5B:16";
    private final String C3331_ADDRESS = "20:91:48:42:84:00";
    private final String RF_ADDRESS = "20:91:48:42:86:D5";
    boolean answeredRight;
    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);
    private BeaconManager beaconManager;
    private Region region;
    int searchedTimes;
    ArrayList<String> nearByBeacons;
    String EMClue,C3101Clue,C3332Clue,C3331Clue,RFClue;
    Button destination;
    String nextHop="";
    public static Activity act;
    int finishedNodes=0;
    String currentClue="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
        setContentView(R.layout.activity_main);
        destination = (Button) findViewById(R.id.destination);
        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ScanThread().execute();
            }
        });
        macAddress= new ArrayList<String>();
        clues= new ArrayList<String>();
        macAddress.add(EMC_ADDRESS);
        macAddress.add(C3331_ADDRESS);
        macAddress.add(C3332_ADDRESS);
        macAddress.add(RF_ADDRESS);
        clues.add("Go to EMC");
        clues.add("Go to C3331");
        clues.add("Go to C3332");
        clues.add("Go to RF");
        C3331Questions = 0;
        C3331Finished=0;
        C3332Questions=0;
        C3332Finished=0;
        RFFinished=0;
        RFQuestions=0;
        nearByBeacons = new ArrayList<String>();
        EMClue="";
        C3101Clue="";
        C3331Clue="";
        C3332Clue="";
        RFClue="";
        C3331= new ArrayList<String>();
        C3332 = new ArrayList<String>();
        RFLab = new ArrayList<String>();
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
        hello = (TextView) findViewById(R.id.namee);
        question = (TextView) findViewById(R.id.Question);
        ans1 = (RadioButton) findViewById(R.id.answer1);
        ans2 = (RadioButton) findViewById(R.id.answer2);
        ans3 = (RadioButton) findViewById(R.id.answer3);
        ans4 = (RadioButton) findViewById(R.id.answer4);
        submit = (Button) findViewById(R.id.subanswer);
//        if(getIntent().getStringExtra("name").equals("not found")){
//            Toast.makeText(getApplicationContext(),"not found",Toast.LENGTH_SHORT).show();
//        } else {
//            hello.setText("Hello "+getIntent().getStringExtra("name"));
//        }
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
//                ans3.startAnimation(in);
//                ans2.startAnimation(in);
//                ans4.startAnimation(in);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        submit.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(MainActivity.this,TestActivity.class);
                startActivity(i);
                return false;
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(submit.getText().toString().equals("Re-scan")){
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
                    beaconManager.setRangingListener(new BeaconManager.RangingListener() {
                        @Override
                        public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                            Log.d("wasalt","hena");
                            searchedTimes++;
                            for (int i = 0; i < list.size(); i++) {
                                Log.i("beaconsMAC", "" + list.get(i).getMacAddress().toStandardString());
                                Log.i("beaconsRSSI", "" + list.get(i).getRssi());

                                //nearByBeacons.add(list.get(i).getMacAddress().toStandardString());
                                nearByBeacons = filterBeacons(list);
                                if(nearByBeacons.size()==0 || nearByBeacons==null){
                                    Log.d("ana gowa el finished","if");
                                    if(searchedTimes==3){
                                        beaconManager.disconnect();
                                        discoverFinished=true;
                                        break;
                                    }
                                } else {
                                    discoverFinished=true;
                                    Log.d("ana gowa el finished","else");
                                    beaconManager.disconnect();
                                }
                            }
                        }
                    });
                    boolean isEmptyyy =false;
                    if(nearByBeacons.size()==0){
                        isEmptyyy=true;
                    } else {
                        if(nearByBeacons.get(0).equals("20:91:48:4C:5B:E6")){
                            submit.setText("Submit");
                            question.setText("Question 1 : "+EMC.get(0));
                            ans1.setText(EMC.get(1).split(",")[0]);
                            ans2.setText(EMC.get(1).split(",")[1]);
                            ans3.setText(EMC.get(1).split(",")[2]);
                            ans4.setText(EMC.get(1).split(",")[3]);
                            rightAnswer=EMC.get(1).split(",")[4];
                            for(int i =0;i<clues.size();i++){
                                if(clues.get(i).contains("EMC")){
                                    int clueIndex = i;
                                    clues.remove(i);
                                    macAddress.remove(i);
                                }
                            }
                            currentQuestion = "EMC";
                            EMCfinished++;
                        } else {
                            if(nearByBeacons.get(0).equals("20:91:48:42:86:D5")){
                                submit.setText("Submit");
                                question.setText("Question 1 : "+RFLab.get(0));
                                ans1.setText(RFLab.get(1).split(",")[0]);
                                ans2.setText(RFLab.get(1).split(",")[1]);
                                ans3.setText(RFLab.get(1).split(",")[2]);
                                ans4.setText(RFLab.get(1).split(",")[3]);
                                rightAnswer=RFLab.get(1).split(",")[4];
                                currentQuestion = "RF";
                                for(int i =0;i<clues.size();i++){
                                    if(clues.get(i).contains("RF")){
                                        int clueIndex = i;
                                        clues.remove(i);
                                        macAddress.remove(i);
                                    }
                                }
                                RFFinished++;
                                C3finished++;
                            } else {
                                if(nearByBeacons.get(0).equals("20:91:48:42:84:00")){
                                    submit.setText("Submit");
                                    question.setText("Question 1 : "+C3331.get(0));
                                    ans1.setText(C3331.get(1).split(",")[0]);
                                    ans2.setText(C3331.get(1).split(",")[1]);
                                    ans3.setText(C3331.get(1).split(",")[2]);
                                    ans4.setText(C3331.get(1).split(",")[3]);
                                    rightAnswer=C3331.get(1).split(",")[4];
                                    currentQuestion = "C3331";
                                    for(int i =0;i<clues.size();i++){
                                        if(clues.get(i).contains("C3331")){
                                            int clueIndex = i;
                                            clues.remove(i);
                                            macAddress.remove(i);
                                        }
                                    }
                                    C3331Finished++;
                                } else {
                                    if(nearByBeacons.get(0).equals("20:91:48:4C:5B:16")){
                                        submit.setText("Submit");
                                        question.setText("Question 1 : "+C3332.get(0));
                                        ans1.setText(C3332.get(1).split(",")[0]);
                                        ans2.setText(C3332.get(1).split(",")[1]);
                                        ans3.setText(C3332.get(1).split(",")[2]);
                                        ans4.setText(C3332.get(1).split(",")[3]);
                                        rightAnswer=C3332.get(1).split(",")[4];
                                        currentQuestion = "C3332";
                                        for(int i =0;i<clues.size();i++){
                                            if(clues.get(i).contains("C3332")){
                                                int clueIndex = i;
                                                clues.remove(i);
                                                macAddress.remove(i);
                                            }
                                        }
                                        C3332Finished++;
                                    }
                                }
                            }
                        }
                    }

                    final Animation in = new AlphaAnimation(0.0f, 1.0f);
                    in.setDuration(1000);

                    if(isEmptyyy){
                        isEmptyyy =false;
                        question.setVisibility(View.INVISIBLE);
                        ans1.setVisibility(View.INVISIBLE);
                        ans2.setVisibility(View.INVISIBLE);
                        ans3.setVisibility(View.INVISIBLE);
                        ans4.setVisibility(View.INVISIBLE);
                        submit.setVisibility(View.VISIBLE);
                        submit.startAnimation(in);
                    } else {

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
                    }


                }
                discoverFinished=false;
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
                    if(currentQuestion.equals("EMC")){
                        if(EMCfinished < EMCquestions){
                            if(EMCfinished !=0){
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
                                rightAnswer= EMC.get(3).split(",")[4];
                                EMCfinished++;
                            } else {
                                out.setDuration(1000);
                                question.startAnimation(in);
                                ans1.startAnimation(in);
                                ans2.startAnimation(in);
                                ans3.startAnimation(in);
                                ans4.startAnimation(in);
                                question.setText("Question : " + EMC.get(0));
                                ans1.setText(EMC.get(1).split(",")[0]);
                                ans2.setText(EMC.get(1).split(",")[1]);
                                ans3.setText(EMC.get(1).split(",")[2]);
                                ans4.setText(EMC.get(1).split(",")[3]);
                                rightAnswer= EMC.get(1).split(",")[4];
                                EMCfinished++;
                            }
                        } else {
                            if(finishedNodes==3){
                                question.setText("Congratualtions you won");
                                ans1.setVisibility(View.INVISIBLE);
                                ans2.setVisibility(View.INVISIBLE);
                                ans3.setVisibility(View.INVISIBLE);
                                ans4.setVisibility(View.INVISIBLE);

                            } else {
                                Log.d("finished",finishedNodes+"");
                                Random randomGenerator = new Random();
                                int randNumber =randomGenerator.nextInt(clues.size());
                                question.setText(clues.get(randNumber));
                                ans1.setVisibility(View.INVISIBLE);
                                ans2.setVisibility(View.INVISIBLE);
                                ans3.setVisibility(View.INVISIBLE);
                                ans4.setVisibility(View.INVISIBLE);
                                nextHop = macAddress.get(randNumber);
                                currentClue = clues.get(randNumber);
                                clues.remove(randNumber);
                                macAddress.remove(randNumber);
                                destination.setVisibility(View.VISIBLE);
                                finishedNodes++;
                            }


                        }

                    } else {
                        if(currentQuestion.equals("C3332")){
                            if(C3332Finished==1){
                                C3332.remove(0);
                                C3332.remove(0);
                                out.setDuration(1000);
                                question.startAnimation(in);
                                ans1.startAnimation(in);
                                ans2.startAnimation(in);
                                ans3.startAnimation(in);
                                ans4.startAnimation(in);
                                question.setText("Question : " + C3332.get(0));
                                ans1.setText(C3332.get(1).split(",")[0]);
                                ans2.setText(C3332.get(1).split(",")[1]);
                                ans3.setText(C3332.get(1).split(",")[2]);
                                ans4.setText(C3332.get(1).split(",")[3]);
                                rightAnswer= C3332.get(1).split(",")[4];
                                C3332Finished++;

                            } else {

                                out.setDuration(1000);
                                question.startAnimation(in);
                                ans1.startAnimation(in);
                                ans2.startAnimation(in);
                                ans3.startAnimation(in);
                                ans4.startAnimation(in);
                                question.setText("Question : " + C3332.get(0));
                                ans1.setText(C3332.get(1).split(",")[0]);
                                ans2.setText(C3332.get(1).split(",")[1]);
                                ans3.setText(C3332.get(1).split(",")[2]);
                                ans4.setText(C3332.get(1).split(",")[3]);
                                rightAnswer= C3332.get(1).split(",")[4];
                                C3332Finished++;
                                for(int i=0;i<C3332.size();i++){
                                    Log.d("ana fl c3",C3332.get(i));
                                }
                                C3332.remove(0);
                                C3332.remove(0);
                            }
                            if(C3332.size()!=0){


                            } else {
                                if(finishedNodes==3){
                                    question.setText("Congratualtions you won");
                                    ans1.setVisibility(View.INVISIBLE);
                                    ans2.setVisibility(View.INVISIBLE);
                                    ans3.setVisibility(View.INVISIBLE);
                                    ans4.setVisibility(View.INVISIBLE);
                                } else {
                                    Log.d("finished",finishedNodes+"");
                                    Random randomGenerator = new Random();
                                    int randNumber =randomGenerator.nextInt(clues.size());
                                    question.setText(clues.get(randNumber));
                                    ans1.setVisibility(View.INVISIBLE);
                                    ans2.setVisibility(View.INVISIBLE);
                                    ans3.setVisibility(View.INVISIBLE);
                                    ans4.setVisibility(View.INVISIBLE);
                                    nextHop = macAddress.get(randNumber);
                                    destination.setVisibility(View.VISIBLE);
                                    currentClue = clues.get(randNumber);
                                    clues.remove(randNumber);
                                    macAddress.remove(randNumber);
                                    finishedNodes++;

                                }

                            }

                        } else {
                            if(currentQuestion.equals("RF")){
                                if(RFFinished==1){
                                    RFLab.remove(0);
                                    RFLab.remove(0);
                                    out.setDuration(1000);
                                    question.startAnimation(in);
                                    ans1.startAnimation(in);
                                    ans2.startAnimation(in);
                                    ans3.startAnimation(in);
                                    ans4.startAnimation(in);
                                    question.setText("Question : " + RFLab.get(0));
                                    ans1.setText(RFLab.get(1).split(",")[0]);
                                    ans2.setText(RFLab.get(1).split(",")[1]);
                                    ans3.setText(RFLab.get(1).split(",")[2]);
                                    ans4.setText(RFLab.get(1).split(",")[3]);
                                    rightAnswer= RFLab.get(1).split(",")[4];
                                    RFFinished++;
                                    RFLab.remove(0);
                                    RFLab.remove(0);
                                } else {
                                    //kan t7t
                                    if(RFLab.size()!=0){
                                        out.setDuration(1000);
                                        question.startAnimation(in);
                                        ans1.startAnimation(in);
                                        ans2.startAnimation(in);
                                        ans3.startAnimation(in);
                                        ans4.startAnimation(in);
                                        question.setText("Question : " + RFLab.get(0));
                                        ans1.setText(RFLab.get(1).split(",")[0]);
                                        ans2.setText(RFLab.get(1).split(",")[1]);
                                        ans3.setText(RFLab.get(1).split(",")[2]);
                                        ans4.setText(RFLab.get(1).split(",")[3]);
                                        rightAnswer= RFLab.get(1).split(",")[4];
                                        RFFinished++;
                                        RFLab.remove(0);
                                        RFLab.remove(0);
                                    } else {
                                        if(finishedNodes==3){
                                            question.setText("Congratualtions you won");
                                            ans1.setVisibility(View.INVISIBLE);
                                            ans2.setVisibility(View.INVISIBLE);
                                            ans3.setVisibility(View.INVISIBLE);
                                            ans4.setVisibility(View.INVISIBLE);
                                        } else {
                                            Log.d("finished",finishedNodes+"");
                                            Random randomGenerator = new Random();
                                            int randNumber =randomGenerator.nextInt(clues.size());
                                            question.setText(clues.get(randNumber));
                                            nextHop = macAddress.get(randNumber);
                                            destination.setVisibility(View.VISIBLE);
                                            ans1.setVisibility(View.INVISIBLE);
                                            ans2.setVisibility(View.INVISIBLE);
                                            ans3.setVisibility(View.INVISIBLE);
                                            ans4.setVisibility(View.INVISIBLE);
                                            currentClue = clues.get(randNumber);
                                            clues.remove(randNumber);
                                            macAddress.remove(randNumber);
                                            finishedNodes++;
                                        }

                                    }


                                }

                                if(C3101.size()!=0){

                                } else {


                                }

                            } else {
                                if(currentQuestion.equals("C3331")){
                                    if(C3331Finished==1){
                                        C3331.remove(0);
                                        C3331.remove(0);
                                        out.setDuration(1000);
                                        question.startAnimation(in);
                                        ans1.startAnimation(in);
                                        ans2.startAnimation(in);
                                        ans3.startAnimation(in);
                                        ans4.startAnimation(in);
                                        question.setText("Question : " + C3331.get(0));
                                        ans1.setText(C3331.get(1).split(",")[0]);
                                        ans2.setText(C3331.get(1).split(",")[1]);
                                        ans3.setText(C3331.get(1).split(",")[2]);
                                        ans4.setText(C3331.get(1).split(",")[3]);
                                        rightAnswer= C3331.get(1).split(",")[4];
                                        C3331Finished++;


                                    } else {
                                        out.setDuration(1000);
                                        question.startAnimation(in);
                                        ans1.startAnimation(in);
                                        ans2.startAnimation(in);
                                        ans3.startAnimation(in);
                                        ans4.startAnimation(in);
                                        question.setText("Question : " + C3331.get(0));
                                        ans1.setText(C3331.get(1).split(",")[0]);
                                        ans2.setText(C3331.get(1).split(",")[1]);
                                        ans3.setText(C3331.get(1).split(",")[2]);
                                        ans4.setText(C3331.get(1).split(",")[3]);
                                        rightAnswer= C3331.get(1).split(",")[4];
                                        C3331.remove(0);
                                        C3331.remove(0);
                                        C3331Finished++;


                                    }

                                    if(C3331.size()!=0){

                                    } else {
                                        if(finishedNodes==3){
                                            question.setText("Congratualtions you won");
                                            ans1.setVisibility(View.INVISIBLE);
                                            ans2.setVisibility(View.INVISIBLE);
                                            ans3.setVisibility(View.INVISIBLE);
                                            ans4.setVisibility(View.INVISIBLE);
                                        } else {
                                            Log.d("finished nodes",finishedNodes+"");
                                            Random randomGenerator = new Random();
                                            int randNumber =randomGenerator.nextInt(clues.size());
                                            question.setText(clues.get(randNumber));
                                            nextHop = macAddress.get(randNumber);
                                            destination.setVisibility(View.VISIBLE);
                                            ans1.setVisibility(View.INVISIBLE);
                                            ans2.setVisibility(View.INVISIBLE);
                                            ans3.setVisibility(View.INVISIBLE);
                                            ans4.setVisibility(View.INVISIBLE);
                                            currentClue = clues.get(randNumber);
                                            clues.remove(randNumber);
                                            macAddress.remove(randNumber);
                                            finishedNodes++;
                                        }



                                    }

                                }
                            }
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(),"Wrong Answer, Try Again",Toast.LENGTH_SHORT).show();
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
                int c3332flag=0;
                int RFflag=0;
                int c3331flag=0;
                for(int k =0;k<databaseArray2.size();k++){
                    if(databaseArray2.get(k).equals("C3-101")){ // di betmla fl C3331 msh C3-101
                        c3331flag=1;
                    } else {
                        if(databaseArray2.get(k).equals("EMC")){
                            emcflag=1;
                        } else {
                            if(databaseArray2.get(k).equals("C3-332")){
                                c3332flag=1;
                            } else {
                                if(databaseArray2.get(k).equals("C3-103")){   // di kanet 331
                                    c3331flag=1;
                                } else {
                                    if(databaseArray2.get(k).equals("RF Lab")){
                                        RFflag =1;
                                    }
                                }
                            }
                        }
                    }
                    if(emcflag==1){
                        emcflag=0;
                        for(int m=k+1;m<databaseArray2.size();m++){
                            if(databaseArray2.get(m).contains(",") || databaseArray2.get(m).contains("?")){
                                EMC.add(databaseArray2.get(m));
                            } else {
                                if(databaseArray2.get(m).contains("!")){
                                    EMClue=databaseArray2.get(m);

                                } else {
                                    break;
                                }

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
                                    if(databaseArray2.get(n).contains("!")){
                                        C3101Clue = databaseArray2.get(n);

                                    } else {
                                        break;
                                    }

                                }
                            }
                            C3questions=C3101.size()/2;
                            Log.d("C3",C3101.size()/2+"");
                        } else {
                            if(c3331flag == 1){
                                c3331flag=0;
                                Log.d("ana gowa c3331","gowa");
                                for(int g=k+1;g<databaseArray2.size();g++){
                                    if(databaseArray2.get(g).contains(",")||databaseArray2.get(g).contains("?")){
                                        C3331.add(databaseArray2.get(g));
                                    } else {
                                        if(databaseArray2.get(g).contains("!")){
                                            Log.d("hwa fe eh ","ana fl else");
                                            C3331Clue = databaseArray2.get(g);

                                        } else {
                                            break;
                                        }
                                    }
                                }
                                C3331Questions = C3331.size()/2;
                            } else {
                                if(c3332flag == 1 ){
                                    c3332flag=0;
                                    for(int f=k+1;f<databaseArray2.size();f++){
                                        if(databaseArray2.get(f).contains(",")|| databaseArray2.get(f).contains("?")){
                                            C3332.add(databaseArray2.get(f));
                                        } else {
                                            if(databaseArray2.get(f).contains("!")){
                                                C3332Clue = databaseArray2.get(f);

                                            } else {
                                                break;
                                            }
                                        }
                                    }
                                    C3332Questions = C3332.size()/2;
                                } else {
                                    if(RFflag==1){
                                        RFflag=0;
                                        for(int d=k+1;d<databaseArray2.size();d++){
                                            if(databaseArray2.get(d).contains("?") || databaseArray2.get(d).contains(",")){
                                                RFLab.add(databaseArray2.get(d));
                                            } else {
                                                if(databaseArray2.get(d).contains("!")){
                                                    RFClue=databaseArray2.get(d);

                                                } else {
                                                    break;
                                                }
                                            }
                                        }
                                        RFQuestions = databaseArray2.size()/2;
                                    }
                                }
                            }
                        }
                    }
                    for(int c=0;c<EMC.size();c++){
                        Log.d("EMC",EMC.get(c));
                    }
                    for(int c=0;c<C3101.size();c++){
                        Log.d("C3",C3101.get(c));
                    }
                }
                for(int c=0;c<RFLab.size();c++){
                    Log.d("RF",RFLab.get(c));
                }
                for(int c=0;c<C3332.size();c++){
                    Log.d("C3332",C3332.get(c));
                }
                boolean isEmptyy =false;
                if(nearByBeacons == null || nearByBeacons.size()==0){
                    isEmptyy=true;
                    question.setVisibility(View.INVISIBLE);
                    ans1.setVisibility(View.INVISIBLE);
                    ans2.setVisibility(View.INVISIBLE);
                    ans3.setVisibility(View.INVISIBLE);
                    ans4.setVisibility(View.INVISIBLE);
                    submit.setText("Re-scan");

                    Toast.makeText(getApplicationContext(),"No Beacons Found, Try Again",Toast.LENGTH_SHORT).show();

                } else {


                    if(nearByBeacons.get(0).equals("20:91:48:4C:5B:E6")){
                        submit.setText("Submit");
                        question.setText("Question 1 : "+EMC.get(0));
                        ans1.setText(EMC.get(1).split(",")[0]);
                        ans2.setText(EMC.get(1).split(",")[1]);
                        ans3.setText(EMC.get(1).split(",")[2]);
                        ans4.setText(EMC.get(1).split(",")[3]);
                        rightAnswer=EMC.get(1).split(",")[4];
                        for(int i =0;i<clues.size();i++){
                            if(clues.get(i).contains("EMC")){
                                int clueIndex = i;
                                clues.remove(i);
                                macAddress.remove(i);
                            }
                        }
                        currentQuestion = "EMC";
                        EMCfinished++;
                    } else {
                        if(nearByBeacons.get(0).equals("20:91:48:42:86:D5")){
                            submit.setText("Submit");
                            question.setText("Question 1 : "+RFLab.get(0));
                            ans1.setText(RFLab.get(1).split(",")[0]);
                            ans2.setText(RFLab.get(1).split(",")[1]);
                            ans3.setText(RFLab.get(1).split(",")[2]);
                            ans4.setText(RFLab.get(1).split(",")[3]);
                            rightAnswer=RFLab.get(1).split(",")[4];
                            currentQuestion = "RF";
                            for(int i =0;i<clues.size();i++){
                                if(clues.get(i).contains("RF")){
                                    int clueIndex = i;
                                    clues.remove(i);
                                    macAddress.remove(i);
                                }
                            }
                            RFFinished++;
                            C3finished++;
                        } else {
                            if(nearByBeacons.get(0).equals("20:91:48:42:84:00")){
                                submit.setText("Submit");
                                question.setText("Question 1 : "+C3331.get(0));
                                ans1.setText(C3331.get(1).split(",")[0]);
                                ans2.setText(C3331.get(1).split(",")[1]);
                                ans3.setText(C3331.get(1).split(",")[2]);
                                ans4.setText(C3331.get(1).split(",")[3]);
                                rightAnswer=C3331.get(1).split(",")[4];
                                currentQuestion = "C3331";
                                for(int i =0;i<clues.size();i++){
                                    if(clues.get(i).contains("C3331")){
                                        int clueIndex = i;
                                        clues.remove(i);
                                        macAddress.remove(i);
                                    }
                                }
                                C3331Finished++;
                            } else {
                                if(nearByBeacons.get(0).equals("20:91:48:4C:5B:16")){
                                    submit.setText("Submit");
                                    question.setText("Question 1 : "+C3332.get(0));
                                    ans1.setText(C3332.get(1).split(",")[0]);
                                    ans2.setText(C3332.get(1).split(",")[1]);
                                    ans3.setText(C3332.get(1).split(",")[2]);
                                    ans4.setText(C3332.get(1).split(",")[3]);
                                    rightAnswer=C3332.get(1).split(",")[4];
                                    currentQuestion = "C3332";
                                    for(int i =0;i<clues.size();i++){
                                        if(clues.get(i).contains("C3332")){
                                            int clueIndex = i;
                                            clues.remove(i);
                                            macAddress.remove(i);
                                        }
                                    }
                                    C3332Finished++;
                                }
                            }
                        }
                    }

                }


                final Animation in = new AlphaAnimation(0.0f, 1.0f);
                in.setDuration(1000);

                if(isEmptyy){
                    isEmptyy =false;
                    question.setVisibility(View.INVISIBLE);
                    ans1.setVisibility(View.INVISIBLE);
                    ans2.setVisibility(View.INVISIBLE);
                    ans3.setVisibility(View.INVISIBLE);
                    ans4.setVisibility(View.INVISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    submit.startAnimation(in);
                } else {

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
                }

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

            //nearByBeacons.add(list.get(i).getMacAddress().toStandardString());
            nearByBeacons = filterBeacons(list);
            if(nearByBeacons.size()==0 || nearByBeacons==null){
                Log.d("ana gowa el finished","if "+searchedTimes );
                if(searchedTimes==3){
                    beaconManager.disconnect();
                    discoverFinished=true;
                    break;
                }
            } else {
                discoverFinished=true;
                Log.d("ana gowa el finished","else");
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
    public ArrayList<String> filterBeacons(List<Beacon> beacons){
        ArrayList<String> filteredBeacons = new ArrayList<String>();
        for(int i =0;i<beacons.size();i++){
            if(beacons.get(i).getMacAddress().toStandardString().equals("20:91:48:4C:5B:E6") ||
                    beacons.get(i).getMacAddress().toStandardString().equals("20:91:48:42:84:00") ||
                    beacons.get(i).getMacAddress().toStandardString().equals("20:91:48:4C:5B:16") ||
                    beacons.get(i).getMacAddress().toStandardString().equals("20:91:48:42:86:D5")||
                    beacons.get(i).getMacAddress().toStandardString().equals("20:91:48:4C:5B:10")){
                Log.d("ana b3ml filter","filter");
                filteredBeacons.add(beacons.get(i).getMacAddress().toStandardString());

            }
        }
        return filteredBeacons;
    }
    public class ScanThread extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
            beaconManager.setRangingListener(new BeaconManager.RangingListener() {
                @Override
                public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                    Log.d("wasalt","hena");
                    searchedTimes++;
                    for (int i = 0; i < list.size(); i++) {
                        Log.i("beaconsMAC", "" + list.get(i).getMacAddress().toStandardString());
                        Log.i("beaconsRSSI", "" + list.get(i).getRssi());

                        //nearByBeacons.add(list.get(i).getMacAddress().toStandardString());
                        nearByBeacons = filterBeacons(list);
                        if(nearByBeacons.size()==0 || nearByBeacons==null){
                            Log.d("ana gowa el finished","if");
                            if(searchedTimes==3){
                                beaconManager.disconnect();
                                discoverFinished=true;
                                break;
                            }
                        } else {
                            discoverFinished=true;
                            Log.d("ana gowa el finished","else");
                            beaconManager.disconnect();
                        }
                    }
                }
            });

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            final Animation in = new AlphaAnimation(0.0f, 1.0f);
            in.setDuration(1000);

            final Animation out = new AlphaAnimation(1.0f, 0.0f);
            if(nearByBeacons.get(0).equals(nextHop)){
                Toast.makeText(getApplicationContext(),"You are in the right destination",Toast.LENGTH_SHORT).show();
                if(finishedNodes==4){

                    question.setText("You have finished");

                } else {

                    // the next hop decision
                    if(currentClue.contains("EMC")){
                        currentQuestion = "EMC";
                        out.setDuration(1000);
                        question.startAnimation(in);
                        ans1.startAnimation(in);
                        ans2.startAnimation(in);
                        ans3.startAnimation(in);
                        ans4.startAnimation(in);
                        question.setText("Question : " + EMC.get(0));
                        ans1.setText(EMC.get(1).split(",")[0]);
                        ans2.setText(EMC.get(1).split(",")[1]);
                        ans3.setText(EMC.get(1).split(",")[2]);
                        ans4.setText(EMC.get(1).split(",")[3]);
                        rightAnswer= EMC.get(1).split(",")[4];
                        EMCfinished++;

                    } else {
                        if(currentClue.contains("C3331")){
                            currentQuestion = "C3331";
                            out.setDuration(1000);
                            question.startAnimation(in);
                            ans1.startAnimation(in);
                            ans2.startAnimation(in);
                            ans3.startAnimation(in);
                            ans4.startAnimation(in);
                            question.setText("Question : " + C3331.get(0));
                            ans1.setText(C3331.get(1).split(",")[0]);
                            ans2.setText(C3331.get(1).split(",")[1]);
                            ans3.setText(C3331.get(1).split(",")[2]);
                            ans4.setText(C3331.get(1).split(",")[3]);
                            rightAnswer= C3331.get(1).split(",")[4];
                            C3331Finished++;


                        } else {
                            if(currentClue.contains("C3332")){
                                currentQuestion = "C3332";
                                out.setDuration(1000);
                                question.startAnimation(in);
                                ans1.startAnimation(in);
                                ans2.startAnimation(in);
                                ans3.startAnimation(in);
                                ans4.startAnimation(in);
                                question.setText("Question : " + C3332.get(0));
                                ans1.setText(C3332.get(1).split(",")[0]);
                                ans2.setText(C3332.get(1).split(",")[1]);
                                ans3.setText(C3332.get(1).split(",")[2]);
                                ans4.setText(C3332.get(1).split(",")[3]);
                                rightAnswer= C3332.get(1).split(",")[4];
                                C3332Finished++;

                            } else {
                                if(currentClue.contains("RF")){
                                    currentQuestion = "RF";
                                    out.setDuration(1000);
                                    question.startAnimation(in);
                                    ans1.startAnimation(in);
                                    ans2.startAnimation(in);
                                    ans3.startAnimation(in);
                                    ans4.startAnimation(in);
                                    question.setText("Question : " + RFLab.get(0));
                                    ans1.setText(RFLab.get(1).split(",")[0]);
                                    ans2.setText(RFLab.get(1).split(",")[1]);
                                    ans3.setText(RFLab.get(1).split(",")[2]);
                                    ans4.setText(RFLab.get(1).split(",")[3]);
                                    rightAnswer= RFLab.get(1).split(",")[4];
                                    RFFinished++;
                                    C3finished++;

                                }
                            }
                        }
                    }

                }
                destination.setVisibility(View.INVISIBLE);
                ans1.setVisibility(View.VISIBLE);
                ans2.setVisibility(View.VISIBLE);
                ans3.setVisibility(View.VISIBLE);
                ans4.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getApplicationContext(),"Not the right destination, Try again",Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            int i =0;
            while (true){
                if(i==0){
                    Log.d("ana lsa m5rgtesh","lsa");
                    i++;
                }

                if(discoverFinished){
                    Log.d("ana 5aragt","5alas");
                    break;
                }
            }

            return null;
        }
    }
}

