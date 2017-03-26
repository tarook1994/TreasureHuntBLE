package example.treasurehunt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.AsyncListUtil;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Login2Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    EditText email,password;
    ImageView logo;
    Button signIn;
    TextView reg;
    String mail;
    String pass;
    public static DBHandler db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        db = new DBHandler(getApplicationContext());
        logo = (ImageView) findViewById(R.id.logo);
        email = (EditText) findViewById(R.id.Email);
        password = (EditText) findViewById(R.id.Password);
        signIn = (Button) findViewById(R.id.Signinn);
        reg = (TextView) findViewById(R.id.Reg);
        mAuth = FirebaseAuth.getInstance();
        new AnimationThread().execute();





        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login2Activity.this,RegisterActivity.class);
                startActivity(i);
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(Login2Activity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please Wait...");
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f1a96f")));
                progressDialog.show();

                mail = email.getText().toString();
                pass = password.getText().toString();
                mAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            // Intent to game;
//                            Toast.makeText(getApplicationContext(),"Login Okay",Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Login2Activity.this,ChooseActivity.class);
                            i.putExtra("name",checkDB(mail));
                            startActivity(i);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


                        }else {
                            Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


    }

    public boolean emailValid(String s){
        if(s.contains("@")){

            return true;
        }
        return false;
    }
    public class AnimationThread extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RotateAnimation anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatCount(Animation.ABSOLUTE);
            anim.setDuration(700);
            logo.startAnimation(anim);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            logo.setAnimation(null);
            new AnimationThreadTwo().execute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public class AnimationThreadTwo extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            final Animation in = new AlphaAnimation(0.0f, 1.0f);
            in.setDuration(1000);

            final Animation out = new AlphaAnimation(1.0f, 0.0f);
            out.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            out.setDuration(1000);
            email.startAnimation(in);
            email.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            final Animation in = new AlphaAnimation(0.0f, 1.0f);
            in.setDuration(1000);

            final Animation out = new AlphaAnimation(1.0f, 0.0f);
            out.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            out.setDuration(1000);
            password.startAnimation(in);
            password.setVisibility(View.VISIBLE);
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final Animation in = new AlphaAnimation(0.0f, 1.0f);
                            in.setDuration(1000);

                            final Animation out = new AlphaAnimation(1.0f, 0.0f);
                            out.setAnimationListener(new Animation.AnimationListener() {

                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });

                            out.setDuration(1000);
                            signIn.startAnimation(in);
                            signIn.setVisibility(View.VISIBLE);
                        }
                    });

                }
            },1000);

            Timer x = new Timer();
            x.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final Animation in = new AlphaAnimation(0.0f, 1.0f);
                            in.setDuration(1000);

                            final Animation out = new AlphaAnimation(1.0f, 0.0f);
                            out.setAnimationListener(new Animation.AnimationListener() {

                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });

                            out.setDuration(1000);
                            reg.startAnimation(in);
                            reg.setVisibility(View.VISIBLE);
                        }
                    });

                }
            },2000);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public String checkDB(String maail){
        String nameOfEmail = "";
        String[] result_columns = new String[] {
                DBHandler.COLOMN_EMAIL,
                DBHandler.COLOMN_NAME};
// Specify the where clause that will limit our results.
        String where = null;
// Replace these with valid SQL statements as necessary.
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        SQLiteDatabase handler = db.getWritableDatabase();
        Cursor cursor = handler.query(DBHandler.TABLE_NAME,
                result_columns, where, whereArgs, groupBy, having, order);
        int nameIndex = cursor.getColumnIndexOrThrow(DBHandler.COLOMN_NAME);
        int index = cursor.getColumnIndexOrThrow(DBHandler.COLOMN_EMAIL);
        while (cursor.moveToNext()) {

            Log.d("hey",cursor.getString(index) + "");
            if(cursor.getString(index).equals(maail)){
                nameOfEmail = cursor.getString(nameIndex);
                Log.d("ana gowa","la2eto");
                break;

            } else {
                nameOfEmail = "not found";
            }


        }
        return nameOfEmail;
    }

}
