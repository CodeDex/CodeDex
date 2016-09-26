package com.github.codedex;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.codedex.codeview.CodeFragment;
import com.github.codedex.codeview.highlight.MonoFontCache;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(new IconicsDrawable(this, CommunityMaterial.Icon.cmd_menu).actionBar());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        TextView toolbarChildView = (TextView) toolbar.getChildAt(0);
        toolbarChildView.setTypeface(MonoFontCache.getInstance(this).getTypeface());

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("bla").withIdentifier(1L)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.d("item", "click");
                        // do something with the clicked item :D
                        return true;
                    }
                })
                .buildView();
        result.setSelection(1);
        result.getSlider().removeAllViews();


        Drawer resultRight = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("bla2").withIdentifier(1L)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.d("item", "click");
                        // do something with the clicked item :D
                        return true;
                    }
                })
                .buildView();
        resultRight.setSelection(1);
        resultRight.getSlider().removeAllViews();

        NavigationView view2 = (NavigationView) findViewById(R.id.navigation_view_2);
        view2.removeAllViews();
        view2.addView(resultRight.getRecyclerView());

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.removeAllViews();
        view.addView(result.getRecyclerView());

        CodeFragment codeFragment = new CodeFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, codeFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_show_files:
                drawerLayout.openDrawer(GravityCompat.END);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
