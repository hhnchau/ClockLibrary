package chauhuynh.info.clock_library;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import chauhuynh.info.clock_library.clock.CallbackClock;
import chauhuynh.info.clock_library.clock.Clock;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int[] overnight = new int[2];
                overnight[0] = 21;
                overnight[1] = 8;

                int[] cinejoy = new int[0];

                int[] position = new int[2];
                position[0] = 9 * 2;
                position[1] = 10 * 2;

                Clock.getInstance().show(MainActivity.this, overnight, cinejoy, position, true, 2, new CallbackClock() {
                    @Override
                    public void onValue(String from, String to) {
                        TextView textView = findViewById(R.id.text);
                        textView.setText(from + " ---- " + to);
                    }
                });


            }
        });
    }
}
