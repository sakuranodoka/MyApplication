package retrofit;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 18/11/2559.
 */
public interface InterfaceListen {
    public void onResponse(Object data, Retrofit retrofit);
    public void onBodyError(ResponseBody responseBodyError);
    public void onBodyErrorIsNull();
    public void onFailure(Throwable t);
}
