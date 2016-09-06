# EasySocial Dev Log

EasySocial

![image](https://github.com/gaoyuyu/EasySocial/raw/master/screenshots/splash.png)

Learn Here：[How to Build Project](http://www.jianshu.com/p/d9e4ddd1c530)


###2016.2.18
Material Design UI初始化
> DrawerLayout+NavigationView,CoordinatorLayout+AppBarLayout+Toolbar+TabLayout,
FloatingActionButton<br>
> issue：NavigationView设置Menu后不显示title<br>
> solution：NavigationView设置app:itemTextColor属性<br>

![image](https://github.com/gaoyuyu/EasySocial/raw/master/screenshots/20160218.png)

###2016.2.19
UI：`多个Fragment多个ToolBar`，更改TabLayout色调

```Java
<LinearLayout>

    <DrawerLayout>

		<CoordinatorLayout>

			<AppBarLayout>
				<Toolbar />
				<TabLayout />
			</AppBarLayout>

			<ViewPager />
			<FloatingActionButton />

		</CoordinatorLayout>

		<NavigationView />
	</DrawerLayout>
</LinearLayout>
```

将`<CoordinatorLayout />`，也就是app_bar_main.xml抽离出来，由MainFragment来填充。
切换Fragment的不再是简单的replace(),之后commit(),而是hide(),show(),性能更好。
当然，在初始化MainFragment的时候是用了replace。

> 参考blog：<br>
> [仿知乎程序（二）fragment的切换以及toolbar在不同页面下显示的menu不同](http://blog.csdn.net/chenguang79/article/details/49486723)<br>
> [Android Fragment 你应该知道的一切](http://blog.csdn.net/lmj623565791/article/details/42628537)<br>

```Java

    //记录当前正在使用的fragment
    private Fragment isFragment;
    ....

    /**
     * 为页面加载初始状态的fragment
     */
    public void initFragment(Bundle savedInstanceState)
    {
        //判断activity是否重建，如果不是，则不需要重新建立fragment.
        if(savedInstanceState==null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if(iFragment==null) {
                iFragment = new IndexFragment();
            }
            isFragment = iFragment;
            ft.replace(R.id.frame_main, iFragment).commit();
        }
    }

    /**
     * 当fragment进行切换时，采用隐藏与显示的方法加载fragment以防止数据的重复加载
     * @param from
     * @param to
     */
    public void switchContent(Fragment from, Fragment to) {
        if (isFragment != to) {
            isFragment = to;
            FragmentManager fm = getSupportFragmentManager();
            //添加渐隐渐现的动画
            FragmentTransaction ft = fm.beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                ft.hide(from).add(R.id.frame_main, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                ft.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }
```

###2016.3.1

 - Ripple：[material-ripple](https://github.com/balysv/material-ripple)
 - MaterialDialog：[drakeet/MaterialDialog](https://github.com/drakeet/MaterialDialog)
 - MaterialEditText：[rengwuxian/MaterialEditText](https://github.com/rengwuxian/MaterialEditText)

Basic Usage：
```Java
dependencies {
    //other dependencies
    compile 'com.balysv:material-ripple:1.0.2'
    compile 'me.drakeet.materialdialog:library:1.2.8'
    compile 'com.rengwuxian.materialedittext:library:2.1.4'
}
```
```Java
final MaterialDialog mMaterialDialog = new MaterialDialog(MainActivity.this);
                mMaterialDialog.setTitle("MaterialDialog")
                        .setMessage("Hello world!")
                        .setPositiveButton("OK", new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                mMaterialDialog.dismiss();
                            }
                        })
                        .setNegativeButton("CANCEL", new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                mMaterialDialog.dismiss();
                            }
                        });

                mMaterialDialog.show();
                Button neButton = mMaterialDialog.getNegativeButton();
                Button poButton = mMaterialDialog.getPositiveButton();
                neButton.setTextColor(getResources().getColor(R.color.colorAccent));
                poButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
```
###2016.3.2
在Fragment中的toolbar中设置返回键
```Java
    public void initToolbar()
    {
        loginToolbar.setTitle(R.string.login);
        activity.setSupportActionBar(loginToolbar);
        //设置返回键可用
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
```
设置后toolbar左上角会出现“返回”箭头，对toolbar的返回键的监听需要在其父Activity中重写`onOptionsItemSelected(MenuItem item)`
```Java

    in Activity

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            Toast.makeText(MainActivity.this,"activity",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
```

> 关于v4包Fragment和非v4包Fragment使用切换动画:[Android Activity和Fragment的转场动画](http://www.cnblogs.com/mengdd/p/3494041.html)

###2016.3.10
一、toolbar中设置SearchView（仿微信点击Search按钮跳转到SearchActivity）
1、MainActivity中设置menu
```Java
menu.xml
<menu xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      xmlns:tools="http://schemas.android.com/tools"
      tools:context=".MainActivity">
    <item
        android:id="@+id/ic_search"
        android:icon="@mipmap/ic_search"
        android:title="@string/search"
        app:showAsAction="ifRoom"/>
    <item
        android:id="@+id/ic_refresh"
        android:icon="@mipmap/ic_refresh"
        android:orderInCategory="80"
        android:title="@string/refresh"
        app:showAsAction="ifRoom"
        />
</menu>

MainActivity.java
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
```
2、SearchActivity设置menu，然后设置SearchView默认展开
```Java
search_menu.xml
<menu xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
     >
    <item android:id="@+id/ic_search_view"
          android:title="@string/search"
          android:icon="@mipmap/ic_search"
          android:inputType="textCapWords"
          app:showAsAction="collapseActionView|ifRoom"
          app:actionViewClass="android.support.v7.widget.SearchView"
        />
</menu>

SearchActivity.java
public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.ic_search_view);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        //这里需要注意的是使用MenuItem来调用collapseActionView()和expandActionView()
        //若使用searchView调用setIconifiedByDefault(boolean),setIconified(boolean),onActionViewExpanded()不起作用
        menuItem.collapseActionView();
        menuItem.expandActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                Toast.makeText(SearchActivity.this,query,Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                Toast.makeText(SearchActivity.this,newText,Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
```
二、在Fragment重写onCreateOptionsMenu和onOptionsItemSelected，需要在onCreateView中调用setHasOptionsMenu(true)才能事其生效。
```Java
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);
        return rootView;
    }
```
三、SearchView展开之后对其返回键的监听，不能监听android.R.id.home，是无效的
```Java
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.ic_search_view);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        menuItem.collapseActionView();
        menuItem.expandActionView();
        //监听SearchView的输入内容
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                Toast.makeText(SearchActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                Toast.makeText(SearchActivity.this, newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //监听SearchView的打开和关闭状态，不能
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener()
        {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item)
            {
                Toast.makeText(SearchActivity.this, "onMenuItemActionExpand", Toast.LENGTH_SHORT).show();
                //true SearchView打开
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item)
            {
                Toast.makeText(SearchActivity.this, "onMenuItemActionCollapse", Toast.LENGTH_SHORT).show();
                //true SearchView关闭
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
```
四、解决Plugin is too old,please update to a more recent version,or set ANDROID_DAILY_OVERRIDE..

> [jcenter gradle](https://jcenter.bintray.com/com/android/tools/build/gradle/) 查看最新版本的gradle，修改project下的build.gradle的classpath
目前最新2.0.0-beta2
```Java
    dependencies {
        classpath 'com.android.tools.build:gradle:2.0.0-beta2'
    }
```

###2016.3.11
添加okHttp库
 - [okhttp](https://github.com/square/okhttp)
 - [okhttp Wiki](https://github.com/square/okhttp/wiki)
```Java
 --demo
                Request request = new Request.Builder()
                        .url(Global.HOST_URL + "User/login")
                        .build();
                client.newCall(request).enqueue(new Callback()
                {
                    @Override
                    public void onFailure(Call call, IOException e)
                    {
                        Log.i(Global.TAG,"onFailure");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException
                    {
                        Log.i(Global.TAG,"onResponse");
                        if (!response.isSuccessful())
                        {
                            Log.i(Global.TAG,"Unexpected");
                            throw new IOException("Unexpected code " + response);
                        }
                        Headers responseHeaders = response.headers();
                        for (int i = 0; i < responseHeaders.size(); i++)
                        {
                            Log.i(Global.TAG,responseHeaders.name(i) + ": " + responseHeaders.value(i));
                        }

                        Log.i(Global.TAG,response.body().string());
                    }
                });
```
###2016.3.23
> Activity最顶层布局必须`android:fitsSystemWindows="true"`，之后再设置SystemBarTintManager

###2016.4.11

PopupWindow在实际的点击位置显示<br>
资料：[android之View坐标系（view获取自身坐标的方法和点击事件中坐标的获取）](http://blog.csdn.net/jason0539/article/details/42743531)

```Java
step1：setOnTouchListener and  setOnItemClickListener
        commentAdapter.setOnTouchListener(this);
        commentAdapter.setOnItemClickListener(this);

step2：getRawX() and getRawY()
    @Override
    public void onTouch(View v, MotionEvent event)
    {
        rawX = (int) event.getRawX();
        rawY = (int) event.getRawY();
    }

step3：show popuwindow
    showPopupWindow(view, position, rawX, rawY);
```
###2016.4.26
ShareSDK assets Location<br>
![image](https://github.com/gaoyuyu/EasySocial/raw/master/screenshots/assets.png)
###2016.8.4
- 滑动时颜色渐变ViewPager

> blog：http://www.jianshu.com/p/c8ac4ed18896

```Java
private SystemBarTintManager tintManager;
private int[] colors = {R.color.indigo_colorPrimary, 	  R.color.purple_colorPrimaryDark, R.color.blue_colorPrimaryDark};
public ArgbEvaluator argbEvaluator = new ArgbEvaluator();
...
tintManager = new SystemBarTintManager(this);
tintManager.setStatusBarTintResource(colors[0]);
tintManager.setStatusBarTintEnabled(true);
guideViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
{
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
        int nextPosition = position + 1;
        int color = (Integer) argbEvaluator.evaluate(positionOffset, getPageColor(position, colors),
                getPageColor(nextPosition, colors));
        guideViewpager.setBackgroundColor(color);
        tintManager.setStatusBarTintColor(color);
    }

    @Override
    public void onPageSelected(int position)
    {

    }

    @Override
    public void onPageScrollStateChanged(int state)
    {

    }
});

private int getPageColor(int position, int[] color)
{
    if (position > (color.length - 1))
    {
        return Color.TRANSPARENT;
    }
    return getResources().getColor(color[position]);

}
```
- Fresco 设置

> 多图请求及图片复用：http://www.fresco-cn.org/docs/requesting-multiple-images.html<br>
> 缩放和旋转图片：http://www.fresco-cn.org/docs/resizing-rotating.html<br>
> 图片请求(Image Requests)：http://www.fresco-cn.org/docs/image-requests.html<br>
> setLowestPermittedRequestLevel允许设置一个最低请求级别，请求级别和上面对应地有以下几个取值:<br>
> BITMAP_MEMORY_CACHE<br>
> ENCODED_MEMORY_CACHE<br>
> DISK_CACHE<br>
> FULL_FETCH<br>
> 如果你需要立即取到一个图片，或者在相对比较短时间内取到图片，否则就不显示的情况下，这非常有用。<br>

```Java
ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder()
        .setUseLastFrameForPreview(true)
        .build();
ImageRequest request = ImageRequestBuilder
        .newBuilderWithSource(picUri)
        .setImageDecodeOptions(decodeOptions)
        .setAutoRotateEnabled(true)
        .setLocalThumbnailPreviewsEnabled(true)
        .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.DISK_CACHE)
        .setProgressiveRenderingEnabled(false)
        .setResizeOptions(new ResizeOptions(800, 600))
        .build();

DraweeController controller = Fresco.newDraweeControllerBuilder()
        .setImageRequest(request)
        .setOldController(img.getController())
        .build();
```
###2016.8.10
- 修改gradle版本2.1.0
- 增加视频播放器lib：https://github.com/lipangit/JieCaoVideoPlayer

1、添加jcvideoplayer-lib：Project->Open Module Settings-> + ->select library dir<br>
2、build.gradle<br>
```Java
         //If you can not start app, you cancel the annotation
         //如果app无法启动,请取消下面的注释
         //Other ABIs: optional
         //    compile 'tv.danmaku.ijk.media:ijkplayer-armv5:0.6.0'
         compile 'tv.danmaku.ijk.media:ijkplayer-arm64:0.6.0'
         compile 'tv.danmaku.ijk.media:ijkplayer-x86:0.6.0'
         compile 'tv.danmaku.ijk.media:ijkplayer-x86_64:0.6.0'
         //ExoPlayer as IMediaPlayer: optional, experimental
         //compile 'tv.danmaku.ijk.media:ijkplayer-exo:0.6.0'
```
 3、AndroidManifest.xml：<br>
```Java
         <uses-sdk tools:overrideLibrary="com.example.ijkplayer_x86_64,tv.danmaku.ijk.media.player_arm64"/>
```
###2016.9.1
在activity_personal.xml中UI不能显示，同时报错NoSuchMethodError
```Java
java.lang.NoSuchMethodError: android.support.v4.graphics.drawable.DrawableCompat.setLayoutDirection(Landroid/graphics/drawable/Drawable;I)V
	at android.support.design.widget.CollapsingToolbarLayout.setStatusBarScrim(CollapsingToolbarLayout.java:663)
	at android.support.design.widget.CollapsingToolbarLayout.<init>(CollapsingToolbarLayout.java:197)
	at android.support.design.widget.CollapsingToolbarLayout.<init>(CollapsingToolbarLayout.java:132)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
	at java.lang.reflect.Constructor.newInstance(Constructor.java:408)
	at org.jetbrains.android.uipreview.ViewLoader.createNewInstance(ViewLoader.java:465)
	at org.jetbrains.android.uipreview.ViewLoader.loadClass(ViewLoader.java:172)
	at org.jetbrains.android.uipreview.ViewLoader.loadView(ViewLoader.java:105)
	at com.android.tools.idea.rendering.LayoutlibCallbackImpl.loadView(LayoutlibCallbackImpl.java:176)
	at android.view.BridgeInflater.loadCustomView(BridgeInflater.java:247)
	at android.view.BridgeInflater.createViewFromTag(BridgeInflater.java:171)
	at android.view.LayoutInflater.createViewFromTag(LayoutInflater.java:727)
	at android.view.LayoutInflater.rInflate_Original(LayoutInflater.java:858)
	at android.view.LayoutInflater_Delegate.rInflate(LayoutInflater_Delegate.java:70)
	at android.view.LayoutInflater.rInflate(LayoutInflater.java:834)
	at android.view.LayoutInflater.rInflateChildren(LayoutInflater.java:821)
	at android.view.LayoutInflater.rInflate_Original(LayoutInflater.java:861)
	at android.view.LayoutInflater_Delegate.rInflate(LayoutInflater_Delegate.java:70)
	at android.view.LayoutInflater.rInflate(LayoutInflater.java:834)
	at android.view.LayoutInflater.rInflateChildren(LayoutInflater.java:821)
	at android.view.LayoutInflater.inflate(LayoutInflater.java:518)
	at android.view.LayoutInflater.inflate(LayoutInflater.java:397)
```
运行app时
```Java
java.lang.RuntimeException: Unable to start activity ComponentInfo{com.gaoyy.easysocial/com.gaoyy.easysocial.ui.PersonalActivity}:
android.view.InflateException: Binary XML file line #16: Error inflating class android.support.design.widget.CollapsingToolbarLayout
    at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2195)
    at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:2245)
    at android.app.ActivityThread.access$800(ActivityThread.java:135)
    at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1196)
    at android.os.Handler.dispatchMessage(Handler.java:102)
    at android.os.Looper.loop(Looper.java:136)
    at android.app.ActivityThread.main(ActivityThread.java:5017)
    at java.lang.reflect.Method.invokeNative(Native Method)
    at java.lang.reflect.Method.invoke(Method.java:515)
    at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:779)
    at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:595)
    at dalvik.system.NativeStart.main(Native Method)
Caused by: android.view.InflateException: Binary XML file line #16: Error inflating class android.support.design.widget.CollapsingToolbarLayout
    at android.view.LayoutInflater.createView(LayoutInflater.java:621)
    at android.view.LayoutInflater.createViewFromTag(LayoutInflater.java:697)
    at android.view.LayoutInflater.rInflate(LayoutInflater.java:756)
    at android.view.LayoutInflater.rInflate(LayoutInflater.java:759)
    at android.view.LayoutInflater.inflate(LayoutInflater.java:492)
    at android.view.LayoutInflater.inflate(LayoutInflater.java:397)
    at android.view.LayoutInflater.inflate(LayoutInflater.java:353)
    at android.support.v7.app.AppCompatDelegateImplV7.setContentView(AppCompatDelegateImplV7.java:280)
    at android.support.v7.app.AppCompatActivity.setContentView(AppCompatActivity.java:140)
    at com.gaoyy.easysocial.ui.PersonalActivity.initContentView(PersonalActivity.java:74)
    at com.gaoyy.easysocial.base.BaseActivity.onCreate(BaseActivity.java:21)
    at android.app.Activity.performCreate(Activity.java:5231)
    at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1087)
    at com.morgoo.droidplugin.hook.handle.PluginInstrumentation.callActivityOnCreate(PluginInstrumentation.java:110)
    at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2159)
    at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:2245) 
    at android.app.ActivityThread.access$800(ActivityThread.java:135) 
    at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1196) 
    at android.os.Handler.dispatchMessage(Handler.java:102) 
    at android.os.Looper.loop(Looper.java:136) 
    at android.app.ActivityThread.main(ActivityThread.java:5017) 
    at java.lang.reflect.Method.invokeNative(Native Method) 
    at java.lang.reflect.Method.invoke(Method.java:515) 
    at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:779) 
    at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:595) 
    at dalvik.system.NativeStart.main(Native Method) 
Caused by: java.lang.reflect.InvocationTargetException
    at java.lang.reflect.Constructor.constructNative(Native Method)
    at java.lang.reflect.Constructor.newInstance(Constructor.java:423)
    at android.view.LayoutInflater.createView(LayoutInflater.java:595)
    at android.view.LayoutInflater.createViewFromTag(LayoutInflater.java:697) 
    at android.view.LayoutInflater.rInflate(LayoutInflater.java:756) 
    at android.view.LayoutInflater.rInflate(LayoutInflater.java:759) 
    at android.view.LayoutInflater.inflate(LayoutInflater.java:492) 
    at android.view.LayoutInflater.inflate(LayoutInflater.java:397) 
    at android.view.LayoutInflater.inflate(LayoutInflater.java:353) 
    at android.support.v7.app.AppCompatDelegateImplV7.setContentView(AppCompatDelegateImplV7.java:280) 
    at android.support.v7.app.AppCompatActivity.setContentView(AppCompatActivity.java:140) 
    at com.gaoyy.easysocial.ui.PersonalActivity.initContentView(PersonalActivity.java:74) 
    at com.gaoyy.easysocial.base.BaseActivity.onCreate(BaseActivity.java:21) 
    at android.app.Activity.performCreate(Activity.java:5231) 
    at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1087) 
    at com.morgoo.droidplugin.hook.handle.PluginInstrumentation.callActivityOnCreate(PluginInstrumentation.java:110) 
    at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2159) 
    at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:2245) 
    at android.app.ActivityThread.access$800(ActivityThread.java:135) 
    at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1196) 
    at android.os.Handler.dispatchMessage(Handler.java:102) 
    at android.os.Looper.loop(Looper.java:136) 
    at android.app.ActivityThread.main(ActivityThread.java:5017) 
    at java.lang.reflect.Method.invokeNative(Native Method) 
    at java.lang.reflect.Method.invoke(Method.java:515) 
    at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:779) 
    at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:595) 
    at dalvik.system.NativeStart.main(Native Method) 
Caused by: java.lang.NoSuchMethodError: android.support.v4.graphics.drawable.DrawableCompat.setLayoutDirection
    at android.support.design.widget.CollapsingToolbarLayout.setStatusBarScrim(CollapsingToolbarLayout.java:663)
    at android.support.design.widget.CollapsingToolbarLayout.<init>(CollapsingToolbarLayout.java:197)
    at android.support.design.widget.CollapsingToolbarLayout.<init>(CollapsingToolbarLayout.java:132)
    at java.lang.reflect.Constructor.constructNative(Native Method) 
    at java.lang.reflect.Constructor.newInstance(Constructor.java:423) 
    at android.view.LayoutInflater.createView(LayoutInflater.java:595) 
    at android.view.LayoutInflater.createViewFromTag(LayoutInflater.java:697) 
    at android.view.LayoutInflater.rInflate(LayoutInflater.java:756) 
    at android.view.LayoutInflater.rInflate(LayoutInflater.java:759) 
    at android.view.LayoutInflater.inflate(LayoutInflater.java:492) 
    at android.view.LayoutInflater.inflate(LayoutInflater.java:397) 
    at android.view.LayoutInflater.inflate(LayoutInflater.java:353) 
    at android.support.v7.app.AppCompatDelegateImplV7.setContentView(AppCompatDelegateImplV7.java:280) 
    at android.support.v7.app.AppCompatActivity.setContentView(AppCompatActivity.java:140) 
    at com.gaoyy.easysocial.ui.PersonalActivity.initContentView(PersonalActivity.java:74) 
    at com.gaoyy.easysocial.base.BaseActivity.onCreate(BaseActivity.java:21) 
    at android.app.Activity.performCreate(Activity.java:5231) 
    at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1087) 
    at com.morgoo.droidplugin.hook.handle.PluginInstrumentation.callActivityOnCreate(PluginInstrumentation.java:110) 
    at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2159) 
    at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:2245) 
    at android.app.ActivityThread.access$800(ActivityThread.java:135) 
    at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1196) 
    at android.os.Handler.dispatchMessage(Handler.java:102) 
    at android.os.Looper.loop(Looper.java:136) 
    at android.app.ActivityThread.main(ActivityThread.java:5017) 
    at java.lang.reflect.Method.invokeNative(Native Method) 
    at java.lang.reflect.Method.invoke(Method.java:515) 
    at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:779) 
    at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:595) 
    at dalvik.system.NativeStart.main(Native Method) 
```
解决办法：http://www.cnblogs.com/liyiran/p/5647509.html<br>
http://stackoverflow.com/questions/37423493/error-inflating-class-collapsingtoolbarlayout<br>
```Java
compile ('com.android.support:appcompat-v7:23.4.0'){
        force = true;
    }
    compile ('com.android.support:support-v4:23.4.0'){
        force = true;
    }
    compile ('com.android.support:design:23.4.0'){
        force = true;
    }
```
###2016.9.5
关于statusbar颜色设置
```Java
    /**
     * 设置NavBar和status颜色
     * @param activity
     * @param color
     */
    public static void setStatusBarColor(Activity activity, int color)
    {
        WeakReference<SystemBarTintManager> ws = new WeakReference<>(new SystemBarTintManager(activity));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(color));
            activity.getWindow().setNavigationBarColor(activity.getResources().getColor(color));
        } else
        {
            SystemBarTintManager systemBarTintManager = ws.get();
            systemBarTintManager.setStatusBarTintResource(color);
            systemBarTintManager.setStatusBarTintEnabled(true);
        }
    }
```
关于CardView在api>19下阴影不显示问题，需要同时设置CardView的`android:layout_margin="2dp"`，api=19下只需设置`card_view:cardElevation="3dp"`即可。
```Java
<android.support.v7.widget.CardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:card_view="http://schemas.android.com/apk/res-auto"
    android:id="@+id/item_home_cardview"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="2dp"
    android:elevation="3dp"
    android:foreground="?attr/selectableItemBackground"
    card_view:cardElevation="3dp">
 <!-- cardview 单独设置cardElevation在api19可以显示阴影，但是在api>19上需要设置margin才能显示出阴影-->
```
###2016.9.6
CoordinatorLayout+AppBarLayout+CollapsingToolbarLayout在5.0+下顶部会出现迷之空白，需要在CoordinatorLayout+AppBarLayout设置`android:fitsSystemWindows="true"`
```Java
<android.support.design.widget.CoordinatorLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/personal_coordinatorlayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true">

    <android.support.design.widget.AppBarLayout
        android:id="@+id/personal_appbarlayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fitsSystemWindows="true"
        >

        <android.support.design.widget.CollapsingToolbarLayout
            android:id="@+id/personal_collapsinglayout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@color/white"
            app:contentScrim="?attr/colorPrimary"
            app:layout_collapseParallaxMultiplier="0.6"
            app:layout_scrollFlags="scroll|exitUntilCollapsed">
```


