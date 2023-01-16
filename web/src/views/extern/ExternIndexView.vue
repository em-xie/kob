<template>
    <ContentField>
      <div class="row mb-3 text-center">
        <div class="col-4 themed-grid-col">
          <button type="button" class="btn btn-outline-secondary" @click="pk">贪吃蛇</button>
        </div>
        <div class="col-4 themed-grid-col">
          <!-- Button trigger modal -->
          <button type="button" class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#staticBackdrop">
            Launch static backdrop modal
          </button>

<!-- Modal -->
<div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-scrollable">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="staticBackdropLabel">Modal title</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        ...
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Understood</button>
      </div>
    </div>
  </div>
</div>
        </div>



        <div class="col-4 themed-grid-col">
          <button class="btn btn-primary" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasRight" aria-controls="offcanvasRight">Toggle right offcanvas</button>

          <div class="offcanvas offcanvas-end" tabindex="-1" id="offcanvasRight" aria-labelledby="offcanvasRightLabel">
            <div class="offcanvas-header">
              <h5 class="offcanvas-title" id="offcanvasRightLabel">Offcanvas right</h5>
              <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
            </div>
            <div class="offcanvas-body">
              ...
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
    </div>



    </ContentField>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import router from '@/router/index'
export default {
    components: {
        ContentField
    },
    setup() {
      let isclick = false
      const pk = () => {
          router.push({ name: 'pk_index' });
      }
      const pictureUpload = () => {
          router.push({ name: 'picture_index' });
      }

      const pictureDownload = () => {
          router.push({ name: 'picturedownload_index' });
      }
      const wechat = () => {
          isclick = true
      }
     
        return {
           pk,
           isclick,
           pictureUpload,
           pictureDownload,
           wechat
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



