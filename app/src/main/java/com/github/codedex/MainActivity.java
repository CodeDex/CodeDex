package com.github.codedex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.codedex.codeview.CodeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CodeFragment codeFragment = new CodeFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, codeFragment)
                .commit();
    }
}
