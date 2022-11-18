acapp剩余页面的完善
1.将App.vue的div.game-body被div.window包裹和并把style的scoped去掉

2.对局列表的适配

```
src/views/record/RecordIndexView.vue

<template>
    <ContentField>
        <div class="game-table">
            <div>
                <table style="text-align: center;">
                    <thead>
                        <tr>
                            <th>A</th>
                            <th>B</th>
                            <th>对战结果</th>
                            <th>对战时间</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="record in records" :key="record.record.id">
                            <td class="game-table-username">
                                <img :src="record.a_photo" alt="" class="record-user-photo">
                                &nbsp;
                                <span class="record-user-username">{{ record.a_username }}</span>
                            </td>
                            <td class="game-table-username">
                                <img :src="record.b_photo" alt="" class="record-user-photo">
                                &nbsp;
                                <span class="record-user-username">{{ record.b_username }}</span>
                            </td>
                            <td>
                                {{ record.result }}
                            </td>
                            <td>{{ record.record.createtime }}</td>
                            <td>
                                <button @click="open_record_content(record.record.id)" type="button">查看录像</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <nav aria-label="...">
                    <ul style="float: right; padding: 0;">
                        <li class="game-page-item" @click="click_page(-2)">
                            <a class="game-page-link" href="#">前一页</a>
                        </li>
                        <li :class="'game-page-item ' + page.is_active" v-for="page in pages" :key="page.number" @click="click_page(page.number)">
                            <a class="game-page-link" href="#">{{ page.number }}</a>
                        </li>
                        <li class="game-page-item" @click="click_page(-1)">
                            <a class="game-page-link" href="#">后一页</a>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
    </ContentField>
</template>

<script>
...
</script>

<style scoped>
img.record-user-photo {
    width: 4vh;
    border-radius: 50%;
}
div.game-table {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
}
div.game-table table {
    background-color: rgba(255, 255, 255, 0.5);
    border-radius: 5px;
}
.game-table-username {
    text-align: left;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    max-width: 7.5vw;
}
td {
    width: 7.5vw;
}
th {
    text-align: center;
}
.game-page-item {
    display: inline-block;
    background-color: white;
    padding: 8px 12px;
    border: 1px solid #dee2e6;
    cursor: pointer;
}
.game-page-item:hover {
    background-color: #E9ECEF;
}
.game-page-item.active {
    background-color: #0d6efd;
}
.game-page-item.active > a {
    color: white;
}
.game-page-link {
    color: #0d6efd;
    text-decoration: none;
}
nav {
    display: flex;
    justify-content: center;
    align-items: center;
    user-select: none;
}
</style>


```

5.排行榜页面的适配



```
src/view/ranklinst/RanklistIndexView.vue

<template>
    <ContentField>
        <div class="game-table">
            <div>
                <table style="text-align: center;">
                    <thead>
                        <tr>
                            <th>玩家</th>
                            <th>天梯分</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="user in users" :key="user.id">
                            <td class="game-table-username">
                                <img :src="user.photo" alt="" class="user-photo">
                                &nbsp;
                                <span class="user-username">{{ user.username }}</span>
                            </td>
                            <td>
                                {{ user.rating }}
                            </td>
                        </tr>
                    </tbody>
                </table>
                <nav>
                    <ul style="padding: 0;">
                        <li class="game-page-item" @click="click_page(-2)">
                            <a class="game-page-link" href="#">前一页</a>
                        </li>
                        <li :class="'game-page-item ' + page.is_active" v-for="page in pages" :key="page.number" @click="click_page(page.number)">
                            <a class="game-page-link" href="#">{{ page.number }}</a>
                        </li>
                        <li class="game-page-item" @click="click_page(-1)">
                            <a class="game-page-link" href="#">后一页</a>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
    </ContentField>
</template>

<script>
...
</script>

<style scoped>
img.user-photo {
    width: 4vh;
    border-radius: 50%;
}
div.game-table {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
}
div.game-table table {
    background-color: rgba(255, 255, 255, 0.5);
    border-radius: 5px;
}
.game-table-username {
    text-align: left;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    max-width: 7.5vw;
}
td {
    width: 15vw;
}
th {
    text-align: center;
}
.game-page-item {
    display: inline-block;
    background-color: white;
    padding: 8px 12px;
    border: 1px solid #dee2e6;
    cursor: pointer;
}
.game-page-item:hover {
    background-color: #E9ECEF;
}
.game-page-item.active {
    background-color: #0d6efd;
}
.game-page-item.active > a {
    color: white;
}
.game-page-link {
    color: #0d6efd;
    text-decoration: none;
}
nav {
    display: flex;
    justify-content: center;
    align-items: center;
    user-select: none;
}
</style>


```





