package com.github.codedex.codeview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
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
        codeView.highlightCode("java")
                .setColorTheme(ColorTheme.SOLARIZED_LIGHT.withBgContent(ContextCompat.getColor(context, R.color.md_grey_300)))
                .setCodeContent(TEST_CODE);
        return rowView;
    }
}
