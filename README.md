# MvpProject
这是一个集合自己平时开发中用到的工具类的集合工程。
欢迎下载
欢迎更新提交自己的到此工程！
如有疑问可以联系 1258624735@qq.com 
<br /> 
1.自定义dialog 基本样式。请看效果图：
<br /> 
![image](https://github.com/g1258624735/MvpProject/blob/master/app/src/main/assets/xixi.gif)
<br /> 
2.你可以这样使用自定义dialog:
<br /> 
![image](https://github.com/g1258624735/MvpProject/blob/master/app/src/main/assets/1.png)
> CommonDialog 详解

- CommonDialog 运用的设计思想

  - 主要是运用的是构建着模式

- CommonDialog 的简单使用步骤：

  ```
  CommonDialog.Builder(activity).setTitle("标题").setLeftText("取消").setRightText("确定")
                      .setContent("我是内容").setEnableOneButton(false).setShowEditText(false).setEditTextHint("请输入用户密码")
                      .setGravity(CommonDialog.DialogGravityType.GRAVITY_CENTER)
                      .setAnimationStyle(CommonDialog.DialogAnimationType.DIALOG_ANIMATION_SCALE)
                      .setOneButtonSureOnClickListener(object : CommonDialog.OnCommonClickListener {
                          override fun onClick(dialog: CommonDialog, text: String) =
                                  dialog.dismiss()
                      }).setLeftOnClickListener(object : CommonDialog.OnCommonClickListener {
                  override fun onClick(dialog: CommonDialog, text: String) = dialog.dismiss()
              }).setRightOnClickListener(object : CommonDialog.OnCommonClickListener {
                  override fun onClick(dialog: CommonDialog, text: String) {
                      dialog.dismiss()
                      Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
                  }
              }).create().show()
  ```

  - 以上就是一个完整的CommonDialog 的使用配置方法，你可以通过构建者模式提供的方法配置dialog 的各种样式。

  - CommonDialog  在屏幕中位置的配置 ，默认有两种位置样式，居中，底部两种样式，当然你也可以通过继承dialog 扩展自己的位置样式。

    ```
     CommonDialog.Builder(activity).setAnimationStyle(CommonDialog.DialogAnimationType.DIALOG_ANIMATION_SCALE)
     ----------------------------------------------- 可选择的位置样式
       /**
         * 位置显示样式选择
         */
        interface DialogGravityType {
            companion object {
                /**
                 * dialog 位置显示样式
                 * 底部显示
                 */
                val GRAVITY_BOTTOM = Gravity.BOTTOM
                /**
                 * dialog 中部显示
                 */
                val GRAVITY_CENTER = Gravity.CENTER
                /**
                 * dialog 顶部显示
                 */
                val GRAVITY_TOP = Gravity.TOP
            }
        }
    ```

    ​

  - CommonDialog  动画样式的设置 ，暂时也只提供了渐变和底部飞入，飞出样式的选择。动画设置的样式如下：

    ```
     CommonDialog.Builder(activity).setAnimationStyle(CommonDialog.DialogAnimationType.DIALOG_ANIMATION_SCALE)
    -----------------------------------------------
     /**
         * 位置显示样式选择
         */
        interface DialogAnimationType {
            companion object {
                /**
                 * dialog 弹出动画
                 * 渐变显示
                 */
                val DIALOG_ANIMATION_SCALE = R.style.DialogScaleAnimation
                /**
                 * dialog 弹出动画
                 * 从底部弹出 动画
                 */
                val DIALOG_ANIMATION_UP_DOWN = R.style.DataSheetAnimation
            }
        }

    ```

  - CommonDialog   当然也支持自定义布局的扩张，只需要传入 自定义view 就可以。依然可以使用其自带的dialog 的各种属性。

    ```
    - 自定义样式CommonDialog   
    val v = layoutInflater.inflate(R.layout.dialog_bottom_home, null)
    val dialog: CommonDialog = CommonDialog.Builder(activity).setView(v)
            .setGravity(CommonDialog.DialogGravityType.GRAVITY_BOTTOM)
            .setWidthStyle(CommonDialog.DialogWidthStyle.WIDTH_STYLE_MATCH_PARENT)
            .setAnimationStyle(CommonDialog.DialogAnimationType.DIALOG_ANIMATION_UP_DOWN)
            .create()
    v.findViewById<Button>(R.id.cancel).setOnClickListener({
        dialog.dismiss()
    })
    dialog.show()


    ```

  - 如果在使用的过程中有碰到什么疑问 可以联系我1258624735@qq.com。我会随时给出意见。欢迎骚然。

