package de.iu.boardgame;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.core.view.WindowInsetsCompat;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        if (controller != null) {
            controller.setAppearanceLightStatusBars(true);
        }
    }
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        applySystemBarsPadding();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        applySystemBarsPadding();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        applySystemBarsPadding();
    }

    private void applySystemBarsPadding() {
        ViewGroup content = findViewById(android.R.id.content);
        if (content == null || content.getChildCount() == 0) {
            return;
        }

        View root = content.getChildAt(0);
        if (root instanceof DrawerLayout) {
            DrawerLayout drawerLayout = (DrawerLayout) root;
            int childCount = drawerLayout.getChildCount();
            int[] initialPadding = new int[childCount * 4];
            for (int i = 0; i < childCount; i++) {
                View child = drawerLayout.getChildAt(i);
                int baseIndex = i * 4;
                initialPadding[baseIndex] = child.getPaddingLeft();
                initialPadding[baseIndex + 1] = child.getPaddingTop();
                initialPadding[baseIndex + 2] = child.getPaddingRight();
                initialPadding[baseIndex + 3] = child.getPaddingBottom();
            }

            ViewCompat.setOnApplyWindowInsetsListener(drawerLayout, (v, insets) -> {
                Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                for (int i = 0; i < childCount; i++) {
                    View child = drawerLayout.getChildAt(i);
                    int baseIndex = i * 4;
                    child.setPadding(
                            initialPadding[baseIndex] + sys.left,
                            initialPadding[baseIndex + 1] + sys.top,
                            initialPadding[baseIndex + 2] + sys.right,
                            initialPadding[baseIndex + 3] + sys.bottom
                    );
                }
                return insets;
            });
            ViewCompat.requestApplyInsets(drawerLayout);
            return;
        }

        int initialLeft = root.getPaddingLeft();
        int initialTop = root.getPaddingTop();
        int initialRight = root.getPaddingRight();
        int initialBottom = root.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    initialLeft + sys.left,
                    initialTop + sys.top,
                    initialRight + sys.right,
                    initialBottom + sys.bottom
            );
            return insets;
        });
        ViewCompat.requestApplyInsets(root);
    }
}
