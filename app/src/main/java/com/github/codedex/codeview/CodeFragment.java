package com.github.codedex.codeview;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.github.codedex.R;
import com.github.codedex.codeview.highlight.ColorTheme;
import com.github.codedex.sourceparser.ImportParser;
import com.github.codedex.sourceparser.SourceParser;

import org.jetbrains.annotations.NotNull;

/**
 * Show source code from source file
 */

public class CodeFragment extends Fragment {

    private static final String TEST_CODE = "package com.github.codedex;\n" +
            "\n" +
            "import android.os.Bundle;\n" +
            "import android.support.annotation.NonNull;\n" +
            "import android.support.design.widget.NavigationView;\n" +
            "import android.support.v4.view.GravityCompat;\n" +
            "import android.support.v4.widget.DrawerLayout;\n" +
            "import android.support.v7.app.ActionBar;\n" +
            "import android.support.v7.app.AppCompatActivity;\n" +
            "import android.support.v7.widget.Toolbar;\n" +
            "import android.view.MenuItem;\n" +
            "\n" +
            "import com.github.codedex.codeview.CodeFragment;\n" +
            "import com.mikepenz.community_material_typeface_library.CommunityMaterialaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa;\n" +
            "import com.mikepenz.iconics.IconicsDrawable;\n" +
            "\n" +
            "public class MainActivity extends AppCompatActivity {\n" +
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
        //codeView.getRecyclerView().setClipToPadding(false);
        codeView.getRecyclerView().setFitsSystemWindows(true);
        /*ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );*/
        //layoutParams.setMargins(0, -(int) (getResources().getDimension(R.dimen.status_bar_height)*2), 0, 0);
        //codeView.setLayoutParams(layoutParams);
        //codeView.setPadding(0, (int) (getResources().getDimension(R.dimen.status_bar_height)), 0, 0);
        SourceParser.parse(TEST_CODE);
        ImportParser.parseImports(TEST_CODE);
        String code = TEST_CODE + TEST_CODE + TEST_CODE + TEST_CODE + TEST_CODE + TEST_CODE + TEST_CODE + TEST_CODE;
        new Highlighter(context)
                .code(code)
                .language("java")
                .theme(ColorTheme.DEFAULT.withBgContent(ContextCompat.getColor(context, R.color.md_white_1000)))
                .lineClickListener(new OnCodeLineClickListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onLineClicked(int n, @NotNull String line) {
                        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                            line = Html.fromHtml(line, Html.FROM_HTML_MODE_LEGACY).toString();
                        else
                            line = Html.fromHtml(line).toString();*/
                        line = line.replaceAll("<[^>]*>","");
                        Log.d("line click", n + ":line " + line);
                    }
                })
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
