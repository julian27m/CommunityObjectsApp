package com.example.community_objects.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TableRow
import com.example.community_objects.R
import com.example.community_objects.databinding.ProductDetailViewBinding
import com.example.community_objects.model.*

class ProductDetailView: FrameLayout {
    private lateinit var binding: ProductDetailViewBinding

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    private fun initView() {
        binding = ProductDetailViewBinding.bind(this)
    }

    fun setProduct(item: Any) {
        if (item is Item) {
            binding.item = item
        } else if (item is Supplies) {
            binding.item =
                Item(item.title, item.category, item.description, item.imageURL, item.user)
        } else if (item is Book) {
            binding.item =
                Item(item.title, item.category, item.description, item.imageURL, item.user)
        }
//        agregar a la tabla una fila con cada uno de los atributos del item segun el tipo de item
        if (item is Book) {
//            book has author and subject
            val authorRow = TableRow(context)
            val authorLabel = android.widget.TextView(context)
            authorLabel.text = context.getString(R.string.author)
            authorRow.addView(authorLabel)
            val authorValue = android.widget.TextView(context)
            authorValue.text = item.author
            authorRow.addView(authorValue)
            binding.tblAttrib.addView(authorRow)

            val subjectRow = TableRow(context)
            val subjectLabel = android.widget.TextView(context)
            subjectLabel.text = context.getString(R.string.subject)
            subjectRow.addView(subjectLabel)
            val subjectValue = android.widget.TextView(context)
            subjectValue.text = item.subject
            subjectRow.addView(subjectValue)
            binding.tblAttrib.addView(subjectRow)
        } else if (item is Supplies) {
            // supplies has reference
            val referenceRow = TableRow(context)
            val referenceLabel = android.widget.TextView(context)
            referenceLabel.text = context.getString(R.string.reference)
            referenceRow.addView(referenceLabel)
            val referenceValue = android.widget.TextView(context)
            referenceValue.text = item.reference
            referenceRow.addView(referenceValue)
            binding.tblAttrib.addView(referenceRow)
        } else if (item is Clothes){
// clothes has size and color
            val sizeRow = TableRow(context)
            val sizeLabel = android.widget.TextView(context)
            sizeLabel.text = context.getString(R.string.size)
            sizeRow.addView(sizeLabel)
            val sizeValue = android.widget.TextView(context)
            sizeValue.text = item.size
            sizeRow.addView(sizeValue)
            binding.tblAttrib.addView(sizeRow)

            val colorRow = TableRow(context)
            val colorLabel = android.widget.TextView(context)
            colorLabel.text = context.getString(R.string.colors)
            colorRow.addView(colorLabel)
            val colorValue = android.widget.TextView(context)
            colorValue.text = item.colors
            colorRow.addView(colorValue)
            binding.tblAttrib.addView(colorRow)
        } else if (item is EPP) {
//            epp has degree and type
            val degreeRow = TableRow(context)
            val degreeLabel = android.widget.TextView(context)
            degreeLabel.text = context.getString(R.string.degree)
            degreeRow.addView(degreeLabel)
            val degreeValue = android.widget.TextView(context)
            degreeValue.text = item.degree
            degreeRow.addView(degreeValue)
            binding.tblAttrib.addView(degreeRow)

            val typeRow = TableRow(context)
            val typeLabel = android.widget.TextView(context)
            typeLabel.text = context.getString(R.string.type)
            typeRow.addView(typeLabel)
            val typeValue = android.widget.TextView(context)
            typeValue.text = item.type
            typeRow.addView(typeValue)
            binding.tblAttrib.addView(typeRow)
        }
    }

}