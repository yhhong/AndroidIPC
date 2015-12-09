# AndroidIPC

The sample of inter process communication on android.
<p/>
应用间通讯（进程间通讯）<br>
一、Activity<br>
1、显示调用/隐式调用<br>
2、加入到当前任务/新任务的Activity栈<br>
3、传递数据<br>
4、回传数据<br>
5、可传递数据量限制大小
<p/>
二、ContentProvider<br>
ContentResolver<br>
ContentObserver<br>
Resource<br>
权限配置<br>
1、android:exported="true"<br>
2、permission针对各Uri进行权限配置<br>
android:permission="string"<br>
android:readPermission="string"<br>
android:writePermission="string"<br>
3、android:exported="false" & android:shareUserId="com.aspirecn.hop"<br>
<p/>
三、Broadcast<br>
<p/>
四、Service AIDL<br>
Server:<br>
1、定义IMyAidlInterface.aidl文件，build，生成对应的java文件在build/generated/source/aidl/debug/xxx.IMyAidlInterface.java<br>
2、创建Service文件，创建IBinder、实现IMyAidlInterface.java中的接口<br>
3、在manifest.xml中配置Service组件，action调用，并且指定在独立进程中运行，android:process=":remote"<br>
Client:<br>
1、从Server拷贝aidl文件连同所在目录也保持一致<br>
2、bindService，成功后得到IBinder(aidlInterface)的引用，即可调用Server提供的AIDL接口；另外注意：bind service，从android5.0开始需要显示调用<br>
<p/>
五、NotificationListenerService<br>

