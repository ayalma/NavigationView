package ayalma.ir.NavigationView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
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
 * Created by alimohammadi on 2015-06-25.
 *
 * @author alimohammadi.
 */
public class NavigationView extends RecyclerView
{

    OnNavigationItemSelectedListener mListener;

    public NavigationView(Context context) {
        super(context);
        initNavigationView(null);
    }

    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initNavigationView(attrs);
    }

    public NavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initNavigationView(attrs);
    }

    private void initNavigationView(AttributeSet attrs) {

        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.NavigationView);

        int menu = -1;
        int headerView = -1;

        for (int i = 0; i < a.getIndexCount(); i++)
        {
            int attr = a.getIndex(i);

            if (attr == R.styleable.NavigationView_headerLayout)
            {
                headerView = a.getResourceId(attr, -1);
            }
            else if (attr == R.styleable.NavigationView_menu)
            {
                menu = a.getResourceId(attr, -1);
            }
            else if (attr == R.styleable.NavigationView_itemRippleColor)
            {
                setRowRippleColor(a.getColor(attr,Color.parseColor("#252525")));
            }
            else if (attr == R.styleable.NavigationView_itemBackgroundColor)
            {
                setRowBackGroundColor(a.getColor(attr,Color.parseColor("#252525")));

            }
            else if (attr == R.styleable.NavigationView_itemIconTint)
            {
                setItemIconTint(a.getColor(attr,Color.parseColor("#252525")));
            }
            else if (attr == R.styleable.NavigationView_itemTextColor)
            {
                setItemTextColor(a.getColor(attr, Color.parseColor("#252525")));
            }
        }

        a.recycle();

        if (menu != -1)
            setMenu(menu);

        if (headerView != -1)
            setHeader(headerView);

        if (headerView!=-1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            addItemDecoration(new SpacesItemDecoration());


    }

    private void setHeader(int resId) {
        if (getAdapter() == null) {
            setAdapter(new Adapter());

        }

        View view = LayoutInflater.from(getContext()).inflate(resId, null);

        ((Adapter) getAdapter()).setHeaderView(view);
    }

    public void setMenu(int resId)
    {
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
                for (int j = 0; j < menuItem.getSubMenu().size(); j++)
                {
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
            } else
            {
                rowItem = new NavRowItem();

                rowItem.setId(menuItem.getItemId());
                rowItem.setTitle(menuItem.getTitle().toString());
                rowItem.setIcon(menuItem.getIcon());

                rowItems.add(rowItem);
            }

        }

        if (getAdapter() == null)
        {
            setAdapter(new Adapter());
        }
        ((Adapter) getAdapter()).setItems(rowItems);
    }

    public OnNavigationItemSelectedListener getNavigationItemSelectedListener() {
        return mListener;
    }

    public void setNavigationItemSelectedListener(OnNavigationItemSelectedListener listener) {
        this.mListener = listener;
        if (getAdapter() == null)
        {
            setAdapter(new Adapter());
        }
        ((Adapter) getAdapter()).setNavigationItemSelectedListener(listener);
    }

    public void setRowRippleColor(int rowRippleColor) {
        if (getAdapter() == null)
        {
            setAdapter(new Adapter());
        }
        ((Adapter) getAdapter()).setRippleColor(rowRippleColor);
    }

    public void setRowBackGroundColor(int rowBackGroundColor) {

        if (getAdapter() == null)
        {
            setAdapter(new Adapter());
        }
        ((Adapter) getAdapter()).setBackColor(rowBackGroundColor);
    }

    public void setItemIconTint(int itemIconTint) {
        if (getAdapter() == null)
        {
            setAdapter(new Adapter());
        }
        ((Adapter) getAdapter()).setItemIconTint(itemIconTint);
    }

    public void setItemTextColor(int itemTextColor)
    {
        if (getAdapter() == null)
        {
            setAdapter(new Adapter());
        }
        ((Adapter) getAdapter()).setItemTextColor(itemTextColor);
    }

    public static class Adapter extends RecyclerView.Adapter<ViewHolder>
    {
        OnNavigationItemSelectedListener mListener;
        int rippleColor = Color.parseColor("#252525");
        int backColor = Color.parseColor("#252525");
        int itemTextColor = Color.parseColor("#000000");
        int itemIconTint = Color.parseColor("#000000");

        private List<NavRowItem> items = null;
        private View headerView;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if (viewType == HolderType.Header.getValue()) {
                return new headerHolder(headerView);
            } else {
                View view = inflater.inflate(R.layout.navigation_row_rtl, parent, false);
                return new ItemHolder(view);
            }

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (holder instanceof ItemHolder)
            {
                if (headerView != null)
                    position = position - 1;

                ItemHolder itemHolder = (ItemHolder) holder;
                itemHolder.iv.setImageDrawable(items.get(position).getIcon());
                itemHolder.tv.setText(items.get(position).getTitle());

                itemHolder.iv.setColorFilter(itemIconTint);
                itemHolder.tv.setTextColor(itemTextColor);

                final int finalPosition = position;
                itemHolder.ivc.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null)
                            mListener.onNavigationItemSelected(finalPosition);
                    }
                });

                if (Build.VERSION.SDK_INT >= 21) {
                    itemHolder.itemView.setBackgroundColor(backColor);
                    itemHolder.ivc.setBackgroundColor(backColor);
                    itemHolder.ivc.setBackground(Util.getPressedColorRippleDrawable(backColor, rippleColor));
                }
                else {
                    itemHolder.itemView.setBackgroundColor(backColor);
                    itemHolder.ivc.setBackgroundDrawable(Util.getStateListDrawable(backColor, rippleColor));

                }
                itemHolder.dvc.setVisibility((items.get(position).isHeader()) ? VISIBLE : GONE);
                itemHolder.stv.setText(items.get(position).getHeaderTitle());
            }

        }

        @Override
        public int getItemCount() {
            if (headerView != null)
                return items.size() + 1;
            else return items.size();
        }

        public OnNavigationItemSelectedListener getsetNavigationItemSelectedListener() {
            return mListener;
        }

        public void setNavigationItemSelectedListener(OnNavigationItemSelectedListener listener) {
            this.mListener = listener;
        }

        public int getRippleColor() {
            return rippleColor;
        }

        public void setRippleColor(int rippleColor) {
            this.rippleColor = rippleColor;
        }

        public int getBackColor() {
            return backColor;
        }

        public void setBackColor(int backColor) {
            this.backColor = backColor;
        }

        public int getItemTextColor() {
            return itemTextColor;
        }

        public void setItemTextColor(int itemTextColor) {
            this.itemTextColor = itemTextColor;
        }

        public int getItemIconTint() {
            return itemIconTint;
        }

        public void setItemIconTint(int itemIconTint) {
            this.itemIconTint = itemIconTint;
        }

        public void setItems(List<NavRowItem> items) {
            this.items = items;
        }

        public void setHeaderView(View headerView) {
            this.headerView = headerView;
        }

        @Override
        public int getItemViewType(int position) {
            if (isHeader(position))
                return HolderType.Header.getValue();
            else return HolderType.Item.getValue();

        }

        private boolean isHeader(int position)
        {
            return position == 0 && headerView != null;
        }
    }


    public static class ItemHolder extends RecyclerView.ViewHolder {
        TextView tv;
        TextView stv;
        ImageView iv;
        View dv;
        View dvc;
        View ivc; // itemViewContainer


        public ItemHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.nav_itemText);
            iv = (ImageView) itemView.findViewById(R.id.nav_itemIcon);
            stv = (TextView) itemView.findViewById(R.id.nav_subTitle);
            dv = itemView.findViewById(R.id.nav_subDivider);
            dvc = itemView.findViewById(R.id.nav_subContainer);
            ivc = itemView.findViewById(R.id.nav_itemContainer);
        }
    }

    public static class headerHolder extends RecyclerView.ViewHolder {

        public headerHolder(View itemView) {
            super(itemView);
        }

    }

    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration() {
            this.space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -24,
                    getResources().getDisplayMetrics()); //8dp as px, value might be obtained e.g. from dimen resources...
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            if(parent.getChildAdapterPosition(view) == 0){
                outRect.top = space;
                outRect.bottom = 0; //dont forget about recycling...
            }
            /*if(parent.getChildAdapterPosition(view) == state.getItemCount()-1){
                outRect.bottom = -300;
                outRect.top = 0;
            }*/
        }
    }

    /**
     * Listener for handling events on navigation items.
     */
    public interface OnNavigationItemSelectedListener {

        /**
         * Called when an item in the navigation menu is selected.
         *
         * @param position The selected item position
         *
         * @return true to display the item as the selected item
         */
        boolean onNavigationItemSelected(int position);
    }
}

