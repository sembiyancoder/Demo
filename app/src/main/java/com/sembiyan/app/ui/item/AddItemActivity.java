package com.sembiyan.app.ui.item;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sembiyan.app.R;
import com.sembiyan.app.model.ItemModel;
import com.sembiyan.app.ui.sales_order.SalesOrderFragment;
import com.sembiyan.app.utilities.AppController;
import com.sembiyan.app.utilities.Constants;
import com.sembiyan.app.utilities.Utils;
import com.sembiyan.app.utilities.WebserviceEndpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddItemActivity extends AppCompatActivity {

    private AutoCompleteTextView mItemAutoCompleteTextView;
    private List<ItemModel> mItemModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inflateXMLView();
        getItemList();
    }

    private void inflateXMLView() {
        mItemAutoCompleteTextView = findViewById(R.id.auto_complete_item_name);
    }

    public void getItemList() {
        String strUrl = WebserviceEndpoints.READ_RECORDS_I_SEARCH;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, strUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (Utils.validateResponseBody(response) != null) {
                        JSONObject jsonObject = new JSONObject(response);
                        int code = jsonObject.optInt("code");
                        if (code != 500) {
                            JSONArray data = jsonObject.optJSONArray("data");
                            if (data != null) {
                                for (int index = 0; index < data.length(); index++) {
                                    JSONObject object = data.optJSONObject(index);
                                    String display_name = object.optString("display_name");
                                    String id = object.optString("id");
                                    ItemModel itemModel = new ItemModel();
                                    itemModel.setId(id);
                                    itemModel.setName(display_name);
                                    mItemModelList.add(itemModel);
                                }

                                setAdapter();
                            }
                        } else {
                            Toast.makeText(AddItemActivity.this, Utils.getErrorMessage(jsonObject), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(SalesOrderFragment.class.getSimpleName(), response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //set authorization token in with the type via Bearer token
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + Utils.getLoginDetails(AddItemActivity.this, Constants.ACCESS_TOKEN));
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("model", Constants.PRODUCT_LIST);
                params.put("limit", Constants.LIMITS);
                params.put("fields", getFields());
                return params;
            }
        };

        int socketTimeout = 300000;// 30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void setAdapter() {
        ArrayList<String> test = new ArrayList<>();

        for (int index = 0; index < mItemModelList.size(); index++) {
            ItemModel customerModel = mItemModelList.get(index);
            test.add(customerModel.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.layout_customer_name_list, test);
        mItemAutoCompleteTextView.setAdapter(adapter);
    }

    private String getFields() {
        return "[\"display_name\",\"id\"]";
    }

}