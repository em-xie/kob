<template>

<ContentField>
  <table class="table table-striped table-hover" style="text-align: center;">
            <thead>
                <tr>
                    <th>配置key</th>
                    <th>访问站点</th>
                    <th>自定义域名</th>
                    <th>桶名称</th>
                    <th>前缀</th>
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
                    
                      <td>{{ ossConfig.prefix }}</td>
                    
                   
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


        <!-- <div class="form-group">
                <label class="col-sm-2 control-label">头像</label>
                <div class="col-sm-10">
                  <upload v-bind:input-id="'image-upload'"
                            v-bind:text="'上传头像'"
                            v-bind:suffixs="['jpg', 'jpeg', 'png']"
                            v-bind:use="FILE_USE.TEACHER.key"
                            v-bind:after-upload="afterUpload"></upload>
                  <div v-show="teacher.image" class="row">
                    <div class="col-md-4">
                      <img v-bind:src="teacher.image" class="img-responsive">
                    </div>
                  </div>

                </div>
              </div> -->


<!-- <div class="mb-3">
  <input class="form-control" type="file" id="formFileMultiple" multiple>
</div> -->


<!-- <button type="button" 
        class="btn btn-outline-success btn-sm"
        data-bs-toggle="modal" 
        data-bs-target="#uploadImgModal" title="添加图片">
    添加图片
</button>



<div class="modal fade" id="uploadImgModal" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="updateImgModal-staticBackdropLabel">请上传新图片</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="row mx-3">
                   
                    <input type="file" id="new-img" name="new_img"
                           class="form-control mb-2"
                           accept="image/gif,image/png,image/jpeg"
                           placeholder="支持jpg、png、gif格式图片">
                    <span class="text-secondary text-opacity-50">
                        <i class="fa fa-volume-down"></i>
                        支持jpg、png、gif格式图片
                    </span>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
               
                <button type="button" class="btn btn-primary" data-bs-dismiss="modal"
                        @click="uploadNewImg()">上传
                </button>
            </div>
        </div>
    </div>
</div> 


<div class="position-fixed top-50 start-50 translate-middle p-3" style="z-index: 11">
    <div id="info-toast" class="toast align-items-center" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
            <div class="toast-body">
               
                <span id="toast-info-success" class="text-success"></span>
                
                <span id="toast-info-error" class="text-danger"></span>
            </div>
            <button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>
</div> -->

<ImageUpload>
<div class="text-center">
  <img src="url" class="rounded" alt="...">
</div>
</ImageUpload>

<!-- <div class="text-center">
  <img src="http://www.xiemingquan.top:9006/test/test.png" class="rounded" alt="...">
</div> -->


</ContentField>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import { ref,reactive,toRefs } from 'vue';
import {listOssConfig} from '@/api/oss/ossConfig'
 

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
                    ossConfigList.value = res.rows;
                    total.value = res.total;
                    udpate_pages();
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

        // 上传新图片
       
// const uploadNewImg = () =>{
    
//     // 构造表单数据
//     let formData = new FormData();
//     // --- 文件
//     let file = $(this.$el).find("#new-img")[0].files[0];
//     formData.append("file",file);
//     console.log(formData);
//     // 文件不能为空
//     if(file){
//         // 发起上传操作
//         return new Promise((resolve, reject) => {
//             upload(formData).then(res => {
//                    console.log(res);
//                    // 显示信息栏
//                     // --- 给提示框赋值
//                     $("#toast-info-success").text("上传成功！");
//                     $("#toast-info-error").text("");
//                     // --- 显示提示栏
//                     var toastBar = document.getElementById('info-toast');
//                     var toast = new bootstrap.Toast(toastBar);
//                     toast.show();
//               resolve()
//             }).catch(error => {
//               reject(error)
//               // --- 给提示框赋值：图片类型在input中有限制，如果绕过前端，会在后端再次验证
//               $("#toast-info-success").text("");
//                     $("#toast-info-error").text("图片类型不匹配！");
//                     // --- 显示提示栏
//                     var toastBar = document.getElementById('info-toast');
//                     var toast = new bootstrap.Toast(toastBar);
//                     toast.show();
//             })
//           })
//     }else{
//         // --- 给提示框赋值
//         $("#toast-info-success").text("");
//         $("#toast-info-error").text("请选择图片！");
//         // --- 显示提示栏
//         var toastBar = document.getElementById('info-toast');
//         var toast = new bootstrap.Toast(toastBar);
//         toast.show();
//     }
//     // 清空input文件表单：再次打开上传界面，不会保留上次的选项
//     $("#new-main-img")[0].value = "";
// } 

       
        // function afterUpload(resp) {
        //     let _this = this
        //     let image = resp.content.path;
        //     _this.teacher.image = image
        // }

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