package com.asiainfo.scratchcard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.asiainfo.scratchcard.R;
import com.asiainfo.scratchcard.view.ScratchCardView;

public class ScratchCardActivity extends Activity {

    private ScratchCardView mScratchCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch_card);

        mScratchCard = (ScratchCardView) findViewById(R.id.mian_ScratchCard);
        mScratchCard.setOnScratchCardCompleteListener(new ScratchCardView.OnScratchCardCompleteListener() {
            @Override
            public void compelete() {

                Toast.makeText(ScratchCardActivity.this, "2017年,小木箱祝大家:", Toast.LENGTH_LONG).show();

            }
        });

        mScratchCard.setText("身体健康,幸福平安");

    }

}