6.我的Bot页面适配

```
src/views/user/bots/UserBotIndexView.vue

<template>
    <ContentField>
        <div class="game-table">
            <div>
                <span style="font-size: 130%">我的Bot</span>
                <button type="button" style="float: right" @click="show_add_modal_handler(true)">
                    创建Bot
                </button>

                <!-- Modal -->
                <div class="game-modal" id="add-bot-btn" tabindex="-1" v-if="show_add_modal">
                    <div>
                        <h5 style="margin: 2px;">创建Bot</h5>
                    </div>
                    <div>
                        <div>
                            <label for="add-bot-title">名称</label>
                            <input style="width: 85%" v-model="botadd.title" type="text" id="add-bot-title" placeholder="请输入Bot名称">
                        </div>
                        <div>
                            <label for="add-bot-description">简介</label>
                            <textarea style="width: 85%; margin-top: 10px" v-model="botadd.description" id="add-bot-description" rows="3" placeholder="请输入Bot简介"></textarea>
                        </div>
                        <div>
                            <label for="add-bot-code">代码</label>
                            <VAceEditor
                                v-model:value="botadd.content"
                                @init="editorInit"
                                lang="c_cpp"
                                theme="textmate"
                                style="height: 300px" />
                        </div>
                    </div>
                    <div>
                        <div class="error-message">{{ botadd.error_message }}</div>
                        <button type="button" @click="add_bot">创建</button>
                        <button type="button" @click="show_add_modal_handler(false)">取消</button>
                    </div>
                </div>
                <table>
                    <thead>
                        <tr>
                            <th>名称</th>
                            <th>创建时间</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="bot in bots" :key="bot.id">
                            <td>{{ bot.title }}</td>
                            <td>{{ bot.createtime }}</td>
                            <td>
                                <button type="button" style="margin-right: 10px;" @click="show_update_modal_handler(bot.id, true)">修改</button>
                                <button type="button" @click="remove_bot(bot)">删除</button>

                                <div class="game-modal" :id="'update-bot-modal-' + bot.id" tabindex="-1" v-if="bot.show_update_modal">
                                    <div>
                                        <h5 style="margin: 2px;">修改Bot</h5>
                                    </div>
                                    <div>
                                        <div>
                                            <label for="add-bot-title">名称</label>
                                            <input style="width: 85%" v-model="bot.title" type="text" id="add-bot-title" placeholder="请输入Bot名称">
                                        </div>
                                        <div>
                                            <label for="add-bot-description" >简介</label>
                                            <textarea style="width: 85%; margin-top: 10px" v-model="bot.description" id="add-bot-description" rows="3" placeholder="请输入Bot简介"></textarea>
                                        </div>
                                        <div>
                                            <label for="add-bot-code" class="form-label">代码</label>
                                            <VAceEditor
                                                v-model:value="bot.content"
                                                @init="editorInit"
                                                lang="c_cpp"
                                                theme="textmate"
                                                style="height: 300px" />
                                        </div>
                                    </div>
                                    <div>
                                        <div class="error-message">{{ bot.error_message }}</div>
                                        <button type="button" @click="update_bot(bot)">保存修改</button>
                                        <button type="button" @click="show_update_modal_handler(bot.id, false)">取消</button>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </ContentField>
</template>

<script>
import { ref, reactive } from 'vue';
import $ from 'jquery';
import { useStore } from 'vuex';
import { VAceEditor } from 'vue3-ace-editor';
import * as ace from 'ace-builds';
import ContentField from '@/components/ContentField.vue';

export default {
    components: {
        VAceEditor,
        ContentField,
    },
    setup() {
        ace.config.set(
            "basePath", 
            "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/");

        const store = useStore();
        let bots = ref([]);
        let show_add_modal = ref(false);

        const botadd = reactive({
            title: "",
            description: "",
            content: "",
            error_message: "",
        });

        const refresh_bots = () => {
            $.ajax({
                url: "https://app3222.acapp.acwing.com.cn:20112/api/user/bot/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    for (const bot of resp) {
                        bot.show_update_modal = false;
                    }
                    bots.value = resp;
                }
            })
        };

        refresh_bots();

        const add_bot = () => {
            botadd.error_message = "";
            $.ajax({
                url: "https://app3222.acapp.acwing.com.cn:20112/api/user/bot/add/",
                type: "post",
                data: {
                    title: botadd.title,
                    description: botadd.description,
                    content: botadd.content,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if(resp.error_message === "success") {
                        botadd.title = "";
                        botadd.description = "";
                        botadd.content = "";
                        show_add_modal.value = false;
                        refresh_bots();
                    } else {
                        botadd.error_message = resp.error_message;
                    }
                }
            });
        };

        const remove_bot = (bot) => {
            $.ajax({
                url: "https://app3222.acapp.acwing.com.cn:20112/api/user/bot/remove/",
                type: "post",
                data: {
                    bot_id: bot.id,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if(resp.error_message === "success") {
                        refresh_bots();
                    }
                }
            })
        };

        const update_bot = (bot) => {
            botadd.error_message = "";
            $.ajax({
                url: "https://app3222.acapp.acwing.com.cn:20112/api/user/bot/update/",
                type: "post",
                data: {
                    bot_id: bot.id,
                    title: bot.title,
                    description: bot.description,
                    content: bot.content,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if(resp.error_message === "success") {
                        refresh_bots();
                    } else {
                        botadd.error_message = resp.error_message;
                    }
                }
            });
        };

        const editorInit = (editor) => {
            editor.renderer.setShowPrintMargin(false);
        }

        const show_add_modal_handler = is_show => {
            show_add_modal.value = is_show;
        }

        const show_update_modal_handler = (bot_id, is_show) => {
            const new_bots = [];
            for (const bot of bots.value) {
                if (bot.id === bot_id) {
                    bot.show_update_modal = is_show;
                }
                new_bots.push(bot);
            }
            bots.value = new_bots;
        }

        return {
            bots,
            botadd,
            add_bot,
            update_bot,
            remove_bot,
            editorInit,
            show_add_modal,
            show_add_modal_handler,
            show_update_modal_handler,
        }
    }
}
</script>

<style scoped>
div.error-message {
    color: red;
}
div.game-table {
    display: flex;
    justify-content: center;
    padding-top: 5vh;
    width: 100%;
    height: calc(100% - 5vh);
}
div.game-table table {
    background-color: rgba(255, 255, 255, 0.5);
    border-radius: 5px;
}
td {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    width: 12vw;
    max-width: 12vw;
    text-align: center;
}
th {
    text-align: center;
}
.game-modal {
    background-color: white;
    padding: 10px;
    border-radius: 5px;
    position: absolute;
    width: 40vw;
    height: 50vh;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    margin: auto;
    text-align: left;
}
</style>


```

