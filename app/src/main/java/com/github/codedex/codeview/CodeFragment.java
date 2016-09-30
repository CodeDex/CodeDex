package com.github.codedex.codeview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.codedex.R;
import com.github.codedex.codeview.highlight.ColorTheme;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

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
            "import com.mikepenz.community_material_typeface_library.CommunityMaterialaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaz;\n" +
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
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rowView = inflater.inflate(R.layout.fragment_code_view, container, false);
        Context context = getActivity();
        CodeView codeView = (CodeView) rowView.findViewById(R.id.code_view);
        codeView.setFitsSystemWindows(true);
        codeView.setClipToPadding(false);
        codeView.setHasFixedSize(true);
        codeView.setWindow(new WeakReference<>(getActivity().getWindow()));
        new Highlighter(context)
                .code(TEST_CODE + TEST_CODE + TEST_CODE + TEST_CODE + TEST_CODE + TEST_CODE)
                .language("java")
                .theme(ColorTheme.DEFAULT.withBgContent(ContextCompat.getColor(context, R.color.md_white_1000)))
                .lineClickListener(new OnCodeLineClickListener() {
                    @Override
                    public void onLineClicked(int n, @NotNull String line) {
                        line = line.replaceAll("<[^>]*>", "");
                        Log.d("line click", n + ":line " + line);
                    }
                })
                .highlight(codeView);
        setHasOptionsMenu(true);
        return rowView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem menuItem = menu.add(0, R.id.menu_show_files, 0, "Files");
        menuItem.setIcon(new IconicsDrawable(getActivity(), CommunityMaterial.Icon.cmd_sort_variant).actionBar().colorRes(R.color.md_black_1000));
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
