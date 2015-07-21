package com.bob.digcsdn.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bob.digcsdn.activity.CommentsActivity;
import com.bob.digcsdn.bean.Blog;
import com.bob.digcsdn.bean.BlogItem;
import com.bob.digcsdn.bean.Comment;
import com.bob.digcsdn.interfaces.JsonCallBackListener;

/**
 *csdn的博客样式都是多页显示，所以就有了我们的Page类
 *Created by bob on 15-6-13.
 */
public class JsoupUtil {//jsoup解析html还需要好好研究研究
	public static boolean contentLastPage = true; // 最后一页
	private static final String BLOG_URL = "http://blog.csdn.net"; // CSDN博客地址

	// 链接样式文件，代码块高亮的处理,搞不懂，也就是根据字符串里的部分分析代码部分
	public final static String linkCss = "<script type=\"text/javascript\" src=\"file:///android_asset/shCore.js\"></script>"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/shBrushJScript.js\"></script>"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/shBrushJava.js\"></script>"
			+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shThemeDefault.css\">"
			+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shCore.css\">"
			+ "<script type=\"text/javascript\">SyntaxHighlighter.all();</script>";


	/**
	 * 使用Jsoup解析博客摘要视图列表
	 * @param blogType
	 * @param html
	 * @return
	 */
	public static void getBlogItemList(final int blogType, final String html, final JsonCallBackListener listener) {

		new Thread(){
			@Override
			public void run() {
				List<BlogItem> list = new ArrayList<>();
				// 将html字符串解析为Document对象
				Document doc = Jsoup.parse(html);
				// Log.e("doc--->", doc.toString());
				// 获取class="article_item"的所有节点元素，也就是视图列表的一个条目
				Elements blogList = doc.getElementsByClass("article_item");
				// Log.e("elements--->", blogList.toString());

				for (Element blogItem : blogList) {//一一遍历每一个article_item，从而分析出每一个item的信息，得到一个个的BlogItem
					BlogItem item = new BlogItem();
					String title = blogItem.select("h1").text(); //得到<h1></h1>节点里的内容，也就是当前文章标题，这里类似于xml的pull解析
					// System.out.println("title----->" + title);
					String description = blogItem.select("div.article_description")
							.text();//抓取class属性是article_description的div的内容
					// System.out.println("descrition--->" + description);
					String msg = blogItem.select("div.article_manage").text();

					String date = blogItem.getElementsByClass("article_manage").get(0)
							.text();//获取article_manage下的第一个元素内容，即就是日期link_postdate
					// System.out.println("date--->" + date);
					String link = BLOG_URL//h1下a节点的href属性值
							+ blogItem.select("h1").select("a").attr("href");
					// System.out.println("link--->" + link);
					item.setTitle(title);
					item.setMsg(msg);
					item.setContent(description);
					item.setDate(date);
					item.setLink(link);
					item.setBlogType(blogType);

					// 没有图片,那还要他有何用？
					item.setImgLink(null);
					list.add(item);
				}

				listener.onFinish(list);//接口回调，送出数据
			}
		}.start();
	}

	/**
	 * 扒取传入url地址的博客详细内容
	 *
	 * @param str
	 * @return
	 */
	public static List<Blog> getContent( String str) {//对一篇文章的分析，这个list里的Blog代表博文的各个组成部分，详情请见Blog实体类中
		List<Blog> list = new ArrayList<>();

		// 获取文档内容
		Document doc = Jsoup.parse(str);

		// 获取class="details"的元素
		Element detail = doc.getElementsByClass("details").get(0);
		detail.select("script").remove(); // 删除每个匹配元素的DOM。

		// 获取标题
		Element title = detail.getElementsByClass("article_title").get(0);
		Blog blogTitle = new Blog();
		blogTitle.setState(Constants.DEF_BLOG_ITEM_TYPE.TITLE); // 设置状态
		blogTitle.setContent(ToDBC(title.text())); // 设置标题内容

		// 获取文章内容
		Element content = detail.select("div.article_content").get(0);

		// 获取所有标签为<a的元素
		Elements as = detail.getElementsByTag("a");
		for (int b = 0; b < as.size(); b++) {
			Element blockquote = as.get(b);
			// 改变这个元素的标记。例如,<span>转换为<div> 如el.tagName("div");。
			blockquote.tagName("bold"); // 转为粗体
		}

		Elements ss = detail.getElementsByTag("strong");
		for (int b = 0; b < ss.size(); b++) {
			Element blockquote = ss.get(b);
			blockquote.tagName("bold");
		}

		// 获取所有标签为<p的元素
		Elements ps = detail.getElementsByTag("p");
		for (int b = 0; b < ps.size(); b++) {
			Element blockquote = ps.get(b);
			blockquote.tagName("body");
		}

		// 获取所有引用元素
		Elements blockquotes = detail.getElementsByTag("blockquote");
		for (int b = 0; b < blockquotes.size(); b++) {
			Element blockquote = blockquotes.get(b);
			blockquote.tagName("body");
		}

		// 获取所有标签为<ul的元素
		Elements uls = detail.getElementsByTag("ul");
		for (int b = 0; b < uls.size(); b++) {
			Element blockquote = uls.get(b);
			blockquote.tagName("body");
		}

		// 找出粗体
		Elements bs = detail.getElementsByTag("b");
		for (int b = 0; b < bs.size(); b++) {
			Element bold = bs.get(b);
			bold.tagName("bold");
		}

		// 遍历博客内容中的所有元素
		for (int j = 0; j < content.children().size(); j++) {
			Element c = content.child(j); // 获取每个元素

			// 抽取出图片
			if (c.select("img").size() > 0) {
				Elements imgs = c.getElementsByTag("img");
				System.out.println("img");
				for (Element img : imgs) {
					if (!img.attr("src").equals("")) {
						Blog blogImgs = new Blog();
						// 大图链接
						if (!img.parent().attr("href").equals("")) {
							blogImgs.setImgLink(img.parent().attr("href"));
							System.out.println("href="
									+ img.parent().attr("href"));
							if (img.parent().parent().tagName().equals("p")) {
								// img.parent().parent().remove();
							}
							img.parent().remove();
						}
						blogImgs.setContent(img.attr("src"));
						blogImgs.setImgLink(img.attr("src"));
						System.out.println(blogImgs.getContent());
						blogImgs.setState(Constants.DEF_BLOG_ITEM_TYPE.IMG);
						list.add(blogImgs);
					}
				}
			}
			c.select("img").remove();

			// 获取博客内容
			Blog blogContent = new Blog();
			blogContent.setState(Constants.DEF_BLOG_ITEM_TYPE.CONTENT);

			if (c.text().equals("")) {
				continue;
			} else if (c.children().size() == 1) {
				if (c.child(0).tagName().equals("bold")
						|| c.child(0).tagName().equals("span")) {
					if (c.ownText().equals("")) {
						// 小标题，咖啡色
						blogContent
								.setState(Constants.DEF_BLOG_ITEM_TYPE.BOLD_TITLE);
					}
				}
			}

			// 代码
			if (c.select("pre").attr("name").equals("code")) {
				blogContent.setState(Constants.DEF_BLOG_ITEM_TYPE.CODE);
				blogContent.setContent(ToDBC(c.outerHtml()));
			} else {
				blogContent.setContent(ToDBC(c.outerHtml()));
			}
			list.add(blogContent);
		}

		return list;
	}


