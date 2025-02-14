import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus, {useFormItem} from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/display.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import vue3JsonExcel from "vue3-json-excel"
import {createWeGlobalConfig, WeEditable, WeEditor, WeToolbar} from 'wangeditor5-for-vue3'
import '@wangeditor/editor/dist/css/style.css'
import '@/assets/icons/iconfont.css'
// md文档预览
import VueMarkdownEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css';
// md文档预览
import VMdPreview from '@kangc/v-md-editor/lib/preview';
import '@kangc/v-md-editor/lib/style/preview.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';

import Prism from 'prismjs';
import hljs from 'highlight.js';

const app = createApp(App)
app.use(router)
app.use(ElementPlus, {size: 'small', locale: zhCn})
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.use(vue3JsonExcel)
app.use(WeToolbar)
app.use(WeEditable)
app.use(WeEditor)
app.use(createWeGlobalConfig({
    attrs: {
        toolbar: {
            class: 'el-we-toolbar'
        }, editable: {
            class: 'el-we-editable'
        }, editor: {
            class: 'el-we-editor'
        }
    }, opts: {
        editable: {
            config: {
                placeholder: '请输入'
            }
        }
    }, formFieldInit () {
        const {formItem} = useFormItem()
        return {
            blur () {
                formItem?.validate('blur').catch(debugWarn)
            }, change () {
                formItem?.validate('change').catch(debugWarn)
            }
        }
    }
}))
VueMarkdownEditor.use(vuepressTheme, {
    Prism
})
app.use(VueMarkdownEditor)
VMdPreview.use(githubTheme, {
    Hljs: hljs
})
app.use(VMdPreview)
app.mount('#app')
