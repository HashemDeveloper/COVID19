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
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

class NewsItemAdapter (private var listener: NewsItemClickListener): RecyclerView.Adapter<BaseViewHolder<*>>() {
    private val newsData: MutableList<COVIDNews> = arrayListOf()
    private var isNightMode: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_news_item_layout, parent, false)
        val newsItemHolder: NewsItemHolder = NewsItemHolder(view, parent.context, getIsNightMode())
        newsItemHolder.getNewsItemHolder()?.let { holder ->
            holder.setOnClickListener {
                val newsItems: COVIDNews = newsItemHolder.itemView.tag as COVIDNews
                this.listener.onNewsItemClicked(newsItems)
            }
        }
        return newsItemHolder
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
            var newsExcerpt: String = item.excerpt
            if (newsExcerpt.length > 200) {
                newsExcerpt = newsExcerpt.substring(0, 200) + "..."
            }
            var publishedTime: String = item.publishedDateTime
            publishedTime = publishedTime.replace("T", " ")
            val time: String = getTime(publishedTime)
            val date: String = getDate(publishedTime)

            val imageList: List<Images>?= item.images
            var newsImage: String = ""

            imageList?.let {listOfImages ->
                for (images in listOfImages) {
                    images.url?.let { url ->
                        newsImage = url
                    }
                }
            }

            item.provider?.let {
                provider = it.name
            }
            val providerAndPublishTime = "$provider  $date $time"
            this.providerNameView?.let { providerView ->
                providerView.text = providerAndPublishTime
            }
            val circularProgressDrawable = CircularProgressDrawable(this.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.setColorSchemeColors(Color.GRAY)
            circularProgressDrawable.start()
            try {
                this.newsImageViewView?.let { imageView ->
                    GlideApp.with(this.view).load(newsImage)
                        .placeholder(circularProgressDrawable)
                        .into(imageView)
                }
            } catch (ex: Exception) {

            }

            this.newsTitleView?.let { titleView ->
                titleView.text = newsTitle
            }
            this.newsExcerptView?.let { excerptView ->
                excerptView.text = newsExcerpt
            }
        }
        fun getNewsItemHolder(): MaterialCardView? {
            return this.holderCardView
        }
    }
    private fun getTime(time: String): String {
        val date: Date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(time)!!
        return SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(date)
    }
    private fun getDate(time: String): String {
        val date: Date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(time)!!
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
    }

    private fun timeInMilli(time: String): Long {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault()
        )

        return OffsetDateTime.parse(time, formatter)
            .toInstant()
            .toEpochMilli()
    }

    interface NewsItemClickListener {
        fun onNewsItemClicked(newsItem: COVIDNews)
    }
}