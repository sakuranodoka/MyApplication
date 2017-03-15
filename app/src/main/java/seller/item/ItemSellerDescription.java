package seller.item;

import seller.TypeSellerReport;

/**
 * Created by Administrator on 13/12/2559.
 */
public class ItemSellerDescription extends SellerBaseItem {
    public String shopDescription;
    public String reportDescription;
    public String dateDescription;

    public boolean isExtended = false;
    public String extendDateDescription;

    public String getShopDescription() {
        return shopDescription;
    }

    public ItemSellerDescription() {
        super(TypeSellerReport.TYPE_SELLER_DESCRIPTION);
        isExtended = false;
    }

    public void setShopDescription(String shopDescription) {
        this.shopDescription = shopDescription;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public String getDateDescription() {
        return dateDescription;
    }

    public void setDateDescription(String dateDescription) {
        this.dateDescription = dateDescription;
    }

    public String getExtendDateDescription() {
        return extendDateDescription;
    }

    public void setExtendDateDescription(String extendDateDescription) {
        this.extendDateDescription = extendDateDescription;
    }
}
