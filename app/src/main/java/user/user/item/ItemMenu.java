package user.user.item;

import android.content.Intent;

import user.UserBaseItem;

/**
 * Created by Administrator on 16/11/2559.
 */
public class ItemMenu extends UserBaseItem {
    public String menuName;

    public String detailName;

    public int imageSource;
    public int imageResourceColor = 0;

    public int menuMethod = 0;

    public Class<?> initial = null;
    public Class<?> target = null;

    public Intent intent = null;

    public ItemMenu() {
        super.setViewType(1);
    }

    public int getMenuMethod() {
        return menuMethod;
    }

    public void setMenuMethod(int menuMethod) {
        this.menuMethod = menuMethod;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public int getImageSource() {
        return imageSource;
    }

    public void setImageSource(int imageSource) {
        this.imageSource = imageSource;
    }

    public int getImageResourceColor() {
        return imageResourceColor;
    }

    public void setImageResourceColor(int imageResourceColor) {
        this.imageResourceColor = imageResourceColor;
    }

    public Class<?> getInitial() {
        return initial;
    }

    public Class<?> getTarget() {
        return target;
    }

    public void setIntent(Class<?> initial, Class<?> target) {
        this.initial = initial;
        this.target = target;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

}
