package hakaton.hakaton_brench1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class result_game extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_game);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset:
                Intent intent = new Intent(result_game.this, Game.class);
                startActivity(intent);
                break;
        }
    }
}
