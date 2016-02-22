package id.technomotion.keyboardheight;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    View viewParent;
    int previousHeightDiffrence = 0;
    int constantaDiff=0;
    private View popUpView;
    PopupWindow popupWindow;
    private int keyboardHeight;
    private LinearLayout emoticonsCover;
    private ToggleButton toggleButton;
    private boolean isKeyBoardVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupView();
        setupViewListener();
        setupKeyboard();
        checkKeyboardHeight();
    }



    private void setupKeyboard() {
        // Defining default height of keyboard which is equal to 230 dip
        final float popUpheight = getResources().getDimension(
                R.dimen.keyboard_height);
        changeKeyboardHeight((int) popUpheight);
    }

    private void checkKeyboardHeight() {
        viewParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                viewParent.getWindowVisibleDisplayFrame(r);

                int screenHeight = viewParent.getRootView()
                        .getHeight();
                int heightDifference = screenHeight - (r.bottom);

                if (previousHeightDiffrence - heightDifference > 50) {
                    popupWindow.dismiss();
                }

                previousHeightDiffrence = heightDifference;

                if (heightDifference > 100) {
                    isKeyBoardVisible = true;
                    changeKeyboardHeight(heightDifference - constantaDiff);
                } else {
                    constantaDiff = screenHeight - (r.bottom);
                    isKeyBoardVisible = false;
                }
            }
        });
    }


    private void setupViewListener() {
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){//if checked it mean the text is "emoticon". Should show querty keyboard
                    if(popupWindow.isShowing()){
                        popupWindow.dismiss();
                    }
                }else{
                    if (!popupWindow.isShowing()) {

                        popupWindow.setHeight((int) (keyboardHeight));
                        if (isKeyBoardVisible) {
                            emoticonsCover.setVisibility(LinearLayout.GONE);
                        } else {
                            emoticonsCover.setVisibility(LinearLayout.VISIBLE);
                        }
                        popupWindow.showAtLocation(viewParent, Gravity.BOTTOM, 0, 0);
                    } else {
                        popupWindow.dismiss();
                    }
                }
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                emoticonsCover.setVisibility(LinearLayout.GONE);
            }
        });
    }

    private void setupView() {
        viewParent=findViewById(R.id.parent);
        emoticonsCover = (LinearLayout) findViewById(R.id.footer_for_emoticons);
        toggleButton= (ToggleButton) findViewById(R.id.toggle_button);
        popUpView = getLayoutInflater().inflate(R.layout.emoticon_popup, null);
        toggleButton.setChecked(true);

        // enable popupView
        // Creating a pop window for emoticons keyboard
        popupWindow = new PopupWindow(popUpView, LayoutParams.MATCH_PARENT,
                (int) keyboardHeight, false);
    }

    /**
     * change height of emoticons keyboard according to height of actual
     * keyboard
     *
     * @param height
     *            minimum height by which we can make sure actual keyboard is
     *            open or not
     */
    private void changeKeyboardHeight(int height) {
        if (height > 100) {
            keyboardHeight = height;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, keyboardHeight);
            emoticonsCover.setLayoutParams(params);
        }
    }
}
