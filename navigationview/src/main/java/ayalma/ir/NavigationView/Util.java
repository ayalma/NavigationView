package ayalma.ir.NavigationView;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import ayalma.ir.ripplecompat.RippleDrawableFroyo;
import ayalma.ir.ripplecompat.RippleDrawableLollipop;
import ayalma.ir.ripplecompat.RippleDrawableMarshmallow;

/**
 * Created by alimohammadi on 4/29/16.
 *
 * @author alimohammadi.
 */
public class Util {

    /*public static void initRippleDrawable(RippleView rippleView, AttributeSet attrs, int defStyleAttr) {
        View view = (View) rippleView;
        if (view.isInEditMode())
            return;

        TypedArray a = view.getContext().obtainStyledAttributes(attrs, R.styleable.NavigationView, defStyleAttr, 0);
        ColorStateList color =  a.getColorStateList(R.styleable.Carbon_carbon_rippleColor);


        if (color != null) {
            ayalma.ir.ripplecompat.RippleDrawable.Style style = ayalma.ir.ripplecompat.RippleDrawable.Style.values()[a.getInt(R.styleable.Carbon_carbon_rippleStyle, ayalma.ir.ripplecompat.RippleDrawable.Style.Background.ordinal())];
            boolean useHotspot = a.getBoolean(R.styleable.Carbon_carbon_rippleHotspot, true);
            int radius = (int) a.getDimension(R.styleable.Carbon_carbon_rippleRadius, -1);

            rippleView.setRippleDrawable(createRippleDrawable(color, style, view, useHotspot, 500));
        }

        a.recycle();
    }*/


    public static ayalma.ir.ripplecompat.RippleDrawable createRippleDrawable(ColorStateList color, ayalma.ir.ripplecompat.RippleDrawable.Style style, View view, boolean useHotspot, int radius)
    {
        ayalma.ir.ripplecompat.RippleDrawable rippleDrawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rippleDrawable = new RippleDrawableMarshmallow(color, style == ayalma.ir.ripplecompat.RippleDrawable.Style.Background ? view.getBackground() : null, style);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rippleDrawable = new RippleDrawableLollipop(color, style == ayalma.ir.ripplecompat.RippleDrawable.Style.Background ? view.getBackground() : null, style);
        } else {
            rippleDrawable = new RippleDrawableFroyo(color, style == ayalma.ir.ripplecompat.RippleDrawable.Style.Background ? view.getBackground() : null, style);
        }
        rippleDrawable.setCallback(view);
        rippleDrawable.setHotspotEnabled(useHotspot);
        rippleDrawable.setRadius(radius);
        return rippleDrawable;
    }

    public static ayalma.ir.ripplecompat.RippleDrawable createRippleDrawable(ColorStateList color, ayalma.ir.ripplecompat.RippleDrawable.Style style, View view, Drawable background, boolean useHotspot, int radius) {
        ayalma.ir.ripplecompat.RippleDrawable rippleDrawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rippleDrawable = new RippleDrawableMarshmallow(color, background, style);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rippleDrawable = new RippleDrawableLollipop(color, background, style);
        } else {
            rippleDrawable = new RippleDrawableFroyo(color, background, style);
        }
        rippleDrawable.setCallback(view);
        rippleDrawable.setHotspotEnabled(useHotspot);
        rippleDrawable.setRadius(radius);
        return rippleDrawable;
    }
}
