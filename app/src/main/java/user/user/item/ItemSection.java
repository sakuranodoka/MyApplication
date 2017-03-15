package user.user.item;

import user.UserBaseItem;

/**
 * Created by Administrator on 15/11/2559.
 */
public class ItemSection extends UserBaseItem {
    String section;

    int color;

    public ItemSection() {
        super.setViewType(0);
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
