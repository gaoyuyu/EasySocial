# EasySocial
EasySocial
<br>
#### 2016.2.18
Material Design UI初始化(DrawerLayout+NavigationView,CoordinatorLayout+AppBarLayout+Toolbar+TabLayout,
FloatingActionButton)<br>
issue：1、NavigationView设置Menu后不显示title<br>
solution：1、NavigationView设置app:itemTextColor属性
<br>
![image](https://github.com/gaoyuyu/EasySocial/raw/master/screenshots/20160218.png)<br>
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


