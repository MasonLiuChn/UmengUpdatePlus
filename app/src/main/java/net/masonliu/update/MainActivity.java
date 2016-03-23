package net.masonliu.update;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.umeng.update.AppUpgrader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppUpgrader.start(getApplication(), "56f237ece0f55a8f7b000a14");
    }
}
