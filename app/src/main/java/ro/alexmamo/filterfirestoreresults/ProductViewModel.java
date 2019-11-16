package ro.alexmamo.filterfirestoreresults;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ProductViewModel extends ViewModel {
    private ProductRepository productRepository;

    public ProductViewModel() {
        productRepository = new ProductRepository();
    }

    LiveData<List<String>> getProductNameListLiveData() {
        return productRepository.getProductNameListMutableLiveData();
    }

    LiveData<Double> getProductPriceLiveData(String productName) {
        return productRepository.getProductPriceMutableLiveData(productName);
    }
}