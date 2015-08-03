package com.example.kelly.mysop;

/**
 * Created by Chris on 2015/7/20.
 */
        import android.util.Log;
        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.UnsupportedEncodingException;
        import java.util.List;
        import org.apache.http.HttpEntity;
        import org.apache.http.HttpResponse;
        import org.apache.http.NameValuePair;
        import org.apache.http.client.ClientProtocolException;
        import org.apache.http.client.entity.UrlEncodedFormEntity;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.client.utils.URLEncodedUtils;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.json.JSONException;
        import org.json.JSONObject;

public class JSONParser {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    public JSONParser() {
    }

    public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params) {
        HttpResponse line;
        try {
            DefaultHttpClient e;
            if(method == "POST") {
                e = new DefaultHttpClient();
                HttpPost sb = new HttpPost(url);
                sb.setEntity(new UrlEncodedFormEntity(params));
                line = e.execute(sb);
                HttpEntity httpResponse = line.getEntity();
                is = httpResponse.getContent();
            } else if(method == "GET") {
                e = new DefaultHttpClient();
                String sb1 = URLEncodedUtils.format(params, "utf-8");
                url = url + "?" + sb1;
                HttpGet line1 = new HttpGet(url);
                HttpResponse httpResponse1 = e.execute(line1);
                HttpEntity httpEntity = httpResponse1.getEntity();
                is = httpEntity.getContent();
            }
        } catch (UnsupportedEncodingException var10) {
            var10.printStackTrace();
        } catch (ClientProtocolException var11) {
            var11.printStackTrace();
        } catch (IOException var12) {
            var12.printStackTrace();
        }

        try {
            BufferedReader e1 = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb2 = new StringBuilder();
            line = null;

            String line2;
            while((line2 = e1.readLine()) != null) {
                sb2.append(line2 + "\n");
                //sb2.append(line2);
            }

            is.close();
            json = sb2.toString();
        } catch (Exception var13) {
            Log.e("Buffer Error", "Error converting result " + var13.toString());
        }

        try {
            jObj = new JSONObject(json);
        } catch (JSONException var9) {
            Log.e("JSON Parser", "Error parsing data " + var9.toString());
        }

        return jObj;
    }
}
