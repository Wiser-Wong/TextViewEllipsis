# TextViewEllipsis
文本+布局 文本超过1行展示省略号...

## 截图
![images](https://github.com/Wiser-Wong/TextViewEllipsis/blob/master/images/text.png)

## 环境配置
    allprojects {
            repositories {
              ...
              maven { url 'https://jitpack.io' }
            }
          }

        dependencies {
              implementation 'com.github.Wiser-Wong:TextViewEllipsis:1.0.0'
      }

## 使用控件
### 注意事项
 * TextEllipsisLayout 控件内不允许超过两个控件，否则会报异常
 * TextEllipsisLayout 控件内第一个控件必须是TextView，因为控件的意义就是针对TextView后跟控件，使TextView显示单行并且以省略号结尾，所以必须第一位是TextView，第二个控件爱是什么是什么。
 
 使用方法如下
 
    <com.wiser.textellipsis.TextEllipsisLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="center">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="20dp"
            android:layout_marginLeft="20dp"
            android:text="我要是省略号了我要是省略号了我要是省略号了我要是省略号了" />

        <ImageView
            android:layout_width="100dp"
            android:layout_height="wrap_content"
            android:adjustViewBounds="true"
            android:scaleType="fitXY"
            android:src="@mipmap/ic_launcher" />
    </com.wiser.textellipsis.TextEllipsisLayout>
