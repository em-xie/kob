import { Modal, Toast } from 'bootstrap';

/**
 * Bootstrap Dialog 弹窗类
 * @version 1.0.0
 * @requires Bootstrap 5.x
 */
class BsDialog {

    /**
     * 字符串转为Dom对象
     */
    static strToDom(htmlStr) {
        // 方法1: 使用 Fragment。效率高
        let frag = document.createRange().createContextualFragment(htmlStr);
        return frag.firstChild;
        // 方法2: 使用 DOMParser。效率低
        // return new DOMParser().parseFromString(htmlStr,'text/html');
        // 方法3: 使用常规方法
        // let node=document.createElement('DIV');
        // node.innerHTML=htmlStr;
        // return node.childNodes;
    }

    // ==============================================================================
    // 兼容layer
    // ==============================================================================
    static msg(message, time = 3000) {
        // 基于 layer 插件的全局弹窗方法 https://layer.layui.com/
        // layer.msg(message, { time: time });
        BsDialog.showToast({ content: message, delay: time })
    }
    static alert(message, option = {}) {
        let _default = {
            content: message, // 消息内容
        }
        BsDialog.showAlert(Object.assign(_default, option))
    }
    static confirm(message, fnOk, fnCancel) {
        BsDialog.showConfirm({ content: message }).then((result) => {
            if (result) {
                fnOk();
            } else {
                fnCancel();
            }
        })
    }

    // ==============================================================================
    // 快捷方法
    // ==============================================================================
    static toast(message, time = 3000) {
        BsDialog.showToast({ content: message, delay: time })
    }
    // ==============================================================================
    // 常规方法
    // ==============================================================================

