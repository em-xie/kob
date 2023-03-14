<template>

<ContentField class="tap">
  <table class="table table-striped table-hover" style="text-align: center;">
            <thead>
                <tr>
                    <th>配置key</th>
                    <th>访问站点</th>
                    <th>自定义域名</th>
                    <th>桶名称</th>
                    <!-- <th>前缀</th> -->
                    <th>域</th>
                    <th>状态</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="ossConfig in ossConfigList" :key="ossConfig.index">
                    <td>
                        {{ossConfig.configKey}}
                    </td>
                    <td>
                        {{ossConfig.endpoint}}
                    </td>
                    <td>{{ossConfig.domain}}</td>
                    <td>{{ ossConfig.bucketName }}</td>
                    
                      <!-- <td>{{ ossConfig.prefix }}</td> -->
                    
                   
                      <td>{{ ossConfig.region }}</td>
                    
                   
                    
                      <td>{{ ossConfig.status }}</td>
                   
                    <td>
                        <button @click="updateOssConfig(ossConfig.id)" type="button" class="btn btn-secondary">修改</button>
                        <button @click="deleteOssConfig(ossConfig.id)" type="button" class="btn btn-secondary">删除</button>
                    </td>
                </tr>
            </tbody>
        </table>
        <nav aria-label="...">
        <ul class="pagination" style="float: right;">
            <li class="page-item" @click="click_page(-2)">
                <a class="page-link" href="#">前一页</a>
            </li>
            <li :class="'page-item ' + page.is_active" v-for="page in pages" :key="page.number" @click="click_page(page.number)">
                <a class="page-link" href="#">{{ page.number }}</a>
            </li>
            <li class="page-item" @click="click_page(-1)">
                <a class="page-link" href="#">后一页</a>
            </li>
        </ul>
        </nav>


        
<ImageUpload>
<div class="text-center">
  <img src="url" class="rounded" alt="...">
</div>
</ImageUpload>

<!-- <div class="text-center">
  <img src="http://www.xiemingquan.top:9006/test/test.png" class="rounded" alt="...">
</div> -->

  <!--信息提示栏-->
  <div class="position-fixed top-50 start-50 translate-middle p-3" style="z-index: 11">
    <div id="info-toast" class="toast align-items-center" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
            <div class="toast-body">
                <!--成功信息-->
                <span id="toast-info-success" class="text-success"></span>
            </div>
            <button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>
</div>

</ContentField>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import { ref,reactive,toRefs } from 'vue';
import {listOssConfig} from '@/api/oss/ossConfig'
import router from '@/router/index'
import $ from 'jquery'
import bootstrap from 'bootstrap/dist/js/bootstrap'
 import ImageUpload from "../../components/ImageUpload.vue";
export default {
    components: {
        ContentField,
        ImageUpload
        // upload
    },

    setup() {
        
        
        const ossConfigList = ref([]);
        const total = ref(0);
        const data = reactive({
  // 查询参数
  queryParams: {
    pageNum: 1,
    pageSize: 10,

  },
});

const { queryParams} = toRefs(data);
       //let current_page = 1;
        let pages = ref([]);

        const click_page = page => {
            
            if (page === -2) page = queryParams.value.pageNum - 1;
            else if (page === -1) page = queryParams.value.pageNum + 1;
            
            let max_pages = parseInt(Math.ceil(total.value/ 10));
            
            if (page >= 1 && page <= max_pages) {
                
                pull_page(page);
            }
        }

        const udpate_pages = () => {
            let max_pages = parseInt(Math.ceil(total.value / 10));
            let new_pages = [];
            for (let i = queryParams.value.pageNum - 2; i <= queryParams.value.pageNum + 2; i ++ ) {
                if (i >= 1 && i <= max_pages) {
                    new_pages.push({
                        number: i,
                        is_active: i === queryParams.value.pageNum ? "active" : "",
                    });
                }
            }
            pages.value = new_pages;
        }

        const pull_page = page => {
            queryParams.value.pageNum = page;
            
            return new Promise((resolve, reject) => {
              listOssConfig( queryParams.value).then(res => {
                    console.log(res)
                    if(res.code === 403){
                        $("#toast-info-success").text(res.msg);
                        // --- 显示提示栏
                        var toastBar = document.getElementById('info-toast');
                        var toast = new bootstrap.Toast(toastBar);
                        toast.show();
                        setTimeout(()=>{
                            router.push({ name: 'home' });
                         },4000); //延时函数，单位是毫秒 
                    }else{
                    ossConfigList.value = res.rows;
                    total.value = res.total;
                    udpate_pages();
                    }
                    resolve()
            }).catch(error => {
              reject(error)
            })
          })
        }

        pull_page(queryParams.value.pageNum);

       
        function updateOssConfig(){
          
        }

        function deleteOssConfig(){

        }

     


        return {
            ossConfigList,
            
            pages,
            click_page,
            queryParams,
            total,
            updateOssConfig,
            deleteOssConfig,
            // uploadNewImg,

            // teacher: {},
            // teachers: [],
            // FILE_USE: FILE_USE,
            // afterUpload
        }
    }
}

</script>


<style scoped> 

 
</style>