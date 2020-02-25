package code.name.monkey.retromusic.dialogs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import code.name.monkey.appthemehelper.util.MaterialUtil
import code.name.monkey.retromusic.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_upi_payment_dialog.*

class UpiPaymentBottomSheetDialogFragment : BottomSheetDialogFragment() {
    companion object {
        const val TAG: String = "UpiPaymentBottomSheetDialogFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upi_payment_dialog, container, false)
    }

    private fun upi(name: String, amount: String, note: String) {
        val currentTime = System.currentTimeMillis() / 1000
        val transactionId = "${currentTime}UPI"
        val upi =
            "upi://pay?pa=retromusic@ybl&pn=$name&mc=0000&tid=$transactionId&tr=$transactionId&tn=$note&am=$amount&cu=INR&refUrl=refurl".replace(
                " ",
                "+"
            )
        val intent = Intent();
        intent.action = Intent.ACTION_VIEW;
        intent.data = Uri.parse(upi);
        val chooser = Intent.createChooser(intent, "Pay with...");
        startActivityForResult(chooser, 1, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MaterialUtil.setTint(submit)
        submit.setOnClickListener {
            val name = nameText.text.toString()
            if (TextUtils.isEmpty(name)) {
                nameContainer.error = "Name is empty"
                return@setOnClickListener
            } else {
                nameContainer.error = null
            }
            val amount = amountText.text.toString()
            if (TextUtils.isEmpty(amount)) {
                amountContainer.error = "Amount is empty"
                return@setOnClickListener
            } else {
                amountContainer.error = null
            }
            upi(name, amount, noteText.text.toString())
        }
    }
}