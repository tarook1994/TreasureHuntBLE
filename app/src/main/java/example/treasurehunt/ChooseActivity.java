package example.treasurehunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ChooseActivity extends AppCompatActivity {
    ImageView gps,indoor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        gps = (ImageView) findViewById(R.id.gps);
        indoor = (ImageView) findViewById(R.id.indoor);
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChooseActivity.this,MainActivity.class);
                i.putExtra("name",getIntent().getStringExtra("name"));
                startActivity(i);
            }
        });

        indoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChooseActivity.this,GpsActivity.class);
                i.putExtra("name",getIntent().getStringExtra("name"));
                startActivity(i);
            }
        });
    }
}