7.录像页面的适配

增加查看录像的跳转

```
src/view/record/RecordIndexView.vue

<template>
    ...
</template>

<script>
...

export default {
    ...

    setup() {
        ...

        const open_record_content = recordId => {
            for(const record of records.value) {
                if(record.record.id === recordId) {
                    ...
                    store.commit("updateRouterName", "record_content");
                    break;
                }
            }
        };

        ...
    }
}
</script>

<style scoped>
...
</style>


```



增加录像页面的返回按钮



```
<template>
    <div class="playground">
        <GameMap/>
    </div>
    <div class="go-back" @click="click_go_back_handler">
            返回
    </div>
</template>

<script>

import GameMap from "./GameMap.vue";
import { useStore } from 'vuex';
export default {
    components: {
        GameMap,
    },
    setup() {
        const store = useStore();

        const click_go_back_handler = () => {
            store.commit("updateRouterName", "record");
        }

        return {
            click_go_back_handler,
        }
    }
}
</script>

<style scoped>
div.content-field {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
}
div.playground {
    width: 40vw;
    height: 50vh;
}
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
    transform: scale(1.2);
    transition: 200;
}
</style>

```

8.对战页面的适配

修改pk页面的显示

```
src/views/pk/PkIndexView.vue

<template>
    ...
</template>

<script>
...
</script>

<style scoped>
div.user-color {
    text-align: center;
    color: white;
    font-size: 30px;
    font-weight: 600;
}
div.user-color {
    position: absolute;
    bottom: 5vh;
    width: 100%;
    text-align: center;
}
</style>


```

结果面板的修改

```
src/components/ResultBoard.vue

<template>
    ...
</template>

<script>
...
</script>

<style scoped>
div.result-board {
    ...
    /* 绝对定位的居中 */
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    margin: auto;
}
...
</style>
```

