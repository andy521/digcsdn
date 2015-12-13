package com.bob.digcsdn.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bob.digcsdn.R;
import com.bob.digcsdn.models.Blog;
import com.bob.digcsdn.utils.Constants;
import com.bob.digcsdn.utils.FileUtil;
import com.bob.digcsdn.utils.ImageLoading;
import com.bob.digcsdn.utils.MyTagHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 博客内容适配器
 * <p/>
 * Created by bob on 15-6-14.
 */
public class BlogDetailAdapter extends BaseAdapter {
    private ViewHolder holder;
    private LayoutInflater layoutInflater;
    private Context context;
    private List<Blog> list;

    private ImageLoading imageLoader;

    public BlogDetailAdapter(Context context) {
        super();
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        list = new ArrayList<>();
        imageLoader = ImageLoading.getInstance();
    }

    public void setList(List<Blog> list) {
        this.list = list;
    }

    public void addList(List<Blog> list) {
        this.list.addAll(list);
    }

    public void clearList() {
        this.list.clear();
    }

    public List<Blog> getList() {
        return list;
    }

    public void removeItem(int position) {
        if (list.size() > 0) {
            list.remove(position);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 这里的主要实现方式是：将blog分为多个部分，每个部分由不同的条目布局显示，也就相当于listview具有多种条目布局
     * 加载的时候根据对应
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {//获取条目布局及view对象
        Blog item = list.get(position);//在初始化holder的时候，需要使用到具体条目的类型
        if (null == convertView) {
            holder = new ViewHolder();
            switch (item.getState()) {
                case Constants.DEF_BLOG_ITEM_TYPE.TITLE:// 显示标题
                    convertView = layoutInflater.inflate(
                            R.layout.article_detail_title_item, null);
                    holder.content = (TextView) convertView.findViewById(R.id.text);
                    break;
                case Constants.DEF_BLOG_ITEM_TYPE.SUMMARY: // 摘要
                    convertView = layoutInflater.inflate(
                            R.layout.article_detail_summary_item, null);
                    holder.content = (TextView) convertView.findViewById(R.id.text);
                    break;
                case Constants.DEF_BLOG_ITEM_TYPE.CONTENT: // 内容
                    convertView = layoutInflater.inflate(
                            R.layout.article_detail_item, null);
                    holder.content = (TextView) convertView.findViewById(R.id.text);
                    break;
                case Constants.DEF_BLOG_ITEM_TYPE.IMG: // 图片
                    convertView = layoutInflater.inflate(
                            R.layout.article_detail_img_item, null);
                    holder.image = (ImageView) convertView
                            .findViewById(R.id.imageView);
                    break;
                case Constants.DEF_BLOG_ITEM_TYPE.BOLD_TITLE: // 加粗标题
                    convertView = layoutInflater.inflate(
                            R.layout.article_detail_bold_title_item, null);
                    holder.content = (TextView) convertView.findViewById(R.id.text);
                    break;
                case Constants.DEF_BLOG_ITEM_TYPE.CODE: // 代码
                    convertView = layoutInflater.inflate(
                            R.layout.article_detail_code_item, null);
                    holder.code = (WebView) convertView
                            .findViewById(R.id.code_view);

                    break;
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // System.out.println(item.getContent());

        if (null != item) {
            switch (item.getState()) {
                case Constants.DEF_BLOG_ITEM_TYPE.IMG: // 图片，异步加载
                    imageLoader.loadImage(item.getImgLink(), holder.image, R.drawable.ic_default, R.drawable.ic_default);
                    break;
                case Constants.DEF_BLOG_ITEM_TYPE.CODE: // 代码，格式显示

                    // 读取代码文件和模板文件
                    String code = item.getContent();//获取代码内容
              
                    String template = FileUtil.getFileContent(context, "code.html");
                    // 生成代码模板
                    String html = template.replace("{{code}}", code);//将代码内容替换进去
                    holder.code.getSettings().setDefaultTextEncodingName("utf-8");
                    holder.code.getSettings().setSupportZoom(true);
                    holder.code.getSettings().setBuiltInZoomControls(true);

                    // holder.code.loadUrl("file:///android_asset/code.html");

                    holder.code.loadDataWithBaseURL("file:///android_asset/", html,
                            "text/html", "utf-8", null);

                    break;
                default://普通文本,这点不太懂
                    holder.content.setText(Html.fromHtml(item.getContent(), null,
                            new MyTagHandler()));
                    break;
            }
        }
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 6;
    }

    @Override
    public int getItemViewType(int position) {
        switch (list.get(position).getState()) {
            case Constants.DEF_BLOG_ITEM_TYPE.TITLE:
                return 0;
            case Constants.DEF_BLOG_ITEM_TYPE.SUMMARY:
                return 1;
            case Constants.DEF_BLOG_ITEM_TYPE.CONTENT:
                return 2;
            case Constants.DEF_BLOG_ITEM_TYPE.IMG:
                return 3;
            case Constants.DEF_BLOG_ITEM_TYPE.BOLD_TITLE:
                return 4;
            case Constants.DEF_BLOG_ITEM_TYPE.CODE:
                return 5;
        }
        return 1;//默认返回概要
    }

    @Override
    public boolean isEnabled(int position) {
        switch (list.get(position).getState()) {
            case Constants.DEF_BLOG_ITEM_TYPE.IMG:
                return true;
            default:
                return false;
        }
    }

    private class ViewHolder {
        TextView id;//暂时无用，需要的话，可以看情况加上
        TextView date;
        TextView title;
        TextView content;
        ImageView image;
        WebView code;
    }
}
