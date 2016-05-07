package ayalma.ir.NavigationView;

import android.content.Context;
import android.content.res.ColorStateList;
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

import ayalma.ir.navigationview.R;
import ayalma.ir.ripplecompat.RippleDrawable;


/**
 * Created by alimohammadi on 2015-06-25.
 *
 * @author alimohammadi.
 */
public class NavigationView extends RecyclerView {

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

        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);

            if (attr == R.styleable.NavigationView_headerLayout) {
                headerView = a.getResourceId(attr, -1);
            } else if (attr == R.styleable.NavigationView_menu) {
                menu = a.getResourceId(attr, -1);
            } else if (attr == R.styleable.NavigationView_item_rippleColor) {
                setRowRippleColor(a.getColor(attr, Color.parseColor("#252525")));
            }
          /*  else if (attr == R.styleable.NavigationView_itemBackgroundColor)
            {
                setRowBackGroundColor(a.getColor(attr,Color.parseColor("#252525")));

            }*/
            else if (attr == R.styleable.NavigationView_itemIconTint) {
                setItemIconTint(a.getColor(attr, Color.parseColor("#252525")));
            } else if (attr == R.styleable.NavigationView_itemTextColor) {
                setItemTextColor(a.getColor(attr, Color.parseColor("#252525")));
            }
        }

        a.recycle();

        if (menu != -1)
            setMenu(menu);

        if (headerView != -1)
            setHeader(headerView);

        if (headerView != -1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            addItemDecoration(new SpacesItemDecoration());


    }

    private void setHeader(int resId) {
        if (getAdapter() == null) {
            setAdapter(new Adapter());

        }

        ((Adapter) getAdapter()).setHeaderView(resId);
    }

    public void setMenu(int resId) {
        Menu menu = new MenuBuilder(getContext());
        MenuInflater inflater = new MenuInflater(getContext());
        inflater.inflate(resId, menu);
        setMenu(menu);
    }

    public void setMenu(Menu menu) {

        if (getAdapter() == null) {
            setAdapter(new Adapter());
        }
        ((Adapter) getAdapter()).setItems(menu);
    }

    public OnNavigationItemSelectedListener getNavigationItemSelectedListener() {
        return mListener;
    }

    public void setNavigationItemSelectedListener(OnNavigationItemSelectedListener listener) {
        this.mListener = listener;
        if (getAdapter() == null) {
            setAdapter(new Adapter());
        }
        ((Adapter) getAdapter()).setNavigationItemSelectedListener(listener);
    }

    public void setRowRippleColor(int rowRippleColor) {
        if (getAdapter() == null) {
            setAdapter(new Adapter());
        }
        ((Adapter) getAdapter()).setRippleColor(rowRippleColor);
    }

    public void setRowBackGroundColor(int rowBackGroundColor) {

        if (getAdapter() == null) {
            setAdapter(new Adapter());
        }
        ((Adapter) getAdapter()).setBackColor(rowBackGroundColor);
    }

    public void setItemIconTint(int itemIconTint) {
        if (getAdapter() == null) {
            setAdapter(new Adapter());
        }
        ((Adapter) getAdapter()).setItemIconTint(itemIconTint);
    }

    public void setItemTextColor(int itemTextColor) {
        if (getAdapter() == null) {
            setAdapter(new Adapter());
        }
        ((Adapter) getAdapter()).setItemTextColor(itemTextColor);
    }

    public static class Adapter extends RecyclerView.Adapter<ViewHolder> {
        OnNavigationItemSelectedListener mListener;
        int rippleColor = Color.parseColor("#252525");
        int backColor = Color.parseColor("#252525");
        int itemTextColor = Color.parseColor("#000000");
        int itemIconTint = Color.parseColor("#000000");

        private Menu items = null;
        private int headerView;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view;
            if (viewType == HolderType.Header.getValue()) {
                view = inflater.inflate(headerView, parent, false);
                return new headerHolder(view);
            } else if (viewType == HolderType.Item.getValue()) {
                view = inflater.inflate(R.layout.navigation_row_rtl, parent, false);
                return new ItemHolder(view);
            } else {
                view = inflater.inflate(R.layout.nav_sub_title_row, parent, false);
                return new subTitleHolder(view);
            }

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            if (isHeader(position))
                return;

            if (headerView != 0)
                position--;

            for (int group = 0; group < items.size(); ) {

                if (items.getItem(group).hasSubMenu()) {
                    if (position > 0) {

                        position--;
                        if (position < items.getItem(group).getSubMenu().size()) {
                            onBindItemViewHolder((ItemHolder) holder, group, position);
                            return;
                        }
                        position -= items.getItem(group).getSubMenu().size();
                        group++;

                    } else {
                        onBindSubTitleHolder((subTitleHolder) holder, group);
                        return;
                    }

                } else if (position > 0) {
                    group++;
                    position--;
                } else {
                    onBindItemViewHolder((ItemHolder) holder, group, position);
                    return;
                }
            }
            throw new IndexOutOfBoundsException();

        }

        private void onBindItemViewHolder(ItemHolder holder, final int group, final int child) {

            MenuItem item = getChildItem(group, child);

            holder.ivc.setRippleDrawable(Util.createRippleDrawable(ColorStateList.valueOf(rippleColor), RippleDrawable.Style.Background, holder.ivc, false, -1));

            holder.iv.setImageDrawable(item.getIcon());
            holder.tv.setText(item.getTitle());

            holder.iv.setColorFilter(itemIconTint);
            holder.tv.setTextColor(itemTextColor);

            holder.ivc.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null)
                        mListener.onNavigationItemSelected(getChildItem(group, child));
                }
            });

        }



        private void onBindSubTitleHolder(subTitleHolder holder, int group)
        {
            holder.subTitleTxt.setText(items.getItem(group).getTitle());
            holder.subTitleTxt.setTextColor(itemTextColor);
        }

        private MenuItem getChildItem(int group, int child) {
            if (items.getItem(group).hasSubMenu())
                return items.getItem(group).getSubMenu().getItem(child);
            else return items.getItem(group);
        }

        @Override
        public int getItemCount() {

            int size = 0;
            if (headerView != 0)
                size++;

            for (int i = 0; i < items.size(); i++) {
                size += items.getItem(i).hasSubMenu() ? items.getItem(i).getSubMenu().size() + 1 : 1;
            }

            return size;
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

        public void setItems(Menu items) {
            this.items = items;
        }

        public void setHeaderView(int headerView) {
            this.headerView = headerView;
        }

        @Override
        public int getItemViewType(int position) {


            if (isHeader(position))
                return HolderType.Header.getValue();


            if (headerView != 0)
                position--;

            for (int group = 0; group < items.size(); ) {
                if (items.getItem(group).hasSubMenu()) {
                    if (position > 0) {

                        position--;
                        if (position < items.getItem(group).getSubMenu().size())
                            return HolderType.Item.getValue();

                        position -= items.getItem(group).getSubMenu().size();
                        group++;
                    } else return HolderType.SubMenuTitle.getValue();

                } else if (position > 0) {
                    group++;
                    position--;
                } else return HolderType.Item.getValue();
            }
            throw new IndexOutOfBoundsException();
        }

        private boolean isHeader(int position) {
            return position == 0 && headerView != 0;
        }
    }


    public static class ItemHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView iv;
        LinearLayout ivc; // itemViewContainer


        public ItemHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.nav_itemText);
            iv = (ImageView) itemView.findViewById(R.id.nav_itemIcon);
            ivc = (LinearLayout) itemView.findViewById(R.id.nav_itemContainer);
        }
    }

    public static class headerHolder extends RecyclerView.ViewHolder {

        public headerHolder(View itemView) {
            super(itemView);
        }

    }

    private static class subTitleHolder extends RecyclerView.ViewHolder {

        private View divider;
        private TextView subTitleTxt;

        public subTitleHolder(View itemView) {
            super(itemView);
            subTitleTxt = (TextView) itemView.findViewById(R.id.nav_sub_txt);
            divider = itemView.findViewById(R.id.nav_sub_divider);
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

            if (parent.getChildAdapterPosition(view) == 0) {
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
         * @param menuItem The selected menuItem
         * @return true to display the item as the selected item
         */
        boolean onNavigationItemSelected(MenuItem menuItem);
    }
}

