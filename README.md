#HScrollListView

横向的可以自定义每个item布局的列表，并且带有scroll特性，可以动态、静态设置数据

## Demo

![](http://img1.ph.126.net/k37p_8bXSc7vrNyzdlEQ2g==/6608503588073885863.gif)

## 使用方法

### xml中设置：

	<info.xiazdong.widget.HScrollListView
        android:id="@+id/menu"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:imageArray="@array/menu_btn_image" <!-- 静态设置：图片数组 -->
        app:textArray="@array/menu_btn_text"   <!-- 静态设置：文字数组 -->
        app:horizontalSpace="7dp"			    <!-- 每个item之间的间距 -->
        app:leftMargin="20dp"                  <!-- 左边距 -->
        app:rightMargin="20dp"                 <!-- 右边距 -->
        app:topMargin="20dp"                   <!-- 上边距 -->
        app:bottomMargin="20dp"                <!-- 下边距 -->
        app:itemLayoutId="@layout/item_menu"   <!-- 自定义的item的布局 -->
        app:defaultSelectedIndex="2"           <!-- 默认的选中的索引 -->
        app:multiSelectable="true"             <!-- 是否可多选 -->
        app:toggleSelf="true"                  <!-- 在选中的情况下是否点击后变成不选中 -->
    />

如果每个item只是：

* 一张图片(ID必须是 @id/image)
* 一行文字(ID必须是 @id/text)
* 上面一张图片＋下面一行文字(@id/image + @id/text)

那么只需要这样即可，不需要再做什么。

### 设置监听事件：

	mMenuView = (HScrollListView) findViewById(R.id.menu);
    mMenuView.setItemClickListener(new HScrollListView.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int index) {
           //参数：view 为点击的item对应的view，index为该item的索引
        }
    });

### 自定义布局＋动态设置数据

	
	class Person {
    	String name;
    	Bitmap pic;
    	int age;

    	public Person(String name, Bitmap pic, int age) {
        	this.name = name;
        	this.pic = pic;
        	this.age = age;
    	}
	}
	
具体设置如下：

* 创建 `HScrollListViewAdapter<T>` 实例，并在构造函数中传入"数据数组"，并实现 `convert(View view,int index, T data)`，其中 view 表示第 index 个 item 的 view 对象，index 表示 view 的索引，data 是构造函数传入的数据数组的第 index 个元素
* 使用 HScrollListView.setAdapter() 设置即可。

        Person[] persons = new Person[10];
        for (int i = 0; i < 10; i++) {
            //向persons中填充数据
        }
        mPersons = (HScrollListView) findViewById(R.id.list_person);
        mPersons.setAdapter(new HScrollListViewAdapter<Person>(persons) {
            @Override
            public void convert(View view, int index, Person data) {
            	//自定义数据
                TextView name = (TextView) view.findViewById(R.id.name);
                ImageView pic = (ImageView) view.findViewById(R.id.pic);
                TextView age = (TextView) view.findViewById(R.id.age);
                name.setText(data.name);
                pic.setImageBitmap(data.pic);
                age.setText(data.age + "");
            }
        });