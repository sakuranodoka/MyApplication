package seller.item;

import java.util.ArrayList;
import java.util.List;

import seller.TypeSellerReport;
import seller.titlebar.SellerOptionalDAO;
import seller.titlebar.SellerRangeDAO;
import seller.titlebar.SellerTitleDAO;

/**
 * Created by Administrator on 17/11/2559.
 */
public class ItemSellerTitle extends SellerBaseItem {
    public List<SellerTitleDAO> listTitleDescription;
    public List<SellerRangeDAO> listTitleRange;
    public List<SellerOptionalDAO> listTitleOptional;

    //public List<String> title;

    public int settingsBackgroundColor;
    public int settingsTint;

    public ItemSellerTitle() {
        super(TypeSellerReport.TYPE_SELLER_TITLE);
    }

    public List<SellerTitleDAO> getListTitleDescription() {
        return listTitleDescription;
    }

    public void setListTitleDescription(List<SellerTitleDAO> listOptionValue) {
        this.listTitleDescription = listOptionValue;
    }

    public List<SellerRangeDAO> getListTitleRange() {
        return listTitleRange;
    }

    public void setListTitleRange(List<SellerRangeDAO> listTitleRange) {
        this.listTitleRange = listTitleRange;
    }

    public List<SellerOptionalDAO> getListTitleOptional() {
        return listTitleOptional;
    }

    public void setListTitleOptional(List<SellerOptionalDAO> listTitleOptional) {
        this.listTitleOptional = listTitleOptional;
    }

    public int getSettingsBackgroundColor() {
        return settingsBackgroundColor;
    }

    public void setSettingsBackgroundColor(int settingsBackgroundColor) {
        this.settingsBackgroundColor = settingsBackgroundColor;
    }

    public int getSettingsTint() {
        return settingsTint;
    }

    public void setSettingsTint(int settingsTint) {
        this.settingsTint = settingsTint;
    }

    public List<String> getTitleList() {
        List<String> ls = new ArrayList<>();
        for(SellerTitleDAO temp : this.listTitleDescription) {
            ls.add(temp.getTitle());
        }
        return ls;
    }
}
