<template>
    <ContentField>

        <form @submit.prevent="send">
        <div class="mb-3 mt-3">
            <label for="email" class="form-label">Email:</label>
            <input v-model="mail" type="email" class="form-control" id="email" placeholder="Enter email" name="email" required>
        </div>
        <div class="mb-3 mt-3">
                <label for="title">标题:</label>
                <input v-model="title"  type="text" class="form-control" id="title" placeholder="请输入标题" name="title" required>
        </div>       
        <div class="mb-3 mt-3">
                <label for="context" class="form-label">正文</label>
                <textarea v-model="context" class="form-control" id="title" rows="3" placeholder="请输入正文" required></textarea>
        </div>
            
            <div class="error-message">{{ error_message }}</div>
            <button @click="send" type="submit" class="btn btn-primary">send</button>
       </form>
    </ContentField>
</template>



<script>
import ContentField from '@/components/ContentField.vue'
import { ref } from 'vue';
import {sendSimpleMessage} from'@/api/mail/mail'
export default {
    components: {
        ContentField
    },
    setup(){
       
        let mail = ref('');
        let title = ref('');
        let context = ref('');
        let error_message = ref('');
       

        const send = () => {
            error_message.value = "";
           
            return new Promise((resolve, reject) => {
                //console.log(page)
                sendSimpleMessage(mail.value,title.value,context.value).then(res => {  
                resolve(res)
            }).catch(error => {
              reject(error)
              error_message.value="发送失败";
            })
          })
        
   
        }
        
        return{
            send,
            mail,
            title,
            context,
            error_message
        }
    }
}
</script>

<style scoped>
</style>
