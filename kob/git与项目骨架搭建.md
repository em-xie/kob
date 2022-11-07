# 配置git环境与项目创建

前言
[更好的阅读]
[Acgit 项目地址]
因为springboot和vue基础不太好

因此这篇题解只追求 不求甚解

Git的配置
Git 工具用来 管理 本地仓库 和 云仓库

中国下载地址 : https://git-scm.com/
官方下载地址 : https://gitforwindows.org/

安装过程 ： 我们只需要傻瓜的Next即可

然后桌面鼠标右键,点击Git Bash,利用ssh-keygen进行生成密钥(注意这里需要cd到Home目录)



然后会生成两个文件一个是id_rsa,id_rsa.pub

利用cat id_ras.pub打开这个文件,复制里面的全部内容

Acgit的配置
(这里建议将刚刚复制的内容存放一下)

登录https://git.acwing.com/进行账号注册

一开始是英文,我们需要点击头像,偏好设置(倒数第二个),然后往下滑找到languague




然后我们将刚刚创建的密钥,添加到我们的Acgit上面



项目仓库配置
我们在本地创建一个项目文件夹

我们进入项目文件夹,鼠标右键git Bash然后输入git init
(可以使用ls -a查看是否初始化成功)

我们利用touch readme.md创建一个md文件,然后利用vim readme.md进行写入
vim怎么使用,我们打开之后还是不能写的,我们需要按下 i才可以进行写,写完之后按esc之后按:,输入wq实现保存和退出


下面就是git的操作了

git status
查看当前分支的情况,这里的情况大致分为三种对应git的三个区域(应该是三种情况)
这里我们发现,readme.md没有add commit,然后它提示git add to track

git add .(将文件放入暂存区,第二个区域)
这里使用的是git add . .表示所有文件,应该是个通配符

git status
我们再次查看状态,发现文件变绿了


git commit -m "创建项目"(将暂存区提交到本地仓库)

1.创建Acgit项目

下面是Acgit的操作
https://git.acwing.com/
我们打开 Ac git



创建一个 空白项目,然后填写相应信息

记得把下面那个勾掉(原因 : 没什么用)

2. 配置
我们创建好之后,会出现一个新网页

我们复制这两个记得去掉--global,(原因 : 如果有这两个那么以后的所有项目用的都是这两个配置)


接着我们在复制这两个,顺序的粘贴运行,将本地仓库push到云端上



完成云端仓库的创建

下面y总联系了一下git pull/commit/add/push操作,具体看视频

IDEA
1. 创建项目

选择Spring Initializr这个应该是专业版才有,社区版无

因为默认的连不上,所以这里选择start.aliyun.com

然后设置名称,改一下Group : com.kob

因为我没有Java8,这里选择Java11

记得将这个项目放到本地仓库那个目录


勾选Spring Web和Thymeleaf,第二个的作用是模拟一下前后端不分离



*首次创建需要注意等 index完之后在操作,时间有点久


2. 启动项目
启动项目,输入127.0.0.1:8080进行访问,如图即成功



3. 前后端不分离

创建Controller

我们创建两个包controller pk和一个IndexController类

然后我们给该类添加@Controller注解和RequestMapping("/pk/)


@Controller标识的类，该类代表控制器类(控制层/表现层)。
@RequestMapping(),用于映射请求

(注解y总不会讲,我们只需要知道每个注解的作用即可)

创建 index.html
我们需要在resource中创建一个pk文件夹,然后创建index.html
(这里的pk文件夹是否是要和前面的controller对应不清楚,个人感觉是不需要的主要还是基础不扎实)

添加相应的映射

我们写一下相关的映射,然后重启服务,在地址栏手动添加pk/index进行对该页面的访问
(除此之外还可以 添加其他东西)


前后端分离小测试
前后端分离,后端不再返回页面,而是返回数据

我们创建BotInfoController并且添加@RestController


@RestController 是@controller和@ResponseBody 的结合
@Controller 将当前修饰的类注入SpringBoot IOC容器，使得从该类所在的项目跑起来的过程中，这个类就被实例化。
@ResponseBody 它的作用简短截说就是指该类中所有的API接口返回的数据，甭管你对应的方法返回Map或是其他Object，它会以Json字符串的形式返回给客户端

重启服务,访问页面
(y总还返回了List,Map这里不多列出)


小插曲

因为Vue默认的端口也是8080,所以我们将Spring的端口改为3000,重启服务


4.将项目上传
git status 项目为上传

git add .放入暂存区
git commit -m "创建后端"
git push

完成


Vue
安装Node.js https://nodejs.org/en/

我们打开cmd最好用管理员的方式
可以先试试直接npm i -g @vue/cli@5.0.4如果卡住很久不动建议使用下面的方法
npm config set registry https://registry.npmmirror.com
安装淘宝镜像
cnpm install -g @vue/cli
安装Vue

安装成功之后,我们vue ui打开控制台


之后我们在kob文件下创建文件,记得勾掉git,并且选择vue3

安装两个依赖JQuery和bootstrap

然后启动并且访问



然后再创建一个项目acapp和上面的操作相同(插件不需要再装vue-router和其他的依赖),回到初始界面左上角
运行测试

用git进行上传

git status

git add .
git commit -m "创建web端和 acapp端"
git push

成功！！！！


Vscode
用Vscode打开web

切回web启动任务运行,调出该网页方便运行

删除无关信息(具体可看视频 1:40:00左右)

完成


前端代码编写
每个Vue就三个标签 template,script,style
分别对应html,js,css

<template>
  <div>

    <div>Bot昵称 :{{ bot_name }}</div>
    <div>Bot战力 :{{ bot_rating }}</div>
  </div>
  <router-view></router-view>
</template>


<script>
import $ from 'jquery'
import { ref } from 'vue';

export default {
  name: "App",
  setup: () => {
    let bot_name = ref("");
    let bot_rating = ref("");

    // 访问后端
    $.ajax({
      url: "http://127.0.0.1:3000/pk/getBotInfo/",
      type: "get",
      success: resp => {
        console.log(resp);
      }
    });
    return {
      bot_name,
      bot_rating
    }
  }
}
</script>

<style>
</style>

这里会出现一个小错误,需要复制讲义里面的类


这里摆烂了,没有写返回List的,哈哈哈跑起来了,总算跑起来了QAQ


最后添加背景图



<template>
  <div>

    <div>Bot昵称 :{{ bot_name }}</div>
    <div>Bot战力 :{{ bot_rating }}</div>
  </div>
  <router-view></router-view>
</template>


<script>
import $ from 'jquery'
import { ref } from 'vue';

export default {
  name: "App",
  setup: () => {
    let bot_name = ref("");
    let bot_rating = ref("");

    // 访问后端
    $.ajax({
      url: "http://127.0.0.1:3000/pk/getBotInfo/",
      type: "get",
      success: resp => {
        // console.log(resp);
        bot_name.value = resp;
        bot_rating.value = resp;
      }
    });
    return {
      bot_name,
      bot_rating
    }
  }
}
</script>

<style>
body {
  background-image: url("@/assets/background.png");
}
</style>



