package com.sembiyan.app.utilities;

import android.content.Context;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class Utils {

    public static String readJSONFromAsset(Context context, String filename) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static String validateResponseBody(String response) {
        return isValidJSON(response) ? response : null;
    }

    private static boolean isValidJSON(String response) {
        try {
            new JSONObject(response);
        } catch (JSONException ex) {
            try {
                new JSONArray(response);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }


    public static String getErrorMessage(JSONObject jsonObject) {
        if (jsonObject != null && jsonObject.has("message")) {
            return jsonObject.optString("message");
        }
        return "";
    }

    public static String getLoginDetails(Context context, String key) {
        PreferencesManager preferencesManager = PreferencesManager.getInstance(context);
        try {
            JSONObject jsonObject = new JSONObject(preferencesManager.getStringValue(Constants.LOGIN_BASIC_DETAILS));
            return jsonObject.optString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Validate Add Line Item
     *
     * @param item
     * @param quantity
     * @param rate
     * @return true none of the field is empty otherwise false
     */
    public static boolean validateAddLineItem(AutoCompleteTextView item, TextInputEditText quantity, TextInputEditText rate) {

        boolean isValid = true;
        if (item.getText().toString().trim().isEmpty()) {
            isValid = false;
//            item.setError("Choose Item");
        }


        if (quantity.getText().toString().trim().isEmpty()) {
            isValid = false;
            quantity.setError("Enter Quantity");
        }

        if (rate.getText().toString().trim().isEmpty()) {
            isValid = false;
            rate.setError("Enter Rate");
        }
        return isValid;
    }

}
