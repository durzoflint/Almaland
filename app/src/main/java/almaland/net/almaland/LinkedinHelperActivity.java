package almaland.net.almaland;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;

import org.json.JSONObject;

public class LinkedinHelperActivity extends AppCompatActivity {

    private static final String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,public-profile-url,picture-url,email-address,picture-urls::(original))";

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkedin_helper);

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
                progress.dismiss();
            }
        });
    }

    public  void  showResult(JSONObject response){
        try {
            String email = response.get("emailAddress").toString();
            //Todo: check login for this email

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
