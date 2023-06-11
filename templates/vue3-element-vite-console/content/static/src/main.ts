import { createApp } from "vue";
import App from "./App.vue";
import vueI18n from './i18n';

// import "~/styles/element/index.scss";

import ElementPlus from "element-plus";
// import all element css, uncommented next line
// import "element-plus/dist/index.css";

// or use cdn, uncomment cdn link in `index.html`

import "~/styles/index.scss";

// If you want to use ElMessage, import it.
import "element-plus/theme-chalk/src/message.scss"
import "element-plus/theme-chalk/src/message-box.scss";

import router from './router'

const app = createApp(App);
app.use(router)
app.use(ElementPlus);
app.use(vueI18n);
app.mount("#app");
