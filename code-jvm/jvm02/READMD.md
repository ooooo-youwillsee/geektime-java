## 20-实现xlass打包的xar（类似class文件打包的jar）的加载：xar里是xlass。

运行命令 `java -jar Application.xar`
运行命令 `java -jar Application2.xar`

说明：

1. `Application.xlass` 源码文件对应 `com.ooooo.Application`
1. `Hello1.xlass` 源码文件对应 `com.ooooo.test.Hello1`
1. `Hello2.xlass` 源码文件对应 `com.ooooo.test.Hello2`

思路来源于 `Spring Boot Loader`

`spring boot` 实现了 `org.springframework.boot.loader.jar.Handler` 对class文件读取规则。