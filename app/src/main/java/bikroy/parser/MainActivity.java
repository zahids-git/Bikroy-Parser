package bikroy.parser;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    RecyclerView recyclerView;
    SearchView searchItem;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchItem = (SearchView) findViewById(R.id.searchItem);
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        recyclerView = new RecyclerView(this);
        ((LinearLayout)findViewById(R.id.activity_main)).addView(recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);

        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getDataFromServer(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    private void getDataFromServer(String value){
        progressDialog.show();
        StringRequest request = new StringRequest("https://bikroy.com/en/ads?query="+value,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    parseData(response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this,"Connectivity Error", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        request.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void parseData(String data){
        Document doc = Jsoup.parseBodyFragment(data);
        Elements normalItem = doc.getElementsByClass("ui-item");
        Elements topItem = doc.getElementsByClass("is-top");

        ArrayList<ItemModel> model = new ArrayList<>();

        for (Element element: topItem) {
            model.add(parseElementData(element,true));
        }

        for (Element element: normalItem) {
            model.add(parseElementData(element,false));
        }

        ItemAdapter itemAdapter = new ItemAdapter(model);
        recyclerView.setAdapter(itemAdapter);
        progressDialog.dismiss();
    }

    private ItemModel parseElementData(Element element, boolean isTop){
        ItemModel model = new ItemModel();
        model.setTop(isTop);

        Elements content = element.getElementsByTag("div");

        /*Set image*/
        try{
            model.setImageUrl(content.get(1).getElementsByTag("img").get(0).attr("data-srcset").toString().split(" 1x, //")[0]);
        }catch (Exception e){}

        /*Set title*/
        try{
            model.setTitle(content.get(2).getElementsByTag("a").text());
        }catch (Exception e){}

        /*Set Area*/
        try{
            model.setLocation(content.get(2).getElementsByClass("item-area").get(0).text());
        } catch (Exception e){}

        /*Set Category*/
        try{
            model.setCategory(content.get(2).getElementsByClass("item-cat").get(0).text());
        } catch (Exception e){}

        /*Set Price*/
        try{
            model.setPrice(content.get(2).getElementsByClass("item-info").get(0).text());
        } catch (Exception e){}

        return model;
    }







    public native String stringFromJNI();

    static {
        System.loadLibrary("native-lib");
    }

}
