package de.winterapps.appxpired.BarcodeScanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import de.winterapps.appxpired.CRUD.addActivity;
import de.winterapps.appxpired.R;
import de.winterapps.appxpired.memberVariables;
import de.winterapps.appxpired.settingsActivity;

/**
 * Created by D062400 on 19.11.2015.
 */
public class uploadItem extends Activity {

    Activity self = this;
    String EAN;
    String Name;
    String Brand;
    String Amount;
    String Unit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_upload);

        final Button buttonUpload = (Button) findViewById(R.id.uploadUploadButton);
        buttonUpload.setOnClickListener(upload);

        EAN = ((memberVariables) ((Activity) self).getApplication()).getCode();
    }

    View.OnClickListener upload = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            {
                final EditText nameEdit = (EditText) findViewById(R.id.uploadNameEdit);
                Name = nameEdit.getText().toString();
                final EditText brandEdit = (EditText) findViewById(R.id.uploadBrandEdit);
                Brand = brandEdit.getText().toString();
                final EditText amountEdit = (EditText) findViewById(R.id.uploadAmountEdit);
                Amount = amountEdit.getText().toString();
                final Spinner unitSpinner = (Spinner) findViewById(R.id.uploadUnitSpinner);
                Unit = unitSpinner.getSelectedItem().toString();

                putAPI();

            }
        }
    };

    public void putAPI(){
        // Instantiate the RequestQueue.
        // RequestQueue queue = Volley.newRequestQueue(((Activity) mListener).getApplication());
        com.android.volley.RequestQueue queue;
        queue = Volley.newRequestQueue(self);
        String url ="https://www.datakick.org/api/items/"+EAN+"?brand_name="+Brand+"&name="+Name+"&size="+Amount+Unit; //4008458803774?brand_name=OberSelters&name=Nassauer Land Medium&size=1l
        //boolean hasResponse = false;
        System.out.println("lol");
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(self, addActivity.class);
                        startActivity(intent);
                    }

                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}