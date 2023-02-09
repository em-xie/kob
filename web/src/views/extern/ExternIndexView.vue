<template>
    <ContentField>
      <div class="row mb-3 text-center">
        <div class="col-4 themed-grid-col">
          <button type="button" class="btn btn-outline-secondary" @click="pk">贪吃蛇</button>
        </div>
        <div class="col-4 themed-grid-col">
          <!-- Button trigger modal -->
          <button type="button" class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#staticBackdrop">
            贪吃蛇游戏说明
          </button>

<!-- Modal -->
<div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-scrollable">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="staticBackdropLabel">贪吃蛇游戏说明</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        在线匹配对战回合制游戏
        <br>
        <span>
          亲自操作：键盘的WASD 为上下左右 
        </span>
        <br>
        <span>
          bot操作：配置一个bot进行对战
        </span>
        <br>
        ........................... 
      </div>
    </div>
  </div>
</div>
        </div>



        <div class="col-4 themed-grid-col">
          <!-- Button trigger modal -->
          <button @click="getBotContextExample" type="button" class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#staticBackdrop1">
            bot代码例子
          </button>

<!-- Modal -->
<div class="modal fade" id="staticBackdrop1" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
  <div class="modal-dialog modal-xl modal-dialog-scrollable">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="staticBackdropLabel">bot代码</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
<VAceEditor
  v-model:value="botadd.content"
  @init="editorInit"
  lang="c_cpp"
   theme="textmate"
  style="height: 300px" />
      </div>
    </div>
  </div>
</div>
        </div>
    </div>
    <div class="row mb-3 text-center">
      <div class="col-4 themed-grid-col">
          <button type="button" class="btn btn-outline-secondary" @click="pictureUpload">图片上传</button>
      </div>

      <div class="col-4 themed-grid-col">
          <button type="button" class="btn btn-outline-secondary" @click="pictureDownload">图片下载</button>
      </div>

      <div class="col-4 themed-grid-col">
          <button type="button" class="btn btn-outline-secondary" @click="mailSend">邮件发送</button>
      </div>
    </div>

    <div class="row mb-3 text-center">
      <div class="col-4 themed-grid-col">
          <button type="button" class="btn btn-outline-secondary" @click="gen">代码生成</button>
      </div>

      <div class="col-4 themed-grid-col">
          <button type="button" class="btn btn-outline-secondary" @click="callme">xxl-job-demo 预约提醒</button>
      </div>

     
    </div>



    </ContentField>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import { VAceEditor } from 'vue3-ace-editor';
import {BotContextExample} from '@/api/bot/bot'
import ace from 'ace-builds';
import router from '@/router/index'
import { reactive} from 'vue'
export default {
    components: {
        ContentField,
        VAceEditor
  
    },
    setup() {

      ace.config.set(
        "basePath", 
        "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")

        const botadd = reactive({
       
        content: ""
        
        })


      const getBotContextExample = () => {
        return new Promise((resolve, reject) => {
                BotContextExample().then(res => {
                console.log(res)
                botadd.content = res.data.example;
                resolve(res)
            }).catch(error => {
                reject(error)
            })
        })
    }

  
      const pk = () => {
          router.push({ name: 'pk_index' });
      }
      const pictureUpload = () => {
          router.push({ name: 'picture_index' });
      }

      const mailSend = () => {
          router.push({ name: 'mail_index' });
      }

      const pictureDownload = () => {
          router.push({ name: 'picturedownload_index' });
      }
     
     
      const gen = () => {
        router.push({ name: 'toolgen_index' });
      }

      const callme = () => {
        router.push({ name: 'callme_index' });
      }
        return {
           pk,
           getBotContextExample,
           pictureUpload,
           pictureDownload,
           botadd,
           mailSend,
           gen,
           callme
        }
    }
}
</script>

<style scoped>
.themed-grid-col {
  padding-top: .75rem;
  padding-bottom: .75rem;
  background-color: rgba(86, 61, 124, .15);
  border: 1px solid rgba(86, 61, 124, .2);
}

.themed-container {
  padding: .75rem;
  margin-bottom: 1.5rem;
  background-color: rgba(0, 123, 255, .15);
  border: 1px solid rgba(0, 123, 255, .2);
}
</style>



