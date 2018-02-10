package almaland.net.almaland;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;

import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

    private static final String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,public-profile-url,picture-url,email-address,picture-urls::(original))";

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize the progressbar
        progress = new ProgressDialog(this);
        progress.setMessage("Retrieve data...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        linkededinApiHelper();
    }

    public void linkededinApiHelper(){
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                showResult(result.getResponseDataAsJson());
                progress.dismiss();
            }

            @Override
            public void onApiError(LIApiError error) {
                Log.d("Abhinav", error.getMessage());
            }
        });
    }

    public  void  showResult(JSONObject response){

        try {
            String email = response.get("emailAddress").toString();

            Log.d("Abhinav", response.toString());

        } catch (Exception e){
            e.printStackTrace();
            Log.d("Abhinav", "Some error in show result");
        }
    }
}
