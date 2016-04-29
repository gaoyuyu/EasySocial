# EasySocial
EasySocial
<br>
Learn Here：[How to Build Project](http://www.jianshu.com/p/d9e4ddd1c530)
#### 2016.2.18
Material Design UI初始化(DrawerLayout+NavigationView,CoordinatorLayout+AppBarLayout+Toolbar+TabLayout,
FloatingActionButton)
<br>
issue：1、NavigationView设置Menu后不显示title
<br>
solution：1、NavigationView设置app:itemTextColor属性
<br>
![image](https://github.com/gaoyuyu/EasySocial/raw/master/screenshots/splash.png)
<br>
#### 2016.2.19
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
将`<CoordinatorLayout />`,也就是app_bar_main.xml抽离出来，由MainFragment来填充。
切换Fragment的不再是简单的replace(),之后commit(),而是hide(),show(),性能更好。
当然，在初始化MainFragment的时候是用了replace。<br>
参考blog：[仿知乎程序（二）fragment的切换以及toolbar在不同页面下显示的menu不同](http://blog.csdn.net/chenguang79/article/details/49486723)<br>
[Android Fragment 你应该知道的一切](http://blog.csdn.net/lmj623565791/article/details/42628537)
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
####2016.3.1
Ripple：[material-ripple](https://github.com/balysv/material-ripple)
<br>
MaterialDialog:[drakeet/MaterialDialog](https://github.com/drakeet/MaterialDialog)
<br>
MaterialEditText:[rengwuxian/MaterialEditText](https://github.com/rengwuxian/MaterialEditText)
<br>
Basic Usage：<br>
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
####2016.3.2
在Fragment中的toolbar中设置返回键<br>
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
<br>
设置后toolbar左上角会出现“返回”箭头，对toolbar的返回键的监听需要在其父Activity中重写`onOptionsItemSelected(MenuItem item)`<br>
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

关于v4包Fragment和非v4包Fragment使用切换动画:[Android Activity和Fragment的转场动画](http://www.cnblogs.com/mengdd/p/3494041.html)
####2016.3.10
一、toolbar中设置SearchView（仿微信点击Search按钮跳转到SearchActivity）
<br>
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
[jcenter gradle](https://jcenter.bintray.com/com/android/tools/build/gradle/) 查看最新版本的gradle，修改project下的build.gradle的classpath
目前最新2.0.0-beta2

```Java
    dependencies {
        classpath 'com.android.tools.build:gradle:2.0.0-beta2'
    }
```
####2016.3.11
添加okHttp库<br>
[okhttp](https://github.com/square/okhttp)<br>
->[okhttp Wiki](https://github.com/square/okhttp/wiki)

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
####2016.3.23
Activity最顶层布局必须`android:fitsSystemWindows="true"`，之后再设置SystemBarTintManager
####2016.4.11
PopupWindow在实际的点击位置显示<br>
资料：[android之View坐标系（view获取自身坐标的方法和点击事件中坐标的获取）](http://blog.csdn.net/jason0539/article/details/42743531)<br>
<br>
````Java
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
````
####2016.4.26
ShareSDK assets Location<br>
![image](https://github.com/gaoyuyu/EasySocial/raw/master/screenshots/assets.png)
