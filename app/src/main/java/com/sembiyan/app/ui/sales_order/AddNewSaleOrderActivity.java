package com.sembiyan.app.ui.sales_order;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.sembiyan.app.R;
import com.sembiyan.app.model.CustomerModel;
import com.sembiyan.app.model.ProductPriceModel;
import com.sembiyan.app.ui.customer.AddNewCustomerActivity;
import com.sembiyan.app.ui.item.AddItemActivity;
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

public class AddNewSaleOrderActivity extends AppCompatActivity {

    private ImageView mAddNewCustomerView;
    private List<CustomerModel> mCustomerModelList = new ArrayList<>();
    private List<ProductPriceModel> mProductPriceList = new ArrayList<>();
    private AutoCompleteTextView mCustomerAutoCompleteTextView, mProductListAutoCompleteTextView;
    private TextView mCustomerNotFound, mPriceListTextView;
    private MaterialButton mAddItemMaterialButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_sale_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inflateXMLView();
        getCustomerList();
    }

    private void inflateXMLView() {
        mCustomerNotFound = findViewById(R.id.txt_customer_not_found);
        mAddNewCustomerView = findViewById(R.id.image_add_new_customer);
        mPriceListTextView = findViewById(R.id.txt_product_list_not_found);
        mCustomerAutoCompleteTextView = findViewById(R.id.auto_complete_customer_name);
        mProductListAutoCompleteTextView = findViewById(R.id.auto_complete_product_price_list);
        mAddItemMaterialButton = findViewById(R.id.btn_add_item);

        mAddNewCustomerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewSaleOrderActivity.this, AddNewCustomerActivity.class);
                startActivity(intent);
            }
        });

        mAddItemMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewSaleOrderActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

        mCustomerAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 1) {
                    if (!mCustomerAutoCompleteTextView.isPopupShowing()) {
                        mCustomerNotFound.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        mCustomerNotFound.setVisibility(View.GONE);
                    }
                }
            }
        });


        mProductListAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 1) {
                    if (!mProductListAutoCompleteTextView.isPopupShowing()) {
                        mPriceListTextView.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        mPriceListTextView.setVisibility(View.GONE);
                    }
                }
            }
        });
    }


    public void getCustomerList() {
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
                                    JSONArray jsonArray = object.optJSONArray("property_product_pricelist");

                                    String price_id = jsonArray.optString(0);
                                    String price_name = jsonArray.optString(1);

                                    String id = object.optString("id");
                                    String display_name = object.optString("display_name");

                                    CustomerModel customerModel = new CustomerModel();
                                    customerModel.setId(id);
                                    customerModel.setName(display_name);
                                    mCustomerModelList.add(customerModel);


                                }
                                setCustomerNameListAdapter();

                            }
                        } else {
                            Toast.makeText(AddNewSaleOrderActivity.this, Utils.getErrorMessage(jsonObject), Toast.LENGTH_SHORT).show();
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
                params.put("Authorization", "Bearer " + Utils.getLoginDetails(AddNewSaleOrderActivity.this, Constants.ACCESS_TOKEN));
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("model", Constants.RES_PARTNER);
                params.put("limit", Constants.LIMITS);
                params.put("fields", getFields());
                //params.put("domain", "[[\"rfid_number\",\"=\"," + "\"" + strDomain + "\"" + "]]");
                return params;
            }
        };

        int socketTimeout = 300000;// 30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private String getFields() {
        return "[\"property_product_pricelist\",\"display_name\"]";
    }

    private String getProductFields() {
        return "[\"display_name\",\"id\"]";
    }

    private void setCustomerNameListAdapter() {
        ArrayList<String> test = new ArrayList<>();

        for (int index = 0; index < mCustomerModelList.size(); index++) {
            CustomerModel customerModel = mCustomerModelList.get(index);
            test.add(customerModel.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.layout_customer_name_list, test);
        mCustomerAutoCompleteTextView.setAdapter(adapter);

        getProductPriceList();
    }


    public void getProductPriceList() {
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
                                    String id = object.optString("id");
                                    String display_name = object.optString("display_name");

                                    ProductPriceModel priceModel = new ProductPriceModel();
                                    priceModel.setId(id);
                                    priceModel.setName(display_name);
                                    mProductPriceList.add(priceModel);
                                }
                                setPriceList();
                            }
                        } else {
                            Toast.makeText(AddNewSaleOrderActivity.this, Utils.getErrorMessage(jsonObject), Toast.LENGTH_SHORT).show();
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
                params.put("Authorization", "Bearer " + Utils.getLoginDetails(AddNewSaleOrderActivity.this, Constants.ACCESS_TOKEN));
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("model", Constants.PRODUCT_PRICE_LIST);
                params.put("limit", Constants.LIMITS);
                params.put("fields", getProductFields());
                //params.put("domain", "[[\"rfid_number\",\"=\"," + "\"" + strDomain + "\"" + "]]");
                return params;
            }
        };

        int socketTimeout = 300000;// 30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void setPriceList() {
        ArrayList<String> productList = new ArrayList<>();
        for (int index = 0; index < mProductPriceList.size(); index++) {
            ProductPriceModel customerModel = mProductPriceList.get(index);
            productList.add(customerModel.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.layout_customer_name_list, productList);
        mProductListAutoCompleteTextView.setAdapter(adapter);
    }

}