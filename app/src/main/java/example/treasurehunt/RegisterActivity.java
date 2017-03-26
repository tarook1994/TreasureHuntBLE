package example.treasurehunt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText name,email,password,confirm;
    Button reg;
    TextView sign;
    String userName,mail,pass,confpass;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirm = (EditText) findViewById(R.id.confirm);
        reg = (Button) findViewById(R.id.Register);
        sign = (TextView) findViewById(R.id.Signin);
        mAuth = FirebaseAuth.getInstance();
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this,Login2Activity.class);
                startActivity(i);
            }
        });

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
        name.startAnimation(in);
        confirm.startAnimation(in);
        email.startAnimation(in);
        reg.startAnimation(in);
        sign.startAnimation(in);
        password.setAnimation(in);
        email.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        confirm.setVisibility(View.VISIBLE);
        reg.setVisibility(View.VISIBLE);
        sign.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);




        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please Wait...");
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f1a96f")));
                progressDialog.show();
                userName = name.getText().toString();
                mail  = email.getText().toString();
                pass = password.getText().toString();
                confpass = confirm.getText().toString();
                if(emailValid(mail)){
                    if(pass.equals(confpass)){
                        Login2Activity.db.add(mail,userName);
                        mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    // Intent to the game
                                    Intent i = new Intent(RegisterActivity.this,Login2Activity.class);
                                    startActivity(i);
                                    finish();
                                    Toast.makeText(getApplicationContext(),"Reg Ok",Toast.LENGTH_LONG).show();

                                }else {
                                    Toast.makeText(getApplicationContext(),"Login Failed , Try Again Later ",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(),"Passwords do not match",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
    public boolean emailValid(String s){
        if(s.contains("@")){

            return true;
        }
        return false;
    }

}
