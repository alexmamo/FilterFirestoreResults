package ro.alexmamo.filterfirestoreresults;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import static ro.alexmamo.filterfirestoreresults.Constants.*;

public class ProductsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, SearchView.OnCloseListener {
    private SearchView searchView;
    private TextView priceTextView;
    private SearchView.SearchAutoComplete autoComplete;
    private ProductViewModel productViewModel;
    private ProductNamesAdapter productNamesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        initViews();
        setSearchAutoComplete();
        initProductNameListViewModel();
        getProductNameList();
    }

    private void initViews() {
        searchView = findViewById(R.id.search_view);
        searchView.setOnCloseListener(this);
        priceTextView = findViewById(R.id.price_text_view);
    }

    private void setSearchAutoComplete() {
        autoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        autoComplete.setDropDownBackgroundResource(R.color.colorWhite);
        autoComplete.setThreshold(1);
        autoComplete.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedProductName = (String) parent.getItemAtPosition(position);
        autoComplete.setText(selectedProductName);
        productViewModel.getProductPriceLiveData(selectedProductName).observe(this, price -> {
            String priceText = PRICE + price;
            priceTextView.setText(priceText);
            hideKeyboard();
        });
    }

    @Override
    public boolean onClose() {
        priceTextView.setText(EMPTY);
        hideKeyboard();
        return false;
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void initProductNameListViewModel() {
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
    }

    private void getProductNameList() {
        productViewModel.getProductNameListLiveData().observe(this, productNameList -> {
            productNamesAdapter = new ProductNamesAdapter(this, productNameList);
            autoComplete.setAdapter(productNamesAdapter);
        });
    }
}