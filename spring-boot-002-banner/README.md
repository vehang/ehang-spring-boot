

![](https://pic.netbian.com/uploads/allimg/210502/235717-16199710375e01.jpg)

大家好，我是一航！

今天带大家玩个好玩儿的配置；源代码的获取见文末。

当我们启动SpringBoot项目的时候，控制台会输出SpringBoot的logo以及版本相关的信息！

![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211109184510445.png)

可能很多朋友不知道，这个logo的是可以自定义的，而且自定义起来还非常简单，只需要在`resources`目录下放上一个`banner`配置，SpringBoot启动的时候，加载txt文件或者图片文件，就会展示对应的内容，支持的文件格式有`.txt`、`.png`、`.gif`；

- **org.springframework.boot.ResourceBanner**

  > 文本格式，SpringBoot 会读取配置项`banner.txt`和`banner.location`，从配置项中获取真实的文件地址；如果配置中没有配置，会把配置项作为文件去加载；

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211109222657456.png)

- **org.springframework.boot.ImageBanner**

  > 图片格式，SpringBoot 加载配置项banner.image.location，从配置项中获取真实的路径，SpringBoot 会根据配置项的路径加载文件。
  > 如果没有配置banner.image.location，转而依次加载banner.gif、banner.jpg、 banner.png这三个中存在的文件；

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211109222416440.png)

好了，这就配置成功了，相信聪明的你看一眼也就明白了...



---

**那么怎样才能让自己的logo更有个性呢！下面介绍几款个性化banner生成工具**



## bootschool

地址：https://www.bootschool.net/ascii

网站支持上百种艺术字体、二维码、以及各种内置好的图案

- **ASCII文字**

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/2021110918520000.gif)

- **二维码**

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/2021110918550000.gif)

- **艺术字/图案**

  预置了几十个大类，上千种艺术字/图

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/2021110918570000.gif)



## TAAG

地址：https://patorjk.com/software/taag

支持315种不同风格的艺术字体。

![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/2021110918590000.gif)



## 图片转ASCII

可以将一张图片转换为ASCII的txt文本格式

地址：https://www.degraeve.com/img2txt-yay.php

![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/20211109224000.gif)



## ASCII Cenerator

地址：http://www.network-science.de/ascii/

![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/2021110923090000.gif)



怎么样？

这么多不同风格的，总有一个喜欢的款式吧！

我就比较喜欢这个佛祖，自从安排上，感觉Bug都少多了。

> ```
> ${AnsiColor.BRIGHT_YELLOW}
>                               _
>                            _ooOoo_                               
>                           o8888888o                              
>                           88" . "88                              
>                           (| -_- |)                              
>                           O\  =  /O                              
>                        ____/`---'\____                           
>                      .'  \\|     |//  `.                         
>                     /  \\|||  :  |||//  \                        
>                    /  _||||| -:- |||||_  \                       
>                    |   | \\\  -  /'| |   |                       
>                    | \_|  `\`---'//  |_/ |                       
>                    \  .-\__ `-. -'__/-.  /                       
>                  ___`. .'  /--.--\  `. .'___                     
>               ."" '<  `.___\_<|>_/___.' _> \"".                  
>              | | :  `- \`. ;`. _/; .'/ /  .' ; |    Buddha       
>              \  \ `-.   \_\_`. _.'_/_/  -' _.' /                 
> ================-.`___`-.__\ \___  /__.-'_.'_.-'================  
>                            `=--=-'                    hjw
> ${AnsiColor.BRIGHT_YELLOW}
> ${AnsiColor.BRIGHT_RED}
> spring-boot.version: ${spring-boot.version}
> ${AnsiColor.BRIGHT_RED}
> ```



最近在整理SpringBoot的学习教程，如果你也在学习SpringBoot，可以[点击查看](https://github.com/vehang/ehang-spring-boot)，期待您的Star

> 本文示例教程：https://github.com/vehang/ehang-spring-boot/tree/main/spring-boot-002-banner
>
> SpringBoot教程：https://github.com/vehang/ehang-spring-boot
