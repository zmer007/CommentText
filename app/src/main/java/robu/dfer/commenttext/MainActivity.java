package robu.dfer.commenttext;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    InputMethodManager imm;
    FragmentManager fm;
    private View mInputPanel;
    private EditText mInputEditText;
    private Button mSendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputPanel = findViewById(R.id.input_panel_container);
        mInputEditText = (EditText) findViewById(R.id.input_panel_editText);
        mSendBtn = (Button) findViewById(R.id.input_panel_sendBtn);

        fm= getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.flower_fragmentContainer);
        if(fragment == null){
            fragment = new FlowerFragment();
            fm.beginTransaction().add(R.id.flower_fragmentContainer, fragment).commit();
        }
    }

    private void showSoftKeyboard(){
        mInputPanel.setVisibility(View.VISIBLE);
    }

    private void hideSoftKeyboard(){
        mInputPanel.setVisibility(View.GONE);
    }

    public void flowerFragmentRecyclerViewSmoothScrollBy(int dy){
        Fragment fragment = fm.findFragmentById(R.id.flower_fragmentContainer);
        if(fragment!=null && fragment instanceof FlowerFragment){
            FlowerFragment ff = (FlowerFragment) fragment;
            ff.smoothScrollBy(dy);
        }
    }
}
