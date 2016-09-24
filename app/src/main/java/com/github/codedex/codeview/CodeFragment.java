package com.github.codedex.codeview;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.github.codedex.R;
import com.github.codedex.codeview.highlight.ColorTheme;

/**
 * Show source code from source file
 */

public class CodeFragment extends Fragment {

    private static final String TEST_CODE = "public class MainActivity extends AppCompatActivity {\n" +
            "\n" +
            "    @Override\n" +
            "    protected void onCreate(Bundle savedInstanceState) {\n" +
            "        super.onCreate(savedInstanceState);\n" +
            "        setContentView(R.layout.activity_main);\n" +
            "    }\n" +
            "}";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rowView = inflater.inflate(R.layout.fragment_code_view, container, false);
        Context context = getActivity();
        CodeView codeView = (CodeView) rowView.findViewById(R.id.code_view);
        codeView.getRecyclerView().setPadding(0, 0, 0, getNavBarHeight(context));
        new Highlighter(context)
                .code(TEST_CODE)
                .language("java")
                .theme(ColorTheme.SOLARIZED_LIGHT.withBgContent(ContextCompat.getColor(context, R.color.md_grey_300)))
                .highlight(codeView);
        return rowView;
    }

    public int getNavBarHeight(Context c) {
        int result = 0;
        boolean hasMenuKey = ViewConfiguration.get(c).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            //The device has a navigation bar
            Resources resources = getContext().getResources();

            int orientation = getResources().getConfiguration().orientation;
            int resourceId;
            if (isTablet(c)) {
                resourceId = resources.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape", "dimen", "android");
            } else {
                resourceId = resources.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_width", "dimen", "android");
            }

            if (resourceId > 0) {
                return getResources().getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }


    private boolean isTablet(Context c) {
        return (c.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
