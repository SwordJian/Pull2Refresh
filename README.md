# Pull2Refresh

此库是从com.handmark.pulltorefresh(这位大神没github吗，链接找不到)修改而成

新增加了适配RecycleView的上下拉
删除了其他语言，只保留了简体中文和英文

How to

To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
Step 2. Add the dependency

	dependencies {
	        compile 'com.github.SwordJian:Pull2Refresh:1.0.0'
	}
