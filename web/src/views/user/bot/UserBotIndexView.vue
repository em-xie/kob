<template>

    <div class="container">
        <div class="row">
            <div class="col-3">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-body">
                        <img :src="photo" alt="" style="width: 100%;">
                    </div>
                </div>
            </div>
            <div class="col-9">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-header">
                        <span style="font-size: 130%;">我的Bot</span>
                        <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal" data-bs-target="#add-bot-btn">
                            创建Bot
                        </button>

                        <div class="modal fade" id="add-bot-btn" tabindex="-1">
                            <div class="modal-dialog modal-xl">
                                <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="exampleModalLabel">创建Bot</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <div class="mb-3">
                                        <label for="add-bot-title" class="form-label">名称</label>
                                        <input v-model="botadd.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入Bot名称">
                                    </div>
                                    <div class="mb-3">
                                        <label for="add-bot-description" class="form-label">简介</label>
                                        <textarea v-model="botadd.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入Bot简介"></textarea>
                                    </div>
                                    <div class="mb-3">
                                        <label for="add-bot-code" class="form-label">代码</label>
                                        <VAceEditor
                                            v-model:value="botadd.content"
                                            @init="editorInit"
                                            lang="c_cpp"
                                            theme="textmate"
                                            style="height: 300px" />                                      
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <div class="error-message">{{ botadd.error_message }}</div>
                                    <button type="button" class="btn btn-primary" @click="add_bot">创建</button>
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                </div>
                                </div>
                            </div>
                        </div>


                    </div>
                    <div class="card-body">
                        <table class="table table-hover">
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

                                        <button type="button" class="btn btn-secondary" style="margin-right: 10px;" data-bs-toggle="modal" :data-bs-target="'#update-bot-modal-' + bot.id">修改</button>
                                        <button type="button" class="btn btn-danger" @click="remove_bot(bot)">删除</button>


                                        <div class="modal fade" :id="'update-bot-modal-' + bot.id" tabindex="-1">
                                            <div class="modal-dialog modal-xl">
                                                <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title" id="exampleModalLabel">修改Bot</h5>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                </div>
                                                <div class="modal-body">
                                                    <div class="mb-3">
                                                        <label for="add-bot-title" class="form-label">名称</label>
                                                        <input v-model="bot.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入Bot名称">
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="add-bot-description" class="form-label">简介</label>
                                                        <textarea v-model="bot.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入Bot简介"></textarea>
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="add-bot-code" class="form-label">代码</label>
                                                        <VAceEditor
                                                            v-model:value="bot.content"
                                                            @init="editorInit"
                                                            lang="c_cpp"
                                                            theme="textmate"
                                                            style="height: 300px" />
                                                    </div>
                                                </div>
                                                <div class="modal-footer">
                                                    <div class="error-message">{{ bot.error_message }}</div>
                                                    <button type="button" class="btn btn-primary" @click="update_bot(bot)">保存修改</button>
                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                                </div>
                                                </div>
                                            </div>
                                        </div>

                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

</template>


<script>
import { ref,reactive} from 'vue'
import {getlist,addBot,removeBot,updateBot} from '@/api/bot/bot'
import {useUserStore} from '@/store/modules/user'
import { Modal } from 'bootstrap/dist/js/bootstrap'
import { VAceEditor } from 'vue3-ace-editor';
import { storeToRefs } from 'pinia'
import ace from 'ace-builds';

export default {
    // 判断一个对象用reactive
    components:{
        VAceEditor
    },
    setup(){
        ace.config.set(
        "basePath", 
        "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")
        const userStore = useUserStore()
        const { photo } = storeToRefs(userStore)
    let bots = ref([]);
    const botadd = reactive({
        title: "",
        description: "",
        content: "",
        error_message: "",
    })
    const refresh_bots = () => {
        return new Promise((resolve, reject) => {
                getlist().then(res => {
               // console.log(res)
                bots.value = res;
                resolve(res)
            }).catch(error => {
                reject(error)
            })
        })
    }
        // 执行
        refresh_bots();

        //创建一个 bot
        const add_bot = () => {
            botadd.error_message = "";
            const data = {
                    title: botadd.title,
                    description: botadd.description,
                    content: botadd.content,
            }
            return new Promise((resolve, reject) => {
            addBot(data).then(res => {
                if (res.data.add.error_message === "success") {
                                botadd.title = "";
                                botadd.description = "";
                                botadd.content = "";
                                // 模态框关闭
                                Modal.getInstance("#add-bot-btn").hide();
                                refresh_bots();
                            } else {
                                botadd.error_message = res.error_message;
                            }
              resolve()
            }).catch(error => {
              reject(error)
            })
          })
                }

                
         //删除一个 bot
         const remove_bot = (bot) => {
            const data = {
                bot_id: bot.id
            }
            return new Promise((resolve, reject) => {
                removeBot(data).then(res => {
                    if (res.data.remove.error_message === "success") {
                        refresh_bots();
                    }
              resolve()
            }).catch(error => {
              reject(error)
            })
          })
        
        }

        const update_bot = (bot) => {
            botadd.error_message = "";
            return new Promise((resolve, reject) => {
                const data = {
                    bot_id: bot.id,
                    title: bot.title,
                    description: bot.description,
                    content: bot.content,
                }
            updateBot(data).then(res => {
                if (res.data.remove.error_message === "success") {
                         // 模态框关闭
                        Modal.getInstance('#update-bot-modal-' + bot.id).hide();
                        refresh_bots();
                    } else {
                        botadd.error_message = res.error_message;
                    }
              resolve()
            }).catch(error => {
              reject(error)
            })
          })

        }






        // 返回
        return {
            bots,
            // v-model
            botadd,
            add_bot,
            remove_bot,
            update_bot,
            userStore,
            photo,
        };
    }
}

</script>


<style scoped> 
div.error-message{
    color:red;
}
.btn-close{
  background-color: black;
}
</style>