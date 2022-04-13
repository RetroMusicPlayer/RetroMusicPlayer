/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic.activities

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.BuildConfig
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsBaseActivity
import code.name.monkey.retromusic.databinding.ActivityDonationBinding
import code.name.monkey.retromusic.databinding.ItemDonationOptionBinding
import code.name.monkey.retromusic.extensions.*
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.PurchaseInfo
import com.anjlab.android.iab.v3.SkuDetails

class SupportDevelopmentActivity : AbsBaseActivity(), BillingProcessor.IBillingHandler {

    lateinit var binding: ActivityDonationBinding

    companion object {
        val TAG: String = SupportDevelopmentActivity::class.java.simpleName
        const val DONATION_PRODUCT_IDS = R.array.donation_ids
    }

    var billingProcessor: BillingProcessor? = null

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun donate(i: Int) {
        val ids = resources.getStringArray(DONATION_PRODUCT_IDS)
        billingProcessor?.purchase(this, ids[i])
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarColorAuto()
        setTaskDescriptionColorAuto()

        setupToolbar()

        billingProcessor = BillingProcessor(this, BuildConfig.GOOGLE_PLAY_LICENSING_KEY, this)
        TintHelper.setTint(binding.progress, accentColor())
        binding.donation.setTextColor(accentColor())
    }

    private fun setupToolbar() {
        val toolbarColor = surfaceColor()
        binding.toolbar.setBackgroundColor(toolbarColor)
        ToolbarContentTintHelper.colorBackButton(binding.toolbar)
        setSupportActionBar(binding.toolbar)
    }

    override fun onBillingInitialized() {
        loadSkuDetails()
    }

    private fun loadSkuDetails() {
        binding.progressContainer.isVisible = true
        binding.recyclerView.isVisible = false
        val ids =
            resources.getStringArray(DONATION_PRODUCT_IDS)
        billingProcessor!!.getPurchaseListingDetailsAsync(
            ArrayList(listOf(*ids)),
            object : BillingProcessor.ISkuDetailsResponseListener {
                override fun onSkuDetailsResponse(skuDetails: MutableList<SkuDetails>?) {
                    if (skuDetails == null || skuDetails.isEmpty()) {
                        binding.progressContainer.isVisible = false
                        return
                    }

                    binding.progressContainer.isVisible = false
                    binding.recyclerView.apply {
                        itemAnimator = DefaultItemAnimator()
                        layoutManager = GridLayoutManager(this@SupportDevelopmentActivity, 2)
                        adapter = SkuDetailsAdapter(this@SupportDevelopmentActivity, skuDetails)
                        isVisible = true
                    }
                }

                override fun onSkuDetailsError(error: String?) {
                    Log.e(TAG, error.toString())
                }
            })
    }

    override fun onProductPurchased(productId: String, details: PurchaseInfo?) {
        // loadSkuDetails();
        showToast(R.string.thank_you)
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        Log.e(TAG, "Billing error: code = $errorCode", error)
    }

    override fun onPurchaseHistoryRestored() {
        // loadSkuDetails();
        showToast(R.string.restored_previous_purchases)
    }

    override fun onDestroy() {
        billingProcessor?.release()
        super.onDestroy()
    }
}

class SkuDetailsAdapter(
    private var donationsDialog: SupportDevelopmentActivity,
    objects: List<SkuDetails>
) : RecyclerView.Adapter<SkuDetailsAdapter.ViewHolder>() {

    private var skuDetailsList: List<SkuDetails> = ArrayList()

    init {
        skuDetailsList = objects
    }

    private fun getIcon(position: Int): Int {
        return when (position) {
            0 -> R.drawable.ic_cookie
            1 -> R.drawable.ic_take_away
            2 -> R.drawable.ic_take_away_coffe
            3 -> R.drawable.ic_beer
            4 -> R.drawable.ic_fast_food_meal
            5 -> R.drawable.ic_popcorn
            6 -> R.drawable.ic_card_giftcard
            7 -> R.drawable.ic_food_croissant
            else -> R.drawable.ic_card_giftcard
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            ItemDonationOptionBinding.inflate(
                LayoutInflater.from(donationsDialog),
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val skuDetails = skuDetailsList[i]
        with(viewHolder.binding) {
            itemTitle.text = skuDetails.title.replace("Music Player - MP3 Player - Retro", "")
                .trim { it <= ' ' }
            itemText.text = skuDetails.description
            itemText.isVisible = false
            itemPrice.text = skuDetails.priceText
            itemImage.setImageResource(getIcon(i))
        }

        val purchased = donationsDialog.billingProcessor!!.isPurchased(skuDetails.productId)
        val titleTextColor = if (purchased) ATHUtil.resolveColor(
            donationsDialog,
            android.R.attr.textColorHint
        ) else donationsDialog.textColorPrimary()
        val contentTextColor =
            if (purchased) titleTextColor else donationsDialog.textColorSecondary()

        with(viewHolder.binding) {
            itemTitle.setTextColor(titleTextColor)
            itemText.setTextColor(contentTextColor)
            itemPrice.setTextColor(titleTextColor)
            strikeThrough(itemTitle, purchased)
            strikeThrough(itemText, purchased)
            strikeThrough(itemPrice, purchased)
        }

        viewHolder.itemView.setOnTouchListener { _, _ -> purchased }
        viewHolder.itemView.setOnClickListener { donationsDialog.donate(i) }
    }

    override fun getItemCount(): Int {
        return skuDetailsList.size
    }

    class ViewHolder(val binding: ItemDonationOptionBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private fun strikeThrough(textView: TextView, strikeThrough: Boolean) {
            textView.paintFlags =
                if (strikeThrough) textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                else textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
}
