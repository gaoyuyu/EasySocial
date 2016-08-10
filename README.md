##EasySocial


基于Google Material Design设计开发的Android客户端，UI界面和功能模块参考暴走日报app，包括朋友圈（暴走日报app为新闻阅读），排行榜，视频，个人中心4个功能模块。

服务器接口采用ThinkPHP+mysql开发（快速，代码嘛，凑合看能用就行），初始阶段部署在新浪SAE（Memcache+Storage+mysql），后来新浪改变了收费策略直接在本地跑，Windows环境，容器用的wampserver。

SAE版：https://github.com/gaoyuyu/easysocialSAE
Local版（推荐）：https://github.com/gaoyuyu/EasySocial-Server/

学习Android至今，Android开发经验谈不上，毕竟实习Android过一段时间，在校学的Android毕业却从事了PHP，嘛，喜欢折腾。项目处于摸索阶段，有些地方处理得可能不是很规范。

###效果图

![images](https://github.com/gaoyuyu/EasySocial/raw/master/screenshots/all_in_one.jpg)




###模块分析

####朋友圈&排行榜

* SwipeRefreshLayout + RecyclerView 下拉刷新和下拉加载更多。
* 网络请求框架 OkHttp+Gson 最原始的用法没有进行封装代码量会稍微多了点。

####图片浏览

* 图片点击放大缩小效果：[chrisbanes/PhotoView
](https://github.com/chrisbanes/PhotoView)  Fresco版 [ongakuer/PhotoDraweeView
](https://github.com/ongakuer/PhotoDraweeView)

####视频

* ~~当初的想法是使用优酷的播放器播放优酷的视频，由于Demo一直没调通，就在[APIStore](http://apistore.baidu.com/apiworks/servicedetail/2279.html) 找的接口自己存到数据库里面，日后会改善（总感觉好Low！）。~~
* 新增[lipangit/JieCaoVideoPlayer](https://github.com/lipangit/JieCaoVideoPlayer)
* 视频真实URL不稳定，调整为`从网站下载视频存到服务器地址`，数据库存储视频地址和标题。
*  _视频播放涉及版权问题，此处为自用学习_


###开源项目说明

> **SystemBarTint**

* **Link:** https://github.com/jgilfelt/SystemBarTint

> **material-ripple**

* **Link:** https://github.com/balysv/material-ripple

> **drakeet/MaterialDialog**

* **Link:** https://github.com/drakeet/MaterialDialog

> **rengwuxian/MaterialEditText**

* **Link:** https://github.com/rengwuxian/MaterialEditText

> **Okhttp**

* **Link:** https://github.com/square/okhttp

> **PhotoView**

* **Link:** https://github.com/chrisbanes/PhotoView
* **Link:** https://github.com/ongakuer/PhotoDraweeView

> **pnikosis/materialish-progress**

* **Link:** https://github.com/pnikosis/materialish-progress


