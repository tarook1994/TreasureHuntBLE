package example.treasurehunt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {
    ArrayList<String> databaseArray,EMC,C3101;
    TextView q,a;
    Button b;
    int EMCquestions,C3questions,EMCfinished,C3finished;
    String currentQuestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        databaseArray = new ArrayList<String>();
        a = (TextView) findViewById(R.id.a);
        q = (TextView) findViewById(R.id.q);
        b = (Button) findViewById(R.id.button);
        EMC = new ArrayList<String>();
        C3101 = new ArrayList<String>();
        EMCquestions=0;
        C3questions=0;
        EMCfinished=0;
        C3finished=0;
         currentQuestion ="";
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index=0;
                if(EMCfinished==1){
                    EMCfinished++;
                    q.setText(EMC.get(2));
                    a.setText(EMC.get(3));
                } else {
                    if(currentQuestion.equals("EMC")){
                        if(EMCfinished<EMCquestions){
                            index= EMCfinished+1;
                            q.setText(EMC.get(index));
                            index++;
                            a.setText(EMC.get(index));
                        } else {
                            currentQuestion="C3";
                            if(C3finished==0){
                              q.setText(C3101.get(0));
                                a.setText(C3101.get(1));
                                C3finished++;
                            } else {
                                 index = C3finished+1;
                                q.setText(C3101.get(index));
                                index++;
                                a.setText(C3101.get(index));
                                C3finished++;
                            }
                        }
                    } else {
                        if(currentQuestion.equals("C3")){
                            if(C3finished<C3questions){
                                index = C3finished+1;
                                q.setText(C3101.get(index));
                                index++;
                                a.setText(C3101.get(index));
                                C3finished++;

                            }
                        }

                    }
                }
            }
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                    int counter=1;

                    databaseArray.add(friendSnapshot.getKey().toString());

                    for(int i =0 ;i<friendSnapshot.getChildrenCount()/2;i++){

                        databaseArray.add(friendSnapshot.child("Question"+counter).getValue()+"");
                        databaseArray.add(friendSnapshot.child("Answer"+counter).getValue()+"");
                        counter++;
                    }

                }
                for(int j=0;j<databaseArray.size();j++){

                    Log.d("arrayitemtest",databaseArray.get(j));
                }
                int emcflag=0;
                int c3flag=0;
                for(int k =0;k<databaseArray.size();k++){
                    if(databaseArray.get(k).equals("C3-101")){
                        c3flag=1;
                    } else {
                        if(databaseArray.get(k).equals("EMC")){
                            emcflag=1;
                        }
                    }
                    if(emcflag==1){
                        emcflag=0;
                        for(int m=k+1;m<databaseArray.size();m++){
                            if(databaseArray.get(m).contains(",") || databaseArray.get(m).contains("?")){
                                EMC.add(databaseArray.get(m));
                            } else {
                                EMCquestions=m-1;
                                break;
                            }
                        }

                    } else {
                        if(c3flag==1){
                            c3flag=0;
                            for(int n=k+1;n<databaseArray.size();n++){
                                if(databaseArray.get(n).contains(",") || databaseArray.get(n).contains("?")){
                                    C3101.add(databaseArray.get(n));
                                } else {
                                    C3questions=n-1;
                                    break;
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
                q.setText(EMC.get(0));
                a.setText(EMC.get(1));
                currentQuestion="EMC";
                EMCfinished++;


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
