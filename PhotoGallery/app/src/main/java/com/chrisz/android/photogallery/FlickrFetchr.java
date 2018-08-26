package com.chrisz.android.photogallery;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by chrisz
 * on 2018/8/24
 */
public class FlickrFetchr {

    private static final String TAG = "FlickrFetchr";
    private static final String KEY = "4f721bgafa75bf6d2cb9af54f937bb70";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<GalleryItem> fetchItems() {
        List<GalleryItem> items = new ArrayList<>();
        try {
//            http://image.baidu.com/search/acjson?tn=resultjson_com&ipn=rj&ct=201326592&is=&fp=result&queryWord=%E8%BF%AA%E4%B8%BD%E7%83%AD%E5%B7%B4&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=&z=&ic=&word=%E8%BF%AA%E4%B8%BD%E7%83%AD%E5%B7%B4&s=&se=&tab=&width=&height=&face=&istype=&qc=&nc=1&fr=&pn=30&rn=30&gsm=1e&1535167763601=
            String url = Uri.parse("http://image.baidu.com/search/acjson?tn=resultjson_com&ipn=rj&ct=201326592&is=&fp=result&queryWord=%E8%BF%AA%E4%B8%BD%E7%83%AD%E5%B7%B4&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=&z=&ic=&word=%E8%BF%AA%E4%B8%BD%E7%83%AD%E5%B7%B4&s=&se=&tab=&width=&height=&face=&istype=&qc=&nc=1&fr=&pn=30&rn=30&gsm=1e&1535167763601=")
                    .buildUpon().build().toString();
//            String url = Uri.parse("http://image.baidu.com/search/index")
//                    .buildUpon()
//                    .appendQueryParameter("tn", "resultjson_com")
//                    .appendQueryParameter("queryWord", "迪丽热巴")
//                    .appendQueryParameter("ipn", "rj")
//                    .appendQueryParameter("ct", "201326592")
//                    .appendQueryParameter("fp", "result")
//                    .appendQueryParameter("cl", "2")
//                    .appendQueryParameter("nc", "1")
//                    .appendQueryParameter("ie", "utf-8").build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "Recieved JSON : " + jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);
            parseItems(items, jsonObject);
        } catch (IOException e) {
            Log.e(TAG, "Fail to fetch items : " + e);
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e(TAG, "Fail to fetch items : " + e);
            e.printStackTrace();
        } finally {

        }
        return items;
    }

    private void parseItems(List<GalleryItem> items, JSONObject jsonObject) throws IOException, JSONException {
        JSONArray photoJsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < photoJsonArray.length(); i++) {
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);
            GalleryItem item = new GalleryItem();
            if(!photoJsonObject.has("di")){
                continue;
            }
            item.setId(photoJsonObject.getString("di"));
            item.setCaption(photoJsonObject.getString("fromPageTitleEnc"));
            if (!photoJsonObject.has("thumbURL")) {
                continue;
            }
            item.setUrl(photoJsonObject.getString("thumbURL"));
            items.add(item);
        }
    }
}
