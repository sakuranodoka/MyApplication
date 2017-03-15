package seller;

/**
 * Created by Administrator on 18/11/2559.
 */
public class SellerData {
    public static int reportId = 1;

    public static int graphOptionId = 0;

    public static int reportOptional = TypeSellerOptional.TYPE_TOP_10;

    public static int dataSetNumber = 0;

    public static String shopCode = null;

    public static String graphUnit = "";

    public static final String _PURE_DATA_TRANSFER_PORT_ = "3WEQTSSJ-";

    // INNER DATA ------------------------------------------------------------------------------->
    private String SHIP_NO;

    private int REPORT_NO;

    public String getSHIP_NO() { return SHIP_NO; }

    public void setSHIP_NO(String SHIP_NO) { this.SHIP_NO = SHIP_NO; }

    public int getREPORT_NO() { return REPORT_NO; }

    public void setREPORT_NO(int REPORT_NO) { this.REPORT_NO = REPORT_NO; }
}
