package com.project.covid19.views.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.project.covid19.R
import com.project.covid19.views.recycler.items.DrawerHeaderItems
import com.project.covid19.views.recycler.items.DrawerNewsItems

class DrawerItemAdapter: RecyclerView.Adapter<BaseViewHolder<*>>() {
    private var data: MutableList<Any> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when(viewType) {
            NAV_HEADER -> {
                val headerView: View = LayoutInflater.from(parent.context).inflate(R.layout.drawer_header_item_layout, parent, false)
                DrawerHeaderViewHolder((headerView))
            }
            NAV_NEWS_ITEMS -> {
                val newsItemView: View = LayoutInflater.from(parent.context).inflate(R.layout.drawer_news_item_layout, parent, false)
                DrawerNewsItemHolder(newsItemView)
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
            is DrawerNewsItemHolder -> holder.bindView(items as DrawerNewsItems)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (this.data[position]) {
            is DrawerHeaderItems -> NAV_HEADER
            is DrawerNewsItems -> NAV_NEWS_ITEMS
            else -> throw IllegalArgumentException("Invalid index position $position")
        }
    }
    fun setData(dataList: MutableList<Any>) {
        this.data.clear()
        this.data.addAll(dataList)
        notifyDataSetChanged()
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
                        bgImageView.setImageResource(R.drawable.covid_message_image_medium)
                    }
                }
            }
        }
    }
    inner class DrawerNewsItemHolder(private var view: View): BaseViewHolder<DrawerNewsItems>(view) {
        private var newsItemTitleView: AppCompatTextView?= null
        init {
            this.newsItemTitleView = this.view.findViewById(R.id.drawer_news_item_title_view_id)
        }

        override fun bindView(item: DrawerNewsItems) {
            item.let {newsItems ->
                this.newsItemTitleView?.let { textView ->
                    textView.text = newsItems.itemName
                }
            }
        }
    }
    companion object {
        private const val NAV_HEADER: Int = 0
        private const val NAV_NEWS_ITEMS: Int = 1
        private const val NAV_FOOTER: Int = 2
    }
}