# UmengUpdatePlus

友盟更新组件的扩展，不借助外部接口实现了强制更新和简单的版本灰度。


#1、支持四种更新模式
0.关闭模式：不更新

1.自由模式：wifi 下静默更新,弹窗可取消  +   非 wifi 弹窗 可取消

2.兼顾模式：wifi 下静默更新,弹窗不可取消 +   非 wifi 弹窗 可取消（如果已经下载完毕则不可取消）

3.强制模式：wifi 下静默更新,弹窗不可取消 +   非 wifi 弹窗 不可取消

#2、给不同版本定制更新规则
实现简单版本灰度:即指定某个 versionCode 采用指定的 upgradeType

rule 的格式为versionCode:upgradeType#versionCode:upgradeType#versionCode:upgradeType

例如：1:1#2:3

#Usage: 
```java
AppUpgrader.start(getApplication(), "56f237ece0f55a8f7b000a14");
```
##更新模式和更新规则的配置：

如下图将更新模式和更新规则写在更新日志下方，放心，它不会显示在界面上。Json中的updateType和updateRule都是String类型。

<img src="https://github.com/MasonLiuChn/UmengUpdatePlus/raw/master/app/doc/1.png"/>

```goovy
allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
	
	dependencies {
	        compile 'com.github.MasonLiuChn:UmengUpdatePlus:1.0.2'
	}
```

#Contact me:

Blog:http://blog.csdn.net/masonblog

Email:MasonLiuChn@gmail.com

#License:
    Copyright 2016 MasonLiu, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


