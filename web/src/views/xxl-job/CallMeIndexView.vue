<template>
    <ContentField>
        <from @submit.prevent="callme">
        <div class="mb-3 mt-3">
            <label for="browser" class="form-label">选择提醒时间：</label>
            <input class="form-control" v-model="site" list="sites" name="site" id="site"  placeholder="请选择提醒时间">
            <datalist id="sites">
                <option value="15分钟"></option>
                <option value="30分钟"></option>
                <option value="60分钟"></option>
            </datalist>
        </div>  
        <div class="mb-3 mt-3">
            <label for="email" class="form-label">Email:</label>
            <input v-model="mail" type="email" class="form-control" id="email" placeholder="请输入邮箱" name="email" required>
        </div>     
        </from>
        <div class="error-message">{{ error_message }}</div>
        <button type="button" class="btn btn-outline-secondary" @click="callme">xxl-job-demo 预约提醒</button>
    </ContentField>
</template>



<script>
import ContentField from '@/components/ContentField.vue'
import {addCallMe} from'@/api/extern/xxl-job'
// import { datetimepicker } from 'bootstrap/dist/js/bootstrap'
import { ref } from 'vue';
export default {
    components: {
        ContentField
    },
    setup(){
        let mail = ref('');
        let site = ref('');
        let error_message = ref('');
        console.log(site.value)
        const callme = () => {
        return new Promise((resolve, reject) => {
            addCallMe(mail.value,site.value).then(res => {
                if(res.code === 200){
                    // mail = '';
                    // site = '';
                }
                resolve(res)
            }).catch(error => {
                reject(error)
                error_message.value="预约失败";
            })
        })
    }
        
        return{
            callme,
            mail,
            site,
            error_message
            
        }
    }
}
</script>

<style scoped>
</style>
