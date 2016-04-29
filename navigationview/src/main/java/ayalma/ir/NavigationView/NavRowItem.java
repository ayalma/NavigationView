package ayalma.ir.NavigationView;

import android.graphics.drawable.Drawable;

/**
 * class that represent navigation ro item .
 * Created by alimohammadi on 4/29/16.
 * @author alimohammadi.
 */
public class NavRowItem {

    private int id;
    private String title;
    private Drawable icon;
    private boolean header;
    private String headerTitle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
