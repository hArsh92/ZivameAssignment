package com.harsh.zivameproduct

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.harsh.zivameproduct.decoration.SelectedItemPointerDecoration
import kotlinx.android.synthetic.main.activity_product_detail.*
import org.json.JSONObject

class ProductDetailActivity : AppCompatActivity() {

    private var mFeatureAdapter: ProductFeatureAdapter? = null
    private var productFeatures: MutableList<ProductFeature?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        setUp()
    }

    private fun setUp() {
        val layoutManager = GridLayoutManager(this, gridSpanCount, GridLayoutManager.VERTICAL, false)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (productFeatures?.get(position) == null) gridSpanCount else 1
            }
        }
        list_item.layoutManager = layoutManager
        list_item.addItemDecoration(SelectedItemPointerDecoration(this))

        productFeatures = mutableListOf()
        var jsonString: String? = Utils.getFeatureStringFromJsonFile(this)
        if (jsonString != null) {
            val jsonObject = JSONObject(jsonString)
            jsonString = jsonObject.optJSONArray("values").toString()
            productFeatures = Gson().fromJson(jsonString, object : TypeToken<List<ProductFeature>>() {}.type)
            mFeatureAdapter = ProductFeatureAdapter(productFeatures)
            list_item.adapter = mFeatureAdapter
        }
    }


    companion object {
        const val gridSpanCount: Int = 3
    }
}