	/**
	 * 获取博文评论列表
	 * 
	 * @param json
	 *            html请求返回的是json字符串.......总算是要复习到json数据的解析了
	 * @return
	 */
	public static List<Comment> getBlogCommentList(String json, int pageIndex,
			int pageSize) {
		List<Comment> list = new ArrayList<>();
		LogUtil.i("jsonString", json);
		try {
			// 创建一个JSONObject对象
			JSONObject jsonObject= new JSONObject(json);
			LogUtil.i("jsonObject", jsonObject.toString());
			JSONArray jsonArray = jsonObject.getJSONArray("list"); // 获取list头的json数组
			LogUtil.i("jsonArray", jsonArray.toString());
			int index = 0;
			int len = jsonArray.length();
			CommentsActivity.commentCount = String.valueOf(len); // 评论条数
			// 如果评论数大于20,假设有25条数据，应该是第三页，那么就应该index== 40
			if (len > 20) {//设置起始下标
				index = (pageIndex * pageSize) - 20;//pageIndex 表示当前页数，pageSize表示每一页的大小
			}

			if (len < pageSize && pageIndex > 1) {//不是第一页，并且长度小于20
				return list;
			}

			if ((pageIndex * pageSize) < len) {//如果长度大于当前加载的所有最大数据量,就将长度设为最大数据量
				len = pageIndex * pageSize;
			}

			for (int i = len-1; i >= index; i--) {//遍历JsonObject里的{}obj对象，倒序可以让显示的时候正序

				JSONObject item = jsonArray.getJSONObject(i);
				String commentId = item.getString("CommentId");//评论id
				String content = item.getString("Content");//评论内容
				String username = item.getString("UserName");//用户名称
				String parentId = item.getString("ParentId");//父节点id
				String postTime = item.getString("PostTime");//提交时间
				String userFace = item.getString("Userface");//用户头像（子评论里不显示头像）

				Comment comment = new Comment();
				comment.setCommentId(commentId);
				comment.setContent(content);
				comment.setUsername(username);
				comment.setParentId(parentId);
				comment.setPostTime(postTime);
				comment.setUserface(userFace);

				if (parentId.equals("0")) {
					// 如果parentId为0的话，表示它是评论的topic
					comment.setType(Constants.DEF_COMMENT_TYPE.PARENT);
				} else {
					comment.setType(Constants.DEF_COMMENT_TYPE.CHILD);
				}
				list.add(comment);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * 获得博主个人资料
	 * 
	 * @param str
	 * @return

	public static Blogger getBloggerInfo(String str) {

		
		if (str == null) {
			return null;
		}
		// 获取文档内容
		Document doc = Jsoup.parse(str);

		Element profile = doc.getElementsByClass("panel").get(0);

		Element profileBody = profile.select("ul.panel_body.profile").get(0);

		Element userface = profileBody.getElementById("blog_userface");
		String userfaceLink = userface.select("a").select("img").attr("src"); // 得到头像链接
		String username = userface.getElementsByTag("a").get(1).text(); // 用户名

		Element blog_rank = profileBody.getElementById("blog_rank");
		Element blog_statistics = profileBody.getElementById("blog_statistics");

		Elements rankLi = blog_rank.select("li");
		StringBuilder sb = new StringBuilder();
		String rankStr = "";
		for (Element rank : rankLi) {
			sb.append(rank.text()).append("|");
		}
		rankStr = sb.toString();

		String statistics = "";
		StringBuilder sb2 = new StringBuilder();
		Elements blogLi = blog_statistics.select("li");
		for (Element info : blogLi) {
			sb2.append(info.text()).append("|");
		}
		statistics = sb2.toString();

		Blogger blogger = new Blogger();
		blogger.setUserface(userfaceLink);
		blogger.setUsername(username);
		blogger.setRank(rankStr);
		blogger.setStatistics(statistics);

		return blogger;
	}*/

	/**
	 * 半角转换为全角 全角---指一个字符占用两个标准字符位置。 半角---指一字符占用一个标准的字符位置。
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

}
