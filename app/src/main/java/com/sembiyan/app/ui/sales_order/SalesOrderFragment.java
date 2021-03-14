package com.sembiyan.app.ui.sales_order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sembiyan.app.R;
import com.sembiyan.app.adapter.ViewSalesListAdapter;
import com.sembiyan.app.utilities.AppController;
import com.sembiyan.app.utilities.Constants;
import com.sembiyan.app.utilities.Utils;
import com.sembiyan.app.utilities.WebserviceEndpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SalesOrderFragment extends Fragment implements ViewSalesListAdapter.onItemSelectedListener {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mAddNewSaleButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sales_order, container, false);
        mRecyclerView = root.findViewById(R.id.recycler_view);
        mAddNewSaleButton = root.findViewById(R.id.add_new_sale_order);
        mAddNewSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), AddNewSaleOrderActivity.class);
//                startActivity(intent);
                Navigation.findNavController(v).navigate(R.id.action_nav_sales_order_to_addNewSaleOrderFragment);
            }
        });
        getSalesList();
        return root;
    }

    public void getSalesList() {
        String strUrl = WebserviceEndpoints.READ_RECORDS_I_SEARCH;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, strUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (Utils.validateResponseBody(response) != null) {
                        JSONObject jsonObject = new JSONObject(response);
                        int code = jsonObject.optInt("code");
                        if (code != 500) {
                            JSONArray jsonArray = jsonObject.optJSONArray("data");
                            ViewSalesListAdapter mDashboardAdapter = new ViewSalesListAdapter(getActivity(), jsonArray, SalesOrderFragment.this);
                            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity());
                            mRecyclerView.setLayoutManager(gridLayoutManager);
                            mRecyclerView.setAdapter(mDashboardAdapter);
                        } else {
                            Toast.makeText(getActivity(), Utils.getErrorMessage(jsonObject), Toast.LENGTH_SHORT).show();
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
                params.put("Authorization", "Bearer " + Utils.getLoginDetails(getActivity(), Constants.ACCESS_TOKEN));
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("model", Constants.SALES_ORDER);
                params.put("limit", Constants.LIMITS);
                params.put("fields", getFields());
                params.put("domain", getDomain());
                return params;
            }
        };

        int socketTimeout = 300000;// 30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private String getFields() {
        return "[\n" +
                "      \"message_needaction\",\n" +
                "      \"name\",\n" +
                "      \"confirmation_date\",\n" +
                "      \"commitment_date\",\n" +
                "      \"expected_date\",\n" +
                "      \"partner_id\",\n" +
                "      \"user_id\",\n" +
                "      \"amount_total\",\n" +
                "      \"currency_id\",\n" +
                "      \"invoice_status\",\n" +
                "      \"state\"\n" +
                "    ]";
    }

    private String getDomain() {
        return "[[\"state\",\"not in\",[\"draft\",\"sent\",\"cancel\"]]]";
    }

    @Override
    public void onSalesItemSelected(JSONObject jsonObject) {

    }
}