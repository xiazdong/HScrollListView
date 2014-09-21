package info.xiazdong.widget.test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import info.xiazdong.widget.HScrollListViewAdapter;
import info.xiazdong.widget.HScrollListView;
import info.xiazdong.widget.R;

public class HScrollListViewActivity extends Activity {
    private final String TAG = HScrollListViewActivity.class.getSimpleName();
    private HScrollListView mMenuView;
    private HScrollListView mPersons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hscroll_list_view);

        // Use 1: 静态设置 HScrollView 的内容，即在 XML 中设置最简单的内容
        mMenuView = (HScrollListView) findViewById(R.id.menu);
        mMenuView.setItemClickListener(new HScrollListView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int index) {
                int[] results = mMenuView.getInScreenItemsRange();
                Log.d(TAG, results[0] + "-" + results[1]);
            }
        });

        // Use 2: 动态设置 HScrollView 的内容，自定义布局
        int[] pics = {
                R.drawable.ic_person_01, R.drawable.ic_person_02, R.drawable.ic_person_03,
                R.drawable.ic_person_04, R.drawable.ic_person_05, R.drawable.ic_person_06,
                R.drawable.ic_person_07, R.drawable.ic_person_08, R.drawable.ic_person_09, R.drawable.ic_person_10};
        Person[] persons = new Person[10];
        for (int i = 0; i < 10; i++) {
            persons[i] = new Person("name-" + i, ((BitmapDrawable) getResources().getDrawable(pics[i])).getBitmap(), i);
        }
        mPersons = (HScrollListView) findViewById(R.id.list_person);
        mPersons.setAdapter(new HScrollListViewAdapter<Person>(persons) {
            @Override
            public void convert(View view, int index, Person data) {
                TextView name = (TextView) view.findViewById(R.id.name);
                ImageView pic = (ImageView) view.findViewById(R.id.pic);
                TextView age = (TextView) view.findViewById(R.id.age);
                name.setText(data.name);
                pic.setImageBitmap(data.pic);
                age.setText(data.age + "");
            }
        });
        mPersons.setItemClickListener(new HScrollListView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int index) {
                TextView name = (TextView) view.findViewById(R.id.name);
                TextView age = (TextView) view.findViewById(R.id.age);
                Log.d(TAG, name.getText() + "," + age.getText());
            }
        });
    }
}

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