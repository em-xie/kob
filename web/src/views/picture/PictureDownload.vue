<template>

    <ContentField>
      <table class="table table-striped table-hover" style="text-align: center;">
                <thead>
                    <tr>
                        <!-- <th>文件名</th> -->
                        <th>原名</th>
                        <th>文件后缀</th>
                        <th>文件展示</th>
                        <th>服务商</th>
                        <th>操作</th>
                    </tr>
                </thead>
                
                <tbody>
                    <tr v-for="oss in ossList" :key="oss.index">
                        <!-- <td class="mytt">
                            {{oss.fileName}}
                        </td> -->
                        <td class="mytt"> 
                            {{oss.originalName}}
                        </td>
                        <td class="mytt">
                            {{oss.fileSuffix}}
                        </td>
                        
                        <td class="image">
                            
                            <img :src="oss.url"  class="img-thumbnail" >
                        </td>
                          <td class="mytt">{{ oss.service }}</td>
                          
                          
                        <td class="mytt">
                            <button @click="DownLoadOss(oss)" type="button" class="btn btn-secondary">下载</button>
                            <button @click="deleteOssConfig(oss)" type="button" class="btn btn-secondary">删除</button>
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
    
    

    
    
    <!-- <div class="text-center">
      <img src="http://www.xiemingquan.top:9006/test/test.png" class="rounded" alt="...">
    </div> -->
    
    
    </ContentField>
    </template>
    
    <script>
    import ContentField from '../../components/ContentField.vue'
    import { ref,reactive,toRefs } from 'vue';
    import {listOss} from '@/api/oss/oss'
    import {getCurrentInstance} from 'vue'
    
    
    export default {
        components: {
            ContentField,
 
            // upload
        },
    
        setup() {
            
            const { proxy } = getCurrentInstance();
            const ossList = ref([]);
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
                    listOss( queryParams.value).then(res => {
                        ossList.value = res.rows;
                        total.value = res.total;
                        udpate_pages();
                  resolve()
                }).catch(error => {
                  reject(error)
                })
              })
            }
    
            pull_page(queryParams.value.pageNum);
    
           
            function DownLoadOss(oss){
               
                proxy.$download.oss(oss.ossId)
            }
    
            function deleteOssConfig(){
    
            }
    
           
    
            return {
                ossList,
                pages,
                click_page,
                queryParams,
                total,
                DownLoadOss,
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
    .mytt{
        width: 150px;
        word-wrap: break-word;
        word-break: break-all;
        padding-top: 50px;
    }
    .image{
        width: 160px;
        padding: 20px;
    }
     
    </style>