package be.kuleuven.gt.dogapp.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageUploadTask {

    private static final String UPLOAD_URL = "https://your-server.com/upload";

    public void uploadImage(Context context, Bitmap bitmap, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(Request.Method.POST, UPLOAD_URL, responseListener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("image", bitmapToString(bitmap));
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
