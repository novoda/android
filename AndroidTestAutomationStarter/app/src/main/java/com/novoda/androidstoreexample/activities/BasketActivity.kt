package com.novoda.androidstoreexample.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.novoda.androidstoreexample.R
import com.novoda.androidstoreexample.adapters.BasketAdapter
import com.novoda.androidstoreexample.dagger.basket.BasketModule
import com.novoda.androidstoreexample.dagger.component.AppComponent
import com.novoda.androidstoreexample.listener.BasketAdapterListener
import com.novoda.androidstoreexample.models.Order
import com.novoda.androidstoreexample.models.Product
import com.novoda.androidstoreexample.mvp.presenter.BasketPresenter
import com.novoda.androidstoreexample.mvp.view.BasketView
import com.novoda.androidstoreexample.utilities.PRODUCT_ID_EXTRA
import kotlinx.android.synthetic.main.activity_basket.*
import javax.inject.Inject

class BasketActivity : BaseActivity(), BasketView {
    @Inject
    lateinit var presenter: BasketPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)
        presenter.loadBasket()
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showBasketItems(orders: List<Order>) {
        val listener = object: BasketAdapterListener {
            override fun onProductImageClicked(product: Product) {
                presenter.onBasketItemClicked(product)
            }

            override fun onIncreaseAmountClicked(product: Product) {
                presenter.onIncreaseItemClicked(product)
            }

            override fun onDecreaseAmountClicked(product: Product) {
                presenter.onDecreaseAmountClicked(product)
            }
        }
        val basketAdapter = BasketAdapter(this, orders, listener)
        basketList.layoutManager = LinearLayoutManager(this)
        basketList.adapter = basketAdapter
    }

    override fun showTotalAmountOfBasket(totalPrice: Int) {
        basket_total_text_field.text = applicationContext.getString(R.string.price_template, totalPrice)
    }

    override fun onProductClicked(product: Product) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra(PRODUCT_ID_EXTRA, product.id)
        startActivity(intent)
    }

    override fun getActivityLayout(): Int {
        return R.layout.activity_basket
    }

    override fun onCheckoutClicked(view: View) {
        val intent = Intent(this, CheckoutActivity::class.java)
        startActivity(intent)
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.injectBasket(BasketModule(this)).inject(this)
    }
}
