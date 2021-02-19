# 第1周作业


参见 我的教室 -> 本周作业

## 作业内容


> Week01 作业题目（周四）：

1.（选做）自己写一个简单的 Hello.java，里面需要涉及基本类型，四则运行，if 和 for，然后自己分析一下对应的字节码，有问题群里讨论。

2.（必做）自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，此文件内容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件。文件群里提供。

3.（必做）画一张图，展示 Xmx、Xms、Xmn、Meta、DirectMemory、Xss 这些内存参数的关系。

`Xmx`: 最大堆内存
`Xms`: 最小堆内存
`Xss`: 栈的大小

4.（选做）检查一下自己维护的业务系统的 JVM 参数配置，用 jstat 和 jstack、jmap 查看一下详情，并且自己独立分析一下大概情况，思考有没有不合理的地方，如何改进。

命令 `jstat -gc <pid> 500`

- S0C：第一个幸存区的大小
- S1C：第二个幸存区的大小
- S0U：第一个幸存区的使用大小
- S1U：第二个幸存区的使用大小
- EC：伊甸园区的大小
- EU：伊甸园区的使用大小
- OC：老年代大小
- OU：老年代使用大小
- MC：方法区大小
- MU：方法区使用大小
- CCSC:压缩类空间大小
- CCSU:压缩类空间使用大小
- YGC：年轻代垃圾回收次数
- YGCT：年轻代垃圾回收消耗时间
- FGC：老年代垃圾回收次数
- FGCT：老年代垃圾回收消耗时间
- GCT：垃圾回收消耗总时间

命令 `jstack <pid>`

查询线程状态，分析死锁原因

命令 `jmap -heap <pid>`

查看堆内存使用情况

注意：如果没有线上系统，可以自己 run 一个 web/java 项目。

> Week01 作业题目（周六）：

1.（选做）本机使用 G1 GC 启动一个程序，仿照课上案例分析一下 JVM 情况。


## 操作步骤


### 作业2

1. 打开 Spring 官网: https://spring.io/
2. 找到 Projects --> Spring Initializr:  https://start.spring.io/
3. 填写项目信息, 生成 maven 项目; 下载并解压。
4. Idea或者Eclipse从已有的Source导入Maven项目。
5. 增加课程资源 Hello.xlass 文件到 src/main/resources 目录。
6. 编写代码，实现 findClass 方法，解码方法
7. 编写main方法，调用 loadClass 方法；
8. 创建实例，以及调用方法
9. 执行.

具体的参见: [https://github.com/renfufei/JAVA-000/blob/main/Week_01/homework01/src/main/java/com/renfufei/homework01/XlassLoader.java](XlassLoader.java)
