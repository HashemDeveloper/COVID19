package com.project.covid19.views.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.project.covid19.R
import com.project.covid19.views.recycler.items.DrawerHeaderItems
import com.project.covid19.views.recycler.items.DrawerNewsItemHeader
import com.project.covid19.views.recycler.items.DrawerNewsItems

class DrawerItemAdapter constructor(private var context: Context, private var listenerNews: OnNewsItemClickListener): RecyclerView.Adapter<BaseViewHolder<*>>() {
    private var data: MutableList<Any> = arrayListOf()
    private var isNightMode: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when(viewType) {
            NAV_HEADER -> {
                val headerView: View = LayoutInflater.from(parent.context).inflate(R.layout.drawer_header_item_layout, parent, false)
                DrawerHeaderViewHolder((headerView))
            }
            NAV_NEWS_ITEM_HEADER -> {
                val newsItemHeaderView: View = LayoutInflater.from(parent.context).inflate(R.layout.drawer_news_item_header_layout, parent, false)
                DrawerNewsItemHeaderHolder(newsItemHeaderView, this.context,  getIsNightMode())
            }
            NAV_NEWS_ITEMS -> {
                val newsItemsView: View = LayoutInflater.from(parent.context).inflate(R.layout.drawer_news_items_layout, parent, false)
                val newsItemsHolder: DrawerNewsItemsHolder = DrawerNewsItemsHolder(newsItemsView, this.context, getIsNightMode())
                newsItemsHolder.getNewsItemTitleView()?.let { textView ->
                    textView.setOnClickListener {
                        val drawerNewsItems: DrawerNewsItems = newsItemsHolder.itemView.tag as DrawerNewsItems
                        this.listenerNews.onItemClicked(drawerNewsItems)
                    }
                }
                newsItemsHolder
            }
            else -> throw IllegalArgumentException("Unsupported view")
        }
    }

    override fun getItemCount(): Int {
       return this.data.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val items: Any = this.data[position]
        when (holder) {
            is DrawerHeaderViewHolder -> holder.bindView(items as DrawerHeaderItems)
            is DrawerNewsItemHeaderHolder -> holder.bindView(items as DrawerNewsItemHeader)
            is DrawerNewsItemsHolder -> holder.bindView(items as DrawerNewsItems)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (this.data[position]) {
            is DrawerHeaderItems -> NAV_HEADER
            is DrawerNewsItemHeader -> NAV_NEWS_ITEM_HEADER
            is DrawerNewsItems -> NAV_NEWS_ITEMS
            else -> throw IllegalArgumentException("Invalid index position $position")
        }
    }
    fun setData(dataList: MutableList<Any>) {
        this.data.clear()
        this.data.addAll(dataList)
        notifyDataSetChanged()
    }
    fun setIsNightMode(isNightMode: Boolean) {
        this.isNightMode = isNightMode
    }

    private fun getIsNightMode(): Boolean {
        return this.isNightMode
    }

    inner class DrawerHeaderViewHolder(private var view: View): BaseViewHolder<DrawerHeaderItems>(view) {
        private var headerBgImage: AppCompatImageView?= null
        init {
            this.headerBgImage = this.view.findViewById(R.id.drawer_header_bg_image_view_id)
        }
        override fun bindView(item: DrawerHeaderItems) {
            item.let {
                this.headerBgImage?.let { bgImageView ->
                    if (it.backgroundImage.isNotEmpty()) {
                        //Display image of top headings
                    } else {
                        try {
                            bgImageView.setImageResource(R.drawable.covid19_background_image)
                        } catch (ex: Exception) {

                        }
                    }
                }
            }
        }
    }
    inner class DrawerNewsItemHeaderHolder(private var view: View, private var context: Context, private var isNightMode: Boolean): BaseViewHolder<DrawerNewsItemHeader>(view) {
        private var newsItemHeaderTitle: AppCompatTextView?= null
        init {
            this.newsItemHeaderTitle = this.view.findViewById(R.id.drawer_news_item_header_title_view_id)
            if (this.isNightMode) {
                this.newsItemHeaderTitle?.setTextColor(ContextCompat.getColor(this.context, R.color.white))
            } else {
                this.newsItemHeaderTitle?.setTextColor(ContextCompat.getColor(this.context, R.color.blue_gray400))
            }
        }

        override fun bindView(item: DrawerNewsItemHeader) {
            item.let {newsItems ->
                this.newsItemHeaderTitle?.let { textView ->
                    textView.text = newsItems.itemName
                }
            }
        }
    }
    inner class DrawerNewsItemsHolder(private var view: View, private var context: Context, private var isNightMode: Boolean): BaseViewHolder<DrawerNewsItems>(view) {
        private var newsItemTitleView: MaterialTextView?= null

        init {
            this.newsItemTitleView = this.view.findViewById(R.id.drawer_news_items_view_id)
            if (this.isNightMode) {
                this.newsItemTitleView?.setTextColor(ContextCompat.getColor(this.context, R.color.white))
            } else {
                this.newsItemTitleView?.setTextColor(ContextCompat.getColor(this.context, R.color.blue_gray400))
            }
        }
        override fun bindView(item: DrawerNewsItems) {
            itemView.tag = item
            item.let { items ->
                this.newsItemTitleView?.let { textView ->
                    textView.text = items.newsItemName
                }
            }
        }
        fun getNewsItemTitleView(): MaterialTextView? {
            return this.newsItemTitleView
        }
    }
    interface OnNewsItemClickListener {
        fun <T> onItemClicked(item: T)
    }

    companion object {
        private const val NAV_HEADER: Int = 0
        private const val NAV_NEWS_ITEM_HEADER = 1
        private const val NAV_NEWS_ITEMS: Int = 2
        private const val NAV_FOOTER: Int = 3
    }
}