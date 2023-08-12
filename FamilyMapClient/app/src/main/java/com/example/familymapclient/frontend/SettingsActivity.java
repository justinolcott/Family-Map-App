package com.example.familymapclient.frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.familymapclient.R;
import com.example.familymapclient.backend.DataCache;

public class SettingsActivity extends AppCompatActivity {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        DataCache dc = DataCache.getInstance();

        Switch showLifeStoryLines  = findViewById(R.id.switch_life_story);
        Switch showFamilyTreeLines = findViewById(R.id.switch_family_tree_lines);
        Switch showSpouseLines     = findViewById(R.id.switch_spouse);
        Switch showFatherSide      = findViewById(R.id.switch_father);
        Switch showMotherSide      = findViewById(R.id.switch_mother);
        Switch showMaleEvents      = findViewById(R.id.switch_male);
        Switch showFemaleEvents    = findViewById(R.id.switch_female);

        showLifeStoryLines .setChecked(dc.isShowLifeStoryLines ());
        showFamilyTreeLines.setChecked(dc.isShowFamilyTreeLines());
        showSpouseLines    .setChecked(dc.isShowSpouseLines    ());
        showFatherSide     .setChecked(dc.isShowFatherSide     ());
        showMotherSide     .setChecked(dc.isShowMotherSide     ());
        showMaleEvents     .setChecked(dc.isShowMaleEvents     ());
        showFemaleEvents   .setChecked(dc.isShowFemaleEvents   ());

        showLifeStoryLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dc.setShowLifeStoryLines(b);
            }
        });

        showFamilyTreeLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dc.setShowFamilyTreeLines(b);
            }
        });
        showSpouseLines    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dc.setShowSpouseLines(b);
            }
        });
        showFatherSide     .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dc.setShowFatherSide(b);
            }
        });
        showMotherSide     .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dc.setShowMotherSide(b);
            }
        });
        showMaleEvents     .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dc.setShowMaleEvents(b);
            }
        });
        showFemaleEvents   .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dc.setShowFemaleEvents(b);
            }
        });

        TextView logoutView = findViewById(R.id.logout);
        logoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Settings", "Logging out");
                DataCache.getInstance().clear();

                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}