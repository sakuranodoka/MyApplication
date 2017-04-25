package retrofit;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public interface InterfaceListen {
    public void onResponse(Object data, Retrofit retrofit);
    public void onBodyError(ResponseBody responseBodyError);
    public void onBodyErrorIsNull();
    public void onFailure(Throwable t);
}
