<template>
    <button type="button" 
        class="btn btn-outline-success btn-sm"
        data-bs-toggle="modal" 
        data-bs-target="#uploadImgModal" title="添加图片">
        添加图片
    </button>

    <!-- 上传图片模态框 -->
<div class="modal fade" id="uploadImgModal" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="updateImgModal-staticBackdropLabel">请上传新图片</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="row mx-3">
                    <!-- 图片类型：gif、png、jpeg -->
                    <input type="file" id="new-img" name="new_img"
                           class="form-control mb-2" 
                           multiple
                           accept="image/jpg,image/png,image/jpeg"
                           
                           placeholder="支持jpg、png、jpeg格式图片">
                    <span class="text-secondary text-opacity-50">
                        <i class="fa fa-volume-down"></i>
                        支持jpg、png、jpeg格式图片
                    </span>
                </div>
            </div>
           
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                <!--执行上传操作，并关闭模态框-->
                <button type="button" class="btn btn-primary" data-bs-dismiss="modal"
                        @click="uploadNewImg()">上传
                </button>
            </div>
        </div>
    </div>
</div> 

<!--信息提示栏-->
<div class="position-fixed top-50 start-50 translate-middle p-3" style="z-index: 11">
    <div id="info-toast" class="toast align-items-center" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
            <div class="toast-body">
                <!--成功信息-->
                <span id="toast-info-success" class="text-success"></span>
                <!--失败信息-->
                <span id="toast-info-error" class="text-danger"></span>
            </div>
            <button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>
</div>

</template>
  
  <script setup>
  
  import bootstrap from 'bootstrap/dist/js/bootstrap'
import $ from 'jquery'
import {upload} from '@/api/oss/oss'
 

  

  // 上传新图片
       
const uploadNewImg = () =>{
    
    // 构造表单数据
    let formData = new FormData();
    // --- 文件
    let file = $("#new-img")[0].files[0];
    formData.append("file",file);
    
    // 文件不能为空
    if(file){
        // 发起上传操作
        return new Promise((resolve, reject) => {
            upload(formData).then(res => {
                  
                   if(res.code === 200){
                    // 显示信息栏
                    // --- 给提示框赋值
                    $("#toast-info-success").text("上传成功！");
                    $("#toast-info-error").text("");
                    // --- 显示提示栏
                    var toastBar = document.getElementById('info-toast');
                    var toast = new bootstrap.Toast(toastBar);

                    toast.show();
                    
                   }else{
                    $("#toast-info-success").text("上传失败！");
                    $("#toast-info-error").text("");
                   } 
              resolve()
            }).catch(error => {
              reject(error)
              // --- 给提示框赋值：图片类型在input中有限制，如果绕过前端，会在后端再次验证
              $("#toast-info-success").text("");
                    $("#toast-info-error").text("图片类型不匹配！");
                    // --- 显示提示栏
                    var toastBar = document.getElementById('info-toast');
                    var toast = new bootstrap.Toast(toastBar);
                    toast.show();
            })
          })
    }else{
        // --- 给提示框赋值
        $("#toast-info-success").text("");
        $("#toast-info-error").text("请选择图片！");
        // --- 显示提示栏
        var toastBar = document.getElementById('info-toast');
        var toast = new bootstrap.Toast(toastBar);
        toast.show();
    }
    // 清空input文件表单：再次打开上传界面，不会保留上次的选项
    $("#new-main-img")[0].value = "";
} 

  </script>
  
  <style scoped lang="scss">
 
 
  </style>