import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from "pinia";
const pinia = createPinia();
// 注册指令
import plugins from './plugins' // plugins
import { download } from '@/utils/request'
import "./assets/css/nucleo-icons.css";
import "./assets/css/nucleo-svg.css";
import SoftUIDashboard from "./soft-ui-dashboard";
const app = createApp(App)
// createApp(App).use(pinia).use(router).use(plugins)
app.config.globalProperties.download = download
app.use(pinia)
app.use(router)
app.use(plugins)
app.use(SoftUIDashboard)
.mount('#app')