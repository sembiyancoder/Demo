package com.sembiyan.app.ui.sales_order;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sembiyan.app.R;
import com.sembiyan.app.databinding.FragmentAddNewSaleOrderBinding;
import com.sembiyan.app.model.CustomerModel;
import com.sembiyan.app.model.ProductPriceModel;
import com.sembiyan.app.ui.customer.AddNewCustomerActivity;
import com.sembiyan.app.utilities.AppController;
import com.sembiyan.app.utilities.Constants;
import com.sembiyan.app.utilities.Utils;
import com.sembiyan.app.utilities.WebserviceEndpoints;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AddNewSaleOrderFragment extends Fragment {

    private FragmentAddNewSaleOrderBinding binding;
    private List<CustomerModel> mCustomerModelList = new ArrayList<>();
    private List<ProductPriceModel> mProductPriceList = new ArrayList<>();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddNewSaleOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inflateXMLView();
        getCustomerList();

    }

    private void inflateXMLView() {

        binding.imageAddNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddNewCustomerActivity.class);
                startActivity(intent);
            }
        });

        binding.btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(getContext(), AddItemActivity.class);
                startActivity(intent);*/

                Navigation.findNavController(v).navigate(R.id.action_addNewSaleOrderFragment_to_addItemFragment);
            }
        });

        binding.autoCompleteCustomerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 1) {
                    if (!binding.autoCompleteCustomerName.isPopupShowing()) {
                        binding.txtCustomerNotFound.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        binding.txtCustomerNotFound.setVisibility(View.GONE);
                    }
                }
            }
        });


        binding.autoCompleteProductPriceList.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 1) {
                    if (!binding.autoCompleteProductPriceList.isPopupShowing()) {
                        binding.txtProductListNotFound.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        binding.autoCompleteProductPriceList.setVisibility(View.GONE);
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
                            Toast.makeText(getContext(), Utils.getErrorMessage(jsonObject), Toast.LENGTH_SHORT).show();
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
                params.put("Authorization", "Bearer " + Utils.getLoginDetails(getContext(), Constants.ACCESS_TOKEN));
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.layout_customer_name_list, test);
        binding.autoCompleteCustomerName.setAdapter(adapter);

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
                            Toast.makeText(getContext(), Utils.getErrorMessage(jsonObject), Toast.LENGTH_SHORT).show();
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
                params.put("Authorization", "Bearer " + Utils.getLoginDetails(getContext(), Constants.ACCESS_TOKEN));
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.layout_customer_name_list, productList);
        binding.autoCompleteProductPriceList.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}