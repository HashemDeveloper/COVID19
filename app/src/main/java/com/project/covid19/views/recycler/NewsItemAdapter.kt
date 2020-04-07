package com.project.covid19.views.recycler

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.project.covid19.R
import com.project.covid19.model.smartableai.COVIDNews
import com.project.covid19.model.smartableai.Images
import com.project.covid19.utils.glide.GlideApp

class NewsItemAdapter: RecyclerView.Adapter<BaseViewHolder<*>>() {
    private val newsData: MutableList<COVIDNews> = arrayListOf()
    private var isNightMode: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_news_item_layout, parent, false)
        return NewsItemHolder(view, parent.context, getIsNightMode())
    }

    override fun getItemCount(): Int {
        return this.newsData.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        ((holder as NewsItemHolder).bindView(this.newsData[position]))
    }

    fun setData(list: MutableList<COVIDNews>) {
        this.newsData.clear()
        this.newsData.addAll(list)
        notifyDataSetChanged()
    }
    fun setIsNightMode(isNightMode: Boolean) {
        this.isNightMode = isNightMode
    }
    private fun getIsNightMode(): Boolean {
        return this.isNightMode
    }

    inner class NewsItemHolder(private var view: View, private var context: Context, private var isNightMode: Boolean): BaseViewHolder<COVIDNews>(view) {
        private var providerNameView: MaterialTextView?= null
        private var newsImageViewView: AppCompatImageView?= null
        private var newsTitleView: MaterialTextView?= null
        private var newsExcerptView: MaterialTextView?= null
        private var holderCardView: MaterialCardView?= null

        init {
            this.holderCardView = this.view.findViewById(R.id.fragment_news_item_holder_view)
            this.providerNameView = this.view.findViewById(R.id.fragment_news_item_provider_view_id)
            this.newsImageViewView = this.view.findViewById(R.id.fragment_news_image_view_id)
            this.newsTitleView = this.view.findViewById(R.id.fragment_news_title_view_id)
            this.newsExcerptView = this.view.findViewById(R.id.fragment_news_excerpt_view_id)
            if (this.isNightMode) {
                this.holderCardView?.setBackgroundColor(ContextCompat.getColor(this.context, R.color.black))
                this.providerNameView?.setTextColor(ContextCompat.getColor(this.context, R.color.white))
                this.newsTitleView?.setTextColor(ContextCompat.getColor(this.context, R.color.white))
                this.newsExcerptView?.setTextColor(ContextCompat.getColor(this.context, R.color.blue_gray400))
            } else {
                this.holderCardView?.setBackgroundColor(ContextCompat.getColor(this.context, R.color.white))
                this.providerNameView?.setTextColor(ContextCompat.getColor(this.context, R.color.black))
                this.newsTitleView?.setTextColor(ContextCompat.getColor(this.context, R.color.black))
                this.newsExcerptView?.setTextColor(ContextCompat.getColor(this.context, R.color.dark_gray))
            }
        }
        override fun bindView(item: COVIDNews) {
            itemView.tag = item
            var provider: String?= ""
            val newsTitle: String = item.title
            val newsExcerpt: String = item.excerpt

            val imageList: List<Images>?= item.images
            var newsImage: String = ""
            var width: Int = 0
            var height: Int = 0
            imageList?.let {listOfImages ->
                for (images in listOfImages) {
                    images.url?.let { url ->
                        newsImage = url
                    }
                    images.width?.let { w ->
                        width = w
                    }
                    images.height?.let { h ->
                        height = h
                    }
                }
            }

            item.provider?.let {
                provider = it.name
            }

            this.providerNameView?.let { providerView ->
                providerView.text = provider
            }
            val circularProgressDrawable = CircularProgressDrawable(this.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.setColorSchemeColors(Color.GRAY)
            circularProgressDrawable.start()
            this.newsImageViewView?.let { imageView ->
                GlideApp.with(this.context).load(newsImage)
                    .placeholder(circularProgressDrawable)
                    .override(width!!, height!!)
                    .into(imageView)
            }
            this.newsTitleView?.let { titleView ->
                titleView.text = newsTitle
            }
            this.newsExcerptView?.let { excerptView ->
                excerptView.text = newsExcerpt
            }
        }
    }
}