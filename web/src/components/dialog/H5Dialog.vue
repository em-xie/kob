<template>
  <div class="modal fade" ref="dlgModal">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">对话框</h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          <slot></slot>
        </div>
        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary"
            data-bs-dismiss="modal"
          >
            确定
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// 导入组件
import { Modal } from 'bootstrap/dist/js/bootstrap';
export default {
  name: "H5Dialog",
  data() {
    return {
      modal: null,
    };
  },
  methods: {
    show() {
      this.modal.show();
    },
    hide() {
      this.modal.hide();
    },
  },
  mounted() {
    this.modal = new Modal(this.$refs.dlgModal, {});
  },
};
</script>

<style>
</style>