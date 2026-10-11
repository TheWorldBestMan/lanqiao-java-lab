import { createApp } from 'vue'
import { ElAlert, ElButton, ElIcon, ElInput, ElOption, ElProgress, ElSelect } from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'
import './styles.css'

const app = createApp(App).use(router)
;[ElAlert, ElButton, ElIcon, ElInput, ElOption, ElProgress, ElSelect].forEach(component => app.use(component))
app.mount('#app')

