package seller.titlebar;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import seller.TypeSellerOptional;
import seller.TypeSellerReport;

/**
 * Created by Administrator on 17/11/2559.
 */
public class SellerTitleBar {

    private static String sellerTitle = "\n" +
            "[\n" +
            "    {\n" +
//            "           \"title\" : \"ยอดขายผลิตภัณฑ์ตาม Collection\"\n," +
//            "           \"id\" : " + TypeSellerReport.TYPE_SELLER_COLLECTION +
//            "    },{\n" +
//            "           \"title\" : \"ยอดขายผลิตภัณฑ์สูงสุด 10 อันดับ\"\n," +
//            "           \"id\" : " + TypeSellerReport.TYPE_SELLER_BEST_SELLER +
//            "    },{\n" +
            "           \"title\" : \"เรียงตาม Day Cover ของ Item\"\n," +
            "           \"id\" : " + TypeSellerReport.TYPE_STORAGE_DATE_COVER +
//            "    },{\n" +
//            "           \"title\" : \"เรียงตามสินค้าที่ไม่มีการเคลื่อนไหว\"\n," +
//            "           \"id\" : " + TypeSellerReport.TYPE_STORAGE_UNDEFINED_DAY_COVER +
            "    },{\n" +
            "           \"title\" : \"เรียงตามจำนวน Stock ปัจจุบัน\"\n," +
            "           \"id\" : " + TypeSellerReport.TYPE_STOCK_KEEPER +
            "    },{\n" +
            "           \"title\" : \"เรียงตามยอดขายผลิตภัณฑ์ Month to Date\"\n," +
            "           \"id\" : " + TypeSellerReport.TYPE_BEST_SELLER_MONTH_TO_DATE +
            "    }\n" +
            "]\n";

    private static String sellerRange = "";

    private static String sellerOptional = "\n" +
            "  [\n" +
            "    {\n" +
            "      \"optional\" : \"TOP 10\"\n," +
            "\"id\" : " + TypeSellerOptional.TYPE_TOP_10 +
            "    },{\n" +
            "      \"optional\" : \"LAST 10\"\n," +
            "\"id\" : " + TypeSellerOptional.TYPE_LAST_10 +
            "    }\n" +
            "    ]\n";

    public static List<SellerTitleDAO> getSellerTitleList() {
        Gson gson = new Gson();

        List<SellerTitleDAO> a = new ArrayList<>();
        try {
            java.lang.reflect.Type listType = new TypeToken<ArrayList<SellerTitleDAO>>(){}.getType();
             a = gson.fromJson(sellerTitle, listType);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
        }
        return a;
    }

    public static List<SellerRangeDAO> getSellerRangeList() {
        Gson gson = new Gson();

        List<SellerRangeDAO> a = new ArrayList<>();
        try {
            java.lang.reflect.Type listType = new TypeToken<ArrayList<SellerRangeDAO>>(){}.getType();

            a = gson.fromJson(sellerRange, listType);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
        }
        return a;
    }

    public static List<SellerOptionalDAO> getSellerOptional() {
        Gson gson = new Gson();

        List<SellerOptionalDAO> a = new ArrayList<>();
        try {
            java.lang.reflect.Type listType = new TypeToken<ArrayList<SellerOptionalDAO>>(){}.getType();

            a = gson.fromJson(sellerOptional, listType);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
        }
        return a;
    }
}
