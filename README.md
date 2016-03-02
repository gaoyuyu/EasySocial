# EasySocial
EasySocial
<br>
#### 2016.2.18
Material Design UI初始化(DrawerLayout+NavigationView,CoordinatorLayout+AppBarLayout+Toolbar+TabLayout,
FloatingActionButton)
<br>
issue：1、NavigationView设置Menu后不显示title
<br>
solution：1、NavigationView设置app:itemTextColor属性
<br>
![image](https://github.com/gaoyuyu/EasySocial/raw/master/screenshots/20160218.png)
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
