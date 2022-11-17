升级vue
vue --version

若vue版本是5.04，需要升级为5.0.8
npm i -g @vue/cli@5.0.8

修改nginx，防止acapp部署在acwing上、后台在自己的服务器上，存在跨域问题
打开应用界面->创建应用->将springboot-nginx.conf内容复制到nginx中

重新加载配置： sudo /etc/init.d/nginx reload

启动vue脚手架，运行本地项目，测试是否能运行网站上所有功能





acapp项目的修改
检查旧版本有无vue.config.js，若没有需要重新创建acapp vue项目

1.重新创建acapp项目

打开vue脚手架，在左上角下拉菜单选择项目管理器
选择创建，选择kob文件夹，在此创建新项目

![0.png](https://cdn.acwing.com/media/article/image/2022/08/31/29231_e353a8e529-0.png)

选择Default(Vue 3)
提示：若想让自己的前端项目运行在acwing应用中，需要将bootstrap、vue-router删掉

安装插件：vue-router，vuex

安装依赖：bootstrap，popperjs/core，vue3-ace-editor，jquery

2.部署acapp项目

删除新创建的acapp的src，将web的src复制到本项目

打开acapp项目，修改vue.config.js

```
const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    // No need for splitting
    optimization: {
      splitChunks: false
    }
  }
})
```

运行网站，测试是否能正常运行：比如看录像、排行榜

build项目

将打包好的js和css传到服务器

```
在dist文件夹下使用命令：
scp js/*.js springboot:kob/acapp/
scp css/*.css springboot:kob/acapp/
```

整理服务器文件位置，并改名

```
cd /kob/acapp
ls
mv *.js app.js
mv *.css app.css
```

输入url,看是否能看到js和css文件
若能看到，将js、css地址填在创建应用的相应地方

```
appxxxx.acapp.acwing.com.cn/acapp/app.js
appxxxx.acapp.acwing.com.cn/acapp/app.css
```

一键将app.js修改成类的封装并传到服务器上
在服务器的~/kob/acapp下新建脚本rename.sh，并赋予可执行权限：chmod +x rename.sh，在文件内写上以下内容：

```
#! /bin/bash

test -e app.js && rm app.js
test -e app.css && rm app.css
mv *.js app.js
mv *.css app.css
```

在本地的kob/acapp下打开git bash，创建upload.sh，赋予可执行权限：chmod +x upload.sh，内容为：

```
#! /bin/bash

find dist/js/*.js | xargs sed -i 's/(function(){var e={/const myfunc = (function(myappid, AcWingOS){var e={/g'

find dist/js/*.js | xargs sed -i 's/.mount("#app")}()})();/.mount(myappid)}()});/g'
```

```
echo "

export class Game {
    constructor(id, AcWingOS) {
        const myappid = '#' + id;
        myfunc(myappid);
    }
}" >> dist/js/*.js

scp dist/js/*.js springboot:kob/acapp
scp dist/css/*.css springboot:kob/acapp

ssh springboot 'cd kob/acapp && ./rename.sh'
```

在git bash中输入命令运行脚本：./upload.sh
注意：此脚本每次build后只能运行一次，要不然会加上多个class

将类名填到创建应用中，并打开应用测试
修改acapp代码

```
将UserAccountLoginView.vue抽取到store中
src/views/user/account/UserAccountLoginView.vue

将以下内容从此文件删除
const jwt_token = localStorage.getItem("jwt_token");
if(jwt_token) {
    store.commit("updateToken", jwt_token);
    store.dispatch("getinfo", {
        success() {
            router.push({name: "home"});
            store.commit("updatePullingInfo", false);
        },
        error() {
            store.commit("updatePullingInfo", false);
        }
    });
} else {
    store.commit("updatePullingInfo", false);
}


```

```
先暂时使用默认帐号登录acapp，在已部署的网页中 -> f12中 -> 应用 -> 本地存储空间 -> jwt_token
```

```
src/App.vue

<template>
    ...
</template>

<script>
...
import { useStore } from 'vuex';

export default {
    ...
    setup() {
        const store = useStore();
        const jwt_token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJlOWNhNjk1MzhjYzg0MDA0YmMyYzZiYzJhMzEwNDhjMiIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY2MTkzODg0NSwiZXhwIjoxNjYzMTQ4NDQ1fQ.ybk3los1-vaVUAY-4T31Ko_SMw2hTI9SQQoUpwtSLgE";
        if(jwt_token) {
            store.commit("updateToken", jwt_token);
            store.dispatch("getinfo", {
                success() {
                    store.commit("updatePullingInfo", false);
                },
                error() {
                    store.commit("updatePullingInfo", false);
                }
            });
        } else {
            store.commit("updatePullingInfo", false);
        }
    }
}
</script>

<style>
...
</style>


```

由于vue-router会修改acwing url，因此手写个router
1.删除views/user下的account文件夹，views下的error文件夹，router文件夹，NavBar.vue
2.在store中新建router.js

```
src/store/router.js

export default {
    state: {
        router_name: "menu",    // menu, pk, record, record_content, ranklist, user_bot
    },
    getters: {
    },
    mutations: {
        updateRouterName(state, router_name) {
            state.router_name = router_name;
        }
    },
    actions: {
    },
    modules: {
    }
}


```

3.引入到store/index.js
注意：由于router.js是export default，所以引入时可以任意起名

```
src/store/index.js

import { createStore } from 'vuex'
import ModuleUser from './user'
import ModulePk from './pk'
import ModuleRecord from './record'
import ModuleRouter from './router'

export default createStore({
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user: ModuleUser,
    pk: ModulePk,
    record: ModuleRecord,
    router: ModuleRouter,
  }
})


```

菜单界面
修改App.vue为导航菜单界面

```
<template>
  <div class="window">
      <div class="game-body">
          <MenuView v-if="$store.state.router.router_name === 'menu'" />
          <PkIndexViewVue v-else-if="$store.state.router.router_name === 'pk'" />
          <RecordIndexViewVue v-else-if="$store.state.router.router_name === 'record'" />
          <RecordContentViewVue v-else-if="$store.state.router.router_name === 'record_content'" />
          <RankListViewVue v-else-if="$store.state.router.router_name === 'ranklist'" />
          <UserBotIndexViewVue v-else-if="$store.state.router.router_name === 'user_bot'" />
      </div>
  </div>
</template>


<script>
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap';
import { useStore } from 'vuex';
import MenuView from './views/MenuView.vue';
import PkIndexViewVue from './views/pk/PkIndexView.vue';
import RecordIndexViewVue from './views/record/RecordIndexView.vue';
import RecordContentViewVue from './views/record/RecordContentView.vue';
import RankListViewVue from './views/ranklist/RanklistIndexView.vue';
import UserBotIndexViewVue from './views/user/bot/UserBotIndexView.vue';



export default {
  components: {
        MenuView,
        PkIndexViewVue,
        RecordIndexViewVue,
        RecordContentViewVue,
        RankListViewVue,
        UserBotIndexViewVue,
    },
  //eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzNDIyYTQ4NGEwZjA0NzMyYmNlZGIwM2Q0NGQ3ZGUwNyIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY2ODU5OTk3NiwiZXhwIjoxNjY5ODA5NTc2fQ.59ScUPcvM4BYay6fPYXD9MJxGyWIs6YVLoc44hbTWOI
  setup(){
        const store = useStore();
        const jwt_token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzNDIyYTQ4NGEwZjA0NzMyYmNlZGIwM2Q0NGQ3ZGUwNyIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY2ODU5OTk3NiwiZXhwIjoxNjY5ODA5NTc2fQ.59ScUPcvM4BYay6fPYXD9MJxGyWIs6YVLoc44hbTWOI";
        if(jwt_token){
            // commit 调用user里面的mutations函数  同步
            //dispatch action 异步
            store.commit("updateToken",jwt_token);
            store.dispatch("getinfo",{
                success() {
                    //router.push({ name: 'home' });
                    store.commit("updatePullingInfo", false);
                },
                error() {
                    store.commit("updatePullingInfo", false);
                }
            })
        }else {
            store.commit("updatePullingInfo", false);
        }
  }
}
</script>

<style scoped>
div.game-body {
    width: 100%;
    height: 100%;
    background-image: url("@/assets/images/background.png");
    background-size: cover;
}

div.window {
    width: 100vw;
    height: 100vh;
}
</style>

```



删除RecordIndexView.vue的router，两处：一处引入一处router.push；删除main.js的router

菜单界面



```
<template>
    <div class="menu-field">
        <div class="menu">
            <div class="menu-item" @click="click_pk_handler">对战</div>
            <div class="menu-item" @click="click_record_handler">对局列表</div>
            <div class="menu-item" @click="click_ranklist_handler">排行榜</div>
            <div class="menu-item" @click="click_user_bot_handler">我的Bot</div>
        </div>
    </div>
</template>

<script>
import { useStore } from 'vuex';

export default {
    setup() {
        const store = useStore();

        const click_pk_handler = () => {
            store.commit("updateRouterName", "pk");
        }

        const click_record_handler = () => {
            store.commit("updateRouterName", "record");
        }

        const click_ranklist_handler = () => {
            store.commit("updateRouterName", "ranklist");
        }

        const click_user_bot_handler = () => {
            store.commit("updateRouterName", "user_bot");
        }

        return {
            click_pk_handler,
            click_record_handler,
            click_ranklist_handler,
            click_user_bot_handler,
        }
    }
}
</script>

<style scoped>
div.menu-field {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
}
div.menu {
    width: 25vw;
    height: 30vh;
    background-color: rgba(0, 0, 0, 0.2);
}
div.menu-item {
    height: 7.5vh;
    width: 100%;
    font-size: 24px;
    line-height: 7.5vh;
    color: white;
    text-align: center;
    font-style: italic;
    font-weight: 600;
    user-select: none;
    cursor: pointer;
}
div.menu-item:hover {
    scale: 1.2;
    transition: 200;
}
</style>


```



增加返回按钮
在公共组件里加上返回按钮

```
<template>
    <div class="content-field">
        <slot></slot>
        <div class="go-back" @click="click_go_back_handler">
            返回
        </div>
    </div>
</template>

<script>
import { useStore } from 'vuex';

export default {
    setup() {
        const store = useStore();

        const click_go_back_handler = () => {
            store.commit("updateRouterName", "menu");
        }

        return {
            click_go_back_handler,
        }
    }
}
</script>

<style scoped>
div.content-field {
    width: 100%;
    height: 100%;
}
div.go-back {
    position: absolute;
    right: 5vh;
    bottom: 5vh;
    color: white;
    font-size: 24px;
    font-style: italic;
    font-weight: 600;
    cursor: pointer;
    user-select: none;
}

div.go-back:hover {
    scale: 1.2;
    transition: 200;
}
</style>

```

pk页面加上ContentField公共组件

```
src/views/pk/PkIndexView.vue

<template>
    <ContentField>
        ...
    </ContentField>
</template>

<script>
import ContentField from '../../components/ContentField.vue';
...

export default {
    components: {
        ContentField,
        ...
    },
    ...
}
```

我的Bot页面加上ContentField公共组件

src/views/user/bot/UserBotIndexView.vue

```


<template>
    <ContentField>
        ...
    </ContentField>
</template>


<script>
...
import ContentField from '@/components/ContentField.vue';

export default {
    components: {
        VAceEditor,
        ContentField,
    },
    ...
}
</script>

<style scoped>
...
</style>

修改匹配界面

将App.vue的bootstrap引入删除
```

匹配界面

```
src/components/MatchGround.vue

<template>
    <div class="matchground-field">
        <div class="matchground">
            <div class="matchground-head">
                <div>
                    <div class="user-photo">
                        <img :src="$store.state.user.photo" alt="">
                    </div>
                    <div class="user-username">
                        {{ $store.state.user.username }}
                    </div>
                </div>
                <div class="user-select-bot">
                    <select class="form-select" aria-label="Default select example" v-model="select_bot">
                        <option value="-1" selected>亲自出马</option>
                        <option v-for="bot in bots" :key="bot.id" :value="bot.id">{{ bot.title }}</option>
                    </select>
                </div>
                <div>
                    <div class="user-photo">
                        <img :src="$store.state.pk.opponent_photo" alt="">
                    </div>
                    <div class="user-username">
                        {{ $store.state.pk.opponent_username }}
                    </div>
                </div>
            </div>
            <div class="start-match-button">
                <button @click="click_match_btn" type="button">{{ match_btn_info }}</button>
            </div>
        </div>
    </div>
</template>


<script>
...


export default {
    setup() {
        ...
    }
}
</script>

<style scoped>
div.matchground-field {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
}
div.matchground {
    width: 60%;
    height: 60%;
    background-color: rgba(50, 50, 50, 0.5);
    display: flex;
    flex-direction: column;
    justify-content: space-around;
}
div.matchground-head {
    display: flex;
    justify-content: space-evenly;
}
div.user-photo {
    text-align: center;
}


div.user-photo > img {
    border-radius: 50%;
    width: 10vh;
}
div.user-username {
    text-align: center;
    font-size: 20px;
    font-weight: 600;
    color: white;
    padding-top: 2vh;
}
div.user-select-bot {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 15vw;
    text-align: center;
}
div.user-select-bot > select {
    margin: 0 auto;
    width: 10vw;
    font-size: 20px;
    border-radius: 5px;
    height: 4.5vh;
}
.start-match-button {
    text-align: center;
}
.start-match-button > button {
    font-size: 20px;
    border-radius: 5px;
    background-color: #FFC310;
    padding: 8px 12px;
    border: none;
    cursor: pointer;
}
</style>
```

build项目，并在git bash中运行脚本./upload.sh测试
注意：打包之前需要删除App.vue中的div.window标签，并在style中加入scoped