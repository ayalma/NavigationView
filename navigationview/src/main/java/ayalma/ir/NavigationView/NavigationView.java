package ayalma.ir.NavigationView;

import android.content.Context;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ayalma.ir.navigationview.R;


/**
 * Created by Marcin on 2015-06-25.
 */
public class NavigationView extends RecyclerView {
    private NavRowItem menu;

    public NavigationView(Context context) {
        super(context);
        initNavigationView();
    }

    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initNavigationView();
    }

    public NavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initNavigationView();
    }

    private void initNavigationView() {
        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    public void setMenu(int resId) {
        Menu menu = new MenuBuilder(getContext());
        MenuInflater inflater = new MenuInflater(getContext());
        inflater.inflate(resId, menu);
        setMenu(menu);
    }

    public void setMenu(Menu menu) {

        List<NavRowItem> rowItems = new ArrayList<>();

        NavRowItem rowItem;
        MenuItem menuItem;

        for (int i = 0; i < menu.size(); i++) {
            menuItem = menu.getItem(i);
            if (menuItem.hasSubMenu()) {
                for (int j = 0; j < menuItem.getSubMenu().size(); j++) {
                    rowItem = new NavRowItem();
                    rowItem.setId(menuItem.getSubMenu().getItem(j).getItemId());
                    rowItem.setTitle(menuItem.getSubMenu().getItem(j).getTitle().toString());
                    rowItem.setIcon(menuItem.getSubMenu().getItem(j).getIcon());

                    if (j == 0) {
                        rowItem.setHeader(true);
                        rowItem.setHeaderTitle(menuItem.getTitle().toString());
                    } else rowItem.setHeader(false);

                    rowItems.add(rowItem);
                }
            } else {
                rowItem = new NavRowItem();
                rowItem.setId(menuItem.getItemId());
                rowItem.setTitle(menuItem.getTitle().toString());
                rowItem.setIcon(menuItem.getIcon());

                rowItems.add(rowItem);
            }

        }

        if (getAdapter() == null) {
            setAdapter(new Adapter());

        }
        ((Adapter) getAdapter()).setItems(rowItems);
    }


    public static class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private List<NavRowItem> items = null;


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.navigation_row_rtl, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.iv.setImageDrawable(items.get(position).getIcon());
            holder.tv.setText(items.get(position).getTitle());

            holder.dvc.setVisibility((items.get(position).isHeader()) ? VISIBLE : GONE);
            holder.stv.setText(items.get(position).getHeaderTitle());

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void setItems(List<NavRowItem> items) {
            this.items = items;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        TextView stv;
        ImageView iv;
        View dv;
        View dvc;


        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.nav_itemText);
            iv = (ImageView) itemView.findViewById(R.id.nav_itemIcon);
            stv = (TextView) itemView.findViewById(R.id.nav_subTitle);
            dv = itemView.findViewById(R.id.nav_subDivider);
            dvc = itemView.findViewById(R.id.nav_subContainer);
        }
    }

}