    /**
     * 显示 Toast
     * @example BsDialog.showToast();
     */
    static showToast(option = {}) {
        let _default = {
            id: 'toast_' + new Date().getTime(), // 元素ID
            content: 'Hello, world! This is a toast message.', // 消息内容
            delay: 2000, // 延迟关闭时间(毫秒)
            autohide: true, // 是否自动关闭
            theme: 'dark', // 主题样式
            customStyle: '', // 自定义主题样式
            customButtonStyle: '', // 自定义按钮样式
        }
        if (typeof (option) == 'object') {
            option = Object.assign(_default, option);
        } else {
            _default.content = option
            option = _default;
        }
        if (option.customStyle == '') {
            switch (option.theme) {
                case 'dark':
                    option.customStyle = 'bg-dark text-white border-0';
                    option.customButtonStyle = 'btn-close-white';
                    break;
                case 'light':
                    option.customStyle = '';
                    option.customButtonStyle = '';
                    break;
            }
        }
        let html = `<div id="${option.id}" class="toast position-fixed top-50 start-50 translate-middle ${option.customStyle}" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
            <div class="toast-body">
                ${option.content}
            </div>
            <button type="button" class="btn-close me-2 m-auto ${option.customButtonStyle}" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>`;
        let el = BsDialog.strToDom(html);
        document.body.appendChild(el);
        var toast = new Toast(el, { delay: option.delay, autohide: option.autohide })
        toast.show()
        // 弹窗隐藏后，删除自身DOM
        el.addEventListener('hidden.bs.toast', function () {
            document.body.removeChild(el);
        })
    }
    /**
     * 显示 Toast
     * @example BsDialog.showToast();
     */
    static showAlert(option = {}) {
        let _default = {
            id: 'alert_' + new Date().getTime(), // 元素ID
            title: 'Modal title',
            content: 'Modal body text goes here.', // 消息内容
        }
        option = Object.assign(_default, option);
        let html = `<div class="modal fade">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title">${option.title}</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
              </div>
              <div class="modal-body">
              ${option.content}
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">确定</button>
              </div>
            </div>
          </div>
        </div>`;
        return new Promise((resolve) => {
            let el = BsDialog.strToDom(html);
            document.body.appendChild(el);
            var modal = new Modal(el)
            modal.show();
            // 弹窗隐藏后，删除自身DOM
            el.addEventListener('hidden.bs.modal', function () {
                resolve(false);
                document.body.removeChild(el);
            })
        });
    }
    /**
     * 显示确认框
     * @example if(await BsDialog.showConfirm({title:'警告', content:'确定要删除吗？'})){alert('确定删除')}else{alert('取消删除')}
     */
    static showConfirm(option = {}) {
        let _default = {
            id: 'confirm_' + new Date().getTime(), // 元素ID
            title: 'Modal title',
            content: 'Modal body text goes here.', // 消息内容
        }
        option = Object.assign(_default, option);
        let html = `<div id="${option.id}" class="modal" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">${option.title}</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <p>${option.content}</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                        <button type="button" class="btn-ok btn btn-primary">确定</button>
                    </div>
                </div>
            </div>
        </div>`;
        return new Promise((resolve) => {
            let el = BsDialog.strToDom(html);
            document.body.appendChild(el);
            var modal = new Modal(el)
            modal.show();
            // 确定按钮点击事件
            el.querySelector('.btn-ok').addEventListener('click', function (evt) {
                evt.preventDefault();
                alert('ok');
                resolve(true);
                document.body.removeChild(el);
            })
            // 弹窗隐藏后，删除自身DOM
            el.addEventListener('hidden.bs.modal', function () {
                resolve(false);
                document.body.removeChild(el);
            })
        })
    }
    /**
     * 显示输入框
     * @example if(await BsDialog.showPrompt({content:'请输入口令：'})=='123'){alert('口令正确')}else{alert('口令错误')}
     */
    static showPrompt(option = {}) {
        let _default = {
            id: 'modal_' + new Date().getTime(), // 元素ID
            title: 'Modal title',
            content: `<div class="mb-3">
                    <label for="recipient-name" class="col-form-label">Recipient:</label>
                    <input type="text" class="form-control" id="recipient-name">
                </div>
                <div class="mb-3">
                    <label for="message-text" class="col-form-label">Message:</label>
                    <textarea class="form-control" id="message-text"></textarea>
                </div>`, // 消息内容
        }
        option = Object.assign(_default, option);
        let html = `<div id="${option.id}" class="modal fade" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
        aria-labelledby="staticBackdropLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-scrollable">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">${option.title}</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form>
                        ${option.content}
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
                    <button type="button" class="btn-ok btn btn-primary">确定</button>
                </div>
            </div>
        </div>
    </div>`;
        let el = BsDialog.strToDom(html);
        document.body.appendChild(el);
        var modal = new Modal(el)
        modal.show();
        // 确定按钮点击事件
        el.querySelector('.btn-ok').addEventListener('click', function (evt) {
            evt.preventDefault();
            alert('ok');
        })
        // 弹窗隐藏后，删除自身DOM
        el.addEventListener('hidden.bs.modal', function () {
            document.body.removeChild(el);
        })
    }
    static showFormDialogByIframe(option = {}) {
        let _default = {
            id: 'modal_' + new Date().getTime(), // 元素ID
            title: 'Modal title',
            url: '', // iframe url
            size: "modal-lg", // modal-xl, modal-lg, modal-sm
            opener: null,
        }
        option = Object.assign(_default, option);
        let html = `<div id="${option.id}" class="modal fade" tabindex="-1">
                <div class="modal-dialog modal-dialog-scrollable modal-dialog-centered modal-fullscreen-sm-down ${option.size}">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">${option.title}</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <iframe src="${option.url}" frameborder="0" scrolling="auto" allowtransparency="true" allowfullscreen style="width:100%;height:60vh"></iframe>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary">保存</button>
                        </div>
                    </div>
                </div>
            </div>`;
        let el = BsDialog.strToDom(html);
        document.body.appendChild(el);
        var modal = new Modal(el)
        modal.handleUpdate()
        modal.show();

        // 设置iframe子窗体的父窗体对象
        let elIframe = el.querySelector('iframe');
        if (option.opener) {
            elIframe.contentWindow.opener = option.opener;
        }
        elIframe.onload = function () {
            // 页面就绪后，执行对 opener 窗体的回调函数 option.opener.onSubWindowCallback()
            if (typeof (elIframe.contentWindow.onOpenerWindowCallback) == 'function') {
                elIframe.contentWindow.onOpenerWindowCallback();
                // onOpenerWindowCallback() 函数的示例代码
                // if (window.opener && typeof (window.opener.onSubWindowCallback) == 'function') {
                //     window.opener.onSubWindowCallback();
                // }
            }
            // 把 iframe 里面的表单按钮移动到当前对话框底部。
            // 注意：chrome要发布网站同源的才能互相操作，本地测试操作不了iframe。
            // elIframe.contentDocument.querySelector('body').style.width = 'calc(' + elDialog.style.width + ' - 80px)';
            let elFormFooter = elIframe.contentDocument.querySelector('.dialog-footer');
            if (elFormFooter) {
                let elFooter = el.querySelector('.modal-footer');
                // 清空弹窗底部所有按钮
                while (elFooter.hasChildNodes()) {
                    elFooter.removeChild(elFooter.lastChild);
                }
                Array.from(elFormFooter.children).forEach(elBtn => {
                   // console.log(elBtn);
                    elFooter.appendChild(elBtn);
                })
            }
        }
        // 弹窗隐藏后，删除自身DOM
        el.addEventListener('hidden.bs.modal', function () {
            document.body.removeChild(el);
        })
    }
}

export default BsDialog;