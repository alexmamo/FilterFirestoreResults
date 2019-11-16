package ro.alexmamo.filterfirestoreresults;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import static ro.alexmamo.filterfirestoreresults.Constants.DATA;
import static ro.alexmamo.filterfirestoreresults.Constants.NAME_PROPERTY;
import static ro.alexmamo.filterfirestoreresults.Constants.PRODUCTS_COLLECTION;
import static ro.alexmamo.filterfirestoreresults.Constants.PRODUCT_NAMES;
import static ro.alexmamo.filterfirestoreresults.Constants.PRODUCT_SEARCH_PROPERTY;

@SuppressWarnings({"ConstantConditions", "unchecked"})
class ProductRepository {
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private DocumentReference productSearchRef = rootRef.collection(DATA).document(PRODUCT_SEARCH_PROPERTY);
    private CollectionReference productsRef = rootRef.collection(PRODUCTS_COLLECTION);

    MutableLiveData<List<String>> getProductNameListMutableLiveData() {
        MutableLiveData<List<String>> productNameListMutableLiveData = new MutableLiveData<>();
        productSearchRef.get().addOnCompleteListener(productNameListTask -> {
            if (productNameListTask.isSuccessful()) {
                DocumentSnapshot document = productNameListTask.getResult();
                if (document.exists()) {
                    List<String> productNameList = (List<String>) document.get(PRODUCT_NAMES);
                    productNameListMutableLiveData.setValue(productNameList);
                }
            } else {
                Log.d(Constants.TAG, productNameListTask.getException().getMessage());
            }
        });
        return productNameListMutableLiveData;
    }

    MutableLiveData<Double> getProductPriceMutableLiveData(String productName) {
        MutableLiveData<Double> productPriceMutableLiveData = new MutableLiveData<>();
        productsRef.whereEqualTo(NAME_PROPERTY, productName).get().addOnCompleteListener(productTask -> {
            if (productTask.isSuccessful()) {
                if (productTask.isSuccessful()) {
                    for (QueryDocumentSnapshot document : productTask.getResult()) {
                        productPriceMutableLiveData.setValue(document.toObject(Product.class).price);
                    }
                }
            } else {
                Log.d(Constants.TAG, productTask.getException().getMessage());
            }
        });
        return productPriceMutableLiveData;
    }
}